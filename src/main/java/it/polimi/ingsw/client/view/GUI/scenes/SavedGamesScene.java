package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.Exceptions.NotAskedException;
import it.polimi.ingsw.client.Exceptions.NotExistingGameException;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Defines the controller for the SavedGamesScene
 */
public class SavedGamesScene extends GUIScene {
    @FXML
    private Button yesButton, noButton, exitButton;
    @FXML
    private MenuButton listGames;

    /**
     * Initialize the savedGamesScene
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
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> checkGameSelection());
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendSavedGames(false, null));
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
    }

    /**
     * Shows the name(s) of the saved game(s)
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    @Override
    public void updateSavedGames(List<String> savedGames) {
        for (String name : savedGames) {
            MenuItem currGameSaved = new MenuItem(name);
            currGameSaved.setId(name);
            listGames.getItems().add(currGameSaved);
            currGameSaved.setOnAction(event -> {
                listGames.setDisable(false);
                listGames.setText(currGameSaved.getText());
            });
        }
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
     * Send to the server if the client wants ot use a saved games o start a new one
     *
     * @param val  if the client wants to use a saved games
     * @param name the name of the game
     */
    private void sendSavedGames(boolean val, String name) {
        try {
            clientController.setSavedGame(val, name);
        } catch (NotAskedException e) {
            printError("Input not recognised... it's not time to set saved games.");
        } catch (NotExistingGameException e) {
            printError("The game you wrote doesn't exist: try again.");
        } catch (Exception e) {
            printError("Impossible to connect to the server.");
        }
    }

    /**
     * Send to the server the game selected
     */
    private void checkGameSelection() {
        sendSavedGames(true, listGames.getText());
    }
}
