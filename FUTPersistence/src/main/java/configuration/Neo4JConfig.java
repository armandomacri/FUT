package configuration;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ConfigurationService")
public class Neo4JConfig {
    @XStreamAlias("Neo4jIp")
    public String neo4jIp;
    @XStreamAlias("Neo4jPort")
    public String neo4jPort;
    @XStreamAlias("Username")
    public String username;
    @XStreamAlias("Password")
    public String password;

    @Override
    public String toString(){

        return "CONFIGURATION NEO4J\n" +
                "ip: " + neo4jIp + "\n" +
                "port: " + neo4jPort + "\n" +
                "username: " + username +"\n" +
                "password: " + password + "\n";
    }
}
