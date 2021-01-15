package org.unipi.group15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serviceExceptions.SignInException;
import serviceExceptions.UserNotFoudException;
import user.AuthenticationService;
import user.UserSessionService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SignInController {
    private static final Logger logger = LogManager.getLogger(SignInController.class);

    @FXML private TextField usernameTextField;

    @FXML private PasswordField passwordTextField;

    @FXML private HBox errorBox;

    @FXML public void initialize() {
        errorBox.setVisible(false);
    }

    @FXML
    private void switchToSecondary() {
        App.setRoot("secondary");
    }

    @FXML
    private void signIn(){
        AuthenticationService authenticationService = new AuthenticationService();
        UserSessionService userSession = null;
        try {
            userSession = authenticationService.signIn(usernameTextField.getText(), passwordTextField.getText());
        } catch (SignInException sie) {
            setErrorBox("Username or Password are wrong!");
            logger.error("Exception happened! ", sie);
            return;
        } catch (UserNotFoudException unfe){
            setErrorBox("Username not registered!");
            logger.error(unfe);
            return;
        }
        App.setSession(userSession);
        changePage();
    }

    private void setErrorBox(String text){
        errorBox.getChildren().clear();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream("src/main/resources/img/warning.png");
        } catch (FileNotFoundException fnfe) {
            logger.error(fnfe);
        }
        assert fis != null;
        Image i = new Image(fis);
        ImageView iw = new ImageView(i);
        errorBox.getChildren().add(0, new Label(text, iw));
        errorBox.setVisible(true);
    }

    private void changePage(){
        if(usernameTextField.getText().equals("admin") & passwordTextField.getText().equals("admin"))
            App.setRoot("adminFirstPage");
        else
            App.setRoot("userPage");
    }

    @FXML
    public void onEnter(ActionEvent ae) { signIn(); }
}
