package org.unipi.group15;

import bean.Squad;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mongo.MongoChallenge;
import mongo.MongoSquad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.UserSessionService;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserPageController {
    private static final Logger logger = LogManager.getLogger(UserPageController.class);
    private static final UserSessionService userSession = App.getSession();
    private static final MongoSquad mongoSquad = new MongoSquad();

    @FXML private Label usernameLabel;

    @FXML private Label userIdLabel;

    @FXML private Label registrationDaysLabel;

    @FXML private Label countryLabel;

    @FXML private Label nameLabel;

    @FXML private Label lastNameLabel;

    @FXML private Label scoreLabel;

    @FXML private GridPane squadsWrapper;

    @FXML private Button buildButton;

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
        scoreLabel.setText("Score: " + userSession.getScore());

        ArrayList<Squad> squads = userSession.getSquads();

        if (squads == null){
            //non ci sono squadre
            //inserire pannello vuoto
            return;
        }


        if(squads.size() == 10){
            buildButton.setDisable(true);
        }

        squadsWrapper.setPadding(new Insets(10, 10, 10, 10));
        squadsWrapper.setHgap(10);
        squadsWrapper.setVgap(20);

        for(int i = 0; i < squads.size(); i++){
            VBox container = new VBox();
            container.setSpacing(2);
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("squadPane");
            HBox h1 = new HBox(new Label(squads.get(i).getName()));
            h1.setAlignment(Pos.CENTER);
            HBox h2 = new HBox(new Label(squads.get(i).getModule()));
            h2.setAlignment(Pos.CENTER);
            HBox h3 = new HBox(new Label(df.format(squads.get(i).getDate())));
            h3.setAlignment(Pos.CENTER);

            Button modifybutton = new Button("Modify");
            Button deleteButton = new Button("Delete");
            modifybutton.setId(Integer.toString(i));
            deleteButton.setId(Integer.toString(i));

            modifybutton.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent event) {
                    BuildSquadController bqc = new BuildSquadController();
                    bqc.setSquadIndex(Integer.parseInt(modifybutton.getId()));
                    App.setRoot("buildSquad");

                }
            });

            deleteButton.setOnAction(new EventHandler<>() {
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
                squadsWrapper.add(container, (i-5), 2);
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
