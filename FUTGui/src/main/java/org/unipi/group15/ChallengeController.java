package org.unipi.group15;

import bean.Challenge;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.MongoSquad;
import mongo.MongoUser;
import neo4j.Neo4jUser;
import user.ComputeScoreService;
import user.UserSessionService;
import java.util.ArrayList;

public class ChallengeController {
    private static final Neo4jUser neo4jUser = new Neo4jUser();
    private Squad selectedSquad;
    private User selectedUser;
    private static final MongoUser mongoUser = new MongoUser();
    private static final MongoSquad mongoSquad = new MongoSquad();
    private final UserSessionService userSession = App.getSession();

    @FXML private Label userIdLabel;

    @FXML private Label usernameLabel;

    @FXML private TextField searchUsersTextField;

    @FXML private ScrollPane userSquadScrollPane;

    @FXML private TableView<User> seachUserTableView;

    @FXML private TableView<User> suggestedUserTableView;

    @FXML public TableColumn<User, Integer> suggestedUserId;

    @FXML public TableColumn<User, String> suggestedUserUsername;

    @FXML public TableColumn<User, Integer> suggestedUserScore;

    @FXML private HBox userCompetitionHBox;

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());
        selectedSquad = null;
        selectedUser = null;
        setTable(seachUserTableView);
        setTable(suggestedUserTableView);
        setSuggestedOpponent();
    }

    @FXML
    private void serchFriend(){
        seachUserTableView.getItems().clear();
        String text = searchUsersTextField.getText();
        //ObservableList<User> users = FXCollections.observableArrayList(neo4jUser.findUsers(text));
        ObservableList<User> users = FXCollections.observableArrayList(mongoUser.findUsers(text, userSession.getUserId()));
        if(users.size() == 0){
            seachUserTableView.setPlaceholder(new Label("No Users found containing "+ searchUsersTextField.getText()));
            return;
        }
        seachUserTableView.setItems(users);

    }

    @FXML
    private void showSelectedUserSquads(){
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
            if(userSquads.get(i).getPlayers().size() < 11)
                continue;

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
                        ComputeScoreService css = new ComputeScoreService();
                        Challenge result = css.results(App.getSession().getUser(), selectedUser, mySelectedSquad, selectedSquad);

                        VBox container2 = new VBox();
                        HBox hResult = new HBox(new Text(App.getSession().getUsername() + " " + result.getHomeScore() + ":" + result.getAwayScore() + " " + selectedUser.getUsername()));
                        HBox hRecap;
                        if(result.getHomeScore() > result.getAwayScore()){
                            hRecap = new HBox(new Text("Congratulations, you won the match. Points earned: " + result.getPoints().toString()));
                        }
                        else if(result.getHomeScore() < result.getAwayScore()){
                            hRecap = new HBox(new Text("Sorry, you lost the match. Points lost: " + result.getPoints().toString()));
                        }
                        else{
                            hRecap =  new HBox(new Text("It's a Draw. Points earned/lost: " + result.getPoints().toString()));
                        }
                        hResult.setStyle("-fx-alignment: center ; -fx-font-size: 28 ");
                        hRecap.setStyle("-fx-alignment: center ; -fx-font-size: 22");
                        container2.getChildren().addAll(hResult, hRecap);
                        userSquadScrollPane.fitToHeightProperty();
                        userSquadScrollPane.fitToWidthProperty();
                        userSquadScrollPane.setContent(null);
                        userSquadScrollPane.setContent(container2);
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

    private void setSuggestedOpponent() {
        ArrayList<User> users = neo4jUser.suggestedUserChallenge(userSession.getUserId());
        suggestedUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        suggestedUserUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        suggestedUserScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        //PropertyValueFactory factory = new PropertyValueFactory<>("userId");
        //PropertyValueFactory factory1 = new PropertyValueFactory<>("username");
        //PropertyValueFactory factory2 = new PropertyValueFactory<>("score");
        for (int i = 0; i < users.size(); i++) {
            suggestedUserTableView.getItems().add(users.get(i));
        }
        suggestedUserTableView.setOnMouseClicked((MouseEvent event) -> {
            if (suggestedUserTableView.getSelectionModel().getSelectedItem() != null) {
                selectedUser = suggestedUserTableView.getSelectionModel().getSelectedItem();
                setSquad(suggestedUserTableView.getSelectionModel().getSelectedItem().getSquads());
            }
            System.out.println(selectedUser.getUserId());
            System.out.println(selectedUser.getSquads());
        });
    }

    @FXML
    public void onEnter(ActionEvent ae) { serchFriend(); }

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
    private void switchToFriends() {
        App.setRoot("friends");
    }


}
