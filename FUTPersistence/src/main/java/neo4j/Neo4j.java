package neo4j;

import bean.User;
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

    private ArrayList<User> searchUser( final String username ){
        ArrayList<User> matchingUsers;
        try ( Session session = driver.session() )
        {
            matchingUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run( "MATCH (u:User) WHERE (u.username) CONTAINS $username\n" +
                                "RETURN u.username as Username, toString(u.id) as Id",
                        parameters( "username", username) );
                ArrayList<User> users = new ArrayList<>();
                while(result.hasNext())
                {
                    User u = null;
                    Record r = result.next();
                    u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        }
        return matchingUsers;
    }

    private ArrayList<User> SuggestedUserByLike( final Integer user_id){
        ArrayList<User> SuggestedUsers;
        try (Session session = driver.session()) {
            SuggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u:User{id: $user_id})-[:Follow]->(u1:User)\n" +
                                "WITH collect(u1) AS FollowedUserYet\n" +
                                "MATCH p=(n:User{id: $user_id})-[:Like]->(:PlayerCard)<-[l:Like]-(u:User)\n" +
                                "WHERE NOT u IN FollowedUserYet\n" +
                                "RETURN toString(u.id) as Id, u.username as Username, count(l) as NumLike\n" +
                                "ORDER BY NumLike DESC\n" +
                                "LIMIT 5",
                        parameters("user_id", user_id));
                ArrayList<User> users = new ArrayList<>();
                while (result.hasNext()){
                    User u = null;
                    Record r = result.next();
                    u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        }
        return SuggestedUsers;
    }

    private ArrayList<User> SuggestedUserByFriends( final Integer user_id) {
        ArrayList<User> SuggestedUsers;
        try (Session session = driver.session()) {
            SuggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH p=(n:User{id: 5})-[:Follow]->(:User)<-[f:Follow]-(u:User)\n" +
                                "RETURN toString(u.id) as Id, u.username as Username, count(f) as NumFollow\n" +
                                "ORDER BY NumFollow DESC\n" +
                                "LIMIT 5",
                        parameters("user_id", user_id));
                ArrayList<User> users = new ArrayList<>();
                while (result.hasNext()) {
                    User u = null;
                    Record r = result.next();
                    u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        }
        return SuggestedUsers;
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4j ex = new Neo4j( "bolt://localhost:7687", "neo4j", "fut" ) )
        {
            ArrayList<User> utenti = null;
            utenti = ex.SuggestedUserByLike(5);
            System.out.println(utenti);
        }
    }
}
