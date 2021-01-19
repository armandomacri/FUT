package org.unipi.group15;

import bean.Player;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import neo4j.Neo4jPlayerCard;
import player.ImageService;
import user.UserSessionService;
import java.util.ArrayList;

public class SearchPlayerController {
    private static final Neo4jPlayerCard neo4jPlayerCard = new Neo4jPlayerCard();
    private final UserSessionService userSession = App.getSession();

    @FXML private Label usernameLabel;

    @FXML private Label userIdLabel;

    @FXML private TextField toFind;

    @FXML private Button toFindButton;

    @FXML private ScrollPane playersWrapper;

    @FXML
    private void switchToProfile(){
        App.setRoot("userPage");
    }

    @FXML
    private void switchToFriend() {
        App.setRoot("friends");
    }

    @FXML
    private void switchToBuildSquad() {
        BuildSquadController bqc = new BuildSquadController();
        bqc.setSquadIndex(-1);
        App.setRoot("buildSquad");
    }

    @FXML
    private void switchToChallenge(){
        App.setRoot("challenge");
    }

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());

        checkService("This service is not currently available");
    }

    @FXML
    private void findPlayer() {

        playersWrapper.setContent(null);

        ArrayList<Player> players = neo4jPlayerCard.searchPlayerCard(toFind.getText());

        if (players.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No player cards were found", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(12);
        gridPane.setVgap(12);

        int j = 0;
        int k = 0;
        for(int i = 0; i < players.size(); i++){

            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.setMaxWidth(200);
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
            container.setCursor(Cursor.HAND);
            container.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    PlayerCardViewController pCVC = new PlayerCardViewController();
                    pCVC.setPlayer(players.get(Integer.parseInt(container.getId())));
                    App.setRoot("player_card_view");
                }
            });
            HBox h1 = new HBox(new Label("Name: "), new Text(players.get(i).getPlayerExtendedName()));
            HBox h5 = new HBox(new Label("Quality: "), new Text(players.get(i).getQuality()));
            HBox h6 = new HBox(new Label("Revision: "), new Text(players.get(i).getRevision()));
            ImageView plImg = new ImageView();
            plImg.setPreserveRatio(true);
            plImg.setFitHeight(150);
            plImg.setFitWidth(150);
            ImageService imageService = new ImageService();
            plImg.setImage(SwingFXUtils.toFXImage(imageService.get(players.get(i).getImg0()), null));


            container.getChildren().add(h1);
            container.getChildren().add(h5);
            container.getChildren().add(h6);
            container.getChildren().add(plImg);
            gridPane.add(container, k, j);
            if (k==2) {
                k=0;
                j++;
            }
            else
                k++;
        }
        playersWrapper.setContent(gridPane);
    }

    @FXML
    public void onEnter(ActionEvent ae) throws Exception {
        findPlayer();
    }

    private boolean checkService(String text){
        if (!neo4jPlayerCard.checkConnection()){
            toFindButton.setDisable(true);
            Alert a = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
            a.show();
            return false;
        }
        return true;
    }

}
