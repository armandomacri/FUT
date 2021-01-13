package neo4j;

import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.ArrayList;
import java.util.HashMap;

import static org.neo4j.driver.Values.parameters;

public class Neo4jUser extends Neo4jConnection{

    @Override
    public void close(){
        driver.close();
    }

    public ArrayList<User> checkalreadyfollow(final String user_id){
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

    public ArrayList<User> searchUser(final String username, final String user_id){
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
                    Record r = result.next();
                    User u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        }
        return matchingUsers;
    }

    public ArrayList<User> suggestedUserByLike(final String user_id){
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
                    Record r = result.next();
                    User u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        }
        return SuggestedUsers;
    }

    public ArrayList<User> suggestedUserByFriends(final String user_id) {
        ArrayList<User> SuggestedUsers;
        try (Session session = driver.session()) {
            SuggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u:User{id: $user_id})-[:Follow]->(u1:User)\n" +
                                        "WITH collect(u1) AS FollowedUserYet\n" +
                                        "MATCH p=(n:User{id: $user_id})-[:Follow]->(:User)<-[f:Follow]-(u:User)\n" +
                                        "WHERE NOT u IN FollowedUserYet\n" +
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

    public ArrayList<User> suggestedUserChallenge(final String user_id) {
        ArrayList<User> SuggestedUsers;
        try (Session session = driver.session()) {
            SuggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u:User{id: $user_id})\n" +
                                        "WITH toInteger(u.score) as userscore\n" +
                                        "MATCH (u1:User)\n" +
                                        "WHERE userscore-2 <= u1.score <= userscore+2 AND (u1.id)<>$user_id\n" +
                                        "RETURN toString(u1.id) AS Id, u1.username AS Username, u1.score AS Score\n" +
                                        "LIMIT 10",
                        parameters("user_id", user_id));
                ArrayList<User> users = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User u = new User(r.get("Username").asString(), r.get("Id").asString(), r.get("Score").asInt());
                    users.add(u);
                }
                return users;
            });
        }
        return SuggestedUsers;
    }

    public boolean createUser(final String user_id, final String username){
        boolean result = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (u:User {id: $user_id, username: $username, score: 0})",
                        parameters("user_id", user_id , "username", username));
               return 1;
            });
        } catch (Exception e){
            result = false;
        }
        return result;
    }

    public void createFollow(final String user_id, final String user_id1){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(u1:User{id: $user_id1})\n" +
                        "CREATE (u)-[:Follow]->(u1)",
                        parameters("user_id", user_id , "user_id1", user_id1));
                return 1;
            });
        }
    }

    public void deleteFollow(final String user_id, final String user_id1){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH path=(u:User{id: $user_id})-[f:Follow]-(u1:User{id: $user_id1})\n" +
                        "DELETE f",
                        parameters("user_id", user_id , "user_id1", user_id1));
                return 1;
            });
        }
    }

    public void createPost(final String user_id, final Integer comment_id){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(c:Comment{id: $comment_id})\n" +
                        "CREATE (u)-[:Post]->(c)",
                        parameters("user_id", user_id , "comment_id", comment_id));
                return 1;
            });
        }
    }

    public boolean updateScore(final String user_id, final Integer score){
        boolean result = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: toInteger($user_id)})\n" +
                        "SET u.score = u.score + $score",
                        parameters("user_id", user_id , "score", score));
                return 1;
            });
        } catch (Exception e){
            result = false;
        }

        return result;
    }

    public HashMap<String, String> mostActiveUser(){
        HashMap<String, String> userMap = new HashMap<>();
        try (Session session = driver.session()) {
            userMap = session.readTransaction((TransactionWork<HashMap<String, String>>) tx -> {
                Result result = tx.run("MATCH (u:User)-[r]->()\n" +
                                        "RETURN u.id, u.username AS Username, COUNT(r) AS numAction\n" +
                                        "ORDER BY numAction DESC\n" +
                                        "LIMIT 25");
                HashMap<String, String> userMapResult = new HashMap<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    userMapResult.put(r.get("Username").asString(), r.get("numAction").toString());
                }
                return userMapResult;
            });
        }
        return userMap;
    }

    public static void main( String... args ) throws Exception{
        Neo4jUser ex = new Neo4jUser();
        System.out.println(ex.createUser("3243234", "Armando"));
    }
}
