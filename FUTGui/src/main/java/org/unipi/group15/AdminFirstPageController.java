package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import mongo.MongoChallenge;
import mongo.MongoPlayerCard;
import mongo.MongoUser;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminFirstPageController {
    private static final MongoChallenge mongoChallenge = new MongoChallenge();
    private static final MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
    private static final MongoUser mongoUser = new MongoUser();
    private ArrayList<Document> userPerCountry = new ArrayList<>();
    private ArrayList<Document> challengePerDay = new ArrayList<>();

    @FXML private BarChart<String, Number> userGraph;

    @FXML private LineChart<Date, Number> challengeGraph;

    @FXML private Label countUsersLabel;

    @FXML private Label countPlayerCardsLabel;

    @FXML
    private void initialize(){
        userPerCountry = mongoUser.getUserPerCountry();
        challengePerDay = mongoChallenge.challengesPerDay();
        fillUserGraph();
        fillChallengeGraph();

        long countUsers = mongoUser.countUsers();
        long coutPlayers = mongoPlayerCard.countPlayerCards();

        if (countUsers == -1 || coutPlayers == -1){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            countUsersLabel.setText("Unknown");
            countPlayerCardsLabel.setText("Unknown");
            a.show();
        } else {
            countUsersLabel.setText(String.valueOf(countUsers));
            countPlayerCardsLabel.setText(String.valueOf(coutPlayers));
        }
    }

    private void fillUserGraph() {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Users");
        //populating the series with data
        for(int i=0; i<userPerCountry.size(); i++) {
            series.getData().add(new XYChart.Data(userPerCountry.get(i).get("_id"), userPerCountry.get(i).get("numUsers")));
        }
        //adding data in the graph
        userGraph.getData().add(series);
    }

    private void fillChallengeGraph(){
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Challenges");
        //populating the series with data
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        for (int i=0; i<challengePerDay.size(); i++) {
            series.getData().add(new XYChart.Data(format.format(challengePerDay.get(i).getDate("date")), challengePerDay.get(i).get("numChallenges")));
        }
        challengeGraph.getData().add(series);
        challengeGraph.setCreateSymbols(false);
    }

    @FXML
    private void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }

    @FXML
    private void playerAnalytics(){ App.setRoot("adminSecondPage"); }
}
