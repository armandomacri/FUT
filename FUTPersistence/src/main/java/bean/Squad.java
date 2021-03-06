package bean;

import java.util.Date;
import java.util.HashMap;

public class Squad {
    private String name;
    private String module;
    private Date date;
    private HashMap<String, Player> players;

    public Squad(String name, String module, Date date){
        this.name = name;
        this.module = module;
        this.date = date;
        this.players = new HashMap<>();
    }

    public Squad(String name, String module, Date date, HashMap<String, Player> players){
        this(name, module, date);
        this.players = players;
    }

    public Squad(){
        players = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    public Date getDate() {
        return date;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    @Override
    public String toString(){
        return "Squad {\n" +
                "\t NAME:" + getName() + "\n" +
                "\t MODULE:" + getModule() + "\n" +
                "\t DATE:" + getDate() + "\n" +
                "\t PLAYERS: " + getPlayers().toString() + "\n" +
                "}";
    }
}

