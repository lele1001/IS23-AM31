package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GUI implements View {
    ClientController clientController;
    SceneHandler currentScene;
    Parent root;

    //controllers of all scenes
    LoginScene loginScene = new LoginScene();
    TakeCardsScene takeCardsScene = new TakeCardsScene();
    PutCardsScene putCardsScene = new PutCardsScene();
    NotMyTurnScene notMyTurnScene = new NotMyTurnScene();
    EndGameScene endGameScene = new EndGameScene();
    private Stage stage;

    public GUI(ClientController clientController) {
        this.clientController = clientController;

        // Initializes all the game scenes
        loginScene.initialize(clientController);
        takeCardsScene.initialize(clientController);
        putCardsScene.initialize(clientController);
        notMyTurnScene.initialize(clientController);
        endGameScene.initialize(clientController);

        currentScene = loginScene;
        setScene("loginForm", "My Shelfie");

        //new Thread(this::listen).start();
    }

    /**
     * Loads and sets a scene.
     *
     * @param fxmlFile   the name of the fxml file to load as a scene
     * @param sceneTitle the title to put on the stage
     */
    public void setScene(String fxmlFile, String sceneTitle) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApp.class.getResource("/FXML/" + fxmlFile + ".fxml"));

        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        currentScene = fxmlLoader.getController();
        Scene scene = new Scene(root);

        if (stage == null) {
            stage = new Stage();
        } else if (stage.getScene() != null) {
            stage.hide();
        }

        stage.setTitle(sceneTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void listen() {

        if (!clientController.isGameStarted()) {
            synchronized (this) {
                GUIApp.out.setText("Waiting for other players to connect...");
            }
        }
    }

    @Override
    public void onSelect() {
        // Cambio: currentScene = takeCardsScene
    }

    @Override
    public void onInsert() {
        // Cambio: currentScene = putCardsScene
    }

    /**
     * Implementation for Cli and Gui of the printing/update of the board
     *
     * @param board updated by the server
     */
    @Override
    public void printBoard(ItemCard[][] board) {
        // TODO: chiamo updateBoard(ItemCard[][] board) su tutte le scene che la contengono

/*        if (clientController.isMyTurn() && clientController.getPhase() == TurnPhase.SELECTCARDS) {
            takeCardsScene.highlightTiles();
        } else {
            notMyTurnScene.show();
        }*/
    }

    /**
     * Implementation for Cli and Gui of the printing of the menu
     */
    @Override
    public void printMenu() {}

    /**
     * Implementation for Cli and Gui of the printing/update of one of the bookshelves
     *
     * @param bookshelves the container of all the bookshelves of all players in the game
     */
    @Override
    public void printBookshelves(Map<String, ItemCard[][]> bookshelves) {
        // TODO: chiamo updateBookshelves(Map<String, ItemCard[][]> bookshelves) su tutte le scene che le contengono

/*        if (clientController.isMyTurn() && clientController.getPhase() == TurnPhase.INSERTCARDS) {
            putCardsScene.orderTiles();
        } else {
            notMyTurnScene.show();
        }*/
    }

    /**
     * Implementation for Cli and Gui of the printing of an error message
     *
     * @param error message to display to the client caused by an error
     */
    @Override
    public void printError(String error) {
        currentScene.printError(error);
    }

    /**
     * Implementation for Cli and Gui of the printing of the ComGoal for the game
     *
     * @param playerComGoal Map in which are present the typology of ComGoal for the game
     */
    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {
        //Crea i commonGoal e i punteggi su tutti gli scenari che li conterranno
    }

    @Override
    public void onCommonGoalDone(int comGoalDoneID, int newValue) {
        // Aggiorna il punteggio del comGoal fatto su tutti gli scenari
    }

    @Override
    public void onChangeTurn(String currPlayer) {
        // Aggiorna il nome dell'attuale giocatore sugli scenari che lo contengono
        // Se currPlayer non è lui, currrentScene = NotMyTurnScene
        // Se, invece, è lui, non fa nulla perché poi il server chiamerà la askSelect e lo scenario commuterà da lì.
    }

    /**
     * Implementation for Cli and Gui of the printing of the player's points
     *
     * @param myPoint points of the player
     */
    @Override
    public void printPoints(int myPoint) {
        //TODO: chiamo updatePoints(... su tutte le scene che contengono il punteggio
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
     * @param nickname of the player
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
        if (clientController.isSelectNumberOfPlayers()) {
            loginScene.isFirst();
        }
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

    @Override
    public void askForSavedGame(List<String> savedGames) {

    }

}
