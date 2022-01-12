package it.unicam.quasylab.gpmonitorgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

     Pane root= FXMLLoader.load(new URL("https","deltasoftware.it","/Unicam/tesi/GuiDesign.fxml"));
     //getClass().getResource("")

        primaryStage.setTitle("GPMonitor");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {

        launch(args);
    }
}
