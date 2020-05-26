package main;


import gui.DraftController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pojos.ScoreType;

public class Gui extends Application {

    private static Stage root;

    @Override
    public void start(Stage stage) throws Exception {
        root = stage;
//        Parent start = FXMLLoader.load(((getClass()
//                .getResource("/fxml/main.fxml"))));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Draft.fxml"));
        Parent start = loader.load();
        DraftController draftController = loader.getController();
        draftController.construct(ScoreType.HALF, 10, 5, false);
        loader.setController(draftController);
        root.setTitle("Welcome to your Mock Draft!");
        root.setScene(new Scene(start));
        root.show();
    }

    public static Stage getRoot(){
        return root;
    }

    public static void main(String[] args){
        launch(args);
    }
}
