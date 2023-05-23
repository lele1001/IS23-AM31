package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.List;
import java.util.Map;

public interface View {
    void onSelect();

    void onInsert();

    void onCommonGoalDone(int comGoalDoneID, int newValue);

    void onChangeTurn(String currPlayer);

    /**
     * Implementation for Cli and Gui of the printing/update of the board
     *
     * @param board updated by the server
     */
    void printBoard(ItemCard[][] board);


    void askForSavedGame(List<String> savedGames);


    /**
     * Implementation for Cli and Gui of the printing of an error message
     *
     * @param error message to display to the client caused by an error
     */
    void printError(String error);

    /**
     * Implementation for Cli and Gui of the printing of the ComGoal for the game
     *
     * @param playerComGoal Map in which are present the typology of ComGoal for the game
     */
    void printCommonGoal(Map<Integer, Integer> playerComGoal);

    /**
     * Implementation for Cli and Gui of the printing of the player's points
     *
     * @param myPoint points of the player
     */
    void printPoints(int myPoint);

    /**
     * Implementation for Cli and Gui of the printing of the player's personal goal
     *
     * @param myPersGoal Personal goal of the player
     * @param newValue   String that defines the PersonalGoal
     */
    void printPersGoal(Map<Integer, HouseItem> myPersGoal, String newValue);

    /**
     * Implementation for Cli and Gui of the printing of the Tiles chosen by the player in the last Phase of the turn
     *
     * @param selectedTiles Tiles selected by the player
     */
    void printSelectedTiles(Map<Integer, ItemCard> selectedTiles);

    /**
     * Implementation for Cli and Gui of the printing if it is the player's turn
     *
     * @param yourTurn String to print
     */
    void print(String yourTurn);

    /**
     * Implementation for Cli and Gui of the printing/update of the player's bookshelf
     *
     * @param book player's bookshelf
     */
    void printBookshelf(ItemCard[][] book, String nickname);

    /**
     * Implementation for Cli and Gui of the printing of the message sent by the sender
     *
     * @param sender  the player that has sent the message
     * @param message The message sent
     */
    void chatToMe(String sender, String message);

    /**
     * Implementation for Cli and Gui of the printing of the request for the number of players he wants in the game
     */
    void printAskPlayerNumber();

    /**
     * Implementation for Cli and Gui of an error occurred, and the consequent closure of the view
     */
    void disconnectionError();

    /**
     * Implementation for Cli and Gui of a request done by the server to disconnect the client
     */
    void disconnectMe();

    /**
     * Implementation for Cli and Gui of the printing of the message that signals the start of the game
     */
    void printStartGame();

    /**
     * Implementation for Cli and Gui of the printing the name(s) of the winning player(s)
     *
     * @param winners the winner(s) of the game
     */
    void printWinners(List<String> winners);

    void printLobby();
}