package org.unipi.group15;

import bean.Comment;
import bean.Player;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import neo4j.Neo4jComment;
import user.UserSessionService;
import javafx.scene.control.*;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentsPageController {
    private static final Neo4jComment neo4jComment = new Neo4jComment();
    private final UserSessionService userSession = App.getSession();
    public static Player player;

    @FXML private Label usernameLabel;

    @FXML private Label userIdLabel;

    @FXML public Label upLabel;

    @FXML private GridPane commentsGridPane;

    @FXML private TextArea newCommentText;

    @FXML private Button postButton;

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
    private void switchToFriends() {
        App.setRoot("friends");
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


    public void setPlayerId(Player p){
        player = p;
    }

    @FXML
    private void initialize() {
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());

        if(userSession.getSquads().size() == 10){
            buildButton.setDisable(true);
        }

        upLabel.setText("Comments related to " + player.getPlayerExtendedName() + " player card. Position: " + player.getPosition() + " Overall: " + player.getOverall() );
        if (!checkService("This service is not currently available")){
            return;
        }

        loadComments();
    }

    @FXML
    public void loadComments(){
        commentsGridPane.getChildren().clear();
        ArrayList<Comment> comments = neo4jComment.showComment(player.getPlayerId());
        if (comments == null){
            if(checkService("This service is not currently available")){
                Alert a = new Alert(Alert.AlertType.WARNING, "Something wrong", ButtonType.OK);
                a.show();
            }
            return;
        }

        if(comments.size() == 0){
             return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < comments.size(); i++){
            String user = comments.get(i).getAuthor().equals(userSession.getUsername()) ? "Me" : comments.get(i).getAuthor();
            VBox v1 = new VBox(new Label("Posted by"), new Text(user));
            HBox h1 = new HBox(v1);
            h1.setAlignment(Pos.CENTER); v1.setAlignment(Pos.CENTER);
            commentsGridPane.add(h1, 0, i);
            HBox hText = new HBox();
            TextArea textArea = new TextArea(comments.get(i).getText());
            textArea.setWrapText(true);
            textArea.setEditable(false);
            hText.getChildren().add(textArea);
            hText.getStyleClass().add("commentContainer");
            commentsGridPane.add(hText, 1, i);
            VBox v2 = new VBox(new Label("Posted on"), new Text(format.format(comments.get(i).getDate())));
            HBox h2 = new HBox(v2);
            h2.setAlignment(Pos.CENTER); v2.setAlignment(Pos.CENTER);
            commentsGridPane.add(h2, 2, i);
        }
        commentsGridPane.setHgap(10);
        commentsGridPane.setHgap(10);

    }

    @FXML
    public void createComment() {
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
            loadComments();
            newCommentText.clear();
        }
    }

    private boolean checkService(String text){
        if (!neo4jComment.checkConnection()){
            Alert a = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
            postButton.setDisable(true);
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
