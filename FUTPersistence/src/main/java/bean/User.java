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
    private Date joinDate;
    private ArrayList<Squad> squads;

    //user constructor with squads
    public User(String username, String first_name, String last_name, String user_id, String country, Date join_date, String password, ArrayList<Squad> squads){
        this.username = username;
        this.firstName = first_name;
        this.lastName = last_name;
        this.userId = user_id;
        this.password = password;
        this.country = country;
        this.joinDate = join_date;
        this.squads = squads;
    }

    //user constructor without squads
    public User(String username, String first_name, String last_name, String user_id, String country, Date join_date, String password){
        this.username = username;
        this.firstName = first_name;
        this.lastName = last_name;
        this.userId = user_id;
        this.password = password;
        this.country = country;
        this.joinDate = join_date;

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

    public void setSquads(ArrayList<Squad> squads) {
        this.squads = squads;
    }

    @Override
    public String toString(){
        return "USER {\n" +
                "\t ID: " + userId + "\n" +
                "\t FIRST NAME: " + firstName + "\n" +
                "\t LAST NAME: " + lastName + "\n " +
                "\t USERNAME: " + username + "\n" +
                "\t JOIN DATE: " + joinDate + "\n" +
                "\t COUNTRY: " + country + "\n" +
                "\t PASSWORD: " + password + "\n" +
                "}";
    }
}
