package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.application.Platform;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUI implements View {
    ClientController clientController;
    private final SceneController sceneController;

    /**
     * Initialization of the client profile
     *
     * @param clientController defines the direct contact with all the object containers sen from the server
     */
    public GUI(ClientController clientController) {
        this.clientController = clientController;
        this.clientController.setView(this);
        this.sceneController = new SceneController(this.clientController);
    }

    /**
     * Shows the Login scene
     */
    public void gameLogin() {
        Platform.runLater(this.sceneController::loadLogin);
    }

    /**
     * Implementation for GUI: prints the request to join or not a saved game
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    @Override
    public void askForSavedGame(List<String> savedGames) {
        Platform.runLater(() -> this.sceneController.printNameGames(savedGames));
        Platform.runLater(this.sceneController::loadSavedGames);
    }

    /**
     * Implementation for GUI: prints the request for the number of players
     */
    @Override
    public void printAskPlayerNumber() {
        if (clientController.isSelectNumberOfPlayers()) {
            Platform.runLater(this.sceneController::loadNumberOfPlayer);
        }
    }

    /**
     * Implementation for GUI: prints a waiting message while other players connect
     */
    @Override
    public void printLobby() {
        if (!clientController.isGameStarted()) {
            Platform.runLater(this.sceneController::loadLobby);
        }
    }

    /**
     * Implementation for GUI: sets the scenes based on the number of players in the game
     */
    @Override
    public void printStartGame() {
        if (clientController.isGameStarted()) {
            Platform.runLater(sceneController::setPlayers);
        }
    }

    /**
     * Implementation for GUI: prints the current player each time somebody ends its turn,
     * and, if it is somebody else's turn, shows the NotMyTurn scene
     *
     * @param currPlayer is the nickname of the current player
     */
    @Override
    public void onChangeTurn(String currPlayer) {
        Platform.runLater(() -> sceneController.updateCurrPlayer(currPlayer));

        if (!currPlayer.equals(clientController.getMyNickname())) {
            Platform.runLater(this.sceneController::notMyTurn);
        }
    }

    /**
     * Implementation for GUI: shows the TakeCard scene
     */
    @Override
    public void onSelect() {
        Platform.runLater(this.sceneController::loadTake);
    }

    /**
     * Implementation for GUI: prints the Tiles chosen by the player in the last Phase of the turn
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    @Override
    public void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        Platform.runLater(() -> sceneController.printSelectedTiles(selectedTiles));
    }

    /**
     * Implementation for GUI: shows the PutCards scene
     */
    @Override
    public void onInsert() {
        printSelectedTiles(clientController.getSelectedTiles());
        Platform.runLater(this.sceneController::loadPut);
    }

    /**
     * Implementation for GUI: prints the Board
     *
     * @param board is the updated Board
     */
    @Override
    public void printBoard(ItemCard[][] board) {
        Platform.runLater(() -> sceneController.updateBoard(board));
    }

    /**
     * Implementation for GUI: updates the Board removing the tiles from the given positions.
     *
     * @param tilesToRemove contains board's positions to remove tiles from.
     */
    @Override
    public void changeBoard(Integer[] tilesToRemove) {
        Platform.runLater(() -> sceneController.changeBoard(tilesToRemove));
    }

    /**
     * Implementation for GUI: prints the player's Bookshelf
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    @Override
    public void printBookshelf(ItemCard[][] bookshelf, String nickname) {
        Platform.runLater(() -> sceneController.updateBookshelf(nickname, bookshelf));
    }

    /**
     * Implementation for GUI: updates the player's Bookshelf removing the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
        Platform.runLater(() -> sceneController.changeBookshelf(tilesToAdd, player));
    }

    /**
     * Implementation for GUI: prints the CommonGoal for the game
     *
     * @param playerComGoal contains the CommonGoalID and its available score
     */
    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {
        Platform.runLater(() -> sceneController.comGoal(playerComGoal));
    }

    /**
     * Updates the score made on the CommonGoal on all scenarios
     *
     * @param comGoalDoneID is the CommonGoal reached
     * @param newValue      is the available score of the CommonGoal
     */
    @Override
    public void onCommonGoalDone(int comGoalDoneID, int newValue) {
        Platform.runLater(() -> sceneController.updateCommonGoal(comGoalDoneID, newValue));
    }

    /**
     * Implementation for GUI: prints the player's PersonalGoal
     *
     * @param myPersGoal represents the PersonalGoal of the player
     * @param newValue   is the string that defines the PersonalGoal
     */
    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal, String newValue) {
        sceneController.persGoal(newValue);
    }

    /**
     * Implementation for GUI: prints the player's points
     *
     * @param myPoint are the points of the player
     */
    @Override
    public void printPoints(int myPoint) {
        Platform.runLater(() -> sceneController.printPoints(myPoint));
    }

    /**
     * Implementation for GUI: prints a generic message from the server using the printError method
     *
     * @param message is the string to print
     */
    @Override
    public void print(String message) {
        Platform.runLater(() -> sceneController.printError(message));
    }

    /**
     * Implementation for GUI: prints the message on the chat
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    @Override
    public void chatToMe(String sender, String message) {
        Platform.runLater(() -> sceneController.receiveMessage(sender, message));
    }

    /**
     * Implementation for GUI: prints an error message
     *
     * @param error is the error message to display
     */
    @Override
    public void printError(String error) {
        Platform.runLater(() -> sceneController.printError(error));
    }

    /**
     * Implementation for GUI: closes the view after an error occurs
     */
    @Override
    public void disconnectionError() {
        Platform.runLater(() -> this.sceneController.fatalError("You've been disconnected from server."));
    }

    /**
     * Implementation for GUI: prints the name(s) of the winning player(s)
     *
     * @param winners is(are) the winner(s) of the game
     */
    @Override
    public void printWinners(List<String> winners) {
        Platform.runLater(this.sceneController::endGame);
    }

    /**
     * Implementation for GUI: disconnects the client after a request done by the server
     */
    @Override
    public void disconnectMe() {
        Platform.runLater(() -> this.sceneController.fatalError("You've been disconnected from server."));
    }

    @Override
    public void finalScores(LinkedHashMap<String, Integer> finalScores) {

    }
}
