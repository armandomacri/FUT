package mongo;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import configuration.LoadXmlConf;
import configuration.MongoConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class MongoConnection implements AutoCloseable{
    private static final Logger logger = LogManager.getLogger(MongoConnection.class);
    private static final String MONGO_CONFIG = "mongoConfig";
    private static final MongoConfig  mongoConfig = LoadXmlConf.getMongoConfigIstance(MONGO_CONFIG);
    protected static MongoClient mongoClient;
    protected static MongoDatabase db;

    static {
        //open connection
            switch (mongoConfig.mode){
                case "local":
                    mongoClient = MongoClients.create("mongodb://"+mongoConfig.mongoIp+":"
                                                        +mongoConfig.mongoPort);
                    break;
                case "replica":
                    mongoClient = MongoClients.create("mongodb://"+mongoConfig.getReplicaIp(0)+":"
                                                                    +mongoConfig.getReplicaPort(0)+","
                                                                    +mongoConfig.getReplicaIp(1)+":"
                                                                    +mongoConfig.getReplicaPort(1)+","
                                                                    +mongoConfig.getReplicaIp(2)+":"
                                                                    +mongoConfig.getReplicaPort(2)
                                                +"/?retryWrites=true&w=3&wtimeoutMS=5000&readPreference=nearest");
                    break;
                default:
                    mongoClient = MongoClients.create("mongodb://localhost:27017");
                    break;
            }
            db = mongoClient.getDatabase(mongoConfig.dbName).withWriteConcern(WriteConcern.MAJORITY).withReadPreference(ReadPreference.nearest());
    }

    abstract public void close();
}
