package configuration;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;

@XStreamAlias("ConfigurationService")
public class MongoConfig {
    public String mode;
    @XStreamAlias("MongoIp")
    public String mongoIp;
    @XStreamAlias("MongoPort")
    public int mongoPort;
    @XStreamAlias("ReplicaSet")
    public List<Replica> replicaSetsList;
    @XStreamAlias("DBName")
    public String dbName;

    @Override
    public String toString(){

        return "CONFIGURATION MOMGO\n" +
                "mode: " + mode + "\n" +
                "ip: " + mongoIp + "\n" +
                "port: " + mongoPort +"\n" +
                "db name: " + dbName + "\n" +
                replicaSetsList;
    }

    @XStreamAlias("Replica")
    class Replica{
        public String ip;
        public String port;

        @Override
        public String toString(){
            return "Replica: IP: " + ip + " PORT: " +port;
        }
    }

    public String getReplicaIp(int index){
        return replicaSetsList.get(index).ip;
    }
    public String getReplicaPort(int index){
        return replicaSetsList.get(index).port;
    }
}
