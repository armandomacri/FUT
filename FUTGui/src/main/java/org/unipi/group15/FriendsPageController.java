package org.unipi.group15;

import bean.User;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.util.ArrayList;

public class FriendsPageController {

    private final UserSessionService userSession = App.getSession();

    String idSelected = null;
    String idSelected1 = null;
    String idSelected2 = null;
    ArrayList<User> followedusers = null;

    @FXML public TextField userToFind;

    @FXML public Button follow_button;

    @FXML public Label valueLbl;

    @FXML private Label userIdLabel;

    @FXML private Label usernameLabel;

    @FXML private ListView FriendsList;

    @FXML private Button SearchButton;

    @FXML private TableView<User> SuggestedFriendLike;

    @FXML public TableColumn<User, String> userIdLike;

    @FXML public TableColumn<User, String> userUsernameLike;

    @FXML public Button follow_button1;

    @FXML public Label valueLbl1;

    @FXML private TableView SuggestedFriend;

    @FXML public TableColumn<User, String> userIdFriends;

    @FXML public TableColumn<User, String> userUsernameFriends;

    @FXML public Button follow_button2;

    @FXML public Label valueLbl2;

    @FXML
    private void initialize() throws Exception {
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        //userToFind.setText(""); non so se va bene
        follow_button.setDisable(true);
        follow_button1.setDisable(true);
        follow_button2.setDisable(true);
        valueLbl.setText("");
        valueLbl1.setText("");
        valueLbl2.setText("");
        FriendsList.getItems().clear();
        followedusers = Neo4jUser.checkalreadyfollow(Integer.valueOf((userSession.getUserId())));
        setSuggestedFriendLike();
        setSuggestedFriend();
    }

    @FXML
    private void setSuggestedFriendLike() throws Exception{
        ArrayList<User> users = Neo4jUser.SuggestedUserByLike(Integer.valueOf((userSession.getUserId())));
        userIdLike.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameLike.setCellValueFactory(new PropertyValueFactory<>("username"));
        PropertyValueFactory factory = new PropertyValueFactory<>("userId");
        PropertyValueFactory factory1 = new PropertyValueFactory<>("username");
        for(int i = 0; i < users.size(); i++) {
            SuggestedFriendLike.getItems().add(users.get(i));
        }
        SuggestedFriendLike.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = (User) SuggestedFriendLike.getSelectionModel().getSelectedItem();
                int i;
                for (i = 0; i < followedusers.size(); i++) {
                    if (followedusers.get(i).getUserId().equals(u.getUserId())) {
                        follow_button1.setDisable(true);
                        valueLbl1.setText("User already followed");
                        break;
                    }
                    else{
                        idSelected1 = (u.getUserId());
                        follow_button1.setDisable(false);
                        valueLbl1.setText(u.getUserId());
                    }
                }
            }
        });
    }

    private void setSuggestedFriend() throws Exception{
        ArrayList<User> users = Neo4jUser.SuggestedUserByFriends(Integer.valueOf((userSession.getUserId())));
        userIdFriends.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userUsernameFriends.setCellValueFactory(new PropertyValueFactory<>("username"));
        PropertyValueFactory factory = new PropertyValueFactory<>("userId");
        PropertyValueFactory factory1 = new PropertyValueFactory<>("username");
        for(int i = 0; i < users.size(); i++) {
            SuggestedFriend.getItems().add(users.get(i));
        }
        SuggestedFriend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                User u = (User) SuggestedFriend.getSelectionModel().getSelectedItem();
                int i;
                for (i = 0; i < followedusers.size(); i++) {
                    if (followedusers.get(i).getUserId().equals(u.getUserId())) {
                        follow_button2.setDisable(true);
                        valueLbl2.setText("User already followed");
                        break;
                    }
                    else{
                        idSelected2 = (u.getUserId());
                        follow_button2.setDisable(false);
                        valueLbl2.setText(u.getUserId());
                    }
                }
            }
        });
    }

    @FXML
    private void searchFriend() throws Exception {
        ArrayList<User> users = Neo4jUser.searchUser(userToFind.getText());
        ObservableList<User> observable_users = FXCollections.observableArrayList(users);
        System.out.println(users);
        if(users.size() == 0){
            //errore: non sono stati trovati utenti
            return;
        }
        FriendsList.setItems(observable_users);
        FriendsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                User u = (User) FriendsList.getSelectionModel().getSelectedItem();
                int i;
                for (i = 0; i < followedusers.size(); i++) {
                    if (followedusers.get(i).getUserId().equals(u.getUserId())) {
                        follow_button.setDisable(true);
                        valueLbl.setText("User already followed");
                        break;
                    }
                    else{
                        idSelected = (u.getUserId());
                        follow_button.setDisable(false);
                        valueLbl.setText(u.getUserId());
                    }
                }
            }
        });
    }

    @FXML
    private void createRelationFollow() throws Exception{
        Neo4jUser.CreateFollow(Integer.valueOf((userSession.getUserId())),Integer.valueOf((idSelected)));
        follow_button.setDisable(true);
        valueLbl.setText("User already followed");
        followedusers = Neo4jUser.checkalreadyfollow(Integer.valueOf((userSession.getUserId())));
    }

    @FXML
    private void createRelationFollow1() throws Exception{
        Neo4jUser.CreateFollow(Integer.valueOf((userSession.getUserId())),Integer.valueOf((idSelected1)));
        follow_button1.setDisable(true);
        valueLbl1.setText("User already followed");
        followedusers = Neo4jUser.checkalreadyfollow(Integer.valueOf((userSession.getUserId())));
    }

    @FXML
    private void createRelationFollow2() throws Exception{
        Neo4jUser.CreateFollow(Integer.valueOf((userSession.getUserId())),Integer.valueOf((idSelected2)));
        follow_button2.setDisable(true);
        valueLbl2.setText("User already followed");
        followedusers = Neo4jUser.checkalreadyfollow(Integer.valueOf((userSession.getUserId())));
    }

}
