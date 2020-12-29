package org.unipi.group15;

import bean.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.ProvaQuery;
import org.bson.Document;
import user.UserSessionService;

import javax.swing.plaf.basic.BasicListUI;
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

        playersWrapper.getChildren().clear();
        ProvaQuery pq = new ProvaQuery();
        ArrayList<Player> players = pq.findPlayers(toFind.getText());
        System.out.println(players);

        if (players.size() == 0){
            //non ci sono giocatori
            //dire all'utente che la ricerca non ha prodotto risultati
            return;
        }

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        Integer j = 0;
        Integer k = 0;
        for(int i = 0; i < players.size(); i++){

            VBox container = new VBox();
            if(players.get(i).getQuality().contains("Gold")) {
                container.getStyleClass().add("goldPlayer");
            }
            else if (players.get(i).getQuality().contains("Silver")) {
                container.getStyleClass().add("silverPlayer");
            }
            else {
                container.getStyleClass().add("bronzePlayer");
            }

            container.setId((Integer.toString(i)));
            container.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    System.out.println();
                    try {
                        App.setRoot("player_card_view");
                        PlayerCardViewController pcvc = new PlayerCardViewController();
                        pcvc.setPlayer(players.get(Integer.parseInt(container.getId())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
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
            gridPane.add(container, k, j);
            if (k==2) {
                k=0;
                j++;
            }
            else
                k++;
        }
        playersWrapper.getChildren().add(gridPane);
    }

}
