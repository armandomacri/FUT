package mongo;

import bean.Challenge;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Sorts.ascending;

public class MongoChallenge extends MongoConnection{
    private MongoCollection<Document> myColl;
    private static final Logger logger = LogManager.getLogger(MongoChallenge.class);

    public String insertChallenge (Challenge newChallenge){
        myColl = db.getCollection("challenge");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = newChallenge.getDate();
        try {
            date = formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            logger.error("Exception occurred: ", e);
            e.printStackTrace();
        }
        Document homeDoc = new Document("id", newChallenge.getHome()).append("username", newChallenge.getHomeUser()).append("score", newChallenge.getHomeScore());
        Document awayDoc = new Document("id", newChallenge.getAway()).append("username", newChallenge.getAwayUser()).append("score", newChallenge.getAwayScore());
        Document doc = new Document("date", date).append("home", homeDoc).append("away", awayDoc).append("points_earned/lost", newChallenge.getPoints());
        String id;
        try {
            myColl.insertOne(doc);
            id = doc.getObjectId("_id").toString();
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
             id = null;
        }
        return id;
    }

    public ArrayList<Challenge> findUserChallenge (String userID){
        myColl = db.getCollection("challenge");
        ArrayList<Challenge> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = myColl.find(or(eq("home.id", userID), eq("away.id", userID))).iterator())
        {
            while (cursor.hasNext())
            {
                Document challenge = cursor.next();
                Document homeDoc = (Document) challenge.get("home");
                Document awayDoc = (Document) challenge.get("away");
                Challenge c = new Challenge(challenge.getObjectId("_id").toString(), homeDoc.get("id").toString(), homeDoc.get("username").toString(),awayDoc.get("id").toString(), awayDoc.get("username").toString(), challenge.getDate("date"), Integer.parseInt(homeDoc.get("score").toString()), Integer.parseInt(awayDoc.get("score").toString()), Integer.parseInt(challenge.get("points_earned/lost").toString()));
                results.add(c);
            }
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            results = null;
        }
        return results;
    }

    public ArrayList<Document> challengesPerDay(){
        myColl = db.getCollection("challenge");
        ArrayList<Document> result = new ArrayList<>();
        //ArrayList<Document> result = new ArrayList<>();
        Consumer<Document> createDocuments = doc -> {result.add(doc);};
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date monthAgo = cal.getTime();
        Bson matchDate = match(and(lt("date", yesterday), gt("date", monthAgo)));
        Bson groupDate = group("$date", sum("numChallenges", 1));
        Bson sortDate = sort(ascending("_id"));

        Bson project = project(fields(excludeId(),
                computed("date", "$_id"),
                include("numChallenges"))
        );
        Bson limit = limit(31);
        myColl.aggregate(Arrays.asList(matchDate, groupDate, sortDate, limit, project)).forEach(createDocuments);


        return result;
    }

    @Override
    public void close(){
        mongoClient.close();
    }

    public static void main(String[] args) {
        MongoChallenge mc = new MongoChallenge();
        System.out.println(mc.challengesPerDay());
    }
}

