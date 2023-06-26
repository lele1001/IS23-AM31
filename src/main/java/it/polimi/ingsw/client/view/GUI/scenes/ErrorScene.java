package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ErrorScene extends GUIScene {
    @FXML
    Label errorDescription, errorArea;
    @FXML
    Button closeButton;

    /**
     * Initialize the errorScene
     *
     * @param clientController created for the GUI app
     */
    @Override
    public void initialize(ClientController clientController) {
        super.initialize(clientController);
        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGameButton());
    }

    /**
     * Prints the player's Bookshelf in the scene
     *
     * @param nickname  is the owner of the Bookshelf
     * @param bookshelf is the player's updated Bookshelf
     */
    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
    }

    /**
     * Prints an error message in the scene
     *
     * @param error is the error message to display
     */
    @Override
    public void printError(String error) {
        errorDescription.setText(error);
        errorDescription.setVisible(true);
    }

    /**
     * Close the GUI instance after disconnecting the client
     */
    private void closeGameButton() {
        clientController.disconnectMe();
        System.exit(1);
    }
}