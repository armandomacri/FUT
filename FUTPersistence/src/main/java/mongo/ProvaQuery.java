package mongo;

import bean.*;
import com.mongodb.client.*;
import org.bson.Document;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class ProvaQuery {
    private static final MongoClient mongoClient;
    private static final MongoDatabase db;

    static {
        //open connection
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("futdb");
    }

    public String user_registration(String firstName, String lastName, String username, String country, Date joinDate, String password){

        MongoCollection<Document> myColl = db.getCollection("users");

        Document user = new Document("username", username)
                .append("first_name", firstName)
                .append("last_name", lastName)
                .append("country", country)
                .append("join_date", joinDate)
                .append("password", password);
        myColl.insertOne(user);

        String mongoId = user.getObjectId("_id").toString();
        return mongoId;
    }

    public User show_profile_information(String username){

        MongoCollection<Document> myColl = db.getCollection("users");
        //query
        Document doc = myColl.find(eq("username",username)).first();

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
        HashMap<String, Player> pos = new HashMap<>();
        ArrayList<Squad> s = new ArrayList<>();
        for (Document squad : squadsDoc){
            Map<String, String> map = (Map)squad.get("players");
            Iterator iterator = map.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next().toString();
                String value = map.get(key);
                pos.put(key, findById(value));
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

        User newUser = new User(doc.get("username").toString(), doc.get("first_name").toString(),
                doc.get("last_name").toString(), doc.get("_id").toString(),
                doc.get("country").toString(), date, doc.get("password").toString(), s);

        System.out.println(newUser);

        return newUser;
    }

    public Integer countElement(String collection){
        MongoCollection<Document> myColl = db.getCollection(collection);
        Integer i = Math.toIntExact(myColl.countDocuments());
        return i;
    }

    public Player findById(String id){

        MongoCollection<Document> myColl = db.getCollection("players");
        //query
        Document playerDoc = myColl.find(eq("_id",id)).first();

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
        System.out.println(playerDoc.get("position").toString());
        if (playerDoc.get("position").toString().equals("GK")) {
            return new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, Integer.parseInt(playerDoc.get("height").toString()), Integer.parseInt(playerDoc.get("weight").toString()), date2, Integer.parseInt(playerDoc.get("gk_diving").toString()), Integer.parseInt(playerDoc.get("gk_reflexes").toString()), Integer.parseInt(playerDoc.get("gk_handling").toString()), Integer.parseInt(playerDoc.get("gk_speed").toString()), Integer.parseInt(playerDoc.get("gk_kicking").toString()), Integer.parseInt(playerDoc.get("gk_positoning").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
        }
        else {
            return new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, Integer.parseInt(playerDoc.get("height").toString()), Integer.parseInt(playerDoc.get("weight").toString()), date2, Integer.parseInt(playerDoc.get("pace").toString()), Integer.parseInt(playerDoc.get("dribbling").toString()), Integer.parseInt(playerDoc.get("shooting").toString()), Integer.parseInt(playerDoc.get("passing").toString()), Integer.parseInt(playerDoc.get("defending").toString()), Integer.parseInt(playerDoc.get("physicality").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
        }
    }

    public ArrayList<Player> findPlayers (String toFind) {

        ArrayList<Player> results = new ArrayList<>();
        MongoCollection<Document> myColl = db.getCollection("player_cards");
        System.out.println(toFind);
        try (MongoCursor<Document> cursor = myColl.find(regex("player_name",".*" + Pattern.quote(toFind) + ".*", "-i")).iterator())
        {
            while (cursor.hasNext())
            {
                Document playerDoc = cursor.next();
                //System.out.println(cursor.next());
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
                System.out.println(playerDoc.get("position").toString());
                if (playerDoc.get("position").toString().equals("GK")) {
                    p = new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, Integer.parseInt(playerDoc.get("height").toString()), Integer.parseInt(playerDoc.get("weight").toString()), date2, Integer.parseInt(playerDoc.get("gk_diving").toString()), Integer.parseInt(playerDoc.get("gk_reflexes").toString()), Integer.parseInt(playerDoc.get("gk_handling").toString()), Integer.parseInt(playerDoc.get("gk_speed").toString()), Integer.parseInt(playerDoc.get("gk_kicking").toString()), Integer.parseInt(playerDoc.get("gk_positoning").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
                }
                else {
                    p = new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, Integer.parseInt(playerDoc.get("height").toString()), Integer.parseInt(playerDoc.get("weight").toString()), date2, Integer.parseInt(playerDoc.get("pace").toString()), Integer.parseInt(playerDoc.get("dribbling").toString()), Integer.parseInt(playerDoc.get("shooting").toString()), Integer.parseInt(playerDoc.get("passing").toString()), Integer.parseInt(playerDoc.get("defending").toString()), Integer.parseInt(playerDoc.get("physicality").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
                }

                results.add(p);
            }
        }

        return results;
    }

    public void closeConnection(){
        mongoClient.close();
    }

    public static void main(String[] args){
        ProvaQuery m = new ProvaQuery();
        //m.show_player_information(1);
        System.out.println(m.findById("1"));
    }
}




