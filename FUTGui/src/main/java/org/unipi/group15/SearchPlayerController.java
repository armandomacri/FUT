package org.unipi.group15;

import bean.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.ProvaQuery;
import org.bson.Document;
import user.UserSessionService;

import javax.swing.text.TabableView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchPlayerController {

    private final UserSessionService userSession = App.getSession();

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private TextField toFind;

    @FXML
    private Button toFindButton;

    @FXML
    private Button playerLink;

    @FXML
    private AnchorPane playersWrapper;



    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
    }

    @FXML
    private void findPlayer() {

        ProvaQuery pq = new ProvaQuery();
        ArrayList<Player> players = pq.findPlayers(toFind.getText());
        System.out.println(players);

        if (players.size() == 0){
            //non ci sono giocatori
            //dire all'utente che la ricerca non ha prodotto risultati
            return;
        }
/*
        TableView table = new TableView();
        table.setEditable(true);

        TableColumn namecol = new TableColumn("Player Name");
        TableColumn imgcol = new TableColumn("Image");
        TableColumn overallcol = new TableColumn("Overall");
        TableColumn positioncol = new TableColumn("Position");

        table.getColumns().addAll(namecol, imgcol, overallcol, positioncol);
*/
        for(int i = 0; i < players.size(); i++){

            VBox container = new VBox();
            container.setId((Integer.toString(i)));
            HBox h1 = new HBox(new Label("Name: "), new Text(players.get(i).getPlayerExtendedName()));
            HBox h2 = new HBox(new Label("Overall: "), new Text(players.get(i).getOverall().toString()));
            HBox h3 = new HBox(new Label("Position: "), new Text(players.get(i).getPosition()));

            //Button button = new Button("Modify");
            //button.setId(Integer.toString(i));
            /*button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println();
                    try {
                        App.setRoot("buildSquad");
                        BuildSquadController bqc = new BuildSquadController();
                        bqc.setSquad(squads.get(Integer.parseInt(button.getId())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }); */

            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            //container.getChildren().add(button);

            playersWrapper.getChildren().add(container);

        }


    }

}
