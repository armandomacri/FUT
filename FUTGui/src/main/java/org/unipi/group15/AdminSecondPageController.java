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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import mongo.MongoAdmin;
import neo4j.Neo4jAdmin;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import java.io.*;
import java.util.*;

public class AdminSecondPageController {
    private static final MongoAdmin mongoAdmin = new MongoAdmin();
    private static final Neo4jAdmin neo4jAdmin = new Neo4jAdmin();
    private File file;

    @FXML private TableView<Document> nationAnalyticsTable;

    @FXML private TableColumn<Document, String> LeagueColumn;

    @FXML private TableColumn<Document, String> numPlayersLeagueColumn;

    @FXML private TableColumn<Document, String> pacLeagueColumn;

    @FXML private TableColumn<Document, String> driLeagueColumn;

    @FXML private TableColumn<Document, String> shoLeagueColumn;

    @FXML private TableColumn<Document, String> defLeagueColumn;

    @FXML private TableColumn<Document, String> pasLeagueColumn;

    @FXML private TableColumn<Document, String> phyLeagueColumn;

    @FXML private TableView<Document> leagueAnalyticsTable;

    @FXML private TableColumn<Document, String> QualityColumn;

    @FXML private TableColumn<Document, String> numPlayersQualityColumn;

    @FXML private TableColumn<Document, String> pacQualityColumn;

    @FXML private TableColumn<Document, String> driQualityColumn;

    @FXML private TableColumn<Document, String> shoQualityColumn;

    @FXML private TableColumn<Document, String> defQualityColumn;

    @FXML private TableColumn<Document, String> pasQualityColumn;

    @FXML private TableColumn<Document, String> phyQualityColumn;

    @FXML private TableView<Document> SquadAnalyticsTable;

    @FXML private TableColumn<Document, String> ModuleColumn;

    @FXML private TableColumn<Document, String> UsageColumn;

    @FXML private TextField NationSelector;

    @FXML private TextField LeagueSelector;

    @FXML private TextField NationSelector1;

    @FXML private Label errorLabel;

    @FXML private Button uploadButton;

    @FXML private TextField choosedFileLabel;

    @FXML private TableView<Map.Entry<String,String>> analyticsTable;

    @FXML private TableColumn<Map.Entry<String, String>, String> firstColumnAnalytics;

    @FXML private TableColumn<Map.Entry<String, String>, String> secondColumnAnalytics;

    @FXML private MenuButton selectAnalyticsMenuButton;

    @FXML private TextField positionTextField;

    @FXML private Label mostUsedPlayerLabel;

    @FXML
    private void initialize(){
        uploadButton.setDisable(true);
        checkService("Service not completely available!");
    }

    @FXML
    public void onEnter() { nationAnalytics(); }

    @FXML
    public void onEnter1() { leagueAnalytics(); }

    @FXML
    public void onEnter2() { squadAnalytics(); }

    private void nationAnalytics(){
        ArrayList<Document> result = mongoAdmin.nationalityAnalytics(NationSelector.getText());
        if (result==null | result.size()==0){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        }

        LeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String) d.getValue().get("league"));
            }
        });

        numPlayersLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty(d.getValue().get("numPlayers").toString());
            }
        });

        pacLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("paceAvg"))));
            }
        });

        driLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("dribblingAvg"))));
            }
        });

        shoLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("shootingAvg"))));
            }
        });

        defLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("defendingAvg"))));
            }
        });

        pasLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("passingAvg"))));
            }
        });

        phyLeagueColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("physicalityAvg"))));
            }
        });

        ObservableList<Document> items = FXCollections.observableArrayList(result);
        nationAnalyticsTable.setItems(items);
        nationAnalyticsTable.getColumns().setAll(LeagueColumn, numPlayersLeagueColumn, pacLeagueColumn, driLeagueColumn, shoLeagueColumn, defLeagueColumn, pasLeagueColumn, phyLeagueColumn);
    }

    private void leagueAnalytics(){
        ArrayList<Document> result = mongoAdmin.leagueAnalytics(LeagueSelector.getText());
        if (result==null | result.size()==0){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        }

        QualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String) d.getValue().get("league"));
            }
        });

        numPlayersQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty(d.getValue().get("numPlayers").toString());
            }
        });

        pacQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("paceAvg"))));
            }
        });

        driQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("dribblingAvg"))));
            }
        });

        shoQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("shootingAvg"))));
            }
        });

        defQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("defendingAvg"))));
            }
        });

        pasQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("passingAvg"))));
            }
        });

        phyQualityColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String.format("%.2f", d.getValue().get("physicalityAvg"))));
            }
        });

        ObservableList<Document> items = FXCollections.observableArrayList(result);
        leagueAnalyticsTable.setItems(items);
        leagueAnalyticsTable.getColumns().setAll(QualityColumn, numPlayersQualityColumn, pacQualityColumn, driQualityColumn, shoQualityColumn, defQualityColumn, pasQualityColumn, phyQualityColumn);
    }

    private void squadAnalytics(){
        ArrayList<Document> result = mongoAdmin.SquadAnalytics(NationSelector1.getText());
        if (result==null | result.size()==0){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        }

        ModuleColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty((String) d.getValue().get("module"));
            }
        });

        UsageColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Document, String> d) {
                return new SimpleStringProperty(d.getValue().get("use").toString());
            }
        });


        ObservableList<Document> items = FXCollections.observableArrayList(result);
        SquadAnalyticsTable.setItems(items);
        SquadAnalyticsTable.getColumns().setAll(ModuleColumn, UsageColumn);
    }

    @FXML
    private void setLikedPlayers(){
        firstColumnAnalytics.setText("Player Name");
        secondColumnAnalytics.setText("Likes");
        Map<String, String> likedplayers = neo4jAdmin.mostLikedPlayer();
        if (likedplayers == null){
            if(checkService("Service not completely available!")){
                Alert a = new Alert(Alert.AlertType.WARNING, "Something wrong");
                a.show();
            }
            return;
        }
        firstColumnAnalytics.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        secondColumnAnalytics.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(likedplayers.entrySet());
        analyticsTable.setItems(items);
        analyticsTable.getColumns().setAll(firstColumnAnalytics, secondColumnAnalytics);
        secondColumnAnalytics.setSortType(TableColumn.SortType.DESCENDING);
        analyticsTable.getSortOrder().setAll(secondColumnAnalytics);
    }

    @FXML
    private void setActiveUsers(){
        firstColumnAnalytics.setText("Username");
        secondColumnAnalytics.setText("Operations");
        Map<String, String> activeusers = neo4jAdmin.mostActiveUser();
        if (activeusers == null){
            if(checkService("Service not completely available!")){
                Alert a = new Alert(Alert.AlertType.WARNING, "Something wrong");
                a.show();
            }
            return;
        }

        firstColumnAnalytics.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        secondColumnAnalytics.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(activeusers.entrySet());
        analyticsTable.setItems(items);
        analyticsTable.getColumns().setAll(firstColumnAnalytics, secondColumnAnalytics);
        secondColumnAnalytics.setSortType(TableColumn.SortType.DESCENDING);
        analyticsTable.getSortOrder().setAll(secondColumnAnalytics);
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
            }
            else{
                errorLabel.setText("There were some problems, check notAdded.txt for further information");
            }
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void showMostUsedPlayer(){
        if (positionTextField.equals(""))
            return;

        String player = mongoAdmin.mostUsedPlayer(positionTextField.getText());
        if (player == null){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        }
        mostUsedPlayerLabel.setText(positionTextField.getText() + ": " + player);
    }

    @FXML
    public void logOut() {
        App.getSession().clear();
        App.setRoot("primary");
    }

    @FXML
    public void userAnalytics(){ App.setRoot("adminFirstPage"); }

    private boolean checkService(String text){
        if (!neo4jAdmin.checkConnection()){
            uploadButton.setDisable(true);
            selectAnalyticsMenuButton.setDisable(true);
            Alert a = new Alert(Alert.AlertType.WARNING, text, ButtonType.OK);
            a.show();
            return false;
        }
        return true;
    }

}
