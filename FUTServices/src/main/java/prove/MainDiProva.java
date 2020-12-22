package prove;

import user.AuthenticationService;


public class MainDiProva {
    public static void main(String[] args){
        AuthenticationService as = new AuthenticationService();
        as.signIn("ciao", "password");

        as.signUp("jd", "jcnfd", "kjsdfnf", "jkdclkmzds", "kjnsc");

    }
}
