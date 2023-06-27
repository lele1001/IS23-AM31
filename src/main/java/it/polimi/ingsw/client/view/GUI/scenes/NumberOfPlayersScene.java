package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.exceptions.NotAskedException;
import it.polimi.ingsw.client.exceptions.NotAvailableNameException;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Defines the controller for the NumberOfPlayersScene
 */
public class NumberOfPlayersScene extends GUIScene {
    @FXML
    private TextField playersNum, gameName;
    @FXML
    private Button submitButton, exitButton;

    /**
     * Initialize the numberOfPlayerScene
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
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> submitAction());
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
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
     * Form to select the number of players
     */
    public void submitAction() {
        int players = Integer.parseInt(playersNum.getText());

        if (players <= 0 || players >= 5) {
            printError("Error: insert a valid number of players");
            submitButton.setDisable(false);
        } else {

            if (!checkText(gameName)) {
                printError("Error: insert a valid name for the game");
                submitButton.setDisable(false);
            } else
                try {
                    //submitButton.setDisable(true);
                    clientController.setPlayersNumber(players, gameName.getText());
                } catch (NotAskedException e) {
                    printError("Input not recognised... it's not time to set game's name.");
                } catch (NotAvailableNameException e) {
                    printError("Name you want to set is not available, please try again.");
                } catch (Exception e) {
                    printError("Impossible to connect to the server");
                }
        }
    }

    /**
     * CHeck if the text is empty
     *
     * @param text the game name
     * @return true if not empty, else false
     */
    private boolean checkText(TextField text) {
        return !text.getText().isEmpty();
    }
}