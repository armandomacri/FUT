package user;

import bean.User;

public class UserSessionService {
    private static UserSessionService instance;
    private User user;


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
    };

    public void cleanUserSession() {
        this.user = null;
    }

    @Override
    public String toString() {
        return "UserSession{\n" +
                "\tFIRST NAME = " + user.getFirstName() + "\n " +
                "\tLAST NAME = " + user.getLastName() + "\n " +
                "\tUSERNAME = " + user.getUsername() + "\n " +
                "\tJOIN DATE = "+ user.getJoinDate() +
                "\n}";
    }
}
