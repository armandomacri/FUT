package user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationService {

    public AuthenticationService(){}

    public boolean signIn(String user, String pwd){

        if (user.equals("") || pwd.equals("")){
            return false;
            //inventare un'eccezione
        }
        String encryptedPwd = encryptPassword(pwd);

        //cercare nel database l'utente

        return true;
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

        System.out.println(passwordToHash + " ------ " + generatedPassword);

        return generatedPassword;
    }
}
