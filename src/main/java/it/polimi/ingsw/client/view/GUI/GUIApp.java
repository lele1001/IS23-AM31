package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.scenes.GUIScene;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class GUIApp extends Application {
    public static Stage stage;
    @FXML
    public static Label out;
    @FXML
    public static Label error;
    private int windowHeight, windowWidth;
    private double windowRatio;
    private ClientController clientController;

    /**
     * Loads the stage and starts the launcher.
     *
     * @param stage the stage created by JavaFX
     */
    @Override
    public void start(Stage stage) {
        this.clientController = new ClientController();

        //Initializes the window dimensions
        windowHeight = 800;
        windowRatio = 16.0 / 9.0;
        windowWidth = (int) (windowHeight * windowRatio);

        GUI gui = new GUI(this.clientController);
        gui.getSceneController().start(stage);
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
