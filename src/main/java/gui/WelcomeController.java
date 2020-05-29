package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Gui;
import pojos.ScoreType;

import java.io.IOException;
import java.util.Objects;

public class WelcomeController extends GodController {

    @FXML
    Label warningLabel;
    @FXML
    RadioButton eightSize, tenSize, twelveSize, standardScore, halfScore, pprScore, yesSuggestion, noSuggestion;
    @FXML
    ToggleGroup sizeGroup, scoreGroup, suggestionGroup;
    @FXML
    TextField userPickText;
    @FXML
    Button exitButton, startButton;
    private int size = 10;
    private ScoreType scoreType = ScoreType.HALF;
    private boolean suggestions = true;

    public void initialize() {
        eightSize.setOnAction(e -> size = 8);
        tenSize.setOnAction(e -> size = 10);
        twelveSize.setOnAction(e -> size = 12);

        standardScore.setOnAction(e -> scoreType = ScoreType.STANDARD);
        halfScore.setOnAction(e -> scoreType = ScoreType.HALF);
        pprScore.setOnAction(e -> scoreType = ScoreType.PPR);

        yesSuggestion.setOnAction(e -> suggestions = true);
        noSuggestion.setOnAction(e -> suggestions = false);

    }

    //go to the draft when they press the start button
    public void start() {
        int userPick = 5;
        try {
            String text = userPickText.getText();
            userPick = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            userPick = 0;
        }
        if (size == 0 || scoreType == null || userPick == 0) {
            warningLabel.setText("set all of the configurations first");
            return;
        }
        Scene draft;
        try {
            //https://stackoverflow.com/questions/30814258/javafx-pass-parameters-while-instantiating-controller-class
            //get the loader so you can load and construct
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            //load the page (this calls initialize)
            Parent parent = loader.load();
            //get the draftController
            MainController mainController = loader.getController();
            //basically just a constructor (needed so we can make the real controller
            mainController.construct(scoreType, size, userPick, suggestions);
            loader.setController(mainController);
            draft = new Scene(parent);
        } catch (IOException e) {
            warningLabel.setText("Something went wrong");
            e.printStackTrace();
            return;
        }
        Stage root = Gui.getRoot();
        root.setTitle("mock draft");
        root.setScene(draft);
        root.show();
    }
}
