package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import user.AuthenticationService;
import user.UserSessionService;


import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField repeatPasswordTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void singUp(){

        AuthenticationService as = new AuthenticationService();
        UserSessionService uss = as.signUp(usernameTextField.getText(), passwordTextField.getText(), countryTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText());

        App.setSession(uss);
        //System.out.println(repeatPasswordTextField.getText());
    }

    public void initialize() {}
}