package user;

import bean.Squad;
import bean.User;

import java.util.ArrayList;
import java.util.Date;

public class UserSessionService {
    private static UserSessionService instance;
    private static User user;

    /*               SINGLETONE PATTERN            */
    private UserSessionService() {
    }

    public static UserSessionService getInstace(User user) {
        if(instance == null) {
            instance = new UserSessionService();
        }
        setUser(user);
        return instance;
    }

    private static void setUser(User u) {user = u;}

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getUsername() { return user.getUsername(); }

    public String getUserId(){ return user.getUserId(); }

    public Date getJoinDate(){ return user.getJoinDate(); }

    public String getCountry(){ return user.getCountry(); }

    public ArrayList<Squad> getSquads() { return user.getSquads(); }

    public int getScore() { return user.getScore(); }

    public User getUser() { return user; }

    public String getAdministrator() { return user.getAdministrator(); }

    public void setSquads(ArrayList<Squad> squads) { user.setSquads(squads); }

    public void clear() {
        this.user = null;
    }

    public void setScore(Integer new_score){ user.setScore(new_score); }

    @Override
    public String toString() {
        return "UserSession{\n" +
                "\tFIRST NAME = " + user.getFirstName() + "\n " +
                "\tLAST NAME = " + user.getLastName() + "\n " +
                "\tUSERNAME = " + user.getUsername() + "\n " +
                "\tJOIN DATE = "+ user.getJoinDate() +
                "\n\t}";
    }
}
