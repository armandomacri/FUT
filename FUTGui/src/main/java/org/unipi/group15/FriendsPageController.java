package org.unipi.group15;

import bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import neo4j.Neo4jUser;
import user.UserSessionService;
import java.util.ArrayList;

public class FriendsPageController {
    private static final Neo4jUser neo4jUser = new Neo4jUser();
    private final UserSessionService userSession = App.getSession();
    private String idSelected = null;
    private String idSelectedFriend = null;
    private String idSelected1 = null;
    private String idSelected2 = null;
    private ArrayList<User> followedusers = null;

    @FXML private TextField userToFind;

    @FXML private Button follow_button;

    @FXML private Label valueLbl;

    @FXML private Label userIdLabel;

    @FXML private Label usernameLabel;

    @FXML private TableView<User> searchUser;

    @FXML private TableColumn<User, String> userIdSearchFriends;

    @FXML private TableColumn<User, String> userUsernameSearchFriends;

    @FXML private Button searchButton;

    @FXML private TableView<User> yourFriends;

    @FXML private TableColumn<User, String> userIdYourFriends;

    @FXML private TableColumn<User, String> userUsernameYourFriends;

    @FXML private Button yourFriendButton;

    @FXML private Label YourFriendLabel;

    @FXML private TableView<User> suggestedFriendLike;

    @FXML private TableColumn<User, String> userIdLike;

    @FXML private TableColumn<User, String> userUsernameLike;

    @FXML private Button follow_button1;

    @FXML private Label valueLbl1;

    @FXML private TableView<User> suggestedFriend;

    @FXML private TableColumn<User, String> userIdFriends;

    @FXML private TableColumn<User, String> userUsernameFriends;

    @FXML private Button follow_button2;

    @FXML private Label valueLbl2;

    @FXML private Button buildButton;

    @FXML
    private void switchToProfile() {
        App.setRoot("userPage");
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
    private void switchToChallenge() {
        App.setRoot("challenge");
    }

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());

        if(userSession.getSquads().size() == 10){
            buildButton.setDisable(true);
        }


