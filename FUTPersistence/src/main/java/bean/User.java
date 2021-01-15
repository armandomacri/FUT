package bean;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String country;
    private Integer score;
    private Date joinDate;
    private ArrayList<Squad> squads;

    //user constructor with squads
    public User(String username, String first_name, String last_name,
                String user_id, String country, Date join_date, String password,
                ArrayList<Squad> squads, int score){
        this.username = username;
        this.firstName = first_name;
        this.lastName = last_name;
        this.userId = user_id;
        this.password = password;
        this.country = country;
        this.joinDate = join_date;
        this.squads = squads;
        this.score = score;
    }

    //user constructor only 2 parameter
    public User(String username, String user_id){
        this.username = username;
        this.userId = user_id;
        this.squads = new ArrayList<>();
    }

    //user constructor only 3 parameter
    public User(String username, String user_id, Integer score){
        this.username = username;
        this.userId = user_id;
        this.score = score;
        this.squads = new ArrayList<>();
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() { return password; }

    public String getCountry() {
        return country;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public Integer getScore() { return score; }

    public ArrayList<Squad> getSquads() {
        return squads;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public void setUserId(String user_id) {
        this.userId = user_id;
    }

    public void setPassword(String password) { this.password = password; }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setJoinDate(Date join_date) {
        this.joinDate = join_date;
    }

    public void setScore(Integer score) { this.score = score; }

    public void setSquads(ArrayList<Squad> squads) {
        this.squads = squads;
    }


    @Override
    public String toString(){
        StringBuilder s;
        if(firstName == null){
            s = new StringBuilder("USER {\n" +
                    "\t ID: " + userId + "\n" +
                    "\t USERNAME: " + username + "\n");
        }
        else{
            s = new StringBuilder("USER {\n" +
                    "\t ID: " + userId + "\n" +
                    "\t FIRST NAME: " + firstName + "\n" +
                    "\t LAST NAME: " + lastName + "\n " +
                    "\t USERNAME: " + username + "\n" +
                    "\t JOIN DATE: " + joinDate + "\n" +
                    "\t COUNTRY: " + country + "\n" +
                    "\t PASSWORD: " + password + "\n" +
                    "\t SCORE: " + score + "\n" +
                    "\t SQUADS {\n");
        }

        if (squads != null)
            for(Squad squad : squads)
                s.append("\t").append(squad.toString());

        s.append("\t }\n}");

        return s.toString();
    }
}
