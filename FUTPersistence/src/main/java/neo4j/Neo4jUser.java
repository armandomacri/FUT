package neo4j;

import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.ArrayList;
import static org.neo4j.driver.Values.parameters;

public class Neo4jUser implements AutoCloseable{
    public static Driver driver;

    public Neo4jUser(){
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "fut"));
    }

    @Override
    public void close(){
        driver.close();
    }

    public ArrayList<User> checkalreadyfollow(final Integer user_id){
        ArrayList<User> followedusers;
        try (Session session = driver.session()) {
            followedusers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u:User{id: $user_id})-[:Follow]->(u1:User)\n" +
                                "RETURN toString(u1.id) AS Id, u1.username AS Username",
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
        return followedusers;
    }

    public ArrayList<User> searchUser(final String username, final Integer user_id){
        ArrayList<User> matchingUsers;
        try (Session session = driver.session())
        {
            matchingUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run( "MATCH (u:User)\n" +
                                "WHERE (u.username) CONTAINS $username AND (u.id)<>$user_id\n" +
                                "RETURN u.username AS Username, toString(u.id) AS Id",
                        parameters( "username", username, "user_id", user_id));
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

    public ArrayList<User> suggestedUserByLike(final Integer user_id){
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

    public ArrayList<User> suggestedUserByFriends(final Integer user_id) {
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

    public void createUser(final Integer user_id, final String username){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (u:User {id: $user_id, username: $username})",
                        parameters("user_id", user_id , "username", username));
               return 1;
            });
        }
    }

    public void createFollow(final Integer user_id, final Integer user_id1){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(u1:User{id: $user_id1})\n" +
                        "CREATE (u)-[:Follow]->(u1)",
                        parameters("user_id", user_id , "user_id1", user_id1));
                return 1;
            });
        }
    }

    public void createLike(final Integer user_id, final Integer playercard){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(p:PlayerCard{id: $playercard})\n" +
                        "CREATE (u)-[:Like]->(p)",
                        parameters("user_id", user_id , "playercard", playercard));
                return 1;
            });
        }
    }

    public void createPost(final Integer user_id, final Integer comment_id){
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
        try ( Neo4jUser ex = new Neo4jUser( ) )
        {
            System.out.println(ex.suggestedUserByLike(0));
        }
    }
}
