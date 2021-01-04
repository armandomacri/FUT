package mongo;


import bean.Challenge;
import bean.Player;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class MongoChallenge extends MongoConnection{
    private MongoCollection<Document> myColl;

    public String insertChallenge (Challenge newChallenge){
        myColl = db.getCollection("challenge");
        Document doc = new Document("_id", Integer.parseInt(newChallenge.getChallengeId())).append("home", newChallenge.getHome()).append("date", newChallenge.getDate()).append("away", newChallenge.getAway()).append("home_score", newChallenge.getHomeScore()).append("away_score", newChallenge.getAwayScore()).append("points_earned/lost", newChallenge.getPoints());
        myColl.insertOne(doc);
        String mongoId = doc.get("_id").toString();
        return mongoId;
    }

    public ArrayList<Challenge> findUserChallenge (String userID){
        myColl = db.getCollection("challenge");
        ArrayList<Challenge> results = new ArrayList<>();

        try (MongoCursor<Document> cursor = myColl.find(or(eq("home", userID), eq("away", userID))).iterator())
        {
            while (cursor.hasNext())
            {
                Document challenge = cursor.next();
                System.out.println(challenge);
                Challenge c = null;
                c = new Challenge(challenge.get("_id").toString(), challenge.get("home").toString(), challenge.get("date").toString(), challenge.get("away").toString(), Integer.parseInt(challenge.get("home_score").toString()), Integer.parseInt(challenge.get("away_score").toString()), Integer.parseInt(challenge.get("points_earned/lost").toString()));
                results.add(c);
            }
        }
        return results;
    }

}

