package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.scenes.GUIScene;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {
    private final ClientController clientController;
    private Stage activeStage = null;
    GUIScene currentController;
    private final Map<String, GUIScene> scenesMap = new HashMap<>();
    private int windowHeight, windowWidth;


    public SceneController(ClientController clientController) {  //chiedere se devo rinominare queste righe per nome sbagliato ??
        this.clientController = clientController;
        this.createScene(GUIResources.loginFXML, "loginScene");
        this.createScene(GUIResources.errorFXML, "errorScene");
        this.createScene(GUIResources.putCardsFXML, "putCardsScene");
        this.createScene(GUIResources.takeCardsFXML, "takeCardsScene");
        //this.createScene(GUIResources.endGameWinFXML, "endGameWinScene");
        //this.createScene(GUIResources.endGameLoseFXML, "endGameLoseScene");
        this.createScene(GUIResources.notMyTurnFXML, "notMyTurnScene");
        this.createScene(GUIResources.numberOfPlayerFXML, "numberOfPlayersScene");

        //Initializes the window dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowHeight = screenSize.height;
        windowWidth = screenSize.width;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

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
     * Asks the user to insert login details
     * (using the current stage)
     */
    public void loadLogin() {
        this.activeStage = new Stage();
        this.activeStage.getIcons().add(GUIResources.icon);
        this.activeStage.setTitle("My Shelfie");
        this.activeStage.setResizable(false);

        this.currentController = this.scenesMap.get("loginScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
        this.activeStage.show();
    }

    /**
     * Shows a loading screen waiting for other players connection
     * (using the current stage)
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
     * Ask the user to insert the number of players of the game
     * (using the current stage)
     */
    public void loadNumberOfPlayer() {
        this.currentController = this.scenesMap.get("numberOfPlayersScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

    /**
     * Loads the game board and allows the player to select cards
     * (closing the current stage and showing a new stage)
     */
    public void loadTake() {
        this.currentController = this.scenesMap.get("takeCardsScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(true);
    }

    /**
     * Loads the player's bookshelf and allows him to put cards
     * (closing the current stage and showing a new stage)
     */
    public void loadPut() {
        this.currentController = this.scenesMap.get("putCardsScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(true);
    }

    /**
     * Loads the game board
     * (closing the current stage and showing a new stage)
     */
    public void notMyTurn() {
        this.currentController = this.scenesMap.get("notMyTurnScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(true);
    }

    /**
     * Displays a victory message
     * (using the current stage)
     */
    public void endGameWin() {
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneController.class.getClassLoader().getResource(GUIResources.endGameWinFXML));

            if (loader.getLocation() == null) {
                System.out.println("Not possible to set winner scene.");
            }

            root = loader.load();
        } catch (IOException e) {
            System.out.println("Winner scene's error.");
            return;
        }

        this.activeStage.setScene(new Scene(root));
    }

    /**
     * Displays a lost message
     * (using the current stage)
     */
    public void endGameLose() {
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneController.class.getClassLoader().getResource(GUIResources.endGameLoseFXML));

            if (loader.getLocation() == null) {
                System.out.println("Not possible to set loser scene.");
            }

            root = loader.load();
        } catch (IOException e) {
            System.out.println("Loser scene's error.");
            return;
        }

        this.activeStage.setScene(new Scene(root));
    }

    public void fatalError(String error) {
        activeStage.setScene(this.scenesMap.get("errorScene").getMyScene());
        this.currentController.printError(error);
    }

    public void updateCurrPlayer(String player) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateCurrPlayer(player);
        }
    }

    public void updateBoard(ItemCard[][] board) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateBoard(board);
        }

    }

    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        for (GUIScene gs : scenesMap.values()) {
            gs.updateBookshelf(nickname, bookshelf);
        }
    }

    public void printError(String error) {
        this.currentController.printError(error);
    }

    public void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        this.scenesMap.get("putCardsScene").updateSelectedTiles(selectedTiles);
    }

    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        for (GUIScene gs : scenesMap.values()) {
            gs.comGoal(playerCommonGoal);
        }
    }

    public void persGoal(String newValue) {
        for(GUIScene gs : scenesMap.values()){
            gs.persGoal(newValue);
        }
    }
}