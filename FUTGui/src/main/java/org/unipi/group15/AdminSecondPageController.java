package org.unipi.group15;

import admin.UpdatePlayerCardsList;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

public class AdminSecondPageController {

    private static final Neo4jPlayerCard neo4jPlayerCard = new Neo4jPlayerCard();
    private static final Neo4jUser neo4jUser = new Neo4jUser();
    private static final MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
    private static final MongoSquad mongoSquad = new MongoSquad();


    @FXML
    private TableView<Document> nationAnalyticsTable;

    @FXML
    private TableColumn<Document, String> LeagueColumn;

    @FXML
    private TableColumn<Document, String> numPlayersLeagueColumn;

    @FXML
    private TableColumn<Document, String> pacLeagueColumn;

    @FXML
    private TableColumn<Document, String> driLeagueColumn;

    @FXML
    private TableColumn<Document, String> shoLeagueColumn;

    @FXML
    private TableColumn<Document, String> defLeagueColumn;

    @FXML
    private TableColumn<Document, String> pasLeagueColumn;

    @FXML
    private TableColumn<Document, String> phyLeagueColumn;

    @FXML
    private TableView<Document> leagueAnalyticsTable;

    @FXML
    private TableColumn<Document, String> QualityColumn;

    @FXML
    private TableColumn<Document, String> numPlayersQualityColumn;

    @FXML
    private TableColumn<Document, String> pacQualityColumn;

    @FXML
    private TableColumn<Document, String> driQualityColumn;

    @FXML
    private TableColumn<Document, String> shoQualityColumn;

    @FXML
    private TableColumn<Document, String> defQualityColumn;

    @FXML
    private TableColumn<Document, String> pasQualityColumn;

    @FXML
    private TableColumn<Document, String> phyQualityColumn;

    @FXML
    private TableView<Document> SquadAnalyticsTable;

    @FXML
    private TableColumn<Document, String> ModuleColumn;

    @FXML
    private TableColumn<Document, String> UsageColumn;

    @FXML
    private TextField NationSelector;

    @FXML
    private TextField LeagueSelector;

    @FXML
    private TextField NationSelector1;

    @FXML
    private Label errorLabel;

    @FXML
    private Button uploadButton;

    @FXML
    private TextField choosedFileLabel;

    @FXML
    private Button chooseFileButton;

    @FXML
    private TableView<Map.Entry<String,String>> likedPlayers;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> likedPlayerName;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> numLikes;

    @FXML
    private TableView<Map.Entry<String,String>> activeUser;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> activeUsername;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> numOperations;

    private File file;

    @FXML
    private void initialize(){
        uploadButton.setDisable(true);
        setLikedPlayers();
        setActiveUsers();
    }

    @FXML
    public void onEnter(ActionEvent ae) { nationAnalytics(); }

    @FXML
    public void onEnter1(ActionEvent ae) { leagueAnalytics(); }

    @FXML
    public void onEnter2(ActionEvent ae) { squadAnalytics(); }

    private void nationAnalytics(){
        ArrayList<Document> result = mongoPlayerCard.nationalityAnalytics(NationSelector.getText());

        LeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String) d.getValue().get("league"));
            }
        });

        numPlayersLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty(d.getValue().get("numPlayers").toString());
            }
        });

        pacLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("paceAvg").toString()));
            }
        });

        driLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("dribblingAvg").toString()));
            }
        });

        shoLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("shootingAvg").toString()));
            }
        });

        defLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("defendingAvg").toString()));
            }
        });

        pasLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("passingAvg").toString()));
            }
        });

        phyLeagueColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("physicalityAvg").toString()));
            }
        });

        ObservableList<Document> items = FXCollections.observableArrayList(result);
        nationAnalyticsTable.setItems(items);
        nationAnalyticsTable.getColumns().setAll(LeagueColumn, numPlayersLeagueColumn, pacLeagueColumn, driLeagueColumn, shoLeagueColumn, defLeagueColumn, pasLeagueColumn, phyLeagueColumn);
    }

    private void leagueAnalytics(){
        ArrayList<Document> result = mongoPlayerCard.leagueAnalytics(LeagueSelector.getText());

        QualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String) d.getValue().get("league"));
            }
        });

        numPlayersQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty(d.getValue().get("numPlayers").toString());
            }
        });

        pacQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("paceAvg").toString()));
            }
        });

        driQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("dribblingAvg").toString()));
            }
        });

        shoQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("shootingAvg").toString()));
            }
        });

        defQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("defendingAvg").toString()));
            }
        });

        pasQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("passingAvg").toString()));
            }
        });

        phyQualityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((d.getValue().get("physicalityAvg").toString()));
            }
        });

        ObservableList<Document> items = FXCollections.observableArrayList(result);
        leagueAnalyticsTable.setItems(items);
        leagueAnalyticsTable.getColumns().setAll(QualityColumn, numPlayersQualityColumn, pacQualityColumn, driQualityColumn, shoQualityColumn, defQualityColumn, pasQualityColumn, phyQualityColumn);
    }

    private void squadAnalytics(){
        ArrayList<Document> result = mongoSquad.SquadAnalytics(NationSelector1.getText());

        ModuleColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String) d.getValue().get("module"));
            }
        });

        UsageColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Document, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty(d.getValue().get("use").toString());
            }
        });


        ObservableList<Document> items = FXCollections.observableArrayList(result);
        SquadAnalyticsTable.setItems(items);
        SquadAnalyticsTable.getColumns().setAll(ModuleColumn, UsageColumn);
    }

    private void setLikedPlayers(){
        Map<String, String> likedplayers = neo4jPlayerCard.mostLikedPlayer();
        likedPlayerName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        numLikes.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(likedplayers.entrySet());
        likedPlayers.setItems(items);
        likedPlayers.getColumns().setAll(likedPlayerName, numLikes);
        numLikes.setSortType(TableColumn.SortType.DESCENDING);
        likedPlayers.getSortOrder().setAll(numLikes);
    }

    private void setActiveUsers(){
        Map<String, String> activeusers = neo4jUser.mostActiveUser();
        activeUsername.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        numOperations.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(activeusers.entrySet());
        activeUser.setItems(items);
        activeUser.getColumns().setAll(activeUsername, numOperations);
        numOperations.setSortType(TableColumn.SortType.DESCENDING);
        activeUser.getSortOrder().setAll(numOperations);
    }



    @FXML
    private void chooseFile(ActionEvent event) {
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(window);
        event.consume();
        choosedFileLabel.setText(file.getAbsolutePath());
        if (FilenameUtils.getExtension(String.valueOf(file)).equals("csv")) {
            uploadButton.setDisable(false);
            errorLabel.setVisible(false);
        }
        else {
            errorLabel.setText("Extension file not allowed, convert file to csv or change file");
            errorLabel.setVisible(true);
            uploadButton.setDisable(true);
        }
    }

    @FXML
    private void uploadFile() throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new FileReader(file));
        String [] nextLine;

        boolean result;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line

            UpdatePlayerCardsList upcd = new UpdatePlayerCardsList();
            result = upcd.insertPlayerCards(nextLine, file.getParent());
            if(result){
                errorLabel.setText("Player cards added correctly");
                errorLabel.setVisible(true);
            }
            else{
                errorLabel.setText("There were some problems, check notAdded.txt for further information");
                errorLabel.setVisible(true);
            }
        }
    }

    @FXML
    public void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }

    @FXML
    public void userAnalytics(){ App.setRoot("adminFirstPage"); }

}
