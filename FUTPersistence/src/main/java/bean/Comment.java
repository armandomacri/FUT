package bean;

import java.util.Date;

public class Comment {
    private Integer id;
    private Integer player_id;
    // aggiungere anche id user????
    private Date date;
    private String text;

    public Comment (Integer id, Integer player_id, Date date, String text){
        this.id = id;
        this.player_id = player_id;
        this.date = date;
        this.text = text;
    }

    public Comment(){

    }

    public Integer getId() {
        return id;
    }

    public Integer getPlayer_id() {
        return player_id;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPlayer_id(Integer player_id) {
        this.player_id = player_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }
}
