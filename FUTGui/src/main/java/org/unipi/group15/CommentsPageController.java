package org.unipi.group15;

import bean.Comment;
import bean.Player;
import bean.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import neo4j.Neo4jComment;
import neo4j.Neo4jUser;
import user.UserSessionService;
import javafx.scene.control.*;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class CommentsPageController {
    private static Neo4jComment neo4jComment = new Neo4jComment();
    private final UserSessionService userSession = App.getSession();
    public static Player player;

    @FXML private javafx.scene.control.Label usernameLabel;

    @FXML private javafx.scene.control.Label userIdLabel;

    @FXML public Label upLabel;

    @FXML private ListView CommentsList;

    @FXML private TextArea newCommentText;

    @FXML private Button PostButton;

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
    private void switchToChallenge(){
        try {
            App.setRoot("challenge");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setPlayerId(Player p){
        player = p;
    }

    @FXML
    private void initialize() throws Exception {
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        upLabel.setText("Comments related to " + player.getPlayerExtendedName() + " player card. Position: " + player.getPosition() + " Overall: " + player.getOverall() );
        loadComments();
    }

    @FXML
    public void loadComments() throws Exception {
        ArrayList<Comment> comments = neo4jComment.showComment(Integer.valueOf(player.getPlayerId()));
        ObservableList<Comment> observable_users = FXCollections.observableArrayList(comments);
        System.out.println(comments.toString());
        if(comments.size() == 0){
            CommentsList.setPlaceholder(new javafx.scene.control.Label("No Comments found for " + player.getPlayerExtendedName() ));
            return;
        }
        CommentsList.setItems(observable_users);
    }

    @FXML
    public void createComment() throws Exception {
        String commentText = newCommentText.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING, "The text field is empty, please fill it", ButtonType.OK);
        if(commentText == "") {
            alert.showAndWait();
            newCommentText.setText("");
        }
        else
            neo4jComment.createComment(171717, Integer.parseInt(player.getPlayerId()), commentText, Integer.parseInt(userSession.getUserId()));
    }
}
