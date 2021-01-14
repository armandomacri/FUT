package org.unipi.group15;

import admin.UpdatePlayerCardsList;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import mongo.MongoChallenge;
import mongo.MongoPlayerCard;
import mongo.MongoSquad;
import mongo.MongoUser;
import neo4j.Neo4jPlayerCard;
import neo4j.Neo4jUser;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminFirstPageController {

    private static final MongoChallenge mongoChallenge = new MongoChallenge();
    private static final MongoUser mongoUser = new MongoUser();
    ArrayList<Document> UserPerCountry = new ArrayList<>();
    ArrayList<Document> ChallengePerDay = new ArrayList<>();

    @FXML
    private BarChart<String, Number> userGraph;

    @FXML
    private LineChart<Date, Number> challengeGraph;

    @FXML
    private CategoryAxis xAxisUsers;

    @FXML
    private NumberAxis yAxisUsers;

    @FXML
    private Axis xAxisChallenges ;

    @FXML
    private NumberAxis yAxisChallenges ;

    @FXML
    private void initialize() throws ParseException {
        UserPerCountry = mongoUser.getUserPerCountryLastYear();
        ChallengePerDay = mongoChallenge.ChallengesPerDay();
        fillUserGraph();
        fillChallengeGraph();
    }

    private void fillUserGraph() {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Users");
        //populating the series with data
        for(int i=0; i<10; i++) {
            series.getData().add(new XYChart.Data(UserPerCountry.get(i).get("_id"), UserPerCountry.get(i).get("numUsers")));
        }
        //adding data in the graph
        userGraph.getData().add(series);
    }

    private void fillChallengeGraph() throws ParseException {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Challenges");
        //populating the series with data
        for(int i=0; i<30; i++) {
            series.getData().add(new XYChart.Data(ChallengePerDay.get(i).get("date"), ChallengePerDay.get(i).get("numChallenges")));
        }
        //adding data in the graph
        challengeGraph.getData().add(series);
        challengeGraph.setCreateSymbols(false);
    }

    @FXML
    public void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }

    @FXML
    public void playerAnalytics(){ App.setRoot("adminSecondPage"); }

}