        userToFind.setText(null);
        follow_button.setDisable(true);
        yourFriendButton.setDisable(true);
        follow_button1.setDisable(true);
        follow_button2.setDisable(true);
        valueLbl.setText(null);
        YourFriendLabel.setText(null);
        valueLbl1.setText(null);
        valueLbl2.setText(null);
        searchUser.getItems().clear();
        suggestedFriend.getItems().clear();
        suggestedFriendLike.getItems().clear();
        if (!checkService("This service is not currently available!"))
            return;
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        setAlreadyFollowed();
        setSuggestedFriendLike();
        setSuggestedFriend();
    }

    @FXML
    private void setAlreadyFollowed(){
        userIdYourFriends.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameYourFriends.setCellValueFactory(new PropertyValueFactory<>("username"));
        for (User user : followedusers) {
            yourFriends.getItems().add(user);
        }
        yourFriends.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = yourFriends.getSelectionModel().getSelectedItem();
                idSelectedFriend = (u.getUserId());
                yourFriendButton.setDisable(false);
                YourFriendLabel.setText(u.getUsername());
            }
        });
    }

    @FXML
    private void setSuggestedFriendLike(){
        ArrayList<User> users = neo4jUser.suggestedUserByLike(userSession.getUserId());
        userIdLike.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameLike.setCellValueFactory(new PropertyValueFactory<>("username"));
        for (User user : users) {
            suggestedFriendLike.getItems().add(user);
        }
        suggestedFriendLike.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = suggestedFriendLike.getSelectionModel().getSelectedItem();
                for (User followeduser : followedusers) {
                    if (followeduser.getUserId().equals(u.getUserId())) {
                        follow_button1.setDisable(true);
                        valueLbl1.setText("User already followed");
                        break;
                    } else {
                        idSelected1 = (u.getUserId());
                        follow_button1.setDisable(false);
                        valueLbl1.setText(u.getUsername());
                    }
                }
            }
        });
    }

    private void setSuggestedFriend(){
        ArrayList<User> users = neo4jUser.suggestedUserByFriends(userSession.getUserId());
        userIdFriends.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameFriends.setCellValueFactory(new PropertyValueFactory<>("username"));
        for (User user : users) {
            suggestedFriend.getItems().add(user);
        }
        suggestedFriend.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = suggestedFriend.getSelectionModel().getSelectedItem();
                for (User followeduser : followedusers) {
                    if (followeduser.getUserId().equals(u.getUserId())) {
                        follow_button2.setDisable(true);
                        valueLbl2.setText("User already followed");
                        break;
                    } else {
                        idSelected2 = (u.getUserId());
                        follow_button2.setDisable(false);
                        valueLbl2.setText(u.getUsername());
                    }
                }
            }
        });
    }

    @FXML
    private void searchFriend(){
        searchUser.getItems().clear();
        userIdSearchFriends.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameSearchFriends.setCellValueFactory(new PropertyValueFactory<>("username"));
        ArrayList<User> users = neo4jUser.searchUser(userToFind.getText(), userSession.getUserId());
        ObservableList<User> observable_users = FXCollections.observableArrayList(users);
        if(users.size() == 0){
            searchUser.setPlaceholder(new Label("No Users found containing "+ userToFind.getText()));
            return;
        }
        searchUser.setItems(observable_users);
        searchUser.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                User u = searchUser.getSelectionModel().getSelectedItem();
                int i;
                if (followedusers.size() == 0) {
                    idSelected = (u.getUserId());
                    follow_button.setDisable(false);
                    valueLbl.setText("Follow " + u.getUsername());
                }
                for (i = 0; i < followedusers.size(); i++) {
                    if (followedusers.get(i).getUserId().equals(u.getUserId())) {
                        valueLbl.setText("User already followed");
                        follow_button.setDisable(true);
                        break;
                    } else {
                        idSelected = (u.getUserId());
                        follow_button.setDisable(false);
                        valueLbl.setText("Follow " + u.getUsername());
                    }
                }
            }
        });
        searchUser.scrollTo(0);
    }

    @FXML
    public void onEnter() {
        searchFriend();
    }

    @FXML
    private void createRelationFollow() {
        if(!neo4jUser.createFollow(userSession.getUserId(),idSelected)){
            checkService("Connection problem!");
            return;
        }
        follow_button.setDisable(true);
        valueLbl.setText("User already followed");
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        yourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    @FXML
    private void deleteFollow(){
        if(!neo4jUser.deleteFollow(userSession.getUserId(),idSelectedFriend)){
            checkService("Connection problem!");
            return;
        }
        yourFriendButton.setDisable(true);
        YourFriendLabel.setText("Unfollow " +idSelectedFriend);
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        yourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    @FXML
    private void createRelationFollow1() {
        if(!neo4jUser.createFollow(userSession.getUserId(), idSelected1)){
            checkService("Connection problem!");
            return;
        }
        follow_button1.setDisable(true);
        valueLbl1.setText("User already followed");
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        yourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    @FXML
    private void createRelationFollow2() {
        if(!neo4jUser.createFollow(userSession.getUserId(),idSelected2)){
            checkService("Connection problem!");
            return;
        }
        follow_button2.setDisable(true);
        valueLbl2.setText("User already followed");
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        yourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    private boolean checkService(String text){
        if (!neo4jUser.checkConnection()){
            Alert a = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
            yourFriends.getItems().clear();
            searchUser.getItems().clear();
            suggestedFriend.getItems().clear();
            suggestedFriendLike.getItems().clear();
            searchButton.setDisable(true);
            follow_button.setDisable(true);
            follow_button1.setDisable(true);
            follow_button2.setDisable(true);
            yourFriendButton.setDisable(true);
            a.show();
            return false;
        }
        return true;
    }

    @FXML
    public void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }
}
