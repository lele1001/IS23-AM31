package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;

public interface View {
    /**
     * Implementation for Cli and Gui of the printing/update of the board
     *
     * @param board updated by the server
     */
    void printBoard(ItemCard[][] board);

    void printMenu();

    /**
     * Implementation for Cli and Gui of the printing/update of one of the bookshelves
     *
     * @param bookshelves the container of all the bookshelves of all players in the game
     */
    void printBookshelves(Map<String, ItemCard[][]> bookshelves);

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
     */
    void printPersGoal(Map<Integer, HouseItem> myPersGoal);

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
    void printMyBookshelf(ItemCard[][] book);

    void chatToMe(String sender, String message);

    void printAskPlayerNumber();
    void disconnectionError();

    void disconnectMe();

    void printStartGame();
}