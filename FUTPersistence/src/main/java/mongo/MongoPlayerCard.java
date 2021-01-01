package mongo;

import bean.Player;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class MongoPlayerCard extends MongoConnection{
    private MongoCollection<Document> myColl;

    public ArrayList<Player> findPlayers (String toFind) {
        myColl = db.getCollection("player_cards");
        ArrayList<Player> results = new ArrayList<>();
        System.out.println(toFind);
        try (MongoCursor<Document> cursor = myColl.find(regex("player_extended_name",".*" + Pattern.quote(toFind) + ".*", "-i")).iterator())
        {
            while (cursor.hasNext())
            {
                Document playerDoc = cursor.next();
                //System.out.println(cursor.next());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = df.parse(playerDoc.get("date_of_birth").toString());
                    date2 = df.parse(playerDoc.get("added_date").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String[] images = playerDoc.get("images").toString().split(",");
                Player p = null;
                if (playerDoc.get("position").toString().equals("GK")) {
                    p = new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, playerDoc.get("height").toString(), playerDoc.get("weight").toString(), date2, Integer.parseInt(playerDoc.get("gk_diving").toString()), Integer.parseInt(playerDoc.get("gk_reflexes").toString()), Integer.parseInt(playerDoc.get("gk_handling").toString()), Integer.parseInt(playerDoc.get("gk_speed").toString()), Integer.parseInt(playerDoc.get("gk_kicking").toString()), Integer.parseInt(playerDoc.get("gk_positoning").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
                }
                else {
                    p = new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, playerDoc.get("height").toString(), playerDoc.get("weight").toString(), date2, Integer.parseInt(playerDoc.get("pace").toString()), Integer.parseInt(playerDoc.get("dribbling").toString()), Integer.parseInt(playerDoc.get("shooting").toString()), Integer.parseInt(playerDoc.get("passing").toString()), Integer.parseInt(playerDoc.get("defending").toString()), Integer.parseInt(playerDoc.get("physicality").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
                }

                results.add(p);
            }
        }

        return results;
    }

    public Player findById(Integer id){
        myColl = db.getCollection("player_cards");
        //query
        Document playerDoc = myColl.find(eq("_id", id)).first();

        /**********************************************************************/
        if(playerDoc == null) //player ancora non caricato
            return null;
        /**********************************************************************/

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = df.parse(playerDoc.get("date_of_birth").toString());
            date2 = df.parse(playerDoc.get("added_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] images = playerDoc.get("images").toString().split(",");
        Player p = null;
        if (playerDoc.get("position").toString().equals("GK"))
            return new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, playerDoc.get("height").toString(), playerDoc.get("weight").toString(), date2, Integer.parseInt(playerDoc.get("gk_diving").toString()), Integer.parseInt(playerDoc.get("gk_reflexes").toString()), Integer.parseInt(playerDoc.get("gk_handling").toString()), Integer.parseInt(playerDoc.get("gk_speed").toString()), Integer.parseInt(playerDoc.get("gk_kicking").toString()), Integer.parseInt(playerDoc.get("gk_positoning").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
        else
            return new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, playerDoc.get("height").toString(), playerDoc.get("weight").toString(), date2, Integer.parseInt(playerDoc.get("pace").toString()), Integer.parseInt(playerDoc.get("dribbling").toString()), Integer.parseInt(playerDoc.get("shooting").toString()), Integer.parseInt(playerDoc.get("passing").toString()), Integer.parseInt(playerDoc.get("defending").toString()), Integer.parseInt(playerDoc.get("physicality").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
    }
}
