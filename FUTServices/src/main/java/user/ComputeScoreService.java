package user;

import bean.Challenge;
import bean.Player;
import bean.Squad;
import bean.User;
import mongo.MongoChallenge;
import mongo.MongoUser;
import neo4j.Neo4jUser;
import java.util.*;

public class ComputeScoreService {

    public Challenge results (User home_user, User away_user, Squad homeSquad, Squad awaySquad){
        HashMap<String, Player> homePlayers = homeSquad.getPlayers();
        HashMap<String, Player> awayPlayers = awaySquad.getPlayers();

        ArrayList<Integer> overallPoints = getOverallPoints(homePlayers, awayPlayers);
        ArrayList<Integer> iconPoints = getIconPoints(homePlayers, awayPlayers);
        ArrayList<Integer> bestPlayerPoints = getBestPlayerPoints(homePlayers, awayPlayers);
        int homeScore = getPositioningPoints(homePlayers) + getNationalityPoints(homePlayers) + getLeaguePoints(homePlayers) + overallPoints.get(0)+ iconPoints.get(0) + bestPlayerPoints.get(0);
        int awayScore = getPositioningPoints(awayPlayers) + getNationalityPoints(awayPlayers) + getLeaguePoints(awayPlayers) + overallPoints.get(1)+ iconPoints.get(1) + bestPlayerPoints.get(1);
        int points = getFinalPoints(home_user, away_user, homeScore, awayScore);
        Date date = new Date(System.currentTimeMillis());
        Challenge result = new Challenge(null, home_user.getUserId(), home_user.getUsername(),away_user.getUserId(), away_user.getUsername(), date, homeScore, awayScore, points);
        MongoChallenge mongoChallenge = new MongoChallenge();
        String challID = mongoChallenge.insertChallenge(result);
        if(challID==null){
            return null;
        }
        result.setChallengeId(challID);
        int homePointsToAdd;
        int awayPointsToAdd;
        if(homeScore>awayScore){
            homePointsToAdd = points;
            if(away_user.getScore() < points){
                awayPointsToAdd = -away_user.getScore();
            }
            else {
                awayPointsToAdd = -points;
            }
        }
        else{
            awayPointsToAdd = points;
            if(home_user.getScore() < points){
                homePointsToAdd = -home_user.getScore();
            }
            else {
                homePointsToAdd = -points;
            }
        }
        MongoUser mongoUser = new MongoUser();
        Neo4jUser neo4jUser = new Neo4jUser();

        if (!mongoUser.updateScore(home_user.getUserId(), homePointsToAdd))
            return null;
        if (!mongoUser.updateScore(away_user.getUserId(), awayPointsToAdd)){
            mongoUser.updateScore(home_user.getUserId(), -homePointsToAdd);
            return null;
        }

        if(!neo4jUser.updateScore(home_user.getUserId(), homePointsToAdd)){
            mongoUser.updateScore(away_user.getUserId(), -awayPointsToAdd);
            mongoUser.updateScore(home_user.getUserId(), -homePointsToAdd);
            return null;
        }

        if (!neo4jUser.updateScore(away_user.getUserId(), awayPointsToAdd)){
            neo4jUser.updateScore(home_user.getUserId(), -homePointsToAdd);
            mongoUser.updateScore(away_user.getUserId(), -awayPointsToAdd);
            mongoUser.updateScore(home_user.getUserId(), -homePointsToAdd);
            return null;
        }

        return result;
    }

    //check if all the players are in the right position
    private int getPositioningPoints (HashMap<String, Player> players){

        for (String key : players.keySet()) {
            if (!players.get(key).getPosition().equals(key))
                return 0;
        }
        return 1;
    }

    //check if all the players have the same nationality
    private int getNationalityPoints (HashMap<String, Player> players){
        if (players.size() == 0)
            return 0;
        String nationality = players.get(players.keySet().toArray()[0]).getNationality();
        for (String key : players.keySet()) {
            if (!players.get(key).getNationality().equals(nationality))
                return 0;
        }
        return 1;
    }

