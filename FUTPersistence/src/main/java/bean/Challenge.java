package bean;

public class Challenge {
    private String challengeId;
    private String home;
    private String homeUser;
    private String date;
    private String away;
    private String awayUser;
    private Integer homeScore;
    private Integer awayScore;
    private Integer points;

    public Challenge(String challengeId, String home, String homeUser, String away, String awayUser, String date, Integer homeScore, Integer awayScore, Integer points){
        this.challengeId = challengeId;
        this.home = home;
        this.homeUser = homeUser;
        this.date = date;
        this.away = away;
        this.awayUser = awayUser;
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

    public String getHomeUser() {
        return homeUser;
    }

    public String getDate() {
        return date;
    }

    public String getAway() {
        return away;
    }

    public String getAwayUser() {
        return awayUser;
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

    public void setHomeUser(String homeUser) {
        this.homeUser = homeUser;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public void setAwayUser(String awayUser) {
        this.awayUser = awayUser;
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

