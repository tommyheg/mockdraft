package gui;

import controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import pojos.ScoreType;
import pojos.teams.cpu.Difficulty;

import java.io.IOException;

public class MainController extends GodController {

    private Controller controller;
    private boolean suggestions;

    @FXML
    TabPane tabPane;
    @FXML
    Tab draftTab, teamTab, suggestionsTab;

    public void construct(ScoreType scoreType, int size, int userPick, boolean suggestions) {
        this.controller = new Controller(scoreType, size, userPick, Difficulty.STUPID);
        this.suggestions = suggestions;
        createDraftTab();
        createTeamTab();
        createSuggestionsTab();
    }

    //dynamically create a draft tab
    //https://stackoverflow.com/questions/37439213/load-new-tab-dynamically-with-own-fxmls
    private void createDraftTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Draft.fxml"));
            Parent parent = loader.load();
            DraftController draftController = loader.getController();
            draftController.construct(controller);
            draftTab = new Tab("draft");
            draftTab.setClosable(true);
            draftTab.setContent(parent);
            tabPane.getTabs().add(draftTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //dynamically create a team tab
    private void createTeamTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Team.fxml"));
            Parent parent = loader.load();
            TeamController teamController = loader.getController();
            teamController.construct(controller);
            teamTab = new Tab("team");
            teamTab.setClosable(true);
            teamTab.setContent(parent);
            teamTab.setOnSelectionChanged(e -> teamController.showTeam());
            tabPane.getTabs().add(teamTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //dynamically create a suggestions tab
    private void createSuggestionsTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Suggestions.fxml"));
            Parent parent = loader.load();
            SuggestionsController suggestionsController = loader.getController();
            suggestionsController.construct(controller, suggestions);
            suggestionsTab = new Tab("suggestions");
            suggestionsTab.setClosable(true);
            suggestionsTab.setContent(parent);
            suggestionsTab.setOnSelectionChanged(e -> suggestionsController.setUp());
            tabPane.getTabs().add(suggestionsTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
