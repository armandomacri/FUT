package configuration;

public class MongoConfig {
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
