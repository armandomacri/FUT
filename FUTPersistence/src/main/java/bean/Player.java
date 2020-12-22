package bean;

import java.util.ArrayList;
import java.util.Date;

public class Player {
    private Integer futbin_id;
    private String player_name;
    private String player_extended_name;
    private String quality;
    private String revision;
    private String origin;
    private Integer overall;
    private String club;
    private String league;
    private String nationality;
    private String position;
    private Date date_of_birth;
    private Integer height;
    private Integer weight;
    private Date added_date;
    private Integer pace;
    private Integer dribbling;
    private Integer shooting;
    private Integer passing;
    private Integer defending;
    private Integer physicality;
    private Integer gk_diving;
    private Integer gk_reflexe;
    private Integer gk_handling;
    private Integer gk_speed;
    private Integer gk_kicking;
    private Integer gk_positioning;
    private String pref_foot;
    private Integer weak_foot;
    private Integer skill_moves;
    private ArrayList<String> traits;
    private ArrayList<String> images;

    public Player(Integer futbin_id, String player_name, String player_extended_name, String quality, String revision, String origin, Integer overall, String club, String league, String nationality, String position, Date date_of_birth, Integer height, Integer weight, Date added_date, Integer pace, Integer dribbling, Integer shooting, Integer passing, Integer defending, Integer physicality, Integer gk_diving, Integer gk_reflexe, Integer gk_handling, Integer gk_speed, Integer gk_kicking, Integer gk_positioning, String pref_foot, Integer weak_foot, Integer skill_moves, ArrayList<String> traits, ArrayList<String> images){
        this.futbin_id = futbin_id;
        this.player_name = player_name;
        this.player_extended_name = player_extended_name;
        this.quality = quality;
        this.revision = revision;
        this.origin = origin;
        this.overall = overall;
        this.club = club;
        this.league = league;
        this.position = position;
        this.date_of_birth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.added_date = added_date;
        this.pace = pace;
        this.dribbling = dribbling;
        this.shooting = shooting;
        this.passing = passing;
        this.defending = defending;
        this.physicality = physicality;
        this.gk_diving = gk_diving;
        this.gk_reflexe = gk_reflexe;
        this.gk_handling = gk_handling;
        this.gk_speed = gk_speed;
        this.gk_kicking = gk_kicking;
        this.gk_positioning = gk_positioning;
        this.pref_foot = pref_foot;
        this.weak_foot = weak_foot;
        this.skill_moves = skill_moves;
        this.traits = traits;
        this.images = images;
    }

    public Player(){

    }

    public Integer getFutbin_id() {
        return futbin_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public String getPlayer_extended_name() {
        return player_extended_name;
    }

    public String getQuality() {
        return quality;
    }

    public String getRevision() {
        return revision;
    }

    public String getOrigin() {
        return origin;
    }

    public Integer getOverall() {
        return overall;
    }

    public String getClub() {
        return club;
    }

    public String getLeague() {
        return league;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPosition() {
        return position;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Date getAdded_date() {
        return added_date;
    }

    public Integer getPace() {
        return pace;
    }

    public Integer getDribbling() {
        return dribbling;
    }

    public Integer getShooting() {
        return shooting;
    }

    public Integer getPassing() {
        return passing;
    }

    public Integer getDefending() {
        return defending;
    }

    public Integer getPhysicality() {
        return physicality;
    }

    public Integer getGk_diving() {
        return gk_diving;
    }

    public Integer getGk_reflexe() {
        return gk_reflexe;
    }

    public Integer getGk_handling() {
        return gk_handling;
    }

    public Integer getGk_speed() {
        return gk_speed;
    }

    public Integer getGk_kicking() {
        return gk_kicking;
    }

    public Integer getGk_positioning() {
        return gk_positioning;
    }

    public String getPref_foot() {
        return pref_foot;
    }

    public Integer getWeak_foot() {
        return weak_foot;
    }

    public Integer getSkill_moves() {
        return skill_moves;
    }

    public ArrayList<String> getTraits() {
        return traits;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setFutbin_id(Integer futbin_id) {
        this.futbin_id = futbin_id;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public void setPlayer_extended_name(String player_extended_name) {
        this.player_extended_name = player_extended_name;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setAdded_date(Date added_date) {
        this.added_date = added_date;
    }

    public void setPace(Integer pace) {
        this.pace = pace;
    }

    public void setDribbling(Integer dribbling) {
        this.dribbling = dribbling;
    }

    public void setShooting(Integer shooting) {
        this.shooting = shooting;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }

    public void setDefending(Integer defending) {
        this.defending = defending;
    }

    public void setPhysicality(Integer physicality) {
        this.physicality = physicality;
    }

    public void setGk_diving(Integer gk_diving) {
        this.gk_diving = gk_diving;
    }

    public void setGk_reflexe(Integer gk_reflexe) {
        this.gk_reflexe = gk_reflexe;
    }

    public void setGk_handling(Integer gk_handling) {
        this.gk_handling = gk_handling;
    }

    public void setGk_speed(Integer gk_speed) {
        this.gk_speed = gk_speed;
    }

    public void setGk_kicking(Integer gk_kicking) {
        this.gk_kicking = gk_kicking;
    }

    public void setGk_positioning(Integer gk_positioning) {
        this.gk_positioning = gk_positioning;
    }

    public void setPref_foot(String pref_foot) {
        this.pref_foot = pref_foot;
    }

    public void setWeak_foot(Integer weak_foot) {
        this.weak_foot = weak_foot;
    }

    public void setSkill_moves(Integer skill_moves) {
        this.skill_moves = skill_moves;
    }

    public void setTraits(ArrayList<String> traits) {
        this.traits = traits;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
