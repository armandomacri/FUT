package neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class Neo4jPlayerCard implements AutoCloseable{
    private static Driver driver;

    public Neo4jPlayerCard(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void createPlayer(final Integer id, final String player_name, final String position, final String images, final Integer overall){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (:PlayerCard{name: $player_name, overall: $overall, images: $images, position:$position, id: $id})",
                        parameters("player_name", player_name, "overall", overall, "images", images, "position", position, "id", id));
                return 1;
            });
        }
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jUser ex = new Neo4jUser() )
        {

        }
    }
}
