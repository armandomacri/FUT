package bean;

import java.util.ArrayList;
import java.util.Date;

public class Player {
    private String futbinId;
    private String playerName;
    private String playerExtendedName;
    private String quality;
    private String revision;
    private String origin;
    private Integer overall;
    private String club;
    private String league;
    private String nationality;
    private String position;
    private Date dateOfBirth;
    private Integer height;
    private Integer weight;
    private Date addedDate;
    private Integer pace;
    private Integer dribbling;
    private Integer shooting;
    private Integer passing;
    private Integer defending;
    private Integer physicality;
    private Integer gkDiving;
    private Integer gkReflexe;
    private Integer gkHandling;
    private Integer gkSpeed;
    private Integer gkKicking;
    private Integer gkPositioning;
    private String prefFoot;
    private Integer weakFoot;
    private Integer skillMoves;
    private String traits;
    private String[] images;

    public Player(String futbin_id, String player_name, String player_extended_name, String quality, String revision, String origin, Integer overall, String club, String league, String nationality, String position, Date date_of_birth, Integer height, Integer weight, Date added_date, Integer pace, Integer dribbling, Integer shooting, Integer passing, Integer defending, Integer physicality, Integer gk_diving, Integer gk_reflexe, Integer gk_handling, Integer gk_speed, Integer gk_kicking, Integer gk_positioning, String pref_foot, Integer weak_foot, Integer skill_moves, String traits, String[] images){
        this.futbinId = futbin_id;
        this.playerName = player_name;
        this.playerExtendedName = player_extended_name;
        this.quality = quality;
        this.revision = revision;
        this.origin = origin;
        this.overall = overall;
        this.club = club;
        this.league = league;
        this.position = position;
        this.nationality = nationality;
        this.dateOfBirth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.addedDate = added_date;
        this.pace = pace;
        this.dribbling = dribbling;
        this.shooting = shooting;
        this.passing = passing;
        this.defending = defending;
        this.physicality = physicality;
        this.gkDiving = gk_diving;
        this.gkReflexe = gk_reflexe;
        this.gkHandling = gk_handling;
        this.gkSpeed = gk_speed;
        this.gkKicking = gk_kicking;
        this.gkPositioning = gk_positioning;
        this.prefFoot = pref_foot;
        this.weakFoot = weak_foot;
        this.skillMoves = skill_moves;
        this.traits = traits;
        this.images = images;
    }

    public Player(String futbin_id, String player_name, String player_extended_name, String quality, String revision, Integer overall, String origin, String club, String league, String nationality, String position, Date date_of_birth, Integer height, Integer weight, Date added_date, Integer gk_diving, Integer gk_reflexe, Integer gk_handling, Integer gk_speed, Integer gk_kicking, Integer gk_positioning, String pref_foot, Integer weak_foot, Integer skill_moves, String traits, String[] images){
        this.futbinId = futbin_id;
        this.playerName = player_name;
        this.playerExtendedName = player_extended_name;
        this.quality = quality;
        this.revision = revision;
        this.origin = origin;
        this.overall = overall;
        this.club = club;
        this.league = league;
        this.position = position;
        this.nationality = nationality;
        this.dateOfBirth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.addedDate = added_date;
        this.gkDiving = gk_diving;
        this.gkReflexe = gk_reflexe;
        this.gkHandling = gk_handling;
        this.gkSpeed = gk_speed;
        this.gkKicking = gk_kicking;
        this.gkPositioning = gk_positioning;
        this.prefFoot = pref_foot;
        this.weakFoot = weak_foot;
        this.skillMoves = skill_moves;
        this.traits = traits;
        this.images = images;
    }

    public Player(String futbin_id, String player_name, String player_extended_name, String quality, String revision, String origin, Integer overall, String club, String league, String nationality, String position, Date date_of_birth, Integer height, Integer weight, Date added_date, Integer pace, Integer dribbling, Integer shooting, Integer passing, Integer defending, Integer physicality, String pref_foot, Integer weak_foot, Integer skill_moves, String traits, String[] images){
        this.futbinId = futbin_id;
        this.playerName = player_name;
        this.playerExtendedName = player_extended_name;
        this.quality = quality;
        this.revision = revision;
        this.origin = origin;
        this.overall = overall;
        this.club = club;
        this.league = league;
        this.position = position;
        this.nationality = nationality;
        this.dateOfBirth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.addedDate = added_date;
        this.pace = pace;
        this.dribbling = dribbling;
        this.shooting = shooting;
        this.passing = passing;
        this.defending = defending;
        this.physicality = physicality;
        this.prefFoot = pref_foot;
        this.weakFoot = weak_foot;
        this.skillMoves = skill_moves;
        this.traits = traits;
        this.images = images;
    }

    public Player(){

    }

    public String getFutbinId() {
        return futbinId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerExtendedName() {
        return playerExtendedName;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Date getAddedDate() {
        return addedDate;
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

    public Integer getGkDiving() {
        return gkDiving;
    }

    public Integer getGkReflexe() {
        return gkReflexe;
    }

    public Integer getGkHandling() {
        return gkHandling;
    }

    public Integer getGkSpeed() {
        return gkSpeed;
    }

    public Integer getGkKicking() {
        return gkKicking;
    }

    public Integer getGkPositioning() {
        return gkPositioning;
    }

    public Integer getSkillMoves() {
        return skillMoves;
    }

    public Integer getWeakFoot() {
        return weakFoot;
    }

    public String getPrefFoot() {
        return prefFoot;
    }

    public String getTraits() {
        return traits;
    }

    public String[] getImages() {
        return images;
    }

    public void setFutbinId(String futbinId) {
        this.futbinId = futbinId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerExtendedName(String playerExtendedName) {
        this.playerExtendedName = playerExtendedName;
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

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
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

    public void setGkDiving(Integer gkDiving) {
        this.gkDiving = gkDiving;
    }

    public void setGkHandling(Integer gkHandling) {
        this.gkHandling = gkHandling;
    }

    public void setGkKicking(Integer gkKicking) {
        this.gkKicking = gkKicking;
    }

    public void setGkPositioning(Integer gkPositioning) {
        this.gkPositioning = gkPositioning;
    }

    public void setGkReflexe(Integer gkReflexe) {
        this.gkReflexe = gkReflexe;
    }

    public void setGkSpeed(Integer gkSpeed) {
        this.gkSpeed = gkSpeed;
    }

    public void setPrefFoot(String prefFoot) {
        this.prefFoot = prefFoot;
    }

    public void setSkillMoves(Integer skillMoves) {
        this.skillMoves = skillMoves;
    }

    public void setWeakFoot(Integer weakFoot) {
        this.weakFoot = weakFoot;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
