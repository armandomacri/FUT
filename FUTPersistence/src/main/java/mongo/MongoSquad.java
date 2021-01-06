package mongo;

import bean.Player;
import bean.Squad;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;

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
        myColl = db.getCollection("users");
        Document doc = myColl.find(eq("username",username)).projection(fields(include("squads"), excludeId())).first();
        return composeSquads(doc);
    }

    private ArrayList<Squad> composeSquads(Document doc){
        ArrayList<Document> squadsDoc = (ArrayList)doc.get("squads");
        //inserire se non ha squadra, inizializzatlo vuoto
        ArrayList<Squad> s = new ArrayList<>();
        SimpleDateFormat df;
        Date date = null;
        for (Document squad : squadsDoc){
            HashMap<String, Player> pos = new HashMap<>();
            Map<String, String> map = (Map)squad.get("players");
            Iterator iterator = map.keySet().iterator();
            MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
            while(iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = map.get(key);

                Player x = mongoPlayerCard.findById(Integer.parseInt(value));
                if(x==null) //giocatore non caricato nel sistema
                    continue;
                pos.put(key, x);
            }
            try {
                df = new SimpleDateFormat("dd.MM.yyyy");
                date = df.parse(squad.get("date").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Squad sq = new Squad(squad.get("name").toString(), squad.get("module").toString(),
                    date, pos);
            s.add(sq);
        }
        return s;
    }



    public static void main(String[] args){
        MongoSquad ms = new MongoSquad();
        ms.getSquads("Arvel");
        //ms.add("1", 1,new Squad("CIAOOOO", "7323", new Date()));
        //ms.delete("0", 7);
        //System.out.println(ms.getSquads("Arvel"));
    }
}
