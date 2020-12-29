package org.unipi.group15;

import bean.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import user.UserSessionService;

public class PlayerCardViewController {
    private static Player player;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private Text playerName;

    @FXML
    private Text overall;

    @FXML
    private Text position;

    @FXML
    private Label attr1;

    @FXML
    private Label attr1Stat;

    @FXML
    private ProgressBar attr1Bar;

    @FXML
    private Label attr2;

    @FXML
    private Label attr2Stat;

    @FXML
    private ProgressBar attr2Bar;

    @FXML
    private Label attr3;

    @FXML
    private Label attr3Stat;

    @FXML
    private ProgressBar attr3Bar;

    @FXML
    private Label attr4;

    @FXML
    private Label attr4Stat;

    @FXML
    private ProgressBar attr4Bar;

    @FXML
    private Label attr5;

    @FXML
    private Label attr5Stat;

    @FXML
    private ProgressBar attr5Bar;

    @FXML
    private Label attr6;

    @FXML
    private Label attr6Stat;

    @FXML
    private ProgressBar attr6Bar;

    @FXML
    private Text extendedName;

    @FXML
    private Text club;

    @FXML
    private Text league;

    @FXML
    private Text nationality;

    @FXML
    private Text dateOfBirth;

    @FXML
    private Text height;

    @FXML
    private Text weight;

    @FXML
    private Text quality;

    @FXML
    private Text revision;

    @FXML
    private Text weakFoot;

    @FXML
    private Text skillMoves;

    @FXML
    private Text prefFoot;

    @FXML
    private Button showComments;

    public void setPlayer(Player p){
        player = p;
        System.out.println(p.toString());

    }

    @FXML
    private void initialize(){
        UserSessionService userSession = App.getSession();
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());

        extendedName.setText(player.getPlayerExtendedName());
        playerName.setText(player.getPlayerName());
        overall.setText(player.getOverall().toString());
        position.setText(player.getPosition());
        club.setText(player.getClub());
        league.setText(player.getLeague());
        nationality.setText(player.getNationality());
        dateOfBirth.setText(player.getDateOfBirth().toString());
        height.setText(player.getHeight().toString());
        weight.setText(player.getWeight().toString());
        quality.setText(player.getQuality());
        revision.setText(player.getRevision());
        weakFoot.setText(player.getWeakFoot().toString());
        skillMoves.setText(player.getSkillMoves().toString());
        prefFoot.setText(player.getPrefFoot());

        if(player.getPosition().equals("GK")){
            attr1.setText("Diving");
            attr1Stat.setText(player.getGkDiving().toString());
            attr1Bar.setProgress(player.getGkDiving());
            attr2.setText("Reflexes");
            attr2Stat.setText(player.getGkReflexe().toString());
            attr2Bar.setProgress(player.getGkReflexe());
            attr3.setText("Handling");
            attr3Stat.setText(player.getGkHandling().toString());
            attr3Bar.setProgress(player.getGkHandling());
            attr4.setText("Speed");
            attr4Stat.setText(player.getGkSpeed().toString());
            attr4Bar.setProgress(player.getGkSpeed());
            attr5.setText("Kicking");
            attr5Stat.setText(player.getGkKicking().toString());
            attr5Bar.setProgress(player.getGkKicking());
            attr6.setText("Positioning");
            attr6Stat.setText(player.getGkPositioning().toString());
            attr6Bar.setProgress(player.getGkPositioning());
        }
        else{
            attr1.setText("Pace");
            attr1Stat.setText(player.getPace().toString());
            attr1Bar.setProgress(player.getPace());
            attr2.setText("Shooting");
            attr2Stat.setText(player.getShooting().toString());
            attr2Bar.setProgress(player.getShooting());
            attr3.setText("Passing");
            attr3Stat.setText(player.getPassing().toString());
            attr3Bar.setProgress(player.getPassing());
            attr4.setText("Dribbling");
            attr4Stat.setText(player.getDribbling().toString());
            attr4Bar.setProgress(player.getDribbling());
            attr5.setText("Defending");
            attr5Stat.setText(player.getDefending().toString());
            attr5Bar.setProgress(player.getDefending());
            attr6.setText("Physicality");
            attr6Stat.setText(player.getPhysicality().toString());
            attr6Bar.setProgress(player.getPhysicality());
        }
    }

    @FXML
    private void goToComments(){
        // carica tutti i commenti del giocatore
    }





}
