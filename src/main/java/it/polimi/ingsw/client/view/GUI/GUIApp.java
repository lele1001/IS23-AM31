package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUIApp extends Application {
    @FXML
    public static Label out;
    @FXML
    public static Label error;
    private ClientController clientController;

    /**
     * Loads the stage and starts the launcher.
     *
     * @param stage the stage created by JavaFX
     */
    @Override
    public void start(Stage stage) {
        this.clientController = new ClientController();

        GUI gui = new GUI(this.clientController);
        gui.gameLogin();
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