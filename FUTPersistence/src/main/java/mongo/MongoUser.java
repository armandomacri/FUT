package mongo;

import bean.*;
import com.mongodb.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.inc;

public class MongoUser extends MongoConnection{
    private static final Logger logger = LogManager.getLogger(MongoUser.class);
    private MongoCollection<Document> myColl;

    public String add(String firstName, String lastName, String username, String country, String joinDate, String password){
        myColl = db.getCollection("users");
        String id;
        Document user = new Document("username", username)
                .append("first_name", firstName)
                .append("last_name", lastName)
                .append("country", country)
                .append("join_date", joinDate)
                .append("password", password)
                .append("score", 0)
                .append("squads", new ArrayList<>());
        try {
            myColl.insertOne(user);
            id = user.getObjectId("_id").toString();
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            id = null;
        }
        return id;
    }

    public void delete(String id){
        myColl = db.getCollection("users");
        myColl.deleteOne(eq("_id", new ObjectId(id)));
    }

    public User getUser(String username){
        myColl = db.getCollection("users");
        Document doc = myColl.find(eq("username", username)).first();
        return composeUser(doc);

    }

    public ArrayList<User> findUsers(String toFind, String userId) {
        myColl = db.getCollection("users");
        ArrayList<User> users = new ArrayList<>();

        try (MongoCursor<Document> cursor = myColl.find(and(regex("username",".*" + Pattern.quote(toFind) + ".*", "-i"), ne("_id", new ObjectId(userId)))).iterator())
        {
            while (cursor.hasNext())
            {
                Document userDoc = cursor.next();
                User user = composeUser(userDoc);
                users.add(user);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    private User composeUser(Document doc){
        //no user found
        if (doc == null)
            return null;

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = df.parse(doc.get("join_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //ricostruisco le squadre dell'utente
        ArrayList<Document> squadsDoc = (ArrayList)doc.get("squads");
        //inserire se non ha squadra, inizializzatlo vuoto
        ArrayList<Squad> s = new ArrayList<>();
        if(squadsDoc.size() == 0){
          s = null;
        }
        else {
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
        }

        return new User(doc.get("username").toString(), doc.get("first_name").toString(),
                doc.get("last_name").toString(), doc.getObjectId("_id").toString(),
                doc.get("country").toString(), date, doc.get("password").toString(), s, (int)Double.parseDouble(doc.get("score").toString()));
    }

    public Integer countElement(){
        myColl = db.getCollection("users");
        return Math.toIntExact(myColl.countDocuments());
    }

    public boolean updateScore (String userId, int points){
        myColl = db.getCollection("users");
        boolean result = true;
        try {
            myColl.updateOne(eq("_id", new ObjectId(userId)), inc("score", points));
        } catch (Exception e){
            result = false;
        }


        return result;

    }

    public ArrayList<Document> getUserPerCountryLastYear(){
        myColl = db.getCollection("users");
        ArrayList<Document> result = new ArrayList<>();
        Consumer<Document> createDocuments = doc -> {result.add(doc);};
        Bson groupCountry = group("$country", sum("numUsers", 1));
        Bson order = sort(descending("numUser"));
        Bson lim = limit(10);
        myColl.aggregate(Arrays.asList(groupCountry, order, lim)).forEach(createDocuments);;
        return result;

    }

    @Override
    public void close(){
        mongoClient.close();
        //logger.info("Mongo close connection!");
    }

    public static void main(String[] args){
        MongoUser mongoUser = new MongoUser();
        System.out.println(mongoUser.getUserPerCountryLastYear());
        //String id = mongoUser.add("armando", "armando", "Armando9876", "italy", "8/01/2021", "armando");
        //mongoUser.delete(id);
        System.out.println(mongoClient.getClusterDescription().getType());

        //System.out.println(mongoClient.getClusterDescription().getType().compareTo(ClusterType.UNKNOWN));
    }


}




