package org.unipi.group15;


import bean.Squad;

public class BuildSquadController {
    private static Squad squad;

    public void setSquad(Squad s){
        squad = s;
        System.out.println(s.toString());
    }
}
