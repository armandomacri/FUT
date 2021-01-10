package mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import configuration.LoadXmlConf;
import configuration.MongoConfig;

abstract class MongoConnection implements AutoCloseable{

    private static final String MONGO_CONFIG = "mongoConfig";
    private static final MongoConfig  mongoConfig = LoadXmlConf.getMongoConfigIstance(MONGO_CONFIG);
    protected static final MongoClient mongoClient;
    protected static final MongoDatabase db;

    static {
        //open connection
        assert mongoConfig != null;
        switch (mongoConfig.mode){
            case "local":
                mongoClient = MongoClients.create("mongodb://"+mongoConfig.mongoIp+":"+mongoConfig.mongoPort);
                break;
            case "replica":
                ConnectionString uri = new ConnectionString("mongodb://"+mongoConfig.getReplicaIp(0)+":"+mongoConfig.getReplicaPort(0)+","+mongoConfig.getReplicaIp(1)+":"+mongoConfig.getReplicaPort(1)+","+mongoConfig.getReplicaIp(2)+":"+mongoConfig.getReplicaPort(2));
                MongoClientSettings mongoClientSettings = MongoClientSettings.builder().
                        applyConnectionString(uri).
                        retryWrites(true).
                        writeConcern(WriteConcern.MAJORITY).
                        readPreference(ReadPreference.nearest()).build();
                mongoClient = MongoClients.create(mongoClientSettings);
                break;
            default:
                mongoClient = MongoClients.create("mongodb://localhost:27017");
        }

        //aggiungere impostazioni per lettura e scrittura nel db
        db = mongoClient.getDatabase(mongoConfig.dbName);
    }

    @Override
    public void close(){
        mongoClient.close();
        //logger.info("Mongo close connection!");
    }
}
