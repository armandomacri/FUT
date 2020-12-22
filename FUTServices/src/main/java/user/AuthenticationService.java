package user;

import bean.User;
import mongo.ProvaQuery;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthenticationService {

    public AuthenticationService(){}

    public UserSessionService signIn(String username, String pwd){

        if (username.equals("") || pwd.equals("")){
            return null;
            //inventare un'eccezione
        }
        String encryptedPwd = encryptPassword(pwd);

        ProvaQuery pq = new ProvaQuery();
        User u = pq.show_profile_information(username);
        /*
        if(!u.getPassword().equals(encryptedPwd))
            return null;

         */

        UserSessionService s = UserSessionService.getInstace(u);

        return s;
    }

    public UserSessionService signUp(String username, String password, String country, String firstName, String lastName){

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = df.parse(df.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //lanciare eccezione se non trova l'utente

        ProvaQuery pq = new ProvaQuery();
        //assegnare l'id che da mongo
        String id = "iudhud";
        User u = new User(username, firstName, lastName, id, country, date, password);
        UserSessionService s = UserSessionService.getInstace(u);
        return s;

    }


    //https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private String encryptPassword(String passwordToHash){
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        //System.out.println(passwordToHash + " ------ " + generatedPassword);

        return generatedPassword;
    }
}
