package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.controller.ClientController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Defines the start of the Application GUI
 */
public class GUIApp extends Application {
    @FXML
    public static Label out;
    @FXML
    public static Label error;

    /**
     * Loads the stage and starts the launcher.
     *
     * @param stage the stage created by JavaFX
     */
    @Override
    public void start(Stage stage) {
        GUI gui = new GUI(new ClientController());
        gui.gameLogin();
    }

    /**
     * Stop the application
     */
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
