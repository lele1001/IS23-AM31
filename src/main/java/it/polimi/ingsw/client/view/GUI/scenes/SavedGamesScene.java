package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.Exceptions.NotAskedException;
import it.polimi.ingsw.client.Exceptions.NotExistingGameException;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Map;

public class SavedGamesScene extends GUIScene {
    @FXML
    Button yesButton, noButton;
    @FXML
    MenuButton listGames;
    @FXML
    Label errorLabel;
    ClientController clientController;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> checkGameSelection());
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendSavedGames(false, null));
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
     * Sets the scene based on the number of players in the game
     */
    @Override
    public void setPlayers() {
    }

    /**
     * Updates the current player
     *
     * @param player is the curren player
     */
    @Override
    public void updateCurrPlayer(String player) {
    }

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
    }

    /**
     * Prints the Board in the scene
     *
     * @param board is the updated Board
     */
    @Override
    public void updateBoard(ItemCard[][] board) {
    }

    /**
     * Updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    @Override
    public void changeBoard(Map<Integer, ItemCard> tilesToRemove) {
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
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
    }

    /**
     * Prints the CommonGoals and its available score in the scene
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
    }

    /**
     * Prints the PersonalGoal assigned to the player
     *
     * @param newValue is the PersonalGoal name
     */
    @Override
    public void persGoal(String newValue) {
    }

    /**
     * Prints the player's points in the scene
     *
     * @param myPoint are the points of the player
     */
    @Override
    public void printPoints(int myPoint) {
    }

    /**
     * Prints the message in the chat field of the scene
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    @Override
    public void receiveMessage(String sender, String message) {
    }

    /**
     * Prints an error message in the scene
     *
     * @param error is the error message to display
     */
    @Override
    public void printError(String error) {
    }

    private void sendSavedGames(boolean val, String name) {
        try {
            clientController.setSavedGame(val, name);
        } catch (NotAskedException e) {
            errorLabel.setText("Input not recognised... it's not time to set saved games.");
            errorLabel.setVisible(true);
        } catch (NotExistingGameException e) {
            errorLabel.setText("The game you wrote doesn't exist: try again.");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            errorLabel.setText("Impossible to connect to the server.");
            errorLabel.setVisible(true);
        }
    }

    private void checkGameSelection() {
        sendSavedGames(true, listGames.getText());
    }
}
