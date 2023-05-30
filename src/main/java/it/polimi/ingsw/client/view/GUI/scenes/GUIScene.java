package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.scene.Scene;

import java.util.List;
import java.util.Map;

public abstract class GUIScene {
    private Scene myScene;

    public abstract void initialize(ClientController clientController);

    /**
     * Prints the error on the scene
     * @param error is the error to print
     */
    public abstract void printError(String error);

    public abstract void bindEvents();

    public Scene getMyScene() {
        return myScene;
    }

    public void setMyScene(Scene myScene) {
        this.myScene = myScene;
    }

    /**
     * Updates the current player
     * @param player is the curren player
     */
    public abstract void updateCurrPlayer(String player);

    /**
     * Prints the board when it changes
     * @param board is a matrix of itemCards
     */
    public abstract void updateBoard(ItemCard[][] board);

    /**
     * Prints the bookshelf that changed
     * @param nickname is the owner of the bookshelf
     * @param bookshelf is a matrix of itemCards
     */
    public abstract void updateBookshelf(String nickname, ItemCard[][] bookshelf);

    /**
     * Memorizes the Tiles selected from the board
     * @param selectedTiles contains the ItemCard and its position on the board
     */
    public abstract void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles);

    /**
     * Prints the CommonGoal of the game
     * @param playerCommonGoal contains the number of the CommonGoal and its available score
     */
    public abstract void comGoal(Map<Integer, Integer> playerCommonGoal);

    /**
     * Prints the PersonalGoal assigned to the player
     * @param newValue is the PersonalGoal name
     */
    public abstract void persGoal(String newValue);

    /**
     * Prints the points of the player
     * @param myPoint are the points
     */
    public abstract void printPoints(int myPoint);

    /**
     * Creates the chat destination Pane and the TabPane for the bookshelves
     */
    public abstract void setPlayers();

    /**
     * Prints the received chat message
     * @param sender is the player that sent the message
     * @param message is the message to print
     */
    public abstract void receiveMessage(String sender, String message);

    public abstract void updateSavedGames(List<String> savedGames);
}
