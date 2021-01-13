package mongo;

import bean.Player;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class MongoPlayerCard extends MongoConnection{
    private static final Logger logger = LogManager.getLogger(MongoPlayerCard.class);
    private MongoCollection<Document> myColl;

    public boolean add(Integer _id, String player_name, String player_extended_name, String quality, String revision, String origin, Integer overall,
                      String club, String league, String nationality, String position, String date_of_birth, Integer weight, Integer height,
                      String added_date, Integer pace, Integer dribbling, Integer shooting, Integer passing, Integer defending,
                      Integer physicality, String pref_foot, Integer weak_foot, Integer skill_moves, String images){
        boolean result = true;
        myColl = db.getCollection("player_cards");
        Document player = new Document("_id", _id)
                .append("player_name", player_name)
                .append("player_extended_name", player_extended_name)
                .append("quality", quality)
                .append("revision", revision)
                .append("origin", origin)
                .append("overall", overall)
                .append("club", club)
                .append("league", league)
                .append("nationality", nationality)
                .append("position", position)
                .append("date_of_birth", date_of_birth)
                .append("weight", weight)
                .append("height", height)
                .append("added_date", added_date)
                .append("pace", pace)
                .append("dribbling", dribbling)
                .append("shooting", shooting)
                .append("passing", passing)
                .append("defending", defending)
                .append("physicality", physicality)
                .append("pref_foot", pref_foot)
                .append("weak_foot", weak_foot)
                .append("skill_moves", skill_moves)
                .append("images", images);
        try {
            myColl.insertOne(player);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    public ArrayList<Player> findPlayers (String toFind) {
        myColl = db.getCollection("player_cards");
        ArrayList<Player> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = myColl.find(regex("player_extended_name",".*" + Pattern.quote(toFind) + ".*", "-i")).iterator())
        {
            while (cursor.hasNext())
            {
                Document playerDoc = cursor.next();
                if (playerDoc == null)
                    return null;
                results.add(composePlayer(playerDoc));
            }
        }catch (Exception e){
            logger.error("Exception occurred: ", e);
            results = null;
        }
        return results;
    }

    public Player findById(Integer id){
        myColl = db.getCollection("player_cards");
        Document playerDoc;
        Player player;

        try{
            playerDoc = myColl.find(eq("_id", id)).first();
            /**********************************************************************/
            if(playerDoc == null) //player ancora non caricato
                return null;
            /**********************************************************************/
            player = composePlayer(playerDoc);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            player = null;
        }

        return player;
    }

    public ArrayList<Player> filterBy(String position, String nation, String quality){
        myColl = db.getCollection("player_cards");
        ArrayList<Player> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = myColl.find(and(and(eq("position", position), eq("nationality", nation)), eq("quality", quality))).iterator())
        {
            while (cursor.hasNext())
            {
                Document playerDoc = cursor.next();
                if (playerDoc == null)
                    return null;
                results.add(composePlayer(playerDoc));
            }
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            results = null;
        }

        return results;
    }

    public boolean deletePlayer (int id){
        boolean result = true;
        myColl = db.getCollection("player_cards");
        try{
            myColl.deleteOne(eq("_id", id));
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }
        return result;
    }

    private Player composePlayer(Document playerDoc){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = df.parse(playerDoc.get("date_of_birth").toString());
            date2 = df.parse(playerDoc.get("added_date").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] images = playerDoc.get("images").toString().split(",");
        Player p;
        if (playerDoc.get("position").toString().equals("GK")) {
            p = new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, playerDoc.get("height").toString(), playerDoc.get("weight").toString(), date2, Integer.parseInt(playerDoc.get("gk_diving").toString()), Integer.parseInt(playerDoc.get("gk_reflexes").toString()), Integer.parseInt(playerDoc.get("gk_handling").toString()), Integer.parseInt(playerDoc.get("gk_speed").toString()), Integer.parseInt(playerDoc.get("gk_kicking").toString()), Integer.parseInt(playerDoc.get("gk_positoning").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
        }
        else {
            p = new Player(playerDoc.get("_id").toString(), playerDoc.get("player_name").toString(), playerDoc.get("player_extended_name").toString(), playerDoc.get("quality").toString(), playerDoc.get("revision").toString(), Integer.parseInt(playerDoc.get("overall").toString()), playerDoc.get("club").toString(), playerDoc.get("league").toString(), playerDoc.get("nationality").toString(), playerDoc.get("position").toString(), date1, playerDoc.get("height").toString(), playerDoc.get("weight").toString(), date2, Integer.parseInt(playerDoc.get("pace").toString()), Integer.parseInt(playerDoc.get("dribbling").toString()), Integer.parseInt(playerDoc.get("shooting").toString()), Integer.parseInt(playerDoc.get("passing").toString()), Integer.parseInt(playerDoc.get("defending").toString()), Integer.parseInt(playerDoc.get("physicality").toString()), playerDoc.get("pref_foot").toString(), Integer.parseInt(playerDoc.get("weak_foot").toString()), Integer.parseInt(playerDoc.get("skill_moves").toString()), images);
        }

        return p;
    }

    /*
    db.player_cards.aggregate(
	[
		//first step

		{
			$match: { nationality: "Italy", league: {$ne: "Icons"} }
		},

		//second step

		{
			$group: {_id: {league: "$league"}, paceAvg : {$avg: "$pace"}, dribblingAvg: {$avg: "$dribbling"},
			shootingAvg: {$avg: "$shooting"}, passingAvg: {$avg: "$passing"}, defendingAvg: {$avg: "$defending"},
			physicalityAvg: {$avg: "$physicality"},
			numPlayers: {$sum: 1}}
		},

		//third step

		{$sort: {numPlayers: -1}},

		//Forth step

		{$limit: 3},

		//formatted

		{
			$project:
			{
				_id: 0,
				league: "$_id.league",
				numPlayers: "$numPlayers",
				feature: { paceAvg : "$paceAvg", dribblingAvg: "$dribblingAvg", shootingAvg: "$shootingAvg",
				passingAvg: "$passingAvg",defendingAvg: "$defendingAvg", physicalityAvg: "$physicalityAvg"},
			}
		}
	]
).pretty()
     */

    public ArrayList<Document> nationalityAnalytics(String nationality){
        ArrayList<Document> result = new ArrayList<>();
        myColl = db.getCollection("player_cards");
        Consumer<Document> createDocuments = doc -> {result.add(doc);};

        Bson matchNationality = match(and(eq("nationality", nationality), ne("league", "Icons")));
        Bson groupLeague = group("$league",
                                sum("numPlayers", 1),
                                avg("paceAvg", "$pace"),
                                avg("dribblingAvg", "$dribbling"),
                                avg("shootingAvg", "$shooting"),
                                avg("passingAvg", "$passing"),
                                avg("defendingAvg", "$defending"),
                                avg("physicalityAvg", "$physicality")
                                );
        Bson sort = sort(descending("numPlayers"));
        Bson limit = limit(3);
        Bson project = project(fields(excludeId(),
                                    computed("league", "$_id"),
                                    include("numPlayers"),
                                    include("paceAvg"),
                                    include("dribblingAvg"),
                                    include("shootingAvg"),
                                    include("passingAvg"),
                                    include("defendingAvg"),
                                    include("physicalityAvg")
                                    )
                                );

        myColl.aggregate(Arrays.asList(matchNationality, groupLeague, sort, limit, project)).forEach(createDocuments);
        return result;
    }


    /*
    db.player_cards.aggregate(
	[
		//first step

		{
			$match: { league: "Serie A TIM"}
		},

		//second step

		{
			$group: {_id: {quality: "$quality"}, paceAvg : {$avg: "$pace"}, dribblingAvg: {$avg: "$dribbling"},
			shootingAvg: {$avg: "$shooting"}, passingAvg: {$avg: "$passing"}, defendingAvg: {$avg: "$defending"},
			physicalityAvg: {$avg: "$physicality"},
			numPlayers: {$sum: 1}}
		},

		//formatted

		{
			$project:
			{
				_id: 0,
				quality: "$_id.quality",
				numPlayers: "$numPlayers"
				feture: { paceAvg : "$paceAvg", dribblingAvg: "$dribblingAvg", shootingAvg: "$shootingAvg",
				passingAvg: "$passingAvg",
							defendingAvg: "$defendingAvg", physicalityAvg: "$physicalityAvg"},
			}
		}
	]
).pretty()
     */

    public ArrayList<Document> leagueAnalytics(String legue){
        ArrayList<Document> result = new ArrayList<>();
        myColl = db.getCollection("player_cards");
        Consumer<Document> createDocuments = doc -> {result.add(doc);};

        Bson matchLeague = match(eq("league", legue));
        Bson groupQuality = group("$quality",
                                sum("numPlayers", 1),
                                avg("paceAvg", "$pace"),
                                avg("dribblingAvg", "$dribbling"),
                                avg("shootingAvg", "$shooting"),
                                avg("passingAvg", "$passing"),
                                avg("defendingAvg", "$defending"),
                                avg("physicalityAvg", "$physicality")
                                );
        Bson project = project(fields(excludeId(),
                computed("league", "$_id"),
                include("numPlayers"),
                include("paceAvg"),
                include("dribblingAvg"),
                include("shootingAvg"),
                include("passingAvg"),
                include("defendingAvg"),
                include("physicalityAvg")
                )
        );

        myColl.aggregate(Arrays.asList(matchLeague, groupQuality, project)).forEach(createDocuments);
        return result;
    }

    @Override
    public void close(){
        mongoClient.close();
        //logger.info("Mongo close connection!");
    }


    public static void main(String[] args){
        MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
        ArrayList<Document> prova = new ArrayList<>();
        prova  = mongoPlayerCard.leagueAnalytics("Serie A TIM");
        System.out.println(prova);
        //mongoPlayerCard.leagueAnalytics("Serie A TIM");
    }
}
