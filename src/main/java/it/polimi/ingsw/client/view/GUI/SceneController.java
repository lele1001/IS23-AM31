package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.GUI.scenes.GUIScene;
import it.polimi.ingsw.commons.ItemCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines the controller for all the GUI Scene
 */
public class SceneController {
    private final ClientController clientController;
    private Stage activeStage = null;
    private GUIScene currentController;
    private final Map<String, GUIScene> scenesMap = new HashMap<>();

    /**
     * Creates the Scene controller and all the controller for the Scenes
     *
     * @param clientController the Client controller
     */
    public SceneController(ClientController clientController) {
        this.clientController = clientController;
        this.createScene(GUIResources.loginFXML, "loginScene");
        this.createScene(GUIResources.askSavedGamesFXML, "askSavedGamesScene");
        this.createScene(GUIResources.numberOfPlayerFXML, "numberOfPlayersScene");
        this.createScene(GUIResources.notMyTurnFXML, "notMyTurnScene");
        this.createScene(GUIResources.takeCardsFXML, "takeCardsScene");
        this.createScene(GUIResources.putCardsFXML, "putCardsScene");
        this.createScene(GUIResources.errorFXML, "errorScene");
        this.createScene(GUIResources.endGameFXML, "endGameScene");
    }

    /**
     * Creates a scene and saves it in the scenesMap
     *
     * @param fxml is the location of the FXML file of the scene
     * @param name is the name of the scene
     */
    public void createScene(String fxml, String name) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneController.class.getClassLoader().getResource(fxml));

            if (loader.getLocation() == null) {
                System.out.println("Not possible to set " + fxml + " scene.");
            }
            Parent root = loader.load();
            GUIScene guiScene = loader.getController();
            guiScene.initialize(this.clientController);
            guiScene.setMyScene(new Scene(root));
            this.scenesMap.put(name, guiScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a login form to connect to the game
     */
    public void loadLogin() {
        this.activeStage = new Stage();
        this.activeStage.getIcons().add(GUIResources.icon);
        this.activeStage.setTitle("My Shelfie");
        this.activeStage.setResizable(false);

        this.activeStage.setOnCloseRequest(e -> {
            GUIScene.closeGame(clientController);
            e.consume();
        });

        this.currentController = this.scenesMap.get("loginScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.show();
    }

    /**
     * Prints the name(s) of the saved game(s) in each scene that shows them
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    public void printNameGames(List<String> savedGames) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateSavedGames(savedGames);
        }
    }

    /**
     * Allows the user to join or not a saved game, and eventually to choose which one
     */
    public void loadSavedGames() {
        this.currentController = this.scenesMap.get("askSavedGamesScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
    }

    /**
     * Ask the user to insert the number of players of the game
     * (using the current stage)
     */
    public void loadNumberOfPlayer() {
        this.currentController = this.scenesMap.get("numberOfPlayersScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

    /**
     * Shows a loading screen while waiting for other players to connect
     */
    public void loadLobby() {
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneController.class.getClassLoader().getResource(GUIResources.lobbyFXML));

            if (loader.getLocation() == null) {
                System.out.println("Not possible to set " + "lobby" + " scene.");
            }
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Lobby's error.");
            return;
        }

        this.activeStage.setTitle("My Shelfie | " + clientController.getMyNickname());
        this.activeStage.setScene(new Scene(root));
    }

    /**
     * Sets the scenes based on the number of players in the game
     */
    public void setPlayers() {
        for (GUIScene gs : scenesMap.values()) {
            gs.setPlayers();
        }
    }

    /**
     * Updates the name of the current player in each scene that shows it
     *
     * @param player is the current player
     */
    public void updateCurrPlayer(String player) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateCurrPlayer(player);
        }
    }

    /**
     * Shows the Board and all the Bookshelves
     */
    public void notMyTurn() {
        String message = this.currentController.getChatMessage();
        String receiver = this.currentController.getChatReceiver();
        this.currentController = this.scenesMap.get("notMyTurnScene");
        this.currentController.putChatMessage(message);
        this.currentController.putChatReceiver(receiver);
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
    }

    /**
     * Allows the player to select some ItemCards from the Board
     */
    public void loadTake() {
        String message = this.currentController.getChatMessage();
        String receiver = this.currentController.getChatReceiver();
        this.currentController = this.scenesMap.get("takeCardsScene");
        this.currentController.putChatMessage(message);
        this.currentController.putChatReceiver(receiver);
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
    }

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    public void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateSelectedTiles(selectedTiles);
        }
    }

    /**
     * Allows the player to put the ItemCards in its Bookshelf
     */
    public void loadPut() {
        String message = this.currentController.getChatMessage();
        String receiver = this.currentController.getChatReceiver();
        this.currentController = this.scenesMap.get("putCardsScene");
        this.currentController.putChatMessage(message);
        this.currentController.putChatReceiver(receiver);
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
    }

    /**
     * Prints the Board in each scene that contains it
     *
     * @param board is the updated Board
     */
    public void updateBoard(ItemCard[][] board) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateBoard(board);
        }
    }

    /**
     * Updates the Board removing the tiles from the given positions.
     *
     * @param tilesToRemove contains board's positions to remove tiles from.
     */
    public void changeBoard(Integer[] tilesToRemove) {
        for (GUIScene gs : scenesMap.values()) {
            gs.changeBoard(tilesToRemove);
        }
    }

    /**
     * Prints the player's Bookshelf in each scene that contains it
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateBookshelf(nickname, bookshelf);
        }
    }

    /**
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
        for (GUIScene gs : scenesMap.values()) {
            gs.changeBookshelf(tilesToAdd, player);
        }
    }

    public void bookshelfCompleted() {
        for (GUIScene gs : scenesMap.values()) {
            gs.bookshelfCompleted();
        }
    }

    /**
     * Prints the CommonGoals and its available score in each scene that contains it
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        for (GUIScene gs : scenesMap.values()) {
            gs.comGoal(playerCommonGoal);
        }
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in each scene that contains it
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateCommonGoal(comGoalDoneID, newValue);
        }
    }

    /**
     * Prints the player's PersonalGoal in each scene that contains it
     *
     * @param newValue is the string that defines the PersonalGoal
     */
    public void persGoal(String newValue) {
        for (GUIScene gs : scenesMap.values()) {
            gs.persGoal(newValue);
        }
    }

    /**
     * Prints the player's points in each scene that shows them
     *
     * @param myPoint are the points of the player
     */
    public void printPoints(int myPoint) {
        for (GUIScene gs : scenesMap.values()) {
            gs.printPoints(myPoint);
        }
    }

    /**
     * Prints the message in each scene that contains the chat
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    public void receiveMessage(String sender, String message) {
        for (GUIScene gs : scenesMap.values()) {
            gs.receiveMessage(sender, message);
        }
    }

    /**
     * Prints an error message in each scene that shows it
     *
     * @param error is the error message to display
     */
    public void printError(String error) {
        this.currentController.printError(error);
    }

    /**
     * Shows the error scene
     *
     * @param error is the error that will be printed
     */
    public void fatalError(String error) {
        if (!activeStage.getScene().equals(this.scenesMap.get("endGameScene").getMyScene())) {
            this.currentController = this.scenesMap.get("errorScene");
            activeStage.setScene(currentController.getMyScene());
            this.currentController.printError(error);
        }
    }

    /**
     * Displays the game ranking
     */
    public void endGame() {
        this.currentController = this.scenesMap.get("endGameScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
    }

    /**
     * Displays the final ranking of the game, showing the score of each player
     *
     * @param finalScores contains the players' nicknames and their score
     */
    public void finalScores(LinkedHashMap<String, Integer> finalScores) {
        this.currentController = this.scenesMap.get("endGameScene");
        this.currentController.finalScores(finalScores);
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
    }
}
