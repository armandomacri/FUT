package neo4j;

import configuration.LoadXmlConf;
import configuration.Neo4JConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

abstract class Neo4jConnection implements AutoCloseable{
    private static final Logger logger = LogManager.getLogger(Neo4jConnection.class);
    private static final String NEO4J_CONFIG = "neo4jConfig";
    private static final Neo4JConfig neo4JConfig = LoadXmlConf.getNeo4jConfigIstance(NEO4J_CONFIG);
    public static Driver driver;

    static{
        driver =  GraphDatabase.driver("bolt://"+neo4JConfig.neo4jIp+":"+neo4JConfig.neo4jPort, AuthTokens.basic(neo4JConfig.username, neo4JConfig.password));
    }

    public boolean checkConnection(){
        boolean result = true;
        try {
            driver.verifyConnectivity();
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

}
