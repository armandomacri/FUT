package mongo;

import bean.Squad;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoSquad {
    private static final MongoClient mongoClient;
    private static final MongoDatabase db;

    static {
        //open connection
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("futdb");
    }

    public void add(int userId, int index, Squad squad){
        MongoCollection<Document> myColl = db.getCollection("users");

        Document squadDoc = new Document();
        squadDoc.append("name", squad.getName());
        squadDoc.append("module", squad.getModule());
        squadDoc.append("date", squad.getDate());

        Document playersDoc = new Document();
        //squad.
        for (int i = 0; i < squad.getPlayers().size()-2; i++){

            String playerID = squad.getPlayers().get(i).getPlayerId();
            //if(squad.getPlayers().)


        }

        squadDoc.append("players", playersDoc);


        if(index == -1){
            //add new squad
        } else{
            myColl.updateOne(eq("_id", userId), set("age", 25));
        }

    }
}
