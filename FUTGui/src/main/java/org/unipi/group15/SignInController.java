package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import serviceExceptions.SignInException;
import serviceExceptions.UserNotFoudException;
import user.AuthenticationService;
import user.UserSessionService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SignInController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private AnchorPane errorBox;

    @FXML
    public void initialize() {
        errorBox.setVisible(false);
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void signIn(){
        AuthenticationService as = new AuthenticationService();
        UserSessionService userSession = null;
        try {
            userSession = as.signIn(usernameTextField.getText(), passwordTextField.getText());
        } catch (SignInException e) {
            setErrorBox("Username or Password are wrong!");
            e.printStackTrace();
        } catch (UserNotFoudException unfe){
            setErrorBox("Username not registered!");
            unfe.printStackTrace();
        }
        App.setSession(userSession);
        System.out.println(userSession.toString());
        changePage("mainView");
    }

    private void setErrorBox(String text){
        errorBox.getChildren().clear();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream("src/main/resources/img/warning.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image i = new Image(fis);
        ImageView iw = new ImageView(i);
        errorBox.getChildren().add(0, new Label(text, iw));
        errorBox.setVisible(true);
    }

    private void changePage(String name){
        try {
            App.setRoot(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
