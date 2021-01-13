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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import mongo.MongoPlayerCard;
import mongo.MongoSquad;
import neo4j.Neo4jPlayerCard;
import neo4j.Neo4jUser;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;

import java.io.*;
import java.util.*;

public class AdminFirstPageController {

    private static final Neo4jPlayerCard neo4jPlayerCard = new Neo4jPlayerCard();
    private static final Neo4jUser neo4jUser = new Neo4jUser();
    private static final MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
    private static final MongoSquad mongoSquad = new MongoSquad();

    @FXML
    private LineChart<String, Number> userGraph;

    @FXML
    private LineChart<String, Number> challengeGraph;

    @FXML
    private CategoryAxis xAxisUsers;

    @FXML
    private NumberAxis yAxisUsers;

    @FXML
    private CategoryAxis xAxisChallenges ;

    @FXML
    private NumberAxis yAxisChallenges ;

    @FXML
    private void initialize(){
        fillUserGraph();
        fillChallengeGraph();
    }

    private void fillUserGraph() {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Users");
        //populating the series with data
        for(int i=0; i<3; i++){
            series.getData().add(new XYChart.Data("mese N: "+i, i*10)); {
            }
        };
        //adding data in the graph
        userGraph.getData().add(series);
    }

    private void fillChallengeGraph() {
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Challenges");
        //populating the series with data
        series.getData().add(new XYChart.Data("Jan 2020", 23));
        series.getData().add(new XYChart.Data("Feb 2020", 14));
        series.getData().add(new XYChart.Data("Mar 2020", 15));
        //adding data in the graph
        challengeGraph.getData().add(series);
    }

    @FXML
    public void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }

    @FXML
    public void playerAnalytics(){ App.setRoot("adminSecondPage"); }

}
