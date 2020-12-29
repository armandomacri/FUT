package org.unipi.group15;

import bean.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import user.UserSessionService;

public class PlayerCardViewController {
    private static Player player;

    public void setPlayer(Player p){
        player = p;
        System.out.println(p.toString());
    }

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    /*@FXML
    private TextField playerName;

    @FXML
    private TextField overall;

    @FXML
    private TextField position;

    @FXML
    private TextField paceStat;

    @FXML
    private TextField dribblingStat;

    @FXML
    private TextField passingStat;

    @FXML
    private TextField shootingStat;

    @FXML
    private TextField defendingStat;

    @FXML
    private TextField physicalityStat;

    @FXML
    private TextField extendedName;

    @FXML
    private TextField club;

    @FXML
    private TextField league;

    @FXML
    private TextField nationality;

    @FXML
    private TextField dateOfBirth;

    @FXML
    private TextField height;

    @FXML
    private TextField weight;

    @FXML
    private TextField quality;

    @FXML
    private TextField revision;

    @FXML
    private TextField origin;

    @FXML
    private TextField weakFoot;

    @FXML
    private TextField skillMoves;

    @FXML
    private TextField traits;

    @FXML
    private TextField prefFoot;

    @FXML
    private ProgressBar paceBar;

    @FXML
    private ProgressBar dribblingBar;

    @FXML
    private ProgressBar shootingBar;

    @FXML
    private ProgressBar defendingBar;

    @FXML
    private ProgressBar physicalityBar;

    @FXML
    private ProgressBar passingBar;

    @FXML
    private Button showComments;

    @FXML
    private void initialize(){
        UserSessionService userSession = App.getSession();
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());

        // mostra tutti i campi della player card

    }
*/
    @FXML
    private void goToComments(){
        // carica tutti i commenti del giocatore
    }





}
