package user;

import bean.Challenge;
import bean.Squad;
import bean.User;
import mongo.MongoChallenge;
import mongo.MongoUser;
import neo4j.Neo4jUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComputeScoreService {

    private static Integer id = 89976;

    public Challenge results (User home_user, User away_user, Squad homeSquad, Squad awaySquad){
        ArrayList<Integer> overallPoints = getOverallPoints(homeSquad, awaySquad);
        ArrayList<Integer> iconPoints = getIconPoints(homeSquad, awaySquad);
        ArrayList<Integer> bestPlayerPoints = getBestPlayerPoints(homeSquad, awaySquad);
        int homeScore = getPositioningPoints(homeSquad) + getNationalityPoints(homeSquad) + getLeaguePoints(homeSquad) + overallPoints.get(0)+ iconPoints.get(0) + bestPlayerPoints.get(0);
        int awayScore = getPositioningPoints(awaySquad) + getNationalityPoints(awaySquad) + getLeaguePoints(awaySquad) + overallPoints.get(1)+ iconPoints.get(1) + bestPlayerPoints.get(1);
        int points = getFinalPoints(home_user, away_user, homeScore, awayScore);
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(System.currentTimeMillis());
        Challenge result = new Challenge(id.toString(), home_user.getUserId(), home_user.getUsername(),away_user.getUserId(), away_user.getUsername(), formatter.format(date), homeScore, awayScore, points);
        MongoChallenge mc = new MongoChallenge();
        String challID = mc.insertChallenge(result);
        System.out.println("Challenge " + challID + " added");
        addId();
        MongoUser mu = new MongoUser();
        Neo4jUser n4u = new Neo4jUser();
        if(homeScore>awayScore){
            mu.updateScore(home_user.getUserId(), points);
            n4u.updateScore(home_user.getUserId(), points);
            if(away_user.getScore() < points){
                mu.updateScore(away_user.getUserId(), -away_user.getScore());
                n4u.updateScore(away_user.getUserId(), -away_user.getScore());
            }
            else {
                mu.updateScore(away_user.getUserId(), -points);
                n4u.updateScore(away_user.getUserId(), -points);
            }
        }
        else{
            mu.updateScore(away_user.getUserId(), points);
            n4u.updateScore(away_user.getUserId(), points);
            if(home_user.getScore() < points){
                mu.updateScore(home_user.getUserId(), -home_user.getScore());
                n4u.updateScore(home_user.getUserId(), -home_user.getScore());
            }
            else {
                mu.updateScore(home_user.getUserId(), -points);
                n4u.updateScore(home_user.getUserId(), -points);
            }
        }
        return result;
    }

    public static void addId() {
         id++;
    }

    public static Integer getId() {
        return id;
    }

    public int getPositioningPoints (Squad homeSquad){
        int count = 0;
        for (int i = 0; i < 11; i++) {
            if (homeSquad.getPlayers().keySet().toArray()[i].toString().contains(homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getPosition())) {
                count++;
            }
        }
        return count == 11 ? 1 : 0;
    }

    public int getNationalityPoints (Squad homeSquad){
        int count = 1;
        String nationality = homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[0]).getNationality();
        for (int i = 1; i < 11; i++) {
            if (homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getNationality().equals(nationality)) {
                count++;
            }
        }
        return count == 11 ? 1 : 0;
    }

    public int getLeaguePoints (Squad homeSquad){
        int count = 1;
        String nationality = homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[0]).getLeague();
        for (int i = 1; i < 11; i++) {
            if (homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getLeague().equals(nationality)) {
                count++;
            }
        }
        return count == 11 ? 1 : 0;
    }

    public ArrayList<Integer> getOverallPoints (Squad homeSquad, Squad awaySquad){
        ArrayList<Integer> points = new ArrayList<>();
        int sumHome = 0;
        int sumAway = 0;
        int overallHome;
        int overallAway;

        for (int i = 0; i < 11; i++) {
           sumHome += homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getOverall();
           sumAway += awaySquad.getPlayers().get(awaySquad.getPlayers().keySet().toArray()[i]).getOverall();
        }
        overallHome=sumHome/11;
        overallAway=sumAway/11;
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

    public ArrayList<Integer> getIconPoints (Squad homeSquad, Squad awaySquad){
        ArrayList<Integer> points = new ArrayList<>();
        int sumHome = 0;
        int sumAway = 0;

        for (int i = 0; i < 11; i++) {
            if(homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getRevision().equals("Icon"))
                sumHome++;
            if(awaySquad.getPlayers().get(awaySquad.getPlayers().keySet().toArray()[i]).getRevision().equals("Icon"))
                sumAway++;
        }
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

    public ArrayList<Integer> getBestPlayerPoints (Squad homeSquad, Squad awaySquad){
        ArrayList<Integer> points = new ArrayList<>();
        int bestOverallHome = homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[0]).getOverall();
        int bestOverallAway = awaySquad.getPlayers().get(awaySquad.getPlayers().keySet().toArray()[0]).getOverall();
        for (int i = 1; i < 11; i++) {
            if(homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getOverall()>bestOverallHome)
                bestOverallHome = homeSquad.getPlayers().get(homeSquad.getPlayers().keySet().toArray()[i]).getOverall();
            if(awaySquad.getPlayers().get(awaySquad.getPlayers().keySet().toArray()[i]).getOverall()>bestOverallAway)
                bestOverallAway = awaySquad.getPlayers().get(awaySquad.getPlayers().keySet().toArray()[i]).getOverall();
        }
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


    public int getFinalPoints(User home_user, User away_user, int homePoints, int awayPoints){
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
