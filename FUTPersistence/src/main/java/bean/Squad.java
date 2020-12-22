package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Squad {
    private String name;
    private String module;
    private Date date;
    private HashMap<String, Integer> players;

    public Squad(String name, String module, Date date, HashMap<String, Integer> players){
        this.name = name;
        this.module = module;
        this.date = date;
        this.players = players;
    }

    public Squad(){

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

    public HashMap<String, Integer> getPlayers() {
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

    public void setPlayers(HashMap<String, Integer> players) {
        this.players = players;
    }
}

