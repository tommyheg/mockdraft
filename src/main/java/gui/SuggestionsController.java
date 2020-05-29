package gui;

import controllers.Controller;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pojos.Player;

import java.util.List;

public class SuggestionsController extends GodController {

    private Controller controller;
    private boolean suggestions;
    private int pickNumber;

    @FXML
    Label disrespectLabel;
    @FXML
    TableView<Player> playerTable;
    @FXML
    TableColumn<Player, String> firstName, lastName, position, team;
    @FXML
    TableColumn<Player, Integer> rank;
    @FXML
    TableColumn<Player, Double> value;

    //pseudo constructor called when loading initial fxml
    public void construct(Controller controller, boolean suggestions) {
        this.controller = controller;
        this.suggestions = suggestions;
        if (suggestions) setUpTable();
    }

    //called every time the tab is opened
    public void setUp() {
        if (controller.getPickNumber() == pickNumber) return;   //only make update suggestions if it a new pick
        pickNumber = controller.getPickNumber();
        if (!suggestions) { //only show suggestions if they want you to
            String s = "how dare you not take our suggestions. very disrespectful of you";
            disrespectLabel.setText(s);
            playerTable.setVisible(false);
        } else {
            disrespectLabel.setVisible(false);
            getSuggestions();
        }
    }

    //populate the table with the suggestions for the current pick
    private void getSuggestions() {
        List<Player> players = controller.getSuggestions();
        ObservableList<Player> observableList = FXCollections.observableList(players);
        playerTable.getItems().setAll(observableList);
    }

    //called in the constructor. initially sets up the table
    private void setUpTable() {
        value.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getValue()).asObject());
        firstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));
        lastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));
        position.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPosition()));
        team.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeam()));
        rank.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRank()).asObject());
    }


}
