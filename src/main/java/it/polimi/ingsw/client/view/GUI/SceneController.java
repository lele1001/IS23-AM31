package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.scenes.*;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class SceneController {
    private final ClientController clientController;
    private Stage activeStage = null;

    GUIScene currentController;

    private Map<String, GUIScene> scenesMap = new HashMap<>();

    public SceneController(ClientController clientController) {
        this.clientController = clientController;
        this.createScene(GUIResources.loginFXML, "loginScene");
        this.createScene(GUIResources.errorFXML, "errorScene");
        //this.createScene(GUIResources.putCardsFXML, "putCardsScene");
        //this.createScene(GUIResources.takeCardsFXML, "takeCardsScene");
        //this.createScene(GUIResources.endGameLoseFXML, "endGameScene");
        //this.createScene(GUIResources.notMyTurnFXML, "notMyTurnScene");
        this.createScene(GUIResources.numberOfPlayerFXML, "numberOfPlayersScene");
    }

    public void createScene (String fxml, String name) {
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

/*    private FXMLLoader changeScene(String fxml, ClientController clientController) {
        try {
            FXMLLoader loader =  loadFXML(fxml);;
            Parent root = setRoot(loader);

            this.activeStage.setScene(new Scene(root));
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            this.clientController.onError("Fatal Error: loading fxml file failed");
        }
        return null;
    }

    private FXMLLoader changeStage(String fxml, ClientController clientController) {
*//*        if (this.activeStage != null) {
            this.activeStage.hide();
        }*//*

        try {
            FXMLLoader loader =  loadFXML(fxml);;
            Parent root = setRoot(loader);

            //Stage stage = new Stage();
            this.activeStage.setScene(new Scene(root));
*//*            stage.getIcons().add(GUIResources.icon);
            stage.setTitle("My Shelfie");
            stage.setResizable(false);
            this.activeStage = stage;
            stage.show();*//*

            return loader;
        } catch (IOException e) {
            e.printStackTrace();
            this.clientController.onError("Fatal Error: loading fxml file failed");
        }
        return null;
    }

    private FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneController.class.getClassLoader().getResource(fxml));

        if (loader.getLocation() == null) {
            throw new IOException();
        }

        return loader;
    }

    private Parent setRoot(FXMLLoader loader) throws IOException {
        Parent root = loader.load();
        GUIScene controller = loader.getController();

        if (controller != null) {
            controller.initialize(clientController);
        }

        return root;
    }

    *//**
     * Initializes the first stage and asks the user for server connection
     *//*
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(GUIResources.loginFXML));

            if(loader.getLocation() == null) {
                throw new IOException();
            }

            Parent root = loader.load();
            this.loginScene = loader.getController();
            this.loginScene.initialize(clientController);

            this.activeStage = stage;
            this.activeStage.getIcons().add(GUIResources.icon);
            this.activeStage.setTitle("My Shelfie");
            this.activeStage.setResizable(false);

            this.activeStage.setScene(new Scene(root));
            this.activeStage.setResizable(false);
            this.activeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.clientController.onError("Fatal Error: loading fxml file failed");
        }
    }*/

    /**
     * Asks the user to insert login details
     * (using the current stage)
     */
    public void loadLogin() {
        //this.clearController();
        this.activeStage = new Stage();
        this.activeStage.getIcons().add(GUIResources.icon);
        this.activeStage.setTitle("My Shelfie");
        this.activeStage.setResizable(false);

        this.currentController = this.scenesMap.get("loginScene");
        this.activeStage.setScene(currentController.getMyScene());
        this.activeStage.setResizable(false);
        this.activeStage.show();
        //this.loginScene = this.changeScene(GUIResources.loginFXML, this.clientController).getController();
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
        this.activeStage.setScene(new Scene(root));
    }

    /**
     * Ask the user to insert the number of players of the game
     * (using the current stage)
     */
    public void loadNumberOfPlayer() {
        this.currentController = this.scenesMap.get("numberOfPlayersScene");
        this.activeStage.setScene(currentController.getMyScene());
        /*        this.clearController();
        this.numberOfPlayersScene = Objects.requireNonNull(this.changeScene(GUIResources.numberOfPlayerFXML, this.clientController)).getController();*/
    }

    /**
     * Loads the game board and allows the player to select cards
     * (closing the current stage and showing a new stage)
     */
    public void loadTake() {
        this.currentController = this.scenesMap.get("takeCardsScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

    /**
     * Loads the player's bookshelf and allows him to put cards
     * (closing the current stage and showing a new stage)
     */
    public void loadPut() {
        this.currentController = this.scenesMap.get("putCardsScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

    /**
     * Loads the game board
     * (closing the current stage and showing a new stage)
     */
    public void notMyTurn() {
        this.currentController = this.scenesMap.get("notMyTurnScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

    /**
     * Displays a victory message
     * (using the current stage)
     */
    public void endGameWin() {
        this.currentController = this.scenesMap.get("endGameScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

    /**
     * Displays a lost message
     * (using the current stage)
     */
    public void endGameLose() {
        this.currentController = this.scenesMap.get("endGameScene");
        this.activeStage.setScene(currentController.getMyScene());
    }

/*    private void clearController() {
        this.loginScene = null;
        this.numberOfPlayersScene = null;
        this.notMyTurnScene = null;
        this.takeCardsScene = null;
        this.putCardsScene = null;
        this.endGameScene = null;
        this.errorScene = null;
    }*/


    public void fatalError(String error) {
        activeStage.setScene(this.scenesMap.get("errorScene").getMyScene());
        this.currentController.printError(error);
    }

    public void updateCurrPlayer (String player) {
        for (GUIScene gs : scenesMap.values())
            gs.updateCurrPlayer(player);
    }

    public void updateBoard (ItemCard[][] board) {
        for (GUIScene gs : scenesMap.values())
            gs.updateBoard(board);
    }

    public void updateBookshelf (String nickname, ItemCard[][] bookshelf) {
        for (GUIScene gs : scenesMap.values())
            gs.updateBookshelf(nickname, bookshelf);
    }

    public void printError (String error) {
        this.currentController.printError(error);
    }

    public void updateSelectedTiles (Map<Integer, ItemCard> selectedTiles) {
        this.scenesMap.get("putCardsScene").updateSelectedTiles(selectedTiles);
    }

}
