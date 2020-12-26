package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import user.UserSessionService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPageController {

    private final UserSessionService userSession = App.getSession();

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Label registrationDaysLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = userSession.getJoinDate();
        registrationDaysLabel.setText(df.format(date));
        countryLabel.setText(userSession.getCountry());
        nameLabel.setText(userSession.getFirstName());
        lastNameLabel.setText(userSession.getLastName());

    }
}
