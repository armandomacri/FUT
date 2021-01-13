package mongo;

import bean.Challenge;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import neo4j.Neo4jUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.function.DoubleBinaryOperator;

import static com.mongodb.client.model.Filters.*;

public class MongoChallenge extends MongoConnection{
    private MongoCollection<Document> myColl;
    private static final Logger logger = LogManager.getLogger(MongoChallenge.class);

    public String insertChallenge (Challenge newChallenge){
        myColl = db.getCollection("challenge");

        Document homeDoc = new Document("id", newChallenge.getHome()).append("username", newChallenge.getHomeUser()).append("score", newChallenge.getHomeScore());
        Document awayDoc = new Document("id", newChallenge.getAway()).append("username", newChallenge.getAwayUser()).append("score", newChallenge.getAwayScore());
        Document doc = new Document("date", newChallenge.getDate()).append("home", homeDoc).append("away", awayDoc).append("points_earned/lost", newChallenge.getPoints());
        String id = null;
        try {
            myColl.insertOne(doc);
            id = doc.getObjectId("_id").toString();
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
             id= null;
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
                System.out.println(challenge);
                Document homeDoc = (Document) challenge.get("home");
                Document awayDoc = (Document) challenge.get("away");
                Challenge c = new Challenge(challenge.getObjectId("_id").toString(), homeDoc.get("id").toString(), homeDoc.get("username").toString(),awayDoc.get("id").toString(), awayDoc.get("username").toString(), challenge.get("date").toString(), Integer.parseInt(homeDoc.get("score").toString()), Integer.parseInt(awayDoc.get("score").toString()), Integer.parseInt(challenge.get("points_earned/lost").toString()));
                results.add(c);
            }
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            results = null;
        }
        return results;
    }

    @Override
    public void close(){
        mongoClient.close();
    }

}

