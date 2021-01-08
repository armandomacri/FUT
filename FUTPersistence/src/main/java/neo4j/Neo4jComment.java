package neo4j;

import bean.Comment;
import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.neo4j.driver.Values.parameters;

public class Neo4jComment implements AutoCloseable{
    public static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "fut"));

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public ArrayList<Comment> showComment(final String player_id) throws Exception {
        ArrayList<Comment> comments;
        try (Session session = driver.session())
        {
            comments = session.readTransaction((TransactionWork<ArrayList<Comment>>) tx -> {
                Result result = tx.run( "MATCH p=(u:User)-[:Post]-(c:Comment)-[:Related]->(pc:PlayerCard{id: $player_id})\n" +
                                        "RETURN c.id AS Id, c.text AS Text, c.comment_date AS Date, u.username AS Username ",
                        parameters( "player_id", player_id) );
                ArrayList<Comment> commentsResult = new ArrayList<>();
                while(result.hasNext())
                {
                    Comment c = null;
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
                    //String date1 = parserSDF.format(c.getDate()); per avere data nel formato corretto in output
                }
                return commentsResult;
            });
        }
        return comments;
    }

    public void createComment(final String player_id,  final String text, final String user_id) throws Exception {
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
                System.out.println(commentId);
                tx.run("MATCH (u:User{id: $user_id}), (c:Comment)\n" +
                        "WHERE id(c) = toInteger($commentId)\n" +
                        "CREATE (u)-[:Post{date: date()}]->(c)",
                        parameters("commentId", commentId, "user_id", user_id));
                tx.run("MATCH (p:PlayerCard{id: $player_id}), (c:Comment)\n" +
                        "WHERE id(c) = toInteger($commentId)\n" +
                        "CREATE (c)-[:Related]->(p)",
                        parameters("commentId", commentId, "player_id", player_id));
                return 1;
            });
        }
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jComment ex = new Neo4jComment() )
        {
            ex.createComment("1", "che forte", "10");
        }
    }
}
