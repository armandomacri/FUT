package org.unipi.group15;

import bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import neo4j.Neo4jUser;
import user.UserSessionService;
import java.util.ArrayList;

public class FriendsPageController {
    private static Neo4jUser neo4jUser = new Neo4jUser();;
    private final UserSessionService userSession = App.getSession();
    private String idSelected = null;
    private String idSelectedFriend = null;
    private String idSelected1 = null;
    private String idSelected2 = null;
    private ArrayList<User> followedusers = null;

    @FXML public TextField userToFind;

    @FXML public Button follow_button;

    @FXML public Label valueLbl;

    @FXML private Label userIdLabel;

    @FXML private Label usernameLabel;

    @FXML private ListView<User> FriendsList;

    @FXML private Button searchButton;

    @FXML private TableView<User> YourFriends;

    @FXML public TableColumn<User, String> userIdYourFriends;

    @FXML public TableColumn<User, String> userUsernameYourFriends;

    @FXML public Button YourFriendButton;

    @FXML public Label YourFriendLabel;

    @FXML private TableView<User> SuggestedFriendLike;

    @FXML public TableColumn<User, String> userIdLike;

    @FXML public TableColumn<User, String> userUsernameLike;

    @FXML public Button follow_button1;

    @FXML public Label valueLbl1;

    @FXML private TableView<User> SuggestedFriend;

    @FXML public TableColumn<User, String> userIdFriends;

    @FXML public TableColumn<User, String> userUsernameFriends;

    @FXML public Button follow_button2;

    @FXML public Label valueLbl2;

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
        userToFind.setText(null);
        follow_button.setDisable(true);
        follow_button1.setDisable(true);
        follow_button2.setDisable(true);
        YourFriendButton.setDisable(true);
        YourFriendLabel.setText(null);
        valueLbl.setText(null);
        valueLbl1.setText(null);
        valueLbl2.setText(null);
        FriendsList.getItems().clear();

        if (!neo4jUser.checkConnection()){
            Alert a = new Alert(Alert.AlertType.WARNING, "This service is not currently available!");
            a.show();
            searchButton.setDisable(true);
            return;
        }

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
            YourFriends.getItems().add(user);
        }

        YourFriends.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = YourFriends.getSelectionModel().getSelectedItem();
                idSelectedFriend = (u.getUserId());
                YourFriendButton.setDisable(false);
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
            SuggestedFriendLike.getItems().add(user);
        }

        SuggestedFriendLike.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = SuggestedFriendLike.getSelectionModel().getSelectedItem();

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
            SuggestedFriend.getItems().add(user);
        }

        SuggestedFriend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = SuggestedFriend.getSelectionModel().getSelectedItem();
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
        FriendsList.getItems().clear();
        ArrayList<User> users = neo4jUser.searchUser(userToFind.getText(), userSession.getUserId());
        ObservableList<User> observable_users = FXCollections.observableArrayList(users);
        if(users.size() == 0){
            FriendsList.setPlaceholder(new Label("No Users found containing "+ userToFind.getText()));
            return;
        }
        FriendsList.setItems(observable_users);
        FriendsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                User u = FriendsList.getSelectionModel().getSelectedItem();
                int i;
                for (i = 0; i < followedusers.size(); i++) {
                    if (followedusers.get(i).getUserId().equals(u.getUserId())) {
                        valueLbl.setText("User already followed");
                        break;
                    }
                    else{
                        idSelected = (u.getUserId());
                        follow_button.setDisable(false);
                        valueLbl.setText("Follow " + u.getUsername());
                    }
                }
            }
        });
        FriendsList.scrollTo(0);
    }

    @FXML
    public void onEnter(ActionEvent ae) {
        searchFriend();
    }

    @FXML
    private void createRelationFollow() throws Exception {
        neo4jUser.createFollow(userSession.getUserId(),idSelected);
        follow_button.setDisable(true);
        //follow_button.setVisible(false);
       // unfollow_button.setDisable(false);
      //  unfollow_button.setVisible(true);
        valueLbl.setText("User already followed");
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        YourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    @FXML
    private void deleteFollow(){
        neo4jUser.deleteFollow(userSession.getUserId(),idSelectedFriend);
        YourFriendButton.setDisable(true);
        YourFriendLabel.setText("Unfollow " +idSelectedFriend);
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        YourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    @FXML
    private void createRelationFollow1() throws Exception {
        neo4jUser.createFollow(userSession.getUserId(),idSelected1);
        follow_button1.setDisable(true);
        valueLbl1.setText("User already followed");
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        YourFriends.getItems().clear();
        setAlreadyFollowed();
    }

    @FXML
    private void createRelationFollow2() throws Exception {
        neo4jUser.createFollow(userSession.getUserId(),idSelected2);
        follow_button2.setDisable(true);
        valueLbl2.setText("User already followed");
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        YourFriends.getItems().clear();
        setAlreadyFollowed();
    }
/*
    @FXML
    private void deleteRelationFollow() throws Exception {
        neo4jUser.deleteFollow(userSession.getUserId(),idSelected);
        unfollow_button.setDisable(true);
        unfollow_button.setVisible(false);
        follow_button.setDisable(false);
        follow_button.setVisible(true);
        valueLbl.setText("Follow " +idSelected);
        followedusers = neo4jUser.checkalreadyfollow(userSession.getUserId());
        YourFriends.getItems().clear();
        setAlreadyFollowed();
    }
    */

}
