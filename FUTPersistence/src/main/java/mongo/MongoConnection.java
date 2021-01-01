package mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public abstract class MongoConnection implements AutoCloseable{
    protected static final MongoClient mongoClient;
    protected static final MongoDatabase db;

    static {
        //open connection
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("futdb");
    }

    @Override
    public void close(){
        mongoClient.close();
    }
}
