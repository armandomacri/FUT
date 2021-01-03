package org.unipi.group15;

import bean.Player;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import levelDB.StoreImage;
import mongo.MongoPlayerCard;
import mongo.MongoUser;
import user.UserSessionService;

import java.io.IOException;
import java.util.ArrayList;

public class SearchPlayerController {

    private final UserSessionService userSession = App.getSession();

    @FXML
    private Label usernameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private TextField toFind;

    @FXML
    private Button toFindButton;

    @FXML
    private Button playerLink;

    @FXML
    private ScrollPane playersWrapper;

    @FXML
    private void switchToProfile() throws IOException {
        App.setRoot("userPage");
    }

    @FXML
    private void switchToFriend() throws IOException {
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

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
    }

    @FXML
    private void findPlayer() throws IOException{

        playersWrapper.setContent(null);
        MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
        ArrayList<Player> players = mongoPlayerCard.findPlayers(toFind.getText());
        System.out.println(players);

        if (players.size() == 0){
            HBox errorBox = new HBox(new Text("Player not found, Try again!"));
            playersWrapper.setContent(errorBox);
            return;
        }

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(12);
        gridPane.setVgap(12);

        Integer j = 0;
        Integer k = 0;
        for(int i = 0; i < players.size(); i++){

            VBox container = new VBox();
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
            container.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(container.getId());
                    try {
                        PlayerCardViewController pCVC = new PlayerCardViewController();
                        pCVC.setPlayer(players.get(Integer.parseInt(container.getId())));
                        App.setRoot("player_card_view");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            HBox h1 = new HBox(new Label("Name: "), new Text(players.get(i).getPlayerName()));
            HBox h2 = new HBox(new Label("Overall: "), new Text(players.get(i).getOverall().toString()));
            HBox h3 = new HBox(new Label("Position: "), new Text(players.get(i).getPosition()));
            HBox h4 = new HBox(new Label("Club: "), new Text(players.get(i).getClub()));
            HBox h5 = new HBox(new Label("Quality: "), new Text(players.get(i).getQuality()));
            HBox h6 = new HBox(new Label("Revision: "), new Text(players.get(i).getRevision()));
            StoreImage si = new StoreImage();
            ImageView plImg = new ImageView();
            plImg.setPreserveRatio(true);
            plImg.setFitHeight(150);
            plImg.setFitWidth(150);
            plImg.setImage(SwingFXUtils.toFXImage(si.findImg(players.get(i).getImages()[0]), null));
            //Button button = new Button("Modify");
            //button.setId(Integer.toString(i));
            /*button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println();
                    try {
                        App.setRoot("buildSquad");
                        BuildSquadController bqc = new BuildSquadController();
                        bqc.setSquad(squads.get(Integer.parseInt(button.getId())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }); */

            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().add(h4);
            container.getChildren().add(h5);
            container.getChildren().add(h6);
            container.getChildren().add(plImg);
            //container.getChildren().add(button);
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

}
