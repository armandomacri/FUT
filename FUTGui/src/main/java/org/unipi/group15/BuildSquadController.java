package org.unipi.group15;

import bean.Player;
import bean.Squad;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mongo.MongoPlayerCard;
import mongo.MongoSquad;
import user.UserSessionService;

import java.util.*;

public class BuildSquadController {
    private static final UserSessionService userSession = App.getSession();
    private static final MongoSquad mongoSquad = new MongoSquad();
    private static final MongoPlayerCard mongoPlayerCard = new MongoPlayerCard();
    private static int squadIndex = -1;
    private static Squad squad;

    @FXML private Label userIdLabel;

    @FXML private TextField squadNameTextField;

    @FXML private ChoiceBox<String> moduleChoiceBox;

    @FXML private ChoiceBox<String> positionChoiceBox;

    @FXML private TableView<Player> findPlayersTableView;

    @FXML private AnchorPane squadsAnchorPane;

    @FXML private TextField findPlayerTextField;

    @FXML private Button addPlayerButton;

    @FXML private Button findButton;

    @FXML private Text overallText;

    @FXML private TextField positionPlayerTextField;

    @FXML private TextField qualityPlayerTextField;

    @FXML private TextField nationPlayerTextField;

    @FXML
    private void initialize(){
        findButton.setDisable(true);
        addPlayerButton.setDisable(true);
        //SaveButton.setDisable(true);
        userIdLabel.setText(userSession.getUserId());
        moduleChoiceBox.getItems().removeAll(moduleChoiceBox.getItems());
        moduleChoiceBox.getItems().addAll(FXCollections.observableArrayList("352",
                                            "4231", "4312", "433", "442"));

        findPlayersTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("playerExtendedName"));
        findPlayersTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("position"));
        findPlayersTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("overall"));
        findPlayersTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("revision"));
        findPlayersTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("league"));
        findPlayersTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nationality"));

        if(squadIndex != -1) { //modify squad
            squad = App.getSession().getSquads().get(squadIndex);
            squadNameTextField.setText(squad.getName());
            moduleChoiceBox.getSelectionModel().select(squad.getModule());
            displayModulePositions(squad.getModule());

            HashMap<String, Player> players = new HashMap<>();
            for (Map.Entry<String, String> item : squad.getPlayers().entrySet()) {
                Player p = mongoPlayerCard.findById(item.getValue());
                if (p == null)
                    continue;
                players.put(item.getKey(), p);
            }

            showSquad(players);

            overallText.setText(computeOverall(squad).toString());
        }
        else {
            squad = new Squad();
        }

        moduleChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                squad.getPlayers().clear();
                displayModulePositions(moduleChoiceBox.getItems().get((Integer) newValue));
            }
        });

        if (userSession.getSquads() == null)
            userSession.setSquads(new ArrayList<>());
    }

    public void setSquadIndex(int index){ squadIndex = index; }

    private void displayModulePositions(String module){

        overallText.setText("");
        squad.setModule(module);
        switch (module){
            case "352":
                ArrayList<String> m352 = new ArrayList<>(Arrays.asList("GK", "CB0", "CB1", "CB2", "CDM0", "CDM1", "CAM1", "LM", "RM", "ST0", "ST1"));
                displayPosition(m352);
                break;
            case "4231":
                ArrayList<String> m4231 = new ArrayList<>(Arrays.asList("GK","CB0","CB1","LB","RB","CAM1","CDM0","CDM1","CAM0","CAM2","ST"));
                displayPosition(m4231);
                break;
            case "4312":
                ArrayList<String> m4312 = new ArrayList<>(Arrays.asList("GK","CB0","CB1","LB","RB","CM0","CM1","CM2","CAM1","ST0","ST1"));
                displayPosition(m4312);
                break;
            case "433":
                ArrayList<String> m433 = new ArrayList<>(Arrays.asList("GK","CB0","CB1","LB","RB","CM0","CM1","CM2","ST0","LW","RW"));
                displayPosition(m433);
                break;
            case "442":
                ArrayList<String> m442 = new ArrayList<>(Arrays.asList("GK","CB0","CB1","LB","RB","CM0","CM1","LM","RM","ST0","ST1"));
                displayPosition(m442);
                break;
            default: break;
        }
        findButton.setDisable(false);
    }

    @FXML
    private void switchToProfile() { App.setRoot("userPage"); }

    private void displayPosition(ArrayList<String> elem){
        choosePlayerBox(elem);
        overallText.setText("");

    }

    private void choosePlayerBox(ArrayList<String> elem){
        positionChoiceBox.getItems().removeAll(positionChoiceBox.getItems());
        positionChoiceBox.getItems().addAll(FXCollections.observableArrayList(elem));
    }

    private void showSquad(HashMap<String, Player> players){
        squadsAnchorPane.getChildren().clear();
        GridPane grid = new GridPane();
        int row = 0, column = 0;

        for (Map.Entry<String, Player> item : players.entrySet()){
            VBox v = new VBox();
            HBox h = new HBox(new Label(item.getKey()));
            h.setAlignment(Pos.CENTER);
            VBox container = new VBox();
            container.getStyleClass().add("showPlayer");
            HBox h1 = new HBox(new Label(item.getValue().getPlayerExtendedName()));
            h1.setAlignment(Pos.CENTER);
            HBox h2 = new HBox(new Label(item.getValue().getOverall().toString()));
            h2.setAlignment(Pos.CENTER);
            if(item.getValue().getQuality().contains("Gold")) {
                container.getStyleClass().add("goldPlayer");
            }
            else if (item.getValue().getQuality().contains("Silver")) {
                container.getStyleClass().add("silverPlayer");
            }
            else {
                container.getStyleClass().add("bronzePlayer");
            }
            container.getChildren().addAll(h1, h2);
            v.getChildren().addAll(h, container);
            grid.setHgap(15); //horizontal gap in pixels => that's what you are asking for
            grid.setVgap(20); //vertical gap in pixels
            grid.setMinSize(0,0);
            grid.add(v, row, column);
            if (column == 2)
                row += 1;
            column = (column == 0) ? 1 : (column == 1) ? 2 : 0;
        }


        squadsAnchorPane.getChildren().add(grid);
    }

    @FXML
    private void setSquadName(){
        squad.setName(squadNameTextField.getText());
    }

    @FXML
    private void selectPlayer(){

        ObservableList<Player> players;
        if (!findPlayerTextField.getText().equals(""))
            players = FXCollections.observableArrayList(mongoPlayerCard.findPlayers(findPlayerTextField.getText()));
        else if (!positionPlayerTextField.getText().equals("") & !nationPlayerTextField.getText().equals("") & !qualityPlayerTextField.getText().equals(""))
            players = FXCollections.observableArrayList(mongoPlayerCard.filterBy(positionPlayerTextField.getText(), nationPlayerTextField.getText(), qualityPlayerTextField.getText()));
        else
            return;

        findPlayersTableView.getItems().clear();
        findPlayersTableView.setItems(players);
        addPlayerButton.setDisable(false);
   }

    @FXML
    private void addPlayer(){
        Player player = findPlayersTableView.getSelectionModel().getSelectedItem();
        if(player==null)
            return;
        String pos = positionChoiceBox.getSelectionModel().getSelectedItem();
        squad.getPlayers().put(pos, player.getPlayerId());

        HashMap<String, Player> players = new HashMap<>();
        for (Map.Entry<String, String> item : squad.getPlayers().entrySet()) {
            players.put(item.getKey(),mongoPlayerCard.findById(item.getValue()));
        }
        showSquad(players);
        overallText.setText(computeOverall(squad).toString());
        findPlayersTableView.getItems().clear();
        /*
        if(squad.getPlayers().size() == 11 )
            SaveButton.setDisable(false);

         */
        addPlayerButton.setDisable(true);
    }

    @FXML
    private void saveSquad(){
        squad.setName(squadNameTextField.getText());

        if (squadIndex == -1){
            squad.setDate(new Date());
            userSession.getSquads().add(squad);
        }
        else
            userSession.getSquads().add(squadIndex, squad);

        mongoSquad.add(userSession.getUserId(), squadIndex, squad);
        App.setRoot("userPage");
    }

    private Integer computeOverall (Squad s){
        Integer sum = 0;
        int overall;

        for (Map.Entry<String, String> item : squad.getPlayers().entrySet()) {
            Player p = mongoPlayerCard.findById(item.getValue());
            if (p == null)
                continue;
            else
                sum += p.getOverall();
        }

        overall = sum/11;
        return overall;
    }

    @FXML
    public void onEnter(ActionEvent ae) {
        selectPlayer();
    }
}
