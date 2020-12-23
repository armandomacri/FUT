package mongo;

import bean.*;
import com.mongodb.client.*;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        User newUser = new User(doc.get("username").toString(), doc.get("first_name").toString(),
                                doc.get("last_name").toString(), doc.get("_id").toString(),
                                doc.get("country").toString(), date, doc.get("password").toString());

        return newUser;
    }

    public Integer countElement(String collection){

        MongoCollection<Document> myColl = db.getCollection(collection);
        //query
        Integer i = Math.toIntExact(myColl.countDocuments());

        return i;
    }

    public Player show_player_information(Integer id){

        MongoCollection<Document> myColl = db.getCollection("players");
        //query
        Document doc = myColl.find(eq("futbin_id",id)).first();

        //User newUser = new User(doc.get("first_name").toString(),//da completare con gli altri attributi);
        System.out.println(doc.toJson());
        return new Player();
    }

    public void closeConnection(){
        mongoClient.close();
    }

    public static void main(String[] args){
        ProvaQuery m = new ProvaQuery();
        m.show_player_information(1);
    }
}




