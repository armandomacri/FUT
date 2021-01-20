package org.unipi.group15;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import mongo.MongoChallenge;
import mongo.MongoUser;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminFirstPageController {
    private static final MongoChallenge mongoChallenge = new MongoChallenge();
    private static final MongoUser mongoUser = new MongoUser();
    private ArrayList<Document> userPerCountry = new ArrayList<>();
    private ArrayList<Document> challengePerDay = new ArrayList<>();

    @FXML private BarChart<String, Number> userGraph;

    @FXML private LineChart<Date, Number> challengeGraph;


    @FXML
    private void initialize(){
        userPerCountry = mongoUser.getUserPerCountryLastYear();
        challengePerDay = mongoChallenge.challengesPerDay();
        fillUserGraph();
        fillChallengeGraph();
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
