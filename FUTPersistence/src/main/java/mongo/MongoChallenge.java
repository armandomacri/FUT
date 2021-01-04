package mongo;


import bean.Challenge;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoChallenge extends MongoConnection{
    private MongoCollection<Document> myColl;

    public String insertChallenge (Challenge newChallenge){
        myColl = db.getCollection("challenge");
        Document doc = new Document("_id", Integer.parseInt(newChallenge.getChallengeId())).append("home", newChallenge.getHome()).append("date", newChallenge.getDate()).append("away", newChallenge.getAway()).append("home_score", newChallenge.getHomeScore()).append("away_score", newChallenge.getAwayScore()).append("points_earned/lost", newChallenge.getPoints());
        myColl.insertOne(doc);
        String mongoId = doc.get("_id").toString();
        return mongoId;
    }


}

