package neo4j;

import bean.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class Neo4jUser extends Neo4jConnection{
    private static final Logger logger = LogManager.getLogger(Neo4jUser.class);

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
                    Record r = result.next();
                    User u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            followedusers = null;
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
                                        "RETURN u.username AS Username, toString(u.id) AS Id, u.score AS Score\n",
                        parameters( "username", username, "user_id", user_id));
                ArrayList<User> users = new ArrayList<>();
                while(result.hasNext())
                {
                    Record r = result.next();
                    User u = new User(r.get("Username").asString(), r.get("Id").asString(), r.get("Score").asInt());
                    users.add(u);
                }
                return users;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            matchingUsers = null;
        }
        return matchingUsers;
    }

    public ArrayList<User> suggestedUserByLike(final String user_id){
        ArrayList<User> suggestedUsers;
        try (Session session = driver.session()) {
            suggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH p=(n:User {id: $user_id})-[:Like]->(:PlayerCard)<-[:Like]-(u:User)\n" +
                                "WHERE NOT EXISTS ((n)-[:Follow]-(u)) AND u.id <> $user_id\n" +
                                "RETURN DISTINCT toString(u.id) AS Id, u.username AS Username\n" +
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
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            suggestedUsers = null;
        }
        return suggestedUsers;
    }

    public ArrayList<User> suggestedUserByFriends(final String user_id) {
        ArrayList<User> suggestedUsers;
        try (Session session = driver.session()) {
            suggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH p=(n:User{id: $user_id})-[:Follow]->(:User)<-[:Follow]-(u:User)\n" +
                                            "WHERE NOT EXISTS ((n)-[:Follow]-(u))\n" +
                                            "WITH u, rand() AS number\n" +
                                            "RETURN toString(u.id) AS Id, u.username AS Username\n" +
                                            "ORDER BY number\n" +
                                            "LIMIT 5",
                                parameters("user_id", user_id));
                ArrayList<User> users = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    User u = new User(r.get("Username").asString(), r.get("Id").asString());
                    users.add(u);
                }
                return users;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            suggestedUsers = null;
        }
        return suggestedUsers;
    }

    public ArrayList<User> suggestedUserChallenge(final String user_id) {
        ArrayList<User> suggestedUsers;
        try (Session session = driver.session()) {
            suggestedUsers = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u:User{id: $user_id})\n" +
                                        "WITH toInteger(u.score) as userscore\n" +
                                        "MATCH (u1:User)\n" +
                                        "WHERE userscore-2 <= u1.score <= userscore+2 AND (u1.id)<>$user_id\n" +
                                        "RETURN toString(u1.id) AS Id, u1.username AS Username, u1.score AS Score\n" +
                                        "ORDER BY rand()\n" +
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
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            suggestedUsers = null;
        }
        return suggestedUsers;
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
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public boolean createFollow(final String user_id, final String user_id1){
        boolean result = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: $user_id}),(u1:User{id: $user_id1})\n" +
                        "CREATE (u)-[:Follow]->(u1)",
                        parameters("user_id", user_id , "user_id1", user_id1));
                return 1;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public boolean deleteFollow(final String user_id, final String user_id1){
        boolean result = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH path=(u:User{id: $user_id})-[f:Follow]-(u1:User{id: $user_id1})\n" +
                        "DELETE f",
                        parameters("user_id", user_id , "user_id1", user_id1));
                return 1;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public boolean updateScore(final String user_id, final Integer score){
        boolean result = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("MATCH (u:User{id: toString($user_id)})\n" +
                        "SET u.score = u.score + $score",
                        parameters("user_id", user_id , "score", score));
                return 1;
            });
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public static void main( String... args ) throws Exception{
        Neo4jUser ex = new Neo4jUser();
        ex.suggestedUserByFriends("5ff97e8283b19024e081534f");

    }
}
