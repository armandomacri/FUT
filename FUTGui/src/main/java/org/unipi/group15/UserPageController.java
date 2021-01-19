package org.unipi.group15;

import bean.Challenge;
import bean.Squad;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.MongoChallenge;
import mongo.MongoSquad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.UserSessionService;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserPageController {
    private static final Logger logger = LogManager.getLogger(UserPageController.class);
    private static UserSessionService userSession = App.getSession();
    private static final MongoSquad mongoSquad = new MongoSquad();
    private static MongoChallenge mongoChallenge = new MongoChallenge();


    @FXML private Label usernameLabel;

    @FXML private Label userIdLabel;

    @FXML private Label registrationDaysLabel;

    @FXML private Label countryLabel;

    @FXML private Label nameLabel;

    @FXML private Label lastNameLabel;

    @FXML private Label scoreLabel;

    @FXML private GridPane squadsWrapper;

    @FXML private ScrollPane challengesWrapper;

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date date = userSession.getJoinDate();
        registrationDaysLabel.setText(df.format(date));
        countryLabel.setText(userSession.getCountry());
        nameLabel.setText(userSession.getFirstName());
        lastNameLabel.setText(userSession.getLastName());
        scoreLabel.setText(String.valueOf(userSession.getScore()));

        ArrayList<Squad> squads = userSession.getSquads();

        if (squads == null){
            //non ci sono squadre
            //inserire pannello vuoto
            return;
        }


        squadsWrapper.setPadding(new Insets(10, 10, 10, 10));
        squadsWrapper.setHgap(10);
        squadsWrapper.setVgap(10);

        for(int i = 0; i < squads.size(); i++){
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("squadPane");
            HBox h1 = new HBox(new Label("Name: "), new Text(squads.get(i).getName()));
            HBox h2 = new HBox(new Label("Module: "), new Text(squads.get(i).getModule()));
            HBox h3 = new HBox(new Label("Date: "), new Text(df.format(squads.get(i).getDate())));

            Button modifybutton = new Button("Modify");
            Button deleteButton = new Button("Delete");
            modifybutton.setId(Integer.toString(i));
            deleteButton.setId(Integer.toString(i));

            modifybutton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    BuildSquadController bqc = new BuildSquadController();
                    bqc.setSquadIndex(Integer.parseInt(modifybutton.getId()));
                    App.setRoot("buildSquad");

                }
            });

            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = Integer.parseInt(modifybutton.getId());
                    squads.remove(index);
                    mongoSquad.delete(userSession.getUserId(), index);
                    App.setRoot("userPage");
                }
            });

            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().addAll(modifybutton, deleteButton);

            if(i<5)
                squadsWrapper.add(container, i, 0);
            else
                squadsWrapper.add(container, (i-5), 1);
        }

    }

    @FXML
    private void switchToPlayer() {
        App.setRoot("searchPlayer");
    }

    @FXML
    private void switchToBuildSquad() {
        BuildSquadController bqc = new BuildSquadController();
        bqc.setSquadIndex(-1);
        App.setRoot("buildSquad");
    }

    @FXML
    public void switchToFriends(){
        App.setRoot("friends");
    }

    @FXML
    public void switchToChallenge(){
        App.setRoot("challenge");
    }

    @FXML
    public void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }
}
