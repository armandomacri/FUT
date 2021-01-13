package org.unipi.group15;

import bean.Comment;
import bean.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import neo4j.Neo4jComment;
import user.UserSessionService;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import java.util.ArrayList;

public class CommentsPageController {
    private static final Neo4jComment neo4jComment = new Neo4jComment();
    private final UserSessionService userSession = App.getSession();
    public static Player player;

    @FXML private javafx.scene.control.Label usernameLabel;

    @FXML private javafx.scene.control.Label userIdLabel;

    @FXML public Label upLabel;

    @FXML private ListView commentsList;

    @FXML private TextArea newCommentText;

    @FXML private Button postButton;

    @FXML
    private void switchToProfile() {
        App.setRoot("userPage");
    }

    @FXML
    private void switchToPlayer() {
        App.setRoot("searchPlayer");
    }

    @FXML
    private void switchToFriends() {
        App.setRoot("friends");
    }

    @FXML
    private void switchToBuildSquad() {
        App.setRoot("buildSquad");
    }

    @FXML
    private void switchToChallenge() {
        App.setRoot("challenge");
    }


    public void setPlayerId(Player p){
        player = p;
    }

    @FXML
    private void initialize() throws Exception {
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        upLabel.setText("Comments related to " + player.getPlayerExtendedName() + " player card. Position: " + player.getPosition() + " Overall: " + player.getOverall() );
        if (!neo4jComment.checkConnection()){
            showError("This service is not currently available");
            return;
        }
        loadComments();
    }

    @FXML
    public void loadComments() throws Exception {
        ArrayList<Comment> c = neo4jComment.showComment(player.getPlayerId());
        ObservableList<Comment> comments = FXCollections.observableArrayList(c);
        if(comments.size() == 0){
            commentsList.setPlaceholder(new javafx.scene.control.Label("No Comments found for " + player.getPlayerExtendedName() ));
            return;
        }
        commentsList.setItems(comments);
    }

    @FXML
    public void createComment() throws Exception {
        String commentText = newCommentText.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING, "The text field is empty, please fill it", ButtonType.OK);
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Comments insert correctly", ButtonType.CLOSE);
        if(commentText.equals("")) {
            alert.showAndWait();
            newCommentText.setText("");
        }
        else {
            neo4jComment.createComment(player.getPlayerId(), commentText, userSession.getUserId());
            alert1.showAndWait();
        }
    }

    private void showError(String text){
        Alert a = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
        postButton.setDisable(true);
        a.show();
    }
}
