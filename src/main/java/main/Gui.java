package main;


import gui.DraftController;
import gui.MainController;
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
        Parent start = FXMLLoader.load(((getClass()
                .getResource("/fxml/Welcome.fxml"))));

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
//        Parent start = loader.load();
//        MainController mainController = loader.getController();
//        mainController.construct(ScoreType.HALF, 10, 5, false);
//        loader.setController(mainController);
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
