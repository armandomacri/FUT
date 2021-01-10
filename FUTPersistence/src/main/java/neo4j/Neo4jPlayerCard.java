package neo4j;

import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.HashMap;

import static org.neo4j.driver.Values.parameters;

public class Neo4jPlayerCard extends Neo4jConnection{

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void createPlayer(final String id, final String player_name, final String position, final String images, final Integer overall){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (:PlayerCard{name: $player_name, overall: $overall, images: $images, position:$position, id: $id})",
                        parameters("player_name", player_name, "overall", overall, "images", images, "position", position, "id", id));
                return 1;
            });
        }
    }

    public void createLike(final String user_id, final String playercard){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(p:PlayerCard{id: $playercard})\n" +
                        "CREATE (u)-[:Like]->(p)",
                        parameters("user_id", user_id , "playercard", playercard));
                return 1;
            });
        }
    }

    public Integer countLikes(final String playercard)
    {
        try ( Session session = driver.session() )
        {
            Integer numLike = session.readTransaction((TransactionWork<Integer>) tx -> {

                String query = "MATCH (p:PlayerCard{id: $playercard})-[l:Like]-(:User)"+
                        "RETURN COUNT(l) AS numLike";
                Result result = tx.run( query, parameters("playercard", playercard) );
                return result.single().get("numLike").asInt();
            });
            return numLike;
        }
    }

    public boolean checkLikes(final String user_id, final String playercard) {
        try ( Session session = driver.session() )
        {
            boolean existLike = session.readTransaction((TransactionWork<Boolean>) tx -> {

                String query = "MATCH (c:User{id: $user_id})-[l:Like]->(p:PlayerCard{id: $playercard}) \n" +
                        "RETURN COUNT(l) > 0 AS boolExist";
                Result result = tx.run( query, parameters("user_id", user_id , "playercard", playercard));
                return result.single().get("boolExist").asBoolean();
            });
            return existLike;
        }
    }

    public HashMap<String, String> mostLikedPlayer(){
        HashMap<String, String> playerMap = new HashMap<>();
        try (Session session = driver.session()) {
            playerMap = session.readTransaction((TransactionWork<HashMap<String, String>>) tx -> {
                Result result = tx.run("MATCH path=(p:PlayerCard)-[l:Like]-(u:User)\n" +
                                        "RETURN p.name AS PlayerName, p.id, COUNT(l) AS numLike \n" +
                                        "ORDER BY numLike DESC\n" +
                                        "LIMIT 25");
                HashMap<String, String> playerMapResult = new HashMap<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    playerMapResult.put(r.get("PlayerName").asString(), r.get("numLike").toString());

                }
                return playerMapResult;
            });
        }
        return playerMap;
    }


    public static void main( String... args ) throws Exception{
        try ( Neo4jPlayerCard ex = new Neo4jPlayerCard() )
        {
            HashMap<String, String> prova = new HashMap<>();
            prova = ex.mostLikedPlayer();
            System.out.println(prova);
        }
    }


}
