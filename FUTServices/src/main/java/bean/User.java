package bean;

import bean.Comment;
import bean.Squad;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String username;
    private String first_name;
    private String last_name;
    private Integer user_id;
    private String country;
    private Date join_date;
    private String password;
    private ArrayList<Squad> squads;
    private ArrayList<Comment> comments;

    public User(String username, String first_name, String last_name, Integer user_id, String country, Date join_date, String password, ArrayList<Squad> squads, ArrayList<Comment> comments){
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_id = user_id;
        this.country = country;
        this.join_date = join_date;
        this.password = password;
        this.squads = squads;
        this.comments = comments;
    }

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getCountry() {
        return country;
    }

    public Date getJoin_date() {
        return join_date;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Squad> getSquads() {
        return squads;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setJoin_date(Date join_date) {
        this.join_date = join_date;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSquads(ArrayList<Squad> squads) {
        this.squads = squads;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