    private int getLeaguePoints (HashMap<String, Player> players){

        String league = players.get(players.keySet().toArray()[0]).getLeague();

        for (String key : players.keySet()) {
            if (!players.get(key).getLeague().equals(league))
                return 0;
        }
        return 1;
    }

    private ArrayList<Integer> getOverallPoints (HashMap<String, Player> homePlayers, HashMap<String, Player> awayPlayers){
        ArrayList<Integer> points = new ArrayList<>();
        int sumHome = computeOverall(homePlayers);
        int sumAway = computeOverall(awayPlayers);
        int overallHome = sumHome/11;
        int overallAway = sumAway/11;

        if(overallHome>overallAway){
            points.add(0, 1);
            points.add(1,0);
        }
        else if(overallHome<overallAway){
            points.add(0,0);
            points.add(1, 1);
        }
        else{
            points.add(0, 0);
            points.add(0,0);
        }
        return points;
    }

    private int computeOverall(HashMap<String, Player> players){
        int ov = 0;

        for (String key : players.keySet())
            ov += players.get(key).getOverall();

        return ov;
    }

    private ArrayList<Integer> getIconPoints(HashMap<String, Player> homePlayers, HashMap<String, Player> awayPlayers){
        ArrayList<Integer> points = new ArrayList<>();
        int sumHome = countIcon(homePlayers);
        int sumAway = countIcon(awayPlayers);

        if(sumHome>sumAway){
            points.add(0, 1);
            points.add(1,0);
        }
        else if(sumHome<sumAway){
            points.add(0, 0);
            points.add(1,1);
        }
        else{
            points.add(0, 0);
            points.add(0,0);
        }
        return points;
    }

    private int countIcon (HashMap<String, Player> players){
        int count = 0;
        for (String key : players.keySet())
            if (players.get(key).getRevision().equals("Icon"))
                count++;

        return count;
    }

    private ArrayList<Integer> getBestPlayerPoints (HashMap<String, Player> homePlayers, HashMap<String, Player> awayPlayers){
        ArrayList<Integer> points = new ArrayList<>();
        int bestOverallHome = maxOveral(homePlayers);
        int bestOverallAway = maxOveral(awayPlayers);

        if(bestOverallHome>bestOverallAway){
            points.add(0, 1);
            points.add(1,0);
        }
        else if(bestOverallHome<bestOverallAway){
            points.add(0, 0);
            points.add(1,1);
        }
        else{
            points.add(0, 0);
            points.add(0,0);
        }
        return points;
    }

    private int maxOveral(HashMap<String, Player> players){
        int max = 0;

        for (String key : players.keySet())
            if (players.get(key).getOverall() > max)
                max = players.get(key).getOverall();

        return max;
    }


    private int getFinalPoints(User home_user, User away_user, int homePoints, int awayPoints){
        int points=0;
        int homeSc = home_user.getScore();
        int awaySc = away_user.getScore();
        int diffScore = homeSc - awaySc;
        int diffPoints = homePoints - awayPoints;
        if(Math.abs(diffScore) > 20){
            if(diffPoints < 0 && awaySc<homeSc)
                points = Math.abs(diffPoints) + 2;
            if(diffPoints > 0 && awaySc>homeSc)
                points = Math.abs(diffPoints) + 2;
            if (diffPoints > 0 && awaySc<homeSc)
                points = Math.abs(diffPoints);
            if (diffPoints < 0 && awaySc>homeSc)
                points = Math.abs(diffPoints);
        }
        if(Math.abs(diffScore) > 10 && Math.abs(diffScore)<=20){
            if(diffPoints < 0 && awaySc<homeSc)
                points = Math.abs(diffPoints) + 1;
            if(diffPoints > 0 && awaySc>homeSc)
                points = Math.abs(diffPoints) + 1;
            if (diffPoints > 0 && awaySc<homeSc)
                points = Math.abs(diffPoints);
            if (diffPoints < 0 && awaySc>homeSc)
                points = Math.abs(diffPoints);
        }
        if(Math.abs(diffScore) > 0 && Math.abs(diffScore)<=10){
            points = Math.abs(diffPoints);
        }
        return points;
    }
}
