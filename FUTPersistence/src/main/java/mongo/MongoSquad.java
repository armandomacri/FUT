package mongo;

import bean.Squad;
import bean.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import static com.mongodb.client.model.Filters.eq;

public class MongoSquad extends MongoConnection{
    private MongoCollection<Document> myColl;

    public void add(String userId, int index, Squad squad){
        myColl = db.getCollection("users");
        Document squadDoc = new Document();
        squadDoc.append("name", squad.getName());
        squadDoc.append("module", squad.getModule());
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
        String date = df.format(squad.getDate());
        squadDoc.append("date", date);

        Document playersDoc = new Document();
        Iterator iterator = squad.getPlayers().keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next().toString();
            String value = squad.getPlayers().get(key).getPlayerId();
            playersDoc.append(key, value);
        }

        squadDoc.append("players", playersDoc);

        if(index == -1){
            myColl.updateOne(
                    eq("_id", userId),
                    Updates.addToSet("squads", squadDoc)
            );
        } else {
            myColl.updateOne(
                    new Document("_id", userId),
                    new Document("$set", new Document("squads."+index, squadDoc))
            );
        }
    }

    public void delete(String userId, int index){
        myColl = db.getCollection("users");
        myColl.updateOne(
                new Document("_id", userId),
                new Document("$unset", new Document("squads."+index, 1))
        );

        myColl.updateOne(
                new Document("_id", userId),
                new Document("$pull", new Document("squads", null))
        );
    }

    public ArrayList<Squad> getSquads(String username){
        MongoUser mongoUser = new MongoUser();
        User user = mongoUser.getUser(username);
        return user.getSquads();
    }



    public static void main(String[] args){
        MongoSquad ms = new MongoSquad();
        //ms.add("1", 1,new Squad("CIAOOOO", "7323", new Date()));
        //ms.delete("0", 7);
        System.out.println(ms.getSquads("Arvel"));
    }
}
