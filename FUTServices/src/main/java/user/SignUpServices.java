package user;

import bean.User;
//import mongo.ProvaQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpServices {

    public SignUpServices(){ }


    public void signUp(String username, String password, String country, String firstName, String lastName){

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = df.parse(df.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //ProvaQuery pq = new ProvaQuery();
        //int id = pq.countElement("users");
        //User u = new User(username, firstName, lastName, id, country, date, password);
        System.out.println("CIAO");

    }

}
