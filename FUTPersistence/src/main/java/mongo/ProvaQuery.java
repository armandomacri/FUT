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

    public void user_registration(User u){
        //open connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("futdb");
        MongoCollection<Document> myColl = db.getCollection("users");

        Document user = new Document("username", u.getUsername())
                .append("first_name", u.getFirstName())
                .append("last_name", u.getLastName())
                .append("country", u.getCountry())
                .append("join_date", u.getJoinDate());
        myColl.insertOne(user);
        //close connection
        mongoClient.close();
    }

    public User show_profile_information(String username){
        //open connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("futdb");
        MongoCollection<Document> myColl = db.getCollection("users");
        //query
        Document doc = myColl.find(eq("username",username)).first();
        //close connection
        mongoClient.close();

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
        //open connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("futdb");
        MongoCollection<Document> myColl = db.getCollection(collection);
        //query
        Integer i = Math.toIntExact(myColl.countDocuments());
        //close connection
        mongoClient.close();
        return i;
    }

    public Player show_player_information(Integer id){
        //open connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("futdb");
        MongoCollection<Document> myColl = db.getCollection("players");
        //query
        Document doc = myColl.find(eq("futbin_id",id)).first();
        //close connection
        mongoClient.close();

        //User newUser = new User(doc.get("first_name").toString(),//da completare con gli altri attributi);
        System.out.println(doc.toJson());
        return new Player();
    }

    public static void main(String[] args){
        ProvaQuery m = new ProvaQuery();
        m.show_player_information(1);
    }
}




