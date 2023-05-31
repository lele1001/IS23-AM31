package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.scene.Scene;

import java.util.List;
import java.util.Map;

public abstract class GUIScene {
    private Scene myScene;

    public abstract void initialize(ClientController clientController);

    public Scene getMyScene() {
        return myScene;
    }

    public void setMyScene(Scene myScene) {
        this.myScene = myScene;
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    public abstract void bindEvents();

    /**
     * Shows the name(s) of the saved game(s)
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    public abstract void updateSavedGames(List<String> savedGames);

    /**
     * Sets the scene based on the number of players in the game
     */
    public abstract void setPlayers();

    /**
     * Updates the current player
     *
     * @param player is the curren player
     */
    public abstract void updateCurrPlayer(String player);

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    public abstract void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles);

    /**
     * Prints the Board in the scene
     *
     * @param board is the updated Board
     */
    public abstract void updateBoard(ItemCard[][] board);

    /**
     * Updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    public abstract void changeBoard(Integer[] tilesToRemove);

    /**
     * Prints the player's Bookshelf in the scene
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    public abstract void updateBookshelf(String nickname, ItemCard[][] bookshelf);

    /**
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    public abstract void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player);

    /**
     * Prints the CommonGoals and its available score in the scene
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    public abstract void comGoal(Map<Integer, Integer> playerCommonGoal);

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    public abstract void updateCommonGoal(int comGoalDoneID, int newValue);

    /**
     * Prints the PersonalGoal assigned to the player
     *
     * @param newValue is the PersonalGoal name
     */
    public abstract void persGoal(String newValue);

    /**
     * Prints the player's points in the scene
     *
     * @param myPoint are the points of the player
     */
    public abstract void printPoints(int myPoint);

    /**
     * Prints the message in the chat field of the scene
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    public abstract void receiveMessage(String sender, String message);

    /**
     * Prints an error message in the scene
     *
     * @param error is the error message to display
     */
    public abstract void printError(String error);
}
