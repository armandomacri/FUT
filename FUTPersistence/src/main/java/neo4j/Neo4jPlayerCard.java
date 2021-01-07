package neo4j;

import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class Neo4jPlayerCard implements AutoCloseable{
    public static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "fut"));

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



    public static void main( String... args ) throws Exception{
        try ( Neo4jPlayerCard ex = new Neo4jPlayerCard() )
        {
            boolean prova = ex.checkLikes("8", "541");
            System.out.println(prova);
        }
    }


}
