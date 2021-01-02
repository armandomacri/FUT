package neo4j;

import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.ArrayList;
import static org.neo4j.driver.Values.parameters;

public class Neo4jUser implements AutoCloseable{
    private final Driver driver;

    public Neo4jUser(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception{
        driver.close();
    }

    public ArrayList<User> findUsers(final String username){
        ArrayList<User> matchingUsers;
        try (Session session = driver.session())
        {
            matchingUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run( "MATCH (u:User) WHERE (u.username) CONTAINS $username\n" +
                                        "RETURN u.username AS Username, toString(u.id) AS Id",
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

    private ArrayList<User> suggestedUserByLike(final Integer user_id){
        ArrayList<User> SuggestedUsers;
        try (Session session = driver.session()) {
            SuggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u:User{id: $user_id})-[:Follow]->(u1:User)\n" +
                                        "WITH collect(u1) AS FollowedUserYet\n" +
                                        "MATCH p=(n:User{id: $user_id})-[:Like]->(:PlayerCard)<-[l:Like]-(u:User)\n" +
                                        "WHERE NOT u IN FollowedUserYet\n" +
                                        "RETURN toString(u.id) AS Id, u.username AS Username, count(l) AS NumLike\n" +
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

    private ArrayList<User> SuggestedUserByFriends(final Integer user_id) {
        ArrayList<User> SuggestedUsers;
        try (Session session = driver.session()) {
            SuggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH p=(n:User{id: 5})-[:Follow]->(:User)<-[f:Follow]-(u:User)\n" +
                                        "RETURN toString(u.id) AS Id, u.username AS Username, count(f) AS NumFollow\n" +
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

    public void CreateUser(final Integer user_id, final String username){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (u:User {id: $user_id, username: $username})",
                        parameters("user_id", user_id , "username", username));
               return 1;
            });
        }
    }

    public void CreateFollow(final Integer user_id, final Integer user_id1){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(u1:User{id: $user_id1})\n" +
                        "CREATE (u)-[:Follow]->(u1)",
                        parameters("user_id", user_id , "user_id1", user_id1));
                return 1;
            });
        }
    }

    public void CreateLike(final Integer user_id, final Integer playercard){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(p:PlayerCard{id: $playercard})\n" +
                        "CREATE (u)-[:Like]->(p)",
                        parameters("user_id", user_id , "playercard", playercard));
                return 1;
            });
        }
    }

    public void CreatePost(final Integer user_id, final Integer comment_id){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(c:Comment{id: $comment_id})\n" +
                        "CREATE (u)-[:Post]->(c)",
                        parameters("user_id", user_id , "comment_id", comment_id));
                return 1;
            });
        }
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jUser ex = new Neo4jUser( "bolt://localhost:7687", "neo4j", "fut" ) )
        {
            ex.CreateUser(171717, "AndreaLagna");
            /*ex.CreateUser(111111, "MirkoCasini");
            ex.CreateFollow(171717,111111);
            ex.CreateLike(171717,1);
            ex.CreatePost(171717,1);*/
        }
    }
}
