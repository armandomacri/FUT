package org.unipi.group15;

import bean.Player;
import bean.Squad;
import bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.MongoSquad;
import mongo.MongoUser;
import neo4j.Neo4jUser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChallengeController {
    private Squad selectedSquad;
    private User selectedUser;
    private MongoUser mongoUser = new MongoUser();
    //private Neo4jUser neo4jUser = new Neo4jUser("bolt://localhost:7687", "neo4j", "fut" );

    @FXML private TextField searchUsersTextField;

    @FXML private ScrollPane userSquadScrollPane;

    @FXML private TableView<User> seachUserTableView;

    @FXML private TableView<User> suggestedUserTableView;

    @FXML private HBox userCompetitionHBox;

    @FXML
    private void initialize(){
        selectedSquad = null;
        selectedUser = null;
        setTable(seachUserTableView);
        setTable(suggestedUserTableView);
    }

    @FXML
    private void serchFriend(){

        String text = searchUsersTextField.getText();
        //ObservableList<User> users = FXCollections.observableArrayList(neo4jUser.findUsers(text));
        ObservableList<User> users = FXCollections.observableArrayList(mongoUser.findUsers(text));
        seachUserTableView.setItems(users);

    }

    @FXML
    private void showSelectedUserShads(){

        //ottenere l'id dell'utente di cui voglio le squadre
        //le squadre appaiono quando clicco sull'utente

        MongoSquad mongoSquad = new MongoSquad();
        ArrayList<Squad> userSquads = mongoSquad.getSquads("Casimir");
        setSquad(userSquads);
    }

    private void setSquad(ArrayList<Squad> userSquads){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);

        //mostrare le squadre che hanno lo stesso modulo
        for(int i = 0; i < userSquads.size(); i++) {

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
                    if (selectedSquad == null) {
                        selectedSquad = userSquads.get(Integer.parseInt(selectButton.getId()));
                        VBox container1 = new VBox();
                        HBox h11 = new HBox(new Label("Username: "), new Text(selectedUser.getUsername()));
                        HBox h21 = new HBox(new Label("Name: "), new Text(selectedSquad.getName()));
                        HBox h31 = new HBox(new Label("Module: "), new Text(selectedSquad.getModule()));
                        container1.getChildren().addAll(h11, h21, h31);
                        userCompetitionHBox.getChildren().add(0, container1);
                        userCompetitionHBox.getChildren().remove(1);
                        setMySquad();
                        seachUserTableView.setDisable(true);
                        suggestedUserTableView.setDisable(true);
                    }
                    else{
                        Squad mySelectedSquad = userSquads.get(Integer.parseInt(selectButton.getId()));
                        VBox container1 = new VBox();
                        HBox h11 = new HBox(new Label("Username: "), new Text(App.getSession().getUsername()));
                        HBox h21 = new HBox(new Label("Name: "), new Text(mySelectedSquad.getName()));
                        HBox h31 = new HBox(new Label("Module: "), new Text(mySelectedSquad.getModule()));
                        container1.getChildren().addAll(h11, h21, h31);
                        userCompetitionHBox.getChildren().remove(2);
                        userCompetitionHBox.getChildren().add(2, container1);
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
        setSquad(App.getSession().getSquads());
    }

    private void setTable(TableView<User> table){

        table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userId"));
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("username"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("score"));

        table.setOnMouseClicked((MouseEvent event) -> {
             if (table.getSelectionModel().getSelectedItem() != null) {
                 selectedUser = table.getSelectionModel().getSelectedItem();
                 setSquad(table.getSelectionModel().getSelectedItem().getSquads());
             }

        });

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
