package user;

import bean.Squad;
import bean.User;

import java.util.ArrayList;
import java.util.Date;

public class UserSessionService {
    private static UserSessionService instance;
    private User user;

    /*               SINGLETONE PATTERN            */
    private UserSessionService(User user) {
        this.user = user;
    }

    public static UserSessionService getInstace(User user) {
        if(instance == null) {
            instance = new UserSessionService(user);
        }
        return instance;
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getUserId(){ return user.getUserId(); }

    public String getPassword() { return user.getPassword(); }

    public Date getJoinDate(){ return user.getJoinDate(); }

    public String getCountry(){ return user.getCountry(); }

    public ArrayList<Squad> getSquads() { return user.getSquads(); }

    public void clear() {
        this.user = null;
    }

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
