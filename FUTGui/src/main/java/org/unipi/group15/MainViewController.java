package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import user.UserSessionService;

public class MainViewController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    public void initialize(){
        UserSessionService userSession = App.getSession();
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
    }
}
