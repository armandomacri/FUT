package neo4j;

import bean.Comment;
import bean.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.neo4j.driver.Values.parameters;

public class Neo4jComment implements AutoCloseable{
    private final Driver driver;

    public Neo4jComment(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    private ArrayList<Comment> showComment(final Integer player_id){
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
                    c = new Comment(r.get("Id").asInt(), player_id, (Date) r.get("Date"), r.get("Text").asString(), r.get("Username").asString());
                    commentsResult.add(c);
                }
                return commentsResult;
            });
        }
        return comments;
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jComment ex = new Neo4jComment( "bolt://localhost:7687", "neo4j", "fut" ) )
        {
            ex.showComment(10);
        }
    }
}
