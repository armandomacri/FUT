package mongo;

import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class MongoAdmin extends MongoConnection{
    private static final Logger logger = LogManager.getLogger(MongoAdmin.class);
    private MongoCollection<Document> myColl;

    public ArrayList<Document> getUserPerCountry(){
        ArrayList<Document> result = new ArrayList<>();
        Consumer<Document> createDocuments = doc -> { result.add(doc);};
        Bson groupCountry = group("$country", sum("numUsers", 1));
        Bson order = sort(descending("numUsers"));
        Bson lim = limit(10);
        boolean t = true;
        try {
            myColl = db.getCollection("users");
            myColl.aggregate(Arrays.asList(groupCountry, order, lim)).forEach(createDocuments);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            t = false;
        }
        if (!t)
            return null;
        return result;
    }

    public ArrayList<Document> challengesPerDay(){
        ArrayList<Document> result = new ArrayList<>();
        Consumer<Document> createDocuments = doc -> {result.add(doc);};
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        cal.add(Calendar.MONTH, -1);
        Date monthAgo = cal.getTime();
        Bson matchDate = match(and(lt("date", yesterday), gt("date", monthAgo)));
        Bson groupDate = group("$date", sum("numChallenges", 1));
        Bson sortDate = sort(ascending("_id"));

        Bson project = project(fields(excludeId(),
                computed("date", "$_id"),
                include("numChallenges"))
        );
        Bson limit = limit(31);
        boolean t = true;
        try {
            myColl = db.getCollection("challenge");
            myColl.aggregate(Arrays.asList(matchDate, groupDate, sortDate, limit, project))
                    .forEach(createDocuments);
        } catch(Exception e){
            logger.error("Exception occurred: ", e);
            t = false;
        }
        if (!t)
            return null;
        return result;
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

        Bson matchNationality = match(and(eq("nationality", nationality),
                                          ne("league", "Icons")));
        Bson groupLeague = group("$league",
                sum("numPlayers", 1),
                avg("paceAvg", "$pace"),
                avg("dribblingAvg", "$dribbling"),
                avg("shootingAvg", "$shooting"),
                avg("passingAvg", "$passing"),
                avg("defendingAvg", "$defending"),
                avg("physicalityAvg", "$physicality"));
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
                include("physicalityAvg"))
        );
        try{
            myColl.aggregate(Arrays.asList(matchNationality, groupLeague, sort, limit, project)).forEach(createDocuments);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
        }
        return result;
    }

        /*
    db.users.aggregate(
  [
    //first step

        { $match: {"country": "Italy"}},

    //second step

        {$unwind: "$squads"},

    //forth step (distinct)

        {
          $group: {
            _id: { module: "$squads.module", user: "$_id"}
          }
        },

    //fifth step

        {
          $group: {
            _id: { module: "$_id.module"},
            num: {$sum: 1}
          }
        },

    //sixth step

        { $sort: {num:-1} },

    //seventh step

        { $limit: 3 },

    //formatting
        {
          $project: {
            _id:0,
            module: "$_id.module", use: "$num"
          }
        }
      ]
    );
 */

    public ArrayList<Document> SquadAnalytics(String country){
        ArrayList<Document> result = new ArrayList<>();
        Consumer<Document> createDocuments = doc -> {result.add(doc);};
        Bson matchCountry = match(and(eq("country", country)));
        Bson unqindSquads = unwind("$squads");
        Bson distinctUserModules = new Document("$group", new Document("_id", new Document("user", "$_id")
                                        .append("module", "$squads.module")));
        Bson groupModules = group("$_id.module", sum("use", 1));
        Bson sort = sort(descending("use"));
        Bson limit = limit(3);
        Bson project = project(fields(excludeId(),
                include("use"),
                computed("module", "$_id")
                )
        );
        try {
            myColl = db.getCollection("users");
            myColl.aggregate(Arrays.asList(matchCountry, unqindSquads, distinctUserModules, groupModules,
                                           sort, limit, project)).
                    forEach(createDocuments);
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            return null;
        }
        return result;
    }



    public long countPlayerCards(){
        long result;
        try {
            myColl = db.getCollection("player_cards");
            result = myColl.countDocuments();
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = -1;
        }
        return result;
    }

    public long countUsers(){
        long result;
        try {
            myColl = db.getCollection("users");
            result = myColl.countDocuments();
        } catch (Exception e){
            logger.error("Exception occurred: ", e);
            result = -1;
        }
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

    public ArrayList<Document> leagueAnalytics(String league){
        ArrayList<Document> result = new ArrayList<>();
        boolean r = true;
        myColl = db.getCollection("player_cards");
        Consumer<Document> createDocuments = doc -> {result.add(doc);};

        Bson matchLeague = match(eq("league", league));
        Bson groupQuality = group("$quality",
                sum("numPlayers", 1),
                avg("paceAvg", "$pace"),
                avg("dribblingAvg", "$dribbling"),
                avg("shootingAvg", "$shooting"),
                avg("passingAvg", "$passing"),
                avg("defendingAvg", "$defending"),
                avg("physicalityAvg", "$physicality"));
        Bson project = project(fields(excludeId(),
                computed("league", "$_id"),
                include("numPlayers"),
                include("paceAvg"),
                include("dribblingAvg"),
                include("shootingAvg"),
                include("passingAvg"),
                include("defendingAvg"),
                include("physicalityAvg")));
        try{
            myColl.aggregate(Arrays.asList(matchLeague, groupQuality, project)).forEach(createDocuments);
        } catch(Exception e){
            logger.error("Exception occurred: ", e);
            r = false;
        }
        if (!r)
            return null;
        return result;
    }

    /*
    db.users.aggregate(
	[
		{ $unwind: "$squads" },
		{ $match: { "squads.players.GK.player_name": { $exists: true}}},
		{
			$group: {_id: { player: "$squads.players.GK.player_name"}, sum : { $sum: 1 } }
		},
		{ $sort: { sum: -1} },
		{ $limit: 1 },
		{
			$project: {
				_id: 0,
				mostUsedPlayer: "$_id.player",
			}
		}
	]
)
     */

    public String mostUsedPlayer(String pos){
        boolean result = true;
        Bson unwindSquads = unwind("$squads");
        Bson matchPos = match(exists("squads.players."+pos+".player_name", true));
        Bson groupPlayer = group("$squads.players."+pos+".player_name", sum("count", 1));
        Bson sortPlayer = sort(descending("count"));
        Bson limit = limit(1);
        Bson project = project(fields(excludeId(), computed("mostUsedPlayer", "$_id")));
        Document doc = null;
        try{
            myColl = db.getCollection("users");
             doc = myColl.aggregate(Arrays.asList(unwindSquads, matchPos, groupPlayer, sortPlayer, limit, project)).first();
        } catch(Exception e){
            logger.error("Exception occurred: ", e);
            result = false;
        }

        if(!result)
            return null;
        return  doc.get("mostUsedPlayer").toString();
    }

    @Override
    public void close(){
        if (mongoClient != null)
            mongoClient.close();
    }

    public static void main(String[] args){
        MongoAdmin mongoAdmin = new MongoAdmin();
        System.out.println(mongoAdmin.mostUsedPlayer("GK"));
    }
}
