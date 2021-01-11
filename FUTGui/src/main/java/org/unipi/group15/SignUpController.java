package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serviceExceptions.UserAlreadyExists;
import user.AuthenticationService;
import user.UserSessionService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SignUpController {
    private static final Logger logger = LogManager.getLogger(SignUpController.class);
    @FXML private TextField usernameTextField;

    @FXML private TextField repeatPasswordTextField;

    @FXML private TextField passwordTextField;

    @FXML private TextField countryTextField;

    @FXML private TextField firstNameTextField;

    @FXML private TextField lastNameTextField;

    @FXML private AnchorPane errorBox;

    @FXML
    private void switchToPrimary() {
        App.setRoot("primary");
    }

    @FXML
    private void singUp(){

        if (!repeatPasswordTextField.getText().equals(passwordTextField.getText())){
            setErrorBox("Something wrong with password field!");
            return;
        }

        AuthenticationService as = new AuthenticationService();
        UserSessionService userSessionService = null;

        try {
            userSessionService = as.signUp(usernameTextField.getText(), passwordTextField.getText(), countryTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText());
        } catch (UserAlreadyExists uae) {
            logger.error("Exeption happened! ", uae);
            setErrorBox("Username already exists!");
            return;
        }

        if (userSessionService == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Something Wrong!");
            alert.show();
            return;
        }

        App.setSession(userSessionService);
        App.setRoot("userPage");
    }

    @FXML
    public void initialize() { errorBox.setVisible(false); }

    private void setErrorBox(String text){
        errorBox.getChildren().clear();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream("src/main/resources/img/warning.png");
        } catch (FileNotFoundException fnfe) {
            logger.error("Exeption happened! ", fnfe);
        }
        Image i = new Image(fis);
        ImageView iw = new ImageView(i);
        errorBox.getChildren().add(0, new Label(text, iw));
        errorBox.setVisible(true);
    }
}