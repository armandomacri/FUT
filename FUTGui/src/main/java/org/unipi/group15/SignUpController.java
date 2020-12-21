package org.unipi.group15;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
        System.out.println(usernameTextField.getText());
        System.out.println(repeatPasswordTextField.getText());
        System.out.println(passwordTextField.getText());
        System.out.println(countryTextField.getText());
        System.out.println(firstNameTextField.getText());
        System.out.println(lastNameTextField.getText());
    }
}