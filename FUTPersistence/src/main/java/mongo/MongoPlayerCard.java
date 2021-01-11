package mongo;

import bean.Player;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
    private MongoCollection<Document> myColl;

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
        }
        return results;
    }

    public Player findById(Integer id){
        myColl = db.getCollection("player_cards");
        Document playerDoc = myColl.find(eq("_id", id)).first();

        /**********************************************************************/
        if(playerDoc == null) //player ancora non caricato
            return null;
        /**********************************************************************/

        return composePlayer(playerDoc);
    }

    public ArrayList<Player> findByPosition(String position){
        myColl = db.getCollection("player_cards");
        ArrayList<Player> results = new ArrayList<>();
        try (MongoCursor<Document> cursor = myColl.find(eq("position", position)).iterator())
        {
            while (cursor.hasNext())
            {
                Document playerDoc = cursor.next();
                if (playerDoc == null)
                    return null;
                results.add(composePlayer(playerDoc));
            }
        }

        return results;
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
        }

        return results;
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
			shootingAvg: {$avg: "$shooting"}, passingAvg: {$avg: "$passing"}, defendingAvg: {$avg: "$defending"}, physicalityAvg: {$avg: "$physicality"},
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
				feature: { paceAvg : "$paceAvg", dribblingAvg: "$dribblingAvg", shootingAvg: "$shootingAvg", passingAvg: "$passingAvg",
							defendingAvg: "$defendingAvg", physicalityAvg: "$physicalityAvg"},
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
			shootingAvg: {$avg: "$shooting"}, passingAvg: {$avg: "$passing"}, defendingAvg: {$avg: "$defending"}, physicalityAvg: {$avg: "$physicality"},
			numPlayers: {$sum: 1}}
		},

		//formatted
		{
			$project:
			{
				_id: 0,
				quality: "$_id.quality",
				numPlayers: "$numPlayers"
				feture: { paceAvg : "$paceAvg", dribblingAvg: "$dribblingAvg", shootingAvg: "$shootingAvg", passingAvg: "$passingAvg",
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
