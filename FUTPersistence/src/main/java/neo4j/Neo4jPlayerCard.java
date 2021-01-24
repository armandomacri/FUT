package neo4j;

import bean.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.ArrayList;
import static org.neo4j.driver.Values.parameters;

public class Neo4jPlayerCard extends Neo4jConnection{
    private static final Logger logger = LogManager.getLogger(Neo4jPlayerCard.class);

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public boolean createPlayer(final String id, final String playername, final String quality, final String revision, final String image) {
        boolean result = true;
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (:PlayerCard {id: $id, name: $playername, quality: $quality, revision: $revision, image: $image})",
                        parameters("id", id, "playername", playername, "quality", quality, "revision", revision, "image", image));
                return 1;
            });

        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public ArrayList<Player> searchPlayerCard(final String name){
        ArrayList<Player> matchingPlayers;
        try (Session session = driver.session())
        {
            matchingPlayers = session.readTransaction((TransactionWork<ArrayList<Player>>) tx -> {
                Result result = tx.run( "MATCH (p:PlayerCard)\n" +
                                        "WHERE (p.name) CONTAINS $name \n" +
                                        "RETURN p.name AS Name, toString(p.id) AS PlayerId, p.quality AS Quality, p.revision AS Revision, p.image AS Img0",
                        parameters( "name", name));
                ArrayList<Player> Players = new ArrayList<>();
                while(result.hasNext())
                {
                    Player p;
                    Record r = result.next();
                    p = new Player(r.get("PlayerId").asString(), r.get("Name").asString(), r.get("Quality").asString(),  r.get("Revision").asString(),  r.get("Img0").asString());
                    Players.add(p);
                }
                return Players;
            });
        }catch (Exception e){
            logger.error("Exception occurred: ", e);
            matchingPlayers = null;
        }
        return matchingPlayers;
    }

    public boolean createLike(final String user_id, final String playercard){
        boolean result = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(p:PlayerCard{id: toInteger($playercard)})\n" +
                        "CREATE (u)-[:Like]->(p)",
                        parameters("user_id", user_id , "playercard", playercard));
                return 1;
            });
        }catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public int countLikes(final String playercard) {
        int numLike;
        try ( Session session = driver.session() )
        {
            numLike = session.readTransaction((TransactionWork<Integer>) tx -> {

                String query = "MATCH (p:PlayerCard{id: toInteger($playercard)})-[l:Like]-(:User)"+
                                "RETURN COUNT(l) AS numLike";
                Result result = tx.run( query, parameters("playercard", playercard) );
                return result.single().get("numLike").asInt();
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            numLike = -1;
        }

        return numLike;
    }

    public boolean checkLikes(final String user_id, final String playercard) {
        boolean existLike;
        try ( Session session = driver.session() )
        {
             existLike = session.readTransaction((TransactionWork<Boolean>) tx -> {

                String query = "MATCH (c:User{id: $user_id})-[l:Like]->(p:PlayerCard{id: toInteger($playercard)}) \n" +
                                "RETURN COUNT(l) > 0 AS boolExist";
                Result result = tx.run( query, parameters("user_id", user_id , "playercard", playercard));
                return result.single().get("boolExist").asBoolean();
            });

        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            existLike = false;
        }
        return existLike;
    }

    public ArrayList<Player> suggestPlayers(String user_id){
        ArrayList<Player> suggestedPlayer;
        try (Session session = driver.session())
        {
            suggestedPlayer = session.readTransaction((TransactionWork<ArrayList<Player>>) tx -> {
                Result result = tx.run( "MATCH (:User {id: $user_id})-[:Follow]-(u:User)\n" +
                                "CALL\n" +
                                "{\n" +
                                    "WITH u\n" +
                                    "MATCH (u)-[:Like]->(p:PlayerCard)\n" +
                                    "RETURN p \n" +
                                    "UNION\n" +
                                    "WITH u\n" +
                                    "MATCH (u)-[:Post]->(:Comment)-[:Related]->(p:PlayerCard)\n" +
                                    "RETURN p\n" +
                                "}\n" +
                                "WITH p, rand() as pos\n" +
                                "ORDER BY pos\n" +
                                "RETURN DISTINCT p.name AS Name, toString(p.id) AS PlayerId, p.quality AS Quality, p.revision AS Revision, p.image AS Img0\n" +
                                "LIMIT 6\n",
                        parameters( "user_id", user_id));
                ArrayList<Player> Players = new ArrayList<>();
                while(result.hasNext())
                {
                    Player p;
                    Record r = result.next();
                    p = new Player(r.get("PlayerId").asString(), r.get("Name").asString(), r.get("Quality").asString(),  r.get("Revision").asString(),  r.get("Img0").asString());
                    Players.add(p);
                }
                return Players;
            });
        }catch (Exception e){
            logger.error("Exception occurred: ", e);
            suggestedPlayer = null;
        }
        return suggestedPlayer;
    }


    public static void main( String... args ) throws Exception{
        try ( Neo4jPlayerCard ex = new Neo4jPlayerCard() )
        {
            System.out.println(ex.suggestPlayers("5ff97e7883b19024e080f651"));
        }
    }


}
