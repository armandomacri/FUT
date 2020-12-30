package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import user.UserSessionService;

import java.io.IOException;

public class MainViewController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private void initialize(){
        UserSessionService userSession = App.getSession();
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
    }

    @FXML
    private void switchToProfile() throws IOException {
        App.setRoot("userPage");
    }

    @FXML
    private void goToPlayer() throws IOException {
        App.setRoot("searchPlayer");
    }

    @FXML
    private void switchToBuildSquad() throws IOException {
        App.setRoot("buildSquad");
    }

}
