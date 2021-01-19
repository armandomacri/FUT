package neo4j;

import bean.Comment;
import bean.User;
import configuration.LoadXmlConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.neo4j.driver.Values.parameters;

public class Neo4jComment extends Neo4jConnection{
    private static final Logger logger = LogManager.getLogger(Neo4jComment.class);

    @Override
    public void close() throws Exception {
        driver.close();
        driver = null;
    }

    public ArrayList<Comment> showComment(final String player_id){
        ArrayList<Comment> comments;
        try (Session session = driver.session())
        {
            comments = session.readTransaction((TransactionWork<ArrayList<Comment>>) tx -> {
                Result result = tx.run( "MATCH path=(u:User)-[p:Post]-(c:Comment)-[:Related]->(pc:PlayerCard{id: toInteger($player_id)})\n" +
                                        "RETURN c.id AS Id, c.text AS Text, p.date AS Date, u.username AS Username",
                        parameters( "player_id", player_id) );
                ArrayList<Comment> commentsResult = new ArrayList<>();
                while(result.hasNext())
                {
                    Comment c;
                    Record r = result.next();
                    SimpleDateFormat parserSDF=new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = parserSDF.parse(r.get("Date").asString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c = new Comment(r.get("Id").asString(), date, r.get("Text").asString(), r.get("Username").asString());
                    commentsResult.add(c);
                }
                return commentsResult;
            });
        } catch (Exception e){
            logger.error("Exeption occurred: ", e);
            comments = null;
        }
        return comments;
    }

    public boolean createComment(final String player_id,  final String text, final String user_id){
        boolean check = true;
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                Result result = tx.run("CREATE (c:Comment{text: $text}) RETURN toString(id(c)) AS commentId",
                        parameters( "text", text));
                String commentId = null;
                while(result.hasNext())
                {
                    Record r = result.next();
                    commentId = r.get("commentId").asString();
                }
                tx.run("MATCH (u:User{id: $user_id}), (c:Comment)\n" +
                        "WHERE id(c) = toInteger($commentId)\n" +
                        "CREATE (u)-[:Post{date: date()}]->(c)",
                        parameters("commentId", commentId, "user_id", user_id));
                tx.run("MATCH (p:PlayerCard{id: toInteger($player_id)}), (c:Comment)\n" +
                        "WHERE id(c) = toInteger($commentId)\n" +
                        "CREATE (c)-[:Related]->(p)",
                        parameters("commentId", commentId, "player_id", player_id));
                return 1;
            });
        } catch (Exception e){
            logger.error("Exeption occurred: ", e);
            check = false;
        }
        return check;
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jComment ex = new Neo4jComment() )
        {
            ex.createComment("1", "che forte", "10");
        }
    }
}
