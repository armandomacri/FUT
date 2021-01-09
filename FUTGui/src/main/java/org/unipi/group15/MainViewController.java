package org.unipi.group15;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import neo4j.Neo4jPlayerCard;
import neo4j.Neo4jUser;
import user.UserSessionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainViewController {

    private static final Neo4jPlayerCard neo4jPlayerCard = new Neo4jPlayerCard();
    private static final Neo4jUser neo4jUser = new Neo4jUser();

    @FXML
    private TableView<Map.Entry<String,String>> likedPlayers;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> likedPlayerName;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> numLikes;

    @FXML
    private TableView<Map.Entry<String,String>> activeUser;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> activeUsername;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> numOperations;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private void initialize(){
        UserSessionService userSession = App.getSession();
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        setLikedPlayers();
        setActiveUsers();
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
    private void switchToFriends() throws IOException {
        App.setRoot("friends");
    }

    @FXML
    private void switchToBuildSquad() throws IOException {
        App.setRoot("buildSquad");
    }

    @FXML
    private void switchToChallenge() throws IOException {
        App.setRoot("challenge");
    }

    private void setLikedPlayers(){
        Map<String, String> likedplayers = neo4jPlayerCard.mostLikedPlayer();
        likedPlayerName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        numLikes.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(likedplayers.entrySet());
        likedPlayers.setItems(items);
        likedPlayers.getColumns().setAll(likedPlayerName, numLikes);
        numLikes.setSortType(TableColumn.SortType.DESCENDING);
        likedPlayers.getSortOrder().setAll(numLikes);
    }

    private void setActiveUsers(){
        Map<String, String> activeusers = neo4jUser.mostActiveUser();
        activeUsername.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        numOperations.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(activeusers.entrySet());
        activeUser.setItems(items);
        activeUser.getColumns().setAll(activeUsername, numOperations);
        numOperations.setSortType(TableColumn.SortType.DESCENDING);
        activeUser.getSortOrder().setAll(numOperations);
    }

}
