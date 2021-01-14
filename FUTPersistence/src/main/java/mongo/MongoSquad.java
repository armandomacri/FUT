package mongo;

import bean.Player;
import bean.Squad;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class MongoSquad extends MongoConnection{
    private static final Logger logger = LogManager.getLogger(MongoSquad.class);
    private MongoCollection<Document> myColl;

    public boolean add(String userId, int index, Squad squad){
        myColl = db.getCollection("users");
        Document squadDoc = new Document();
        squadDoc.append("name", squad.getName());
        squadDoc.append("module", squad.getModule());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        squadDoc.append("date", df.format(squad.getDate()));

        Document playersDoc = new Document();
        Iterator iterator = squad.getPlayers().keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            String value = squad.getPlayers().get(key);
            playersDoc.append(key, value);
        }
        boolean result = true;
        squadDoc.append("players", playersDoc);
        try{
            if(index == -1){
                myColl.updateOne(
                        eq("_id", new ObjectId(userId)),
                        Updates.push("squads", squadDoc)
                );
            } else {
                myColl.updateOne(
                        new Document("_id", new ObjectId(userId)),
                        new Document("$set", new Document("squads."+index, squadDoc))
                );
            }
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }

        return result;
    }

    public boolean delete(String userId, int index){
        myColl = db.getCollection("users");
        boolean result = true;
        try {
            myColl.updateOne(
                    new Document("_id", new ObjectId(userId)),
                    new Document("$unset", new Document("squads."+index, 1))
            );

            myColl.updateOne(
                    new Document("_id", new ObjectId(userId)),
                    new Document("$pull", new Document("squads", null))
            );
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public ArrayList<Squad> getSquads(String id){
        myColl = db.getCollection("users");
        Document doc;
        try{
            doc = myColl.find(eq("_id",new ObjectId(id))).projection(fields(include("squads"), excludeId())).first();
        } catch (Exception e){
            doc = null;
        }

        if (doc == null)
            return null;
         return composeSquads(doc);
    }

    private ArrayList<Squad> composeSquads(Document doc){
        ArrayList<Document> squadsDoc = (ArrayList)doc.get("squads");
        //inserire se non ha squadra, inizializzatlo vuoto
        ArrayList<Squad> s = new ArrayList<>();
        SimpleDateFormat df;
        Date date = null;
        for (Document squad : squadsDoc) {
            Document playersDoc = (Document) squad.get("players");
            HashMap<String, String> x = new HashMap<>();
            for (Map.Entry<String, Object> curEntry : playersDoc.entrySet()) {
                x.put(curEntry.getKey(), (String) curEntry.getValue());
            }

            try {
                df = new SimpleDateFormat("dd/MM/yyyy");
                date = df.parse(squad.get("date").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Squad sq = new Squad(squad.get("name").toString(), squad.get("module").toString(),
                    date, x);
            s.add(sq);
        }
        return s;
    }

    public ArrayList<Document> SquadAnalytics(String country){
        ArrayList<Document> result = new ArrayList<>();
        myColl = db.getCollection("users");

        Consumer<Document> createDocuments = doc -> {result.add(doc);};

        Bson matchCountry = match(and(eq("country", country)));
        Bson unqindSquads = unwind("$squads");

        Bson distinctUserModules = new Document("$group", new Document("_id", new Document("user", "$_id").append("module",
                "$squads.module")));

        Bson groupModules = group("$_id.module", sum("use", 1));
        Bson sort = sort(descending("use"));
        Bson limit = limit(3);
        Bson project = project(fields(excludeId(),
                include("use"),
                computed("module", "$_id")
                )
        );

        myColl.aggregate(Arrays.asList(matchCountry, unqindSquads, distinctUserModules, groupModules, sort, limit, project)).
                forEach(createDocuments);
        return result;
    }

    /*
    db.users.aggregate(
  [
    //first step

        { $match: {"country": "Italy"}},

    //second step

        {$unwind: "$squads"},

    //forth step (distinct)

        {
          $group: {
            _id: { module: "$squads.module", user: "$_id"}
          }
        },

    //fifth step

        {
          $group: {
            _id: { module: "$_id.module"},
            num: {$sum: 1}
          }
        },

    //sixth step

        { $sort: {num:-1} },

    //seventh step

        { $limit: 3 },

    //formatting
        {
          $project: {
            _id:0,
            module: "$_id.module", use: "$num"
          }
        }
      ]
    );
 */

    @Override
    public void close(){
        mongoClient.close();
        //logger.info("Mongo close connection!");
    }

    public static void main(String[] args){
        MongoSquad ms = new MongoSquad();
        ArrayList<Document> prova = new ArrayList<>();
        prova  = ms.SquadAnalytics("Italy");
        System.out.println(prova);
        //ms.add("1", 1,new Squad("CIAOOOO", "7323", new Date()));
        //ms.delete("0", 7);
        //System.out.println(ms.getSquads("Arvel"));
    }
}
