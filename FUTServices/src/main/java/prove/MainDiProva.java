package prove;

import user.AuthenticationService;
import user.SignUpServices;


public class MainDiProva {
    public static void main(String[] args){
        AuthenticationService as = new AuthenticationService();
        as.signIn("ciao", "password");


        SignUpServices sus = new SignUpServices();
        sus.signUp("jd", "jcnfd", "kjsdfnf", "jkdclkmzds", "kjnsc");


    }
}
