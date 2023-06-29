package it.polimi.ingsw.client.view;

import it.polimi.ingsw.commons.HouseItem;
import it.polimi.ingsw.commons.ItemCard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines all the methods for GUI or CLI
 */
public interface View {
    /**
     * Implementation for CLI and GUI: prints the request to join or not a saved game
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    void askForSavedGame(List<String> savedGames);

    /**
     * Implementation for CLI and GUI: prints the request for the number of players
     */
    void printAskPlayerNumber();

    /**
     * Implementation for CLI and GUI: prints a waiting message while other players connect
     */
    void printLobby();

    /**
     * Implementation for CLI and GUI: prints the message that signals the start of the game
     */
    void printStartGame();

    /**
     * Implementation for CLI and GUI: prints the current player each time somebody ends its turn
     *
     * @param currPlayer is the nickname of the current player
     */
    void onChangeTurn(String currPlayer);

    /**
     * Implementation for CLI and GUI: prints a selection message or scene,
     * then calls the methods to check and communicate the selection to the server
     */
    void onSelect();

    /**
     * Implementation for CLI and GUI: prints the Tiles chosen by the player in the last Phase of the turn
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    void printSelectedTiles(Map<Integer, ItemCard> selectedTiles);

    /**
     * Implementation for CLI and GUI: prints an insertion message or scene,
     * then calls the methods to check and communicate the insertion to the server
     */
    void onInsert();

    /**
     * Implementation for CLI and GUI: prints the Board
     *
     * @param board is the updated Board
     */
    void printBoard(ItemCard[][] board);

    /**
     * Implementation for CLI and GUI: updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    void changeBoard(Integer[] tilesToRemove);

    /**
     * Implementation for CLI and GUI: prints the player's Bookshelf
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    void printBookshelf(ItemCard[][] bookshelf, String nickname);

    /**
     * Implementation for CLI and GUI: updates the player's Bookshelf removing the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player);

    /**
     * Implementation for CLI and GUI: prints the CommonGoal for the game
     *
     * @param playerComGoal contains the CommonGoalID and its available score
     */
    void printCommonGoal(Map<Integer, Integer> playerComGoal);

    /**
     * Implementation for CLI and GUI: updates the available score of a CommonGoal each time it is reached
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    void onCommonGoalDone(int comGoalDoneID, int newValue);

    /**
     * Implementation for CLI and GUI: prints the player's PersonalGoal
     *
     * @param myPersGoal represents the PersonalGoal of the player
     * @param newValue   is the string that defines the PersonalGoal
     */
    void printPersGoal(Map<Integer, HouseItem> myPersGoal, String newValue);

    /**
     * Implementation for CLI and GUI: prints the player's points
     *
     * @param myPoint are the points of the player
     */
    void printPoints(int myPoint);

    /**
     * Implementation for CLI and GUI: prints a generic message from the server
     *
     * @param message is the string to print
     */
    void print(String message);

    /**
     * Implementation for CLI and GUI: prints the message on the chat
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    void chatToMe(String sender, String message);

    /**
     * Implementation for CLI and GUI: prints an error message
     *
     * @param error is the error message to display
     */
    void printError(String error);

    /**
     * Implementation for CLI and GUI: closes the view after an error occurs
     */
    void disconnectionError();

    /**
     * Called when the game has been interrupted because there are too many absents.
     */
    void gameInterrupted();

    /**
     * Implementation for CLI and GUI: prints the name(s) of the winning player(s)
     *
     * @param winners is(are) the winner(s) of the game
     */
    void printWinners(List<String> winners);

    /**
     * Implementation for CLI and GUI: disconnects the client after a request done by the server
     */
    void disconnectMe();

    /**
     * Implementation for CLI and GUI: prints the name of each player and its score
     *
     * @param finalScores contains the players' nicknames and their score
     */
    void finalScores(LinkedHashMap<String, Integer> finalScores);

    /**
     * Implementation for CLI and GUI: Just notifies the client that someone has completed his bookshelf.
     */
    void bookshelfCompleted();
}