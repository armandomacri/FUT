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
import mongo.MongoChallenge;
import mongo.MongoSquad;
import neo4j.Neo4jUser;
import user.ComputeScoreService;
import user.UserSessionService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChallengeController {
    private static final Neo4jUser neo4jUser = new Neo4jUser();
    private static final MongoSquad mongoSquad = new MongoSquad();
    private static final MongoChallenge mongoChallenge = new MongoChallenge();
    private final UserSessionService userSession = App.getSession();
    private Squad selectedSquad;
    private User selectedUser;

    @FXML private Label mutableLabel;

    @FXML private Label userIdLabel;

    @FXML private Label usernameLabel;

    @FXML private TextField searchUsersTextField;

    @FXML private ScrollPane userScrollPane;

    @FXML private TableView<User> seachUserTableView;

    @FXML private TableView<User> suggestedUserTableView;

    @FXML public TableColumn<User, Integer> suggestedUserId;

    @FXML private TableColumn<User, String> suggestedUserUsername;

    @FXML private TableColumn<User, Integer> suggestedUserScore;

    @FXML private HBox userCompetitionHBox;

    @FXML private Button findPlayerButton;

    @FXML private Button buildButton;

    @FXML
    private void initialize(){
        usernameLabel.setText(userSession.getUsername());
        userIdLabel.setText(userSession.getUserId());

        if(userSession.getSquads().size() == 10){
            buildButton.setDisable(true);
        }

        selectedSquad = null;
        selectedUser = null;
        suggestedUserTableView.getItems().clear();
        setTable(seachUserTableView);
        setTable(suggestedUserTableView);
        if (!neo4jUser.checkConnection()){
            showError("This service is not currently available");
            findPlayerButton.setDisable(true);
            return;
        }
        setSuggestedOpponent();
        setChallengeBox();
    }

    @FXML
    private void setChallengeBox(){
        ArrayList<Challenge> challenges = mongoChallenge.findUserChallenge(userSession.getUserId());
        if (challenges.size() == 0){
            return;
        }

        GridPane gridPane1 = new GridPane();
        gridPane1.setPadding(new Insets(10, 10, 10, 10));
        gridPane1.setHgap(10);

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        for(int i = 0; i < challenges.size(); i++){
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("challengePane");
            HBox h1 = new HBox(new Label("Match: "), new Text(challenges.get(i).getHomeUser() + " vs " + challenges.get(i).getAwayUser()));
            HBox h2 = new HBox(new Label("Result: "), new Text(challenges.get(i).getHomeScore().toString() + "-" + challenges.get(i).getAwayScore().toString()));
            HBox h3 = new HBox(new Label("Date: "), new Text(sdf.format(challenges.get(i).getDate())));
            HBox h4 = new HBox(new Label("Points earned/lost: "), new Text(challenges.get(i).getPoints().toString()));
            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().add(h4);
            if(((challenges.get(i).getHomeScore() > challenges.get(i).getAwayScore())&(challenges.get(i).getHomeUser().equals(userSession.getUsername())))|
                ((challenges.get(i).getHomeScore() < challenges.get(i).getAwayScore())&(challenges.get(i).getAwayUser().equals(userSession.getUsername())))){
                container.getStyleClass().add("challengePaneWin");
            }
            if(((challenges.get(i).getHomeScore() > challenges.get(i).getAwayScore())&(challenges.get(i).getAwayUser().equals(userSession.getUsername())))|
                    ((challenges.get(i).getHomeScore() < challenges.get(i).getAwayScore())&(challenges.get(i).getHomeUser().equals(userSession.getUsername())))){
                container.getStyleClass().add("challengePaneLose");
            }
            gridPane1.add(container, i, 0);
        }
        userScrollPane.setContent(gridPane1);
    }

    @FXML
    private void searchFriend(){

        seachUserTableView.getItems().clear();
        ArrayList<User> users = neo4jUser.searchUser(searchUsersTextField.getText(), userSession.getUserId());
        ObservableList<User> observable_users = FXCollections.observableArrayList(users);
        if(users.size() == 0){
            seachUserTableView.setPlaceholder(new Label("No Users found containing " + searchUsersTextField.getText()));
            return;
        }
        seachUserTableView.setItems(observable_users);
    }

    @FXML
    private void showSelectedUserSquads(final String user_id){
        //ottenere l'id dell'utente di cui voglio le squadre
        //le squadre appaiono quando clicco sull'utente
        mutableLabel.setText("Select opponent's squad");
        setSquad(mongoSquad.getSquads(user_id));
    }

    private void setSquad(ArrayList<Squad> userSquads){
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);

        int j = 0;
        //mostrare le squadre che hanno lo stesso modulo
        for(int i = 0; i < userSquads.size(); i++) {
            if(userSquads.get(i).getPlayers().size() < 11)
                continue;

            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            container.getStyleClass().add("squadPane");
            HBox h1 = new HBox(new Label(userSquads.get(i).getName()));
            h1.setAlignment(Pos.CENTER);
            HBox h2 = new HBox(new Label(userSquads.get(i).getModule()));
            h2.setAlignment(Pos.CENTER);
            HBox h3 = new HBox();

            Button selectButton = new Button("Select");
            selectButton.setId(Integer.toString(i));
            h3.getChildren().add(selectButton);
            selectButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (selectedSquad == null) {
                        mutableLabel.setText("Select your squad");
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
                        if(result == null){
                            Alert a = new Alert(Alert.AlertType.ERROR, "Something Wrong! This challenge is not valid.");
                            a.show();
                            return;
                        }
                        VBox container2 = new VBox();
                        HBox hResult = new HBox(new Text(App.getSession().getUsername() + " " + result.getHomeScore() + ":" + result.getAwayScore() + " " + selectedUser.getUsername()));
                        HBox hRecap;
                        mutableLabel.setText("Challenge's result");
                        if(result.getHomeScore() > result.getAwayScore()){
                            hRecap = new HBox(new Text("Congratulations, you won the match. Points earned: " + result.getPoints().toString()));
                            userSession.setScore(userSession.getScore()+result.getPoints());
                        }
                        else if(result.getHomeScore() < result.getAwayScore()){
                            hRecap = new HBox(new Text("Sorry, you lost the match. Points lost: " + result.getPoints().toString()));
                            userSession.setScore(Math.max(userSession.getScore() - result.getPoints(), 0));

                        }
                        else{
                            hRecap =  new HBox(new Text("It's a Draw. Points earned/lost: " + result.getPoints().toString()));
                        }
                        hResult.setStyle("-fx-alignment: center ; -fx-font-size: 28 ");
                        hRecap.setStyle("-fx-alignment: center ; -fx-font-size: 22");
                        container2.getChildren().addAll(hResult, hRecap);
                        userScrollPane.fitToHeightProperty();
                        userScrollPane.fitToWidthProperty();
                        userScrollPane.setContent(null);
                        userScrollPane.setContent(container2);
                    }
                }
            });
            container.getChildren().add(h1);
            container.getChildren().add(h2);
            container.getChildren().add(h3);
            container.getChildren().add(selectButton);
            gridPane.add(container, j, 0);
            j++;
        }
        userScrollPane.setContent(gridPane);
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
                 selectedUser.setSquads(mongoSquad.getSquads(selectedUser.getUserId()));
                 setSquad(selectedUser.getSquads());
             }

        });

    }

    private void setSuggestedOpponent() {
        suggestedUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        suggestedUserUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        suggestedUserScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        ObservableList<User> users = FXCollections.observableArrayList(neo4jUser.suggestedUserChallenge(userSession.getUserId()));
        suggestedUserTableView.setItems(users);
        suggestedUserScore.setSortType(TableColumn.SortType.DESCENDING);
        suggestedUserTableView.getSortOrder().setAll(suggestedUserScore);
        suggestedUserTableView.setOnMouseClicked((MouseEvent event) -> {
            selectedUser = suggestedUserTableView.getSelectionModel().getSelectedItem();
            showSelectedUserSquads(suggestedUserTableView.getSelectionModel().getSelectedItem().getUserId());
        });
    }

    @FXML
    public void onEnter() { searchFriend(); }

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
    private void switchToFriends() {
        App.setRoot("friends");
    }

    private void showError(String text){
        Alert a = new Alert(Alert.AlertType.WARNING, text);
        a.show();
    }

}
