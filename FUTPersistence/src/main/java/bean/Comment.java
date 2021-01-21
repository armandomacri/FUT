package bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String id;
    private String author;
    private Date date;
    private String text;

    public Comment (String id, Date date, String text, String author){
        this.id = id;
        this.date = date;
        this.text = text;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() { return author; }

    public void setId(String id) {
        this.id = id;
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
                "\t DATE: " + date1 + "\t AUTHOR: " + author +
                "\t TEXT: " + text + "\n";
    }
}
