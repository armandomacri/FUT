package mongo;

import bean.Challenge;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.sun.source.tree.Tree;
import neo4j.Neo4jUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Sorts.descending;

public class MongoChallenge extends MongoConnection{
    private MongoCollection<Document> myColl;
    private static final Logger logger = LogManager.getLogger(MongoChallenge.class);

    public String insertChallenge (Challenge newChallenge){
        myColl = db.getCollection("challenge");
        Document homeDoc = new Document("id", newChallenge.getHome()).append("username", newChallenge.getHomeUser()).append("score", newChallenge.getHomeScore());
        Document awayDoc = new Document("id", newChallenge.getAway()).append("username", newChallenge.getAwayUser()).append("score", newChallenge.getAwayScore());
        Document doc = new Document("date", newChallenge.getDate()).append("home", homeDoc).append("away", awayDoc).append("points_earned/lost", newChallenge.getPoints());
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
                Challenge c = new Challenge(challenge.getObjectId("_id").toString(), homeDoc.get("id").toString(), homeDoc.get("username").toString(),awayDoc.get("id").toString(), awayDoc.get("username").toString(), challenge.get("date").toString(), Integer.parseInt(homeDoc.get("score").toString()), Integer.parseInt(awayDoc.get("score").toString()), Integer.parseInt(challenge.get("points_earned/lost").toString()));
                results.add(c);
            }
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            results = null;
        }
        return results;
    }

    public TreeMap<Date, Integer> ChallengesPerDay(){
        TreeMap<Date, Integer> result = new TreeMap<>();
        try {
            myColl = db.getCollection("challenge");
            TreeMap<Date, Integer> finalResult = result;
            Consumer<Document> createDocuments = doc -> {
                try {
                    finalResult.put(new SimpleDateFormat("dd/MM/yyyy").parse(doc.get("date").toString()), Integer.parseInt(doc.get("numChallenges").toString()));
                } catch (ParseException e) {
                    logger.error("Exception occurred: ", e);
                }
            };
            LocalDate today = LocalDate.now();
            String toFind;
            if (today.getMonthValue() == 1){
                toFind = "/" + 12 + "/";
            }else{
                toFind = "/" + (today.getMonthValue() - 1) + "/";
            }
            Bson matchDate = match(and(regex("date",".*" + Pattern.quote(toFind) + ".*")));
            Bson groupDate = group("$date",
                    sum("numChallenges", 1)
            );
            Bson limit = limit(31);
            Bson project = project(fields(excludeId(),
                    computed("date", "$_id"),
                    include("numChallenges")
                    )
            );
            myColl.aggregate(Arrays.asList(matchDate, groupDate, limit, project)).forEach(createDocuments);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = null;
        }
        return result;
    }

    @Override
    public void close(){
        mongoClient.close();
    }

    public static void main(String[] args) {
        MongoChallenge mc = new MongoChallenge();
        System.out.println(mc.ChallengesPerDay());
    }
}

