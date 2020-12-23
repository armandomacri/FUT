package prove;

import serviceExceptions.SignInException;
import serviceExceptions.UserAlreadyExists;
import serviceExceptions.UserNotFoudException;
import user.AuthenticationService;


public class MainDiProva {
    public static void main(String[] args) throws SignInException, UserNotFoudException, UserAlreadyExists {
        AuthenticationService as = new AuthenticationService();
        as.signIn("ciao", "password");

        as.signUp("jd", "jcnfd", "kjsdfnf", "jkdclkmzds", "kjnsc");

    }
}
