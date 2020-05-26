package main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    private static Stage root;

    @Override
    public void start(Stage stage) throws Exception {
        root = stage;
        Parent start = FXMLLoader.load(((getClass()
                .getResource("/fxml/Main.fxml"))));
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
