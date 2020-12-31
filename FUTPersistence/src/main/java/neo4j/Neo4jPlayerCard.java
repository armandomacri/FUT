package neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class Neo4jPlayerCard implements AutoCloseable{
    private final Driver driver;

    public Neo4jPlayerCard(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void CreatePlayer(final Integer id, final String player_name, final String position, final String images, final Integer overall){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (:PlayerCard{name: $player_name, overall: $overall, images: $images, position:$position, id: $id})",
                        parameters("player_name", player_name, "overall", overall, "images", images, "position", position, "id", id));
                return 1;
            });
        }
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jPlayerCard ex = new Neo4jPlayerCard( "bolt://localhost:7687", "neo4j", "fut" ) )
        {
            ex.CreatePlayer(171717, "Andrea Lagna", "COC", "" , 90);
        }
    }
}
