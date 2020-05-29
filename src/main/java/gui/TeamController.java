package gui;

import controllers.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pojos.Player;
import pojos.teams.Team;

import java.util.List;

public class TeamController extends GodController {

    private Team userTeam;

    @FXML
    TableView<Player> playerTable;
    @FXML
    TableColumn<Player, String> firstName, lastName, position, team;
    @FXML
    TableColumn<Player, Integer> round, pick, selection, adp;

    //pseudo constructor called when loading initial fxml
    public void construct(Controller controller) {
        userTeam = controller.getUserTeam();
        setUpTable();
    }

    //show their team when tab is opened
    public void showTeam() {
        List<Player> playersList = userTeam.getPlayers();
        ObservableList<Player> observableList = FXCollections.observableList(playersList);
        playerTable.getItems().setAll(observableList);
    }

    //called in the constructor. initially sets up the table
    private void setUpTable() {
        round.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRoundNum()).asObject());
        pick.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPickNum()).asObject());
        firstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));
        lastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));
        position.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPosition()));
        team.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeam()));
        selection.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getSelection()).asObject());
        adp.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRank()).asObject());
    }
}
