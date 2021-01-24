package neo4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import java.util.HashMap;

public class Neo4jAdmin extends Neo4jConnection{
    private static final Logger logger = LogManager.getLogger(Neo4jPlayerCard.class);

    public HashMap<String, String> mostActiveUser(){
        HashMap<String, String> userMap;
        try (Session session = driver.session()) {
            userMap = session.readTransaction((TransactionWork<HashMap<String, String>>) tx -> {
                Result result = tx.run("MATCH (u:User)-[r]->()\n" +
                        "RETURN u.id, u.username AS Username, COUNT(r) AS numAction\n" +
                        "ORDER BY numAction DESC\n" +
                        "LIMIT 10");
                HashMap<String, String> userMapResult = new HashMap<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    userMapResult.put(r.get("Username").asString(), r.get("numAction").toString());
                }
                return userMapResult;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            userMap = null;
        }
        return userMap;
    }

    public HashMap<String, String> mostLikedPlayer(){
        HashMap<String, String> playerMap;
        try (Session session = driver.session()) {
            playerMap = session.readTransaction((TransactionWork<HashMap<String, String>>) tx -> {
                Result result = tx.run("MATCH path=(p:PlayerCard)-[l:Like]-(u:User)\n" +
                        "RETURN p.name AS PlayerName, p.id, COUNT(l) AS numLike \n" +
                        "ORDER BY numLike DESC\n" +
                        "LIMIT 10");
                HashMap<String, String> playerMapResult = new HashMap<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    playerMapResult.put(r.get("PlayerName").asString(), r.get("numLike").toString());

                }
                return playerMapResult;
            });
        }catch (Exception e){
            logger.error("Exception occurred: ", e);
            playerMap = null;
        }
        return playerMap;
    }

    @Override
    public void close(){
        if(driver != null)
            driver.close();
    }
}
