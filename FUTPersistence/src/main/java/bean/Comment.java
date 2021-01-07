package bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String id;
    private String player_id;
    private String author_username;
    private Date date;
    private String text;

    public Comment (String id, String player_id, Date date, String text){
        this.id = id;
        this.player_id = player_id;
        this.date = date;
        this.text = text;
    }

    public Comment (String id, String player_id, Date date, String text, String author){
        this.id = id;
        this.player_id = player_id;
        this.date = date;
        this.text = text;
        this.author_username = author;
    }

    public String getId() {
        return id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = df.format(date);
        return "Comment: " + id + " {\n" +
                "\t DATE: " + date1 + "\t AUTHOR: " + author_username +
                "\t TEXT: " + text + "\n";
    }
}
