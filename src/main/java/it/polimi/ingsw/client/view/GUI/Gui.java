package it.polimi.ingsw.client.view.GUI;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class Gui extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setTitle("My First JavaFX App");
        primaryStage.show();
    }

    /**
     * Main method of the class
     */
    public static void main(String[] args) {
        launch(args);
    }
}
