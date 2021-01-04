package bean;

import java.util.Date;

public class Challenge {
    private String challengeId;
    private String home;
    private String date;
    private String away;
    private Integer homeScore;
    private Integer awayScore;
    private Integer points;

    public Challenge(String challengeId, String home, String date, String away, Integer homeScore, Integer awayScore, Integer points){
        this.challengeId = challengeId;
        this.home = home;
        this.date = date;
        this.away = away;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.points = points;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public String getHome() {
        return home;
    }

    public String getDate() {
        return date;
    }

    public String getAway() {
        return away;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public Integer getPoints() {
        return points;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}

