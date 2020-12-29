package org.unipi.group15;


import bean.Player;
import bean.Squad;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mongo.ProvaQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BuildSquadController {
    private static int squadIdex = -1;
    private static Squad squad;
    private HashMap<String, AnchorPane> squadPos;
    private TableView<Player> playersTable = new TableView();

    @FXML private TextField squadNameTextField;

    @FXML private ChoiceBox<String> moduleChoiceBox;

    @FXML private AnchorPane footbalField;

    @FXML private ChoiceBox<String> positionChoiceBox;

    @FXML private TextField findPlayerTextField;

    @FXML private ScrollPane playersScrollPane;

    @FXML private Button addPlayerButton;

    @FXML
    private void initialize(){

        moduleChoiceBox.getItems().removeAll(moduleChoiceBox.getItems());
        moduleChoiceBox.getItems().addAll(FXCollections.observableArrayList("3-5-2",
                                            "4-2-3-1", "4-3-1-2", "4-3-3", "4-4-2"));
        if(squadIdex != -1) {
            squad = App.getSession().getSquads().get(squadIdex);
            moduleChoiceBox.getSelectionModel().select(squad.getModule());
            displayModulePositions(squad.getModule());
            return;
        }
        else
            squad = new Squad();

        moduleChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                displayModulePositions(moduleChoiceBox.getItems().get((Integer) newValue));
            }
        });
        squadPos = new HashMap<>();
    }

    public void setSquadIndex(int index){
        squadIdex = index;
        //squadNameTextField.setText(squad.getName());
        //moduleChoiceBox.getSelectionModel().select(squad.getModule());
    }

    private void displayModulePositions(String module){

        squad.setModule(module);
        switch (module){
            case "3-5-2":
                ArrayList<String> m352 = new ArrayList(Arrays.asList("GK", "CB0", "CB1", "CB2", "CDM0", "CDM2", "CAM1", "LM", "RM", "ST0", "ST2"));
                displayPosition(m352);
                break;
            case "4-2-3-1":
                ArrayList<String> m4231 = new ArrayList(Arrays.asList("GK","CB0","CB2","LB","RB","CAM1","CDM0","CDM2","CAM0","CAM2","ST1"));
                displayPosition(m4231);
                break;
            case "4-3-1-2":
                ArrayList<String> m4312 = new ArrayList(Arrays.asList("GK","CB0","CB2","LB","RB","CM0","CM1","CM2","CAM1","ST0","ST2"));
                displayPosition(m4312);
                break;
            case "4-3-3":
                ArrayList<String> m433 = new ArrayList(Arrays.asList("GK","CB0","CB2","LB","RB","CM0","CM1","CM2","ST1","LW","RW"));
                displayPosition(m433);
                break;
            case "4-4-2":
                ArrayList<String> m442 = new ArrayList(Arrays.asList("GK","CB0","CB2","LB","RB","CM0","CM2","LM","RM","ST0","ST2"));
                displayPosition(m442);
                break;
            default: break;
        }
    }

    @FXML
    private void switchToProfile() throws IOException {
        App.setRoot("userPage");
        App.setHeight(600);
        App.setWidth(900);
    }

    private void displayPosition(ArrayList<String> elem){
        squadPos.clear();
        for(Node e: footbalField.getChildren()){
            e.setVisible(false);
            if(elem.contains(e.getId())){
                e.setVisible(true);
                squadPos.put(e.getId(), (AnchorPane) e);
            }
        }

        choosePlayerBox(elem);
    }

    private void choosePlayerBox(ArrayList<String> elem){
        positionChoiceBox.getItems().removeAll(moduleChoiceBox.getItems());
        positionChoiceBox.getItems().addAll(FXCollections.observableArrayList(elem));

    }

    @FXML
    private void setSquadNane(){
        squad.setName(squadNameTextField.getText());
    }

    @FXML
    private void selectPlayer(){
        ProvaQuery pq = new ProvaQuery();
        playersTable.getItems().clear();
        ObservableList<Player> players = FXCollections.observableArrayList(pq.findPlayers(findPlayerTextField.getText()));

        TableColumn<Player, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("playerExtendedName"));

        TableColumn<Player, Integer> column2 = new TableColumn<>("Overall");
        column2.setCellValueFactory(new PropertyValueFactory<>("overall"));

        TableColumn<Player, String> column3 = new TableColumn<>("Quality");
        column3.setCellValueFactory(new PropertyValueFactory<>("quality"));

        TableColumn<Player, String> column4 = new TableColumn<>("Revision");
        column4.setCellValueFactory(new PropertyValueFactory<>("revision"));

        TableColumn<Player, String> column5 = new TableColumn<>("Club");
        column5.setCellValueFactory(new PropertyValueFactory<>("club"));

        TableColumn<Player, String> column6 = new TableColumn<>("ADD");

        playersTable.getColumns().addAll(column1, column2, column3, column4, column5);

        playersTable.setItems(players);

        playersTable.setFixedCellSize(25);
        playersTable.prefHeightProperty().bind(Bindings.size(playersTable.getItems()).multiply(playersTable.getFixedCellSize()).add(30));

        playersScrollPane.setContent(playersTable);
    }

    @FXML
    private void addPlayer(){
        Player player = (Player) playersTable.getSelectionModel().getSelectedItem();
        String pos = (String) positionChoiceBox.getSelectionModel().getSelectedItem();

        squad.addPlayer(pos, player);
        AnchorPane n = squadPos.get(pos);
        System.out.println(squad.toString());
        //n.getChildren().add(new Text(p.getPlayerId()));
    }
}
