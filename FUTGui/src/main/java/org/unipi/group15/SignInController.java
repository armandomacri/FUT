package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import user.AuthenticationService;

import java.io.IOException;

public class SignInController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void signIn(){
        AuthenticationService as = new AuthenticationService();
        as.signIn(usernameTextField.getText(), passwordTextField.getText());
    }
}
