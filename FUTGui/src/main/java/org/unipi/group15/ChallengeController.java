package org.unipi.group15;

import bean.Squad;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.MongoSquad;
import neo4j.Neo4jUser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChallengeController {
    private Squad selectedSquad;
    //private Neo4jUser ex = new Neo4jUser( "bolt://localhost:7687", "neo4j", "fut" );
    @FXML private TextField searchUsersTextField;

    @FXML private ScrollPane userSquadScrollPane;

    @FXML
    private void initialize(){
        selectedSquad = null;
        showSelectedUserShads();
    }

    @FXML
    private void serchFriend(){
        String text = searchUsersTextField.getText();
    }

    @FXML
    private void showSelectedUserShads(){

        //ottenere l'id dell'utente di cui voglio le squadre
        //le squadre appaiono quando clicco sull'utente

        MongoSquad mongoSquad = new MongoSquad();
        ArrayList<Squad> userSquads = mongoSquad.getSquads("Casimir");
        setSquad(userSquads, null);
    }

    private void setSquad(ArrayList<Squad> userSquads, String module){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);

        //mostrare le squadre che hanno lo stesso modulo
        for(int i = 0; i < userSquads.size(); i++) {

            if(!userSquads.get(i).getModule().equals(module))
                continue;

            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("squadPane");
            HBox h1 = new HBox(new Label("Name: "), new Text(userSquads.get(i).getName()));
            HBox h2 = new HBox(new Label("Module: "), new Text(userSquads.get(i).getModule()));
            HBox h3 = new HBox();

            Button selectButton = new Button("Select");
            selectButton.setId(Integer.toString(i));
            h3.getChildren().add(selectButton);
            selectButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (selectButton == null)
                        selectedSquad = userSquads.get(Integer.parseInt(selectButton.getId()));
                    else{
                        setMySquad();
                    }

                }
            });
            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().addAll(selectButton);
            gridPane.add(container, i, 0);
        }
        userSquadScrollPane.setContent(gridPane);
    }

    private void setMySquad(){
        setSquad(App.getSession().getSquads(), selectedSquad.getModule());
    }

    @FXML
    private void switchToProfile() throws IOException {
        App.setRoot("userPage");
    }

    @FXML
    private void switchToPlayer() throws IOException {
        App.setRoot("searchPlayer");
    }

    @FXML
    private void switchToBuildSquad() throws IOException {
        App.setRoot("buildSquad");
    }


}
