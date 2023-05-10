package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.View;
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

    public static GUI controller;
    @FXML
    public static Label out;
    @FXML
    public static Label error;
    ClientController clientController;
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
        windowRatio = 16.0/9.0;
        windowWidth = (int) (windowHeight * windowRatio);

        GUIApp.stage = stage;
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/publisherMaterial/Icon50x50px.png"))));
        stage.setOnCloseRequest(e -> System.exit(0));
        setScene("launcher", "My Shelfie");
    }

    /**
     * Loads and sets a scene.
     *
     * @param fxmlFile   the name of the fxml file to load as a scene
     * @param sceneTitle the title to put on the stage
     */
    public void setScene(String fxmlFile, String sceneTitle) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApp.class.getResource("/FXML/" + fxmlFile + ".fxml"));
        Scene scene;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        controller = fxmlLoader.getController();

        if (stage == null) {
            stage = new Stage();
        } else if (stage.getScene() != null) {
            stage.hide();
        }

        stage.setTitle(sceneTitle);
        stage.setScene(scene);
        stage.show();
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
