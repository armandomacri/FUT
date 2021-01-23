package user;

import bean.User;
import mongo.MongoUser;
import neo4j.Neo4jUser;
import serviceExceptions.SignInException;
import serviceExceptions.UserAlreadyExists;
import serviceExceptions.UserNotFoudException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthenticationService {
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    public UserSessionService signIn(String username, String pwd) throws SignInException, UserNotFoudException {

        if (username.equals("") || pwd.equals(""))
            throw new SignInException("Password or Username are incorrect!");

        String encryptedPwd = encryptPassword(pwd);

        MongoUser mongoUser = new MongoUser();
        User u = mongoUser.getUser(username);

        if (u == null)
            throw new UserNotFoudException("User not found");

        /* togliere commento appena creato un profilo
        if (!u.getPassword().equals(encryptedPwd))
            throw new SignInException("Password or Username are incorrect!");
         */

        logger.info("User logged!");
        return UserSessionService.getInstace(u);
    }

    public UserSessionService signUp(String username, String password, String country, String firstName, String lastName) throws UserAlreadyExists {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String encryptedPwd = encryptPassword(password);

        MongoUser mongoUser = new MongoUser();
        Neo4jUser neo4jUser = new Neo4jUser();
        String id = mongoUser.add(firstName, lastName, username, country, df.format(date), encryptedPwd);
        if (id == null)
            throw new UserAlreadyExists("User already Exists!");

        if(!neo4jUser.createUser(id, username)){
            mongoUser.delete(id);
            return null;
        }

        User user = new User(username, firstName, lastName, id, country, date, encryptedPwd, null, 0);

        return UserSessionService.getInstace(user);

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
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException nsae) {
            logger.error("Exception happened! ", nsae);
        }

        return generatedPassword;
    }

    public static void main(String[] args){
        AuthenticationService authenticationService = new AuthenticationService();
        /*
        try {
            authenticationService.signIn("Armando", "oijd");
            //authenticationService.signUp("Armando", "ciao", "Iatly", "armando", "armando");
        }  catch (SignInException | UserNotFoudException e) {
            e.printStackTrace();
        }

         */
        System.out.println(authenticationService.encryptPassword("admin"));
    }

}
