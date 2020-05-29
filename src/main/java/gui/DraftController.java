package gui;

import controllers.Controller;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Gui;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.cpu.Difficulty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class DraftController extends GodController {

    private Controller controller;
    private FilteredList<Player> filteredList;
    private ObservableList<Player> observableList;
    private String[] projectionNames;

    @FXML
    TableView<Player> playerTable;
    @FXML
    TableColumn<Player, String> firstName, lastName, position, team;
    @FXML
    TableColumn<Player, Integer> rank;
    @FXML
    Button start, selectButton;
    @FXML
    Label playerLabel, roundLabel, pickLabel;
    @FXML
    TextField searchBar;
    @FXML
    List<TableColumn<Player, Double>> projectionColumns;

    //pseudo constructor called when loading initial fxml
    public void construct(Controller controller) {
        this.controller = controller;
        this.projectionNames = new String[]{
                "Points", "Rush Att", "Rush Yds", "Rush Tds",
                "Recs", "Rec Yds", "Rec Tds", "Pass Cmp",
                "Pass Att", "Pass Yds", "Pass Tds",
                "Pass Ints", "Fumbles"
        };
        setUp();
    }

    //set up the gui. this acts as initialize(). needed bc of constructor
    private void setUp() {
        //set up the table
        setUpColumns();
        List<Player> playersList = controller.nextAvailablePlayers();
        observableList = FXCollections.observableList(playersList);
        filteredList = new FilteredList<>(observableList);
        playerTable.getItems().setAll(observableList);
        //filter function
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(p -> Integer.toString(p.getRank()).contains(searchBar.getText().trim()) ||
                    p.getName().toLowerCase().contains(searchBar.getText().toLowerCase().trim()) ||
                    p.getPosition().toLowerCase().contains(searchBar.getText().toLowerCase().trim()) ||
                    p.getTeam().toLowerCase().contains(searchBar.getText().toLowerCase().trim()));
            playerTable.getItems().setAll(filteredList);
        });
        //TODO: this only works if you click the button first. fix that
        selectButton.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) selectPlayer();
        });
    }

    //just called in setUp()
    private void setUpColumns() {
        //set all the values of the columns
        rank.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRank()).asObject());
        firstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));
        lastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));
        position.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPosition()));
        team.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeam()));

        //set projection values
        projectionColumns = new ArrayList<TableColumn<Player, Double>>();
        for (int i = 0; i < projectionNames.length; i++) {
            TableColumn<Player, Double> column = new TableColumn<Player, Double>(projectionNames[i]);
            projectionColumns.add(column);
            int finalI = i;
            column.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue()
                    .getProjections().get(projectionNames[finalI])).asObject());
            //change all 0s to null
            column.setCellFactory(param -> new TableCell<Player, Double>(){
                @Override
                protected void updateItem(Double val, boolean empty) {
                    if (empty || val.equals(0.0)) setText("");
                    else setText(val.toString());
                }
            });
        }
        playerTable.getColumns().addAll(projectionColumns);
    }

    //start the draft when the button is clicked
    public void start() {
        //this should never happen bc of this method but just in case
        if (controller.started()) return;
        //draft until the user pick
        cpuDraft();
        //change the design of the button
        start.setDisable(false);
        start.setStyle("-fx-background-color: transparent");
        start.setText("");
    }

    //draft until the user pick
    public void cpuDraft() {
        //draft the player for the cpu, remove from table
        while (!controller.getCurrentTeam().isUser()) {
            Player player = controller.draftPlayerCPU();
            //only way this happens is if draft is done
            if (player == null) {
                playerLabel.setText("draft is done mofo");
                //TODO: do something when draft is done
                //maybe show full table of players ordered by draft selection?
                    //that means we will have to keep another list of drafted players
                //automatically go to team tab?
            }
            observableList.remove(player);
            filteredList = new FilteredList<>(observableList);
            playerTable.getItems().setAll(observableList);
            controller.draft(player);
        }
        //update the pick label bc it is now the user's turn
        roundLabel.setText("Round: " + controller.getRound());
        pickLabel.setText("Pick: " + controller.getCurrentPick());
    }

    //select the current player then the user presses the button
    //TODO: call this when they press enter- in setUp()
    public void selectPlayer() {
        //they clicked the button when it wasn't there turn (doubt this would ever happen bc cpu is fast)
        if (!controller.getCurrentTeam().isUser()) {
            System.out.println("e: button click during cpu turn");
            return;
        }
        //get the player they have selected
        String name = playerLabel.getText();
        Player player = controller.draftPlayer(name);
        if (player == null) {
            System.out.println("e: select valid player. inside userDraft()");
            playerLabel.setText("Must select a valid player.");
            return;
        }
        //remove the player from the table
        observableList.remove(player);
        filteredList = new FilteredList<>(observableList);
        playerTable.getItems().setAll(observableList);
        controller.draft(player);
        //draft until the user's turn again
        cpuDraft();
    }

    //update the player label when the table is clicked
    //TODO: call this when they use up and down keys to select a player- in setUp()
    public void updatePlayer() {
        Player player = playerTable.getSelectionModel().getSelectedItem();
        if (player == null) return;  //needed for when user sorts a column. it calls this method by default
        playerLabel.setText(player.getName());
    }

}