package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUIApp extends Application {
    public static Stage stage;

    public static SceneHandler controller;
    private GUI gui;
    @FXML
    public static Label out;
    @FXML
    public static Label error;
    private int windowHeight, windowWidth;
    private double windowRatio;


    /**
     * Launches the application.
     */
    public static void run() {
        launch();
    }

    /**
     * Loads the stage and starts the launcher.
     *
     * @param stage the stage created by JavaFX
     */
    @Override
    public void start(Stage stage) {
        //Initializes the window dimensions
        windowHeight = 800;
        windowRatio = 16.0 / 9.0;
        windowWidth = (int) (windowHeight * windowRatio);

        GUIApp.stage = stage;
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/publisherMaterial/Icon50x50px.png"))));
        stage.setOnCloseRequest(e -> System.exit(0));
        gui = new GUI(new ClientController());
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public double getWindowRatio() {
        return windowRatio;
    }

    public double getWindowWidth() {
        return windowWidth;
    }
}
