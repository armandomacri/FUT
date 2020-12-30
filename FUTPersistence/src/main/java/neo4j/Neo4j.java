package neo4j;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class Neo4j implements AutoCloseable{
    private final Driver driver;

    public Neo4j(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception{
        driver.close();
    }

    private void searchUser( final String username ){
        try ( Session session = driver.session() )
        {
            List<String> matchingUsers = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run( "MATCH (u:User) WHERE (u.username) CONTAINS $username" +
                                " RETURN u.username as Username",
                        parameters( "username", username) );
                ArrayList<String> users = new ArrayList<>();
                while(result.hasNext())
                {
                    Record r = result.next();
                    users.add(r.get("Username").asString());
                }
                return users;
            });
            System.out.println("Users that contains '" + username + "' in the username are:");
            for (String matchingUser: matchingUsers)
            {
                System.out.println("\t- " + matchingUser);
            }
        }
    }

    private void SuggestedUserByLike( final Integer user_id ){
        try ( Session session = driver.session() )
        {
            List<Integer> SuggestedUsers = session.readTransaction((TransactionWork<List<Integer>>) tx -> {
                Result result = tx.run( "MATCH (u:User{id: $user_id})-[:Follow]->(u1:User)\n" +
                                "WITH collect(u1) AS FollowedUserYet\n" +
                                "MATCH p=(n:User{id: $user_id})-[:Like]->(:PlayerCard)<-[l:Like]-(u:User)\n" +
                                "WHERE NOT u IN FollowedUserYet\n" +
                                "RETURN u.id as Id ,count(l) as NumLike, FollowedUserYet\n" +
                                "ORDER BY NumLike DESC\n" +
                                "LIMIT 5",
                        parameters( "user_id", user_id) );
                ArrayList<Integer> users = new ArrayList<>();
                while(result.hasNext())
                {
                    Record r = result.next();
                    //costruttore
                    users.add(r.get("Id").asInt());
                }
                return users;
            });
            System.out.println("Users that like your own players are: '");
            for (Integer SuggestedUser: SuggestedUsers)
            {
                System.out.println("\t- " + SuggestedUser);
            }
        }
    }

    private void SuggestedUserByFriends( final Integer user_id ){
        try ( Session session = driver.session() )
        {
            List<String> SuggestedUsers = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run( "MATCH p=(n:User{id: $user_id})-[:Follow]->(:User)<-[:Follow]-(u:User)\n" +
                                "RETURN u.username as Username",
                        parameters( "user_id", user_id) );
                ArrayList<String> users = new ArrayList<>();
                while(result.hasNext())
                {
                    Record r = result.next();
                    users.add(r.get("Username").asString());
                }
                return users;
            });
            System.out.println("Users that follow your friends are: '");
            for (String SuggestedUser: SuggestedUsers)
            {
                System.out.println("\t- " + SuggestedUser);
            }
        }
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4j ex = new Neo4j( "bolt://localhost:7687", "neo4j", "fut" ) )
        {
            ex.SuggestedUserByLike(5);
        }
    }
}
