package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Gui;

import java.io.IOException;

public abstract class GodController {

    public void goToWelcome() {
        confirm();
        Scene main;
        try {
            Parent parent = new FXMLLoader(getClass().getResource("/fxml/Welcome.fxml")).load();
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

    private void confirm() {
        //TODO: make a popup window confirming they want to leave
    }

    public void exit() {
        System.exit(0);
    }
}
