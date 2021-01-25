package org.unipi.group15;

import bean.Player;
import bean.Squad;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private static final ArrayList<String> supportedModule = new ArrayList<>(Arrays.asList("352", "4231", "4312", "433", "442"));
    private static int squadIndex = -1;
    private static Squad squad;

    @FXML private Label userIdLabel;

    @FXML private TextField squadNameTextField;

    @FXML private ChoiceBox<String> moduleChoiceBox;

    @FXML private ChoiceBox<String> positionChoiceBox;

    @FXML private TableView<Player> findPlayersTableView;

    @FXML private GridPane squadsGridPane;

    @FXML private TextField findPlayerTextField;

    @FXML private Button addPlayerButton;

    @FXML private Button findButton;

    @FXML private Text overallText;

    @FXML private TextField positionPlayerTextField;

    @FXML private TextField qualityPlayerTextField;

    @FXML private TextField nationPlayerTextField;

    @FXML private Button saveButton;

    @FXML
    private void initialize(){
        //SaveButton.setDisable(true);
        userIdLabel.setText(userSession.getUserId());

        if(squadIndex != -1) { //modify squad
            squad = App.getSession().getSquads().get(squadIndex);
            squadNameTextField.setText(squad.getName());
            showSquad(squad.getPlayers());
            overallText.setText(computeOverall().toString());
            //not all modules are supported
            if (!supportedModule.contains(squad.getModule())){
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Can't modify this module");
                a.show();
                findButton.setDisable(true);
                findPlayerTextField.setDisable(true);
                addPlayerButton.setDisable(true);
                saveButton.setDisable(true);
                return;
            }
            moduleChoiceBox.getSelectionModel().select(squad.getModule());
            displayModulePositions(squad.getModule());
        }
        else {
            squad = new Squad();
        }

        moduleChoiceBox.getItems().removeAll(moduleChoiceBox.getItems());
        moduleChoiceBox.getItems().addAll(FXCollections.observableArrayList(supportedModule));
        findPlayersTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("playerExtendedName"));
        findPlayersTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("position"));
        findPlayersTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("overall"));
        findPlayersTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("revision"));
        findPlayersTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("league"));
        findPlayersTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nationality"));
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
        squadsGridPane.getChildren().clear();
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
            container.getChildren().addAll(h1, h2);
            v.getChildren().addAll(h, container);
            squadsGridPane.setHgap(5);
            if (row == 0 & column == 2)
                row = 2;
            squadsGridPane.add(v, row, column);
            if (row < 4)
                row += 1;
            else{
                row = 0;
                column += 1;
            }
        }
    }

    @FXML
    private void selectPlayer(){

        ObservableList<Player> players;
        if (!findPlayerTextField.getText().equals(""))
            players = FXCollections.observableArrayList(mongoPlayerCard.findPlayers(findPlayerTextField.getText()));
        else if (!positionPlayerTextField.getText().equals("") & !nationPlayerTextField.getText().equals("") & !qualityPlayerTextField.getText().equals(""))
            players = FXCollections.observableArrayList(mongoPlayerCard.filterBy(positionPlayerTextField.getText(), nationPlayerTextField.getText(), qualityPlayerTextField.getText()));
        else{
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        };

        findPlayersTableView.getItems().clear();
        findPlayersTableView.setItems(players);
        addPlayerButton.setDisable(false);
   }

    @FXML
    private void addPlayer(){
        Player player = findPlayersTableView.getSelectionModel().getSelectedItem();
        if (player==null){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        }
        String pos = positionChoiceBox.getSelectionModel().getSelectedItem();
        squad.getPlayers().put(pos, player);
        showSquad(squad.getPlayers());
        overallText.setText(computeOverall().toString());
        findPlayersTableView.getItems().clear();
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

        boolean result = mongoSquad.add(userSession.getUserId(), squadIndex, squad);
        if (result==false){
            Alert a = new Alert(Alert.AlertType.WARNING, "Something Wrong");
            a.show();
            return;
        }
        App.setRoot("userPage");
    }

    private Integer computeOverall (){
        Integer sum = 0;
        int overall;

        for (Map.Entry<String, Player> item : squad.getPlayers().entrySet()) {
                sum += item.getValue().getOverall();
        }

        overall = sum/11;
        return overall;
    }

    @FXML
    public void onEnter() {
        selectPlayer();
    }
}
