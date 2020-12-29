package org.unipi.group15;


import bean.Player;
import bean.Squad;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mongo.ProvaQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BuildSquadController {
    private static Squad squad;
    private HashMap<String, String> pos;

    @FXML private TextField squadNameTextField;

    @FXML private ChoiceBox<String> moduleChoiceBox;

    @FXML private AnchorPane footbalField;

    @FXML private ChoiceBox<String> positionChoiceBox;

    @FXML private TextField findPlayerTextField;

    @FXML
    private void initialize(){

        moduleChoiceBox.getItems().removeAll(moduleChoiceBox.getItems());
        moduleChoiceBox.getItems().addAll(FXCollections.observableArrayList("3-5-2",
                                            "4-2-3-1", "4-3-1-2", "4-3-3", "4-4-2"));
        moduleChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                displayModulePositions(moduleChoiceBox.getItems().get((Integer) newValue));
            }
        });
        pos = new HashMap<>();
    }

    public void setSquad(Squad s){
        squad = s;
        //squadNameTextField.setText(squad.getName());
        //moduleChoiceBox.getSelectionModel().select(squad.getModule());
    }

    private void displayModulePositions(String module){

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

    @FXML
    private void chooseModule(){
        String module = moduleChoiceBox.getSelectionModel().getSelectedItem();
    }

    private void displayPosition(ArrayList<String> elem){
        pos.clear();
        for(Node e: footbalField.getChildren()){
            e.setVisible(false);
            if(elem.contains(e.getId())){
                e.setVisible(true);
                pos.put(e.getId(), "");
            }
        }

        choosePlayerBox(elem);
    }

    private void choosePlayerBox(ArrayList<String> elem){
        positionChoiceBox.getItems().removeAll(moduleChoiceBox.getItems());
        positionChoiceBox.getItems().addAll(FXCollections.observableArrayList(elem));

    }

    @FXML
    private void selectPlayer(){
        ProvaQuery pq = new ProvaQuery();
        ArrayList<Player> players = pq.findPlayers(findPlayerTextField.getText());
        //System.out.println(players.get(0).getPlayerId());
    }
}
