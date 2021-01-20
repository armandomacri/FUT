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
        Document squadDoc = new Document();
        squadDoc.append("name", squad.getName());
        squadDoc.append("module", squad.getModule());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        squadDoc.append("date", df.format(squad.getDate()));

        Document playersDoc = new Document();
        Iterator iterator = squad.getPlayers().keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            Player value = squad.getPlayers().get(key);
            playersDoc.append(key, new Document("player_name", value.getPlayerExtendedName())
                                                .append("revision", value.getRevision())
                                                .append("overall", value.getOverall())
                                                .append("nationality", value.getNationality())
                                                .append("league", value.getLeague())
                                                .append("position", value.getPosition()));
        }
        boolean result = true;
        squadDoc.append("players", playersDoc);
        try{
            myColl = db.getCollection("users");
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
        boolean result = true;
        try {
            myColl = db.getCollection("users");
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
        Document doc;
        try{
            myColl = db.getCollection("users");
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
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        for (Document squad : squadsDoc) {
            Document playersDoc = (Document) squad.get("players");
            HashMap<String, Player> x = new HashMap<>();
            for (Map.Entry<String, Object> curEntry : playersDoc.entrySet()) {
                String pos = curEntry.getKey();
                Document p = (Document) curEntry.getValue();
                x.put(pos, new Player(p.get("player_name").toString(), p.get("revision").toString(), p.get("nationality").toString(), Integer.parseInt(p.get("overall").toString()), p.get("league").toString(), p.get("position").toString()));
            }

            try {
                date = df.parse(squad.get("date").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Squad sq = new Squad(squad.get("name").toString(), squad.get("module").toString(), date, x);
            s.add(sq);
        }
        return s;
    }

    public ArrayList<Document> SquadAnalytics(String country){
        ArrayList<Document> result = new ArrayList<>();


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

        try {
            myColl = db.getCollection("users");
            myColl.aggregate(Arrays.asList(matchCountry, unqindSquads, distinctUserModules, groupModules, sort, limit, project)).
                    forEach(createDocuments);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            return null;
        }

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
        ArrayList<Document> prova;
        prova  = ms.SquadAnalytics("Italy");
        System.out.println(prova);
        //ms.add("1", 1,new Squad("CIAOOOO", "7323", new Date()));
        //ms.delete("0", 7);
        //System.out.println(ms.getSquads("Arvel"));
    }
}
