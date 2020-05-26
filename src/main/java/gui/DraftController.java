package gui;

import controllers.Controller;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DraftController {

    private Controller controller;
    private boolean suggestions;

    @FXML TableView<Player> playerTable;
    @FXML TableColumn<Player, String> rank, firstName, lastName, position, team;
    @FXML Button start, mainMenu, exit, selectButton;
    @FXML Label playerLabel, roundLabel, pickLabel;
    @FXML TextField searchBar;

    public void construct(ScoreType scoreType, int size, int userPick, boolean suggestions){
        controller = new Controller(scoreType, size, userPick, Difficulty.STUPID);
        this.suggestions = suggestions;
        updateTable();
    }

    public void start(){
        cpuDraft();
    }

    public void cpuDraft(){
        if(!controller.getCurrentTeam().isUser()) System.out.println("cpu teams drafting now. in cpuDraft()");
        while(!controller.getCurrentTeam().isUser()){
            Player player = controller.draftPlayerCPU();
            playerTable.getItems().remove(player);
            controller.draft(player);
            System.out.println("cpu chose "+player.getName());
        }
        //update the pick label bc it is now the user's turn
        roundLabel.setText("Round: "+controller.getRound());
        pickLabel.setText("Pick: "+controller.getCurrentPick());
    }

    public void selectPlayer(){
        //TODO: draft a player from the gui
        // wait until they have selected a player with a button?
        // tried while loop didn't work
        // do it like sde tic tac toe?  --this will work
        System.out.println("they clicked the button. in selectPlayer()");
        //they clicked the button when it wasn't there turn (doubt this would ever happen)
        if(!controller.getCurrentTeam().isUser()) {
            System.out.println("e: button click during cpu turn");
            return;
        }
        //TODO: run the suggestions
        //get the player they have selected
        String name = playerLabel.getText();
        System.out.println("player attempting to draft "+name);
        Player player = controller.draftPlayer(name);
        if(player == null){
            System.out.println("e: select valid player. inside userDraft()");
            playerLabel.setText("Must select a valid player.");
            return;
        }
        playerTable.getItems().remove(player);
        controller.draft(player);
        System.out.println("drafted "+name);
        cpuDraft();
    }

    /**
     * Update the table based on what they search
     */
    public void search(){
        //TODO: update table. just use filters
    }

    public void updatePlayer(){
        Player player = playerTable.getSelectionModel().getSelectedItem();
        playerLabel.setText(player.getName());
    }

    /**
     * Populate the table with all of the players
     */
    private void updateTable(){
        List<Player> playersList = controller.nextAvailablePlayers(40);
        rank.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getRank())));
        firstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));
        lastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));
        position.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPosition()));
        team.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeam()));
        playerTable.getItems().setAll(playersList);
    }

    public void goToMain(){
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
    public void exit(){
        System.exit(0);
    }


}
