package configuration;

import java.io.Serializable;

public class MongoConfig implements Serializable {
    public String mongoIp;
    public int mongoPort;
    public String dbName;

    @Override
    public String toString(){

        return "CONFIGURATION MOMGO\n" +
                "ip: " + mongoIp + "\n" +
                "port: " + mongoPort +"\n" +
                "db name: " + dbName;
    }


}
