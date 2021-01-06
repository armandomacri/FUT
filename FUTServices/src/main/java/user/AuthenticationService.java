package user;

import bean.User;
import mongo.MongoUser;
import serviceExceptions.SignInException;
import serviceExceptions.UserAlreadyExists;
import serviceExceptions.UserNotFoudException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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

        MongoUser pq = new MongoUser();
        User u = pq.getUser(username);

        if (u == null)
            throw new UserNotFoudException("User not found");

        /* tigliere commento appena creato un profilo
        if (!u.getPassword().equals(encryptedPwd))
            throw new SignInException("Password or Username are incorrect!");

         */

        UserSessionService s = UserSessionService.getInstace(u);
        logger.info("User logged!");
        return s;
    }

    public UserSessionService signUp(String username, String password, String country, String firstName, String lastName) throws UserAlreadyExists {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = df.parse(df.format(new Date()));
        } catch (ParseException pe) {
            logger.error("Exception happened! ", pe);
        }

        String encryptedPwd = encryptPassword(password);

        MongoUser pq = new MongoUser();
        User u = pq.getUser(username);
        if (u != null)
            throw new UserAlreadyExists("User already Exists!");

        String id = pq.add(firstName, lastName, username, country, date, encryptedPwd);

        User user = new User(username, firstName, lastName, id, country, date, encryptedPwd);
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
}
