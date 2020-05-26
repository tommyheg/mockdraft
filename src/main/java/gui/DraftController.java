package gui;

import controllers.Controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Gui;
import pojos.Player;
import pojos.ScoreType;
import pojos.teams.cpu.Difficulty;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class DraftController {

    private Controller controller;
    private boolean suggestions;
    private FilteredList<Player> filteredList;
    private ObservableList<Player> observableList;

    @FXML TableView<Player> playerTable;
    @FXML TableColumn<Player, String> firstName, lastName, position, team;
    @FXML TableColumn<Player, Integer> rank;
    @FXML Button start, mainMenu, exit, selectButton;
    @FXML Label playerLabel, roundLabel, pickLabel;
    @FXML TextField searchBar;
    @FXML ComboBox<String> fieldBox;

    //this acts as a constructor. need this to pass in parameters
    public void construct(ScoreType scoreType, int size, int userPick, boolean suggestions) {
        controller = new Controller(scoreType, size, userPick, Difficulty.STUPID);
        this.suggestions = suggestions;
        setUp();
    }

    //set up the gui. this acts as initialize(). needed bc of constructor
    private void setUp() {
        //declare the values for each column
        rank.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRank()).asObject());
        firstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));
        lastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));
        position.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPosition()));
        team.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeam()));
        //get the players from the controller
        List<Player> playersList = controller.nextAvailablePlayers(40);
        observableList = FXCollections.observableList(playersList);
        //initalize the table
        playerTable.getItems().setAll(observableList);
        filteredList = new FilteredList<>(observableList);
        fieldBox.getItems().addAll(Arrays.asList("rank", "name", "position", "team"));
        fieldBox.getSelectionModel().select("name");
        //enable the filtering through the search bar
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                    switch (fieldBox.getValue()) {
                        case "rank":
                            filteredList.setPredicate(p -> Integer.toString(p.getRank()).contains(searchBar.getText().trim()));
                            break;
                        case "name":
                            filteredList.setPredicate(p -> p.getName().toLowerCase().contains(searchBar.getText().toLowerCase().trim()));
                            break;
                        case "position":
                            filteredList.setPredicate(p -> p.getPosition().toLowerCase().contains(searchBar.getText().toLowerCase().trim()));
                            break;
                        case "team":
                            filteredList.setPredicate(p -> p.getTeam().toLowerCase().contains(searchBar.getText().toLowerCase().trim()));
                            break;
                    }
                    //refresh the table
                    playerTable.getItems().setAll(filteredList);
                }
        );

    }

    //start the draft when the button is clicked
    public void start() {
        //this should never happen bc of this method but just in case
        if(controller.started()) return;
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
            observableList.remove(player);
            filteredList = new FilteredList<>(observableList);
            playerTable.getItems().setAll(observableList);
            controller.draft(player);
        }
        //update the pick label bc it is now the user's turn
        roundLabel.setText("Round: " + controller.getRound());
        pickLabel.setText("Pick: " + controller.getCurrentPick());
    }

    public void selectPlayer() {
        //they clicked the button when it wasn't there turn (doubt this would ever happen bc cpu is fast)
        if (!controller.getCurrentTeam().isUser()) {
            System.out.println("e: button click during cpu turn");
            return;
        }
        //TODO: run the suggestions
        // probably in another tab
        //get the player they have selected
        String name = playerLabel.getText();
        System.out.println("player attempting to draft " + name);
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
    public void updatePlayer() {
        Player player = playerTable.getSelectionModel().getSelectedItem();
        if(player == null) return;  //needed for sorting columns. it calls this method by default
        playerLabel.setText(player.getName());
    }

    //go back to the main screen
    public void goToMain() {
        Scene main;
        try {
            Parent parent = FXMLLoader.load(((getClass()
                    .getResource("/fxml/Main.fxml"))));
            main = new Scene(parent);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage root = Gui.getRoot();
        root.setTitle("mock draft");
        root.setScene(main);
        root.show();
    }

    public void exit() {
        System.exit(0);
    }


}
