package org.unipi.group15;

import bean.Challenge;
import bean.Squad;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.MongoChallenge;
import mongo.MongoSquad;
import user.UserSessionService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserPageController {

    private final UserSessionService userSession = App.getSession();
    private static MongoSquad mongoSquad = new MongoSquad();

    @FXML private Label usernameLabel;

    @FXML private Label userIdLabel;

    @FXML private Label registrationDaysLabel;

    @FXML private Label countryLabel;

    @FXML private Label passwordLabel;

    @FXML private Label nameLabel;

    @FXML private Label lastNameLabel;

    @FXML private ScrollPane squadsWrapper;

    @FXML private ScrollPane challengesWrapper;

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = userSession.getJoinDate();
        registrationDaysLabel.setText(df.format(date));
        passwordLabel.setText(userSession.getPassword());
        countryLabel.setText(userSession.getCountry());
        nameLabel.setText(userSession.getFirstName());
        lastNameLabel.setText(userSession.getLastName());

        ArrayList<Squad> squads = userSession.getSquads();

        if (squads.size() == 0){
            //non ci sono squadre
            //inserire pannello vuoto
            return;
        }

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);

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
                    try {
                        BuildSquadController bqc = new BuildSquadController();
                        bqc.setSquadIndex(Integer.parseInt(modifybutton.getId()));
                        App.setRoot("buildSquad");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = Integer.parseInt(modifybutton.getId());
                    squads.remove(index);
                    mongoSquad.delete(userSession.getUserId(), index);
                    try {
                        App.setRoot("userPage");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().addAll(modifybutton, deleteButton);

            /*
            HashMap<String, String> players = squads.get(i).getPlayers();

            Iterator iterator = players.keySet().iterator();
            while(iterator.hasNext()) {
                String key = iterator.next().toString();
                HBox h4 = new HBox(new Label(key+": "), new Text(players.get(key)));
                container.getChildren().add(h4);
            }

             */

            gridPane.add(container, i, 0);
        }
        squadsWrapper.setContent(gridPane);

        MongoChallenge mc = new MongoChallenge();
        ArrayList<Challenge> challenges = mc.findUserChallenge(userSession.getUserId());
        if (challenges.size() == 0){
            //non ci sono squadre
            //inserire pannello vuoto
            return;
        }

        GridPane gridPane1 = new GridPane();
        gridPane1.setPadding(new Insets(10, 10, 10, 10));
        gridPane1.setHgap(10);

        for(int i = 0; i < challenges.size(); i++){
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("challengePane");
            HBox h1 = new HBox(new Label("Match: "), new Text(challenges.get(i).getHome() + " vs " + challenges.get(i).getAway()));
            HBox h2 = new HBox(new Label("Result: "), new Text(challenges.get(i).getHomeScore().toString() + "-" + challenges.get(i).getAwayScore().toString()));
            HBox h3 = new HBox(new Label("Date: "), new Text(challenges.get(i).getDate()));
            HBox h4 = new HBox(new Label("Points earned/lost: "), new Text(challenges.get(i).getPoints().toString()));
            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().add(h4);
            gridPane1.add(container, i, 0);
        }

        challengesWrapper.setContent(gridPane1);
    }

    @FXML
    private void goToPlayer() throws IOException{
        App.setRoot("searchPlayer");
    }

    @FXML
    private void switchToBuildSquad() throws IOException {
        App.setRoot("buildSquad");
    }

}
