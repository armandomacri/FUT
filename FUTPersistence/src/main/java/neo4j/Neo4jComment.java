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
                    SimpleDateFormat parserSDF=new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = parserSDF.parse(r.get("Date").asString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c = new Comment(r.get("Id").asInt(), player_id, date, r.get("Text").asString(), r.get("Username").asString());
                    commentsResult.add(c);
                    //String date1 = parserSDF.format(c.getDate()); per avere data nel formato corretto in output
                }
                return commentsResult;
            });
        }
        return comments;
    }

    public void CreateComment(final Integer id, final Integer player_id, final String text, final Integer user_id){
        try (Session session = driver.session()){
            session.writeTransaction( tx -> {
                tx.run("CREATE (:Comment{comment_date: date(), id: $id, player_id: $player_id, text: $text})",
                        parameters("id", id, "player_id", player_id, "text", text));
                tx.run("MATCH (c:Comment{id: $id}), (u:User{id: $user_id})\n" +
                        "CREATE (u)-[:Post]->(c)",
                        parameters("id", id, "user_id", user_id));
                tx.run("MATCH (c:Comment{id: $id}), (p:PlayerCard{id: $player_id})\n" +
                        "CREATE (c)-[:Related]->(p)",
                        parameters("id", id, "player_id", player_id));
                return 1;
            });
        }
    }

    public static void main( String... args ) throws Exception{
        try ( Neo4jComment ex = new Neo4jComment( "bolt://localhost:7687", "neo4j", "fut" ) )
        {
            ex.CreateComment(171717, 1, "Awesome Mirko", 171717);
        }
    }
}
