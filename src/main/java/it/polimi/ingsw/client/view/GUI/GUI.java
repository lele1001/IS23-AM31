package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;

public class GUI implements View {
    ClientController clientController;
    private final SceneController sceneController;

    public GUI(ClientController clientController) {
        this.clientController = clientController;
        this.clientController.setView(this);
        this.sceneController = new SceneController(this.clientController);
    }

    public SceneController getSceneController() {
        return this.sceneController;
    }

    public void gameLogin() {
        Platform.runLater(this.sceneController::loadLogin);
    }

    @Override
    public void onSelect() {
        Platform.runLater(this.sceneController::loadTake);
    }

    @Override
    public void onInsert() {
        printSelectedTiles(clientController.getSelectedTiles());
        Platform.runLater(this.sceneController::loadPut);
    }

    /**
     * Implementation for Cli and Gui of the printing/update of the board
     *
     * @param board updated by the server
     */
    @Override
    public void printBoard(ItemCard[][] board) {
        Platform.runLater(() -> sceneController.updateBoard(board));
    }

    /**
     * Implementation for Cli and Gui of the printing of an error message
     *
     * @param error message to display to the client caused by an error
     */
    @Override
    public void printError(String error) {
        Platform.runLater(() -> sceneController.printError(error));
    }

    /**
     * Implementation for Cli and Gui of the printing of the ComGoal for the game
     *
     * @param playerComGoal Map in which are present the typology of ComGoal for the game
     */
    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {
        Platform.runLater(() -> sceneController.comGoal(playerComGoal));
    }

    @Override
    public void onCommonGoalDone(int comGoalDoneID, int newValue) {
        // Aggiorna il punteggio del comGoal fatto su tutti gli scenari
    }

    @Override
    public void onChangeTurn(String currPlayer) {
        Platform.runLater(() -> sceneController.updateCurrPlayer(currPlayer));

        if (!currPlayer.equals(clientController.getMyNickname())) {
            Platform.runLater(this.sceneController::notMyTurn);
        }
    }

    /**
     * Implementation for Cli and Gui of the printing of the player's points
     *
     * @param myPoint points of the player
     */
    @Override
    public void printPoints(int myPoint) {
        Platform.runLater(() -> sceneController.printPoints(myPoint));
    }

    /**
     * Implementation for Cli and Gui of the printing of the player's personal goal
     *
     * @param myPersGoal Personal goal of the player
     * @param newValue   String that defines the PersonalGoal
     */
    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal, String newValue) {
        sceneController.persGoal(newValue);
    }

    /**
     * Implementation for Cli and Gui of the printing of the Tiles chosen by the player in the last Phase of the turn
     *
     * @param selectedTiles Tiles selected by the player
     */
    @Override
    public void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        Platform.runLater(() -> sceneController.printSelectedTiles(selectedTiles));
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
     * @param nickname of the player
     */
    @Override
    public void printBookshelf(ItemCard[][] book, String nickname) {
        Platform.runLater(() -> sceneController.updateBookshelf(nickname, book));
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
        if (clientController.isSelectNumberOfPlayers()) {
            Platform.runLater(this.sceneController::loadNumberOfPlayer);
        }
    }

    /**
     * Implementation for Cli and Gui of an error occurred, and the consequent closure of the view
     */
    @Override
    public void disconnectionError() {
        Platform.runLater(() -> this.sceneController.fatalError("You've been disconnected from server."));
    }

    /**
     * Implementation for Cli and Gui of a request done by the server to disconnect the client
     */
    @Override
    public void disconnectMe() {
        Platform.runLater(() -> this.sceneController.fatalError("You've been disconnected from server."));
    }

    /**
     * Implementation for Cli and Gui of the printing of the message that signals the start of the game
     */
    @Override
    public void printStartGame() {
        if (clientController.isGameStarted()) {
            int playersNumber = clientController.getPlayersBookshelves().size();
            Platform.runLater(() -> sceneController.setPlayers(playersNumber));
        }
    }

    /**
     * Implementation for Cli and Gui of the printing the name(s) of the winning player(s)
     *
     * @param winners the winner(s) of the game
     */
    @Override
    public void printWinners(List<String> winners) {
        if (winners.contains(clientController.getMyNickname())) {
            Platform.runLater(this.sceneController::endGameWin);
        } else {
            Platform.runLater(this.sceneController::endGameLose);
        }
    }

    @Override
    public void printLobby() {
        if (!clientController.isGameStarted()) {
            Platform.runLater(this.sceneController::loadLobby);
        }
    }

    @Override
    public void changeBoard(Map<Integer, ItemCard> tilesToRemove) {

    }

    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd,String player) {

    }

    @Override
    public void askForSavedGame(List<String> savedGames) {

    }
}
