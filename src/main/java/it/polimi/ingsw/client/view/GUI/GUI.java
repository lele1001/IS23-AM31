package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.List;
import java.util.Map;

public class GUI implements View {
    ClientController clientController;
    public GUI(ClientController clientController) {
       this.clientController = clientController;
    }
    /**
     * Implementation for Cli and Gui of the printing/update of the board
     *
     * @param board updated by the server
     */
    @Override
    public void printBoard(ItemCard[][] board) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the menu
     */
    @Override
    public void printMenu() {

    }

    /**
     * Implementation for Cli and Gui of the printing/update of one of the bookshelves
     *
     * @param bookshelves the container of all the bookshelves of all players in the game
     */
    @Override
    public void printBookshelves(Map<String, ItemCard[][]> bookshelves) {

    }

    /**
     * Implementation for Cli and Gui of the printing of an error message
     *
     * @param error message to display to the client caused by an error
     */
    @Override
    public void printError(String error) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the ComGoal for the game
     *
     * @param playerComGoal Map in which are present the typology of ComGoal for the game
     */
    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the player's points
     *
     * @param myPoint points of the player
     */
    @Override
    public void printPoints(int myPoint) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the player's personal goal
     *
     * @param myPersGoal Personal goal of the player
     */
    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the Tiles chosen by the player in the last Phase of the turn
     *
     * @param selectedTiles Tiles selected by the player
     */
    @Override
    public void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {

    }

    /**
     * Implementation for Cli and Gui of the printing if it is the player's turn
     *
     * @param yourTurn String to print
     */
    @Override
    public void print(String yourTurn) {

    }

    /**
     * Implementation for Cli and Gui of the printing/update of the player's bookshelf
     *
     * @param book     player's bookshelf
     * @param nickname
     */
    @Override
    public void printBookshelf(ItemCard[][] book, String nickname) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the message sent by the sender
     *
     * @param sender  the player that has sent the message
     * @param message The message sent
     */
    @Override
    public void chatToMe(String sender, String message) {

    }

    /**
     * Implementation for Cli and Gui of the printing of the request for the number of players he wants in the game
     */
    @Override
    public void printAskPlayerNumber() {

    }

    /**
     * Implementation for Cli and Gui of an error occurred, and the consequent closure of the view
     */
    @Override
    public void disconnectionError() {

    }

    /**
     * Implementation for Cli and Gui of a request done by the server to disconnect the client
     */
    @Override
    public void disconnectMe() {

    }

    /**
     * Implementation for Cli and Gui of the printing of the message that signals the start of the game
     */
    @Override
    public void printStartGame() {

    }

    /**
     * Implementation for Cli and Gui of the printing the name(s) of the winning player(s)
     *
     * @param winners the winner(s) of the game
     */
    @Override
    public void printWinners(List<String> winners) {

    }
}
