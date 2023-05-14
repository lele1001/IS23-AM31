package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.scenes.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private final ClientController clientController;
    private Stage activeStage = null;

    private LoginScene loginScene = null;
    private NumberOfPlayersScene numberOfPlayersScene = null;
    private TakeCardsScene takeCardsScene = null;
    private PutCardsScene putCardsScene = null;
    private NotMyTurnScene notMyTurnScene = null;
    private EndGameScene endGameScene = null;
    private ErrorScene errorScene = null;


    public SceneController(ClientController clientController) {
        this.clientController = clientController;
    }

    private FXMLLoader changeScene(String fxml, ClientController clientController) {
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
        if (this.activeStage != null) {
            this.activeStage.hide();
        }

        try {
            FXMLLoader loader =  loadFXML(fxml);;
            Parent root = setRoot(loader);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(GUIResources.icon);
            stage.setTitle("My Shelfie");
            stage.setResizable(false);
            this.activeStage = stage;
            stage.show();

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

    /**
     * Initialize the first stage and ask the user for server connection
     */
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
    }

    /**
     * Ask the user to insert username and gameID
     * (using the current stage)
     */
    public void loadLogin() {
        this.clearController();
        this.loginScene = this.changeScene(GUIResources.loginFXML, this.clientController).getController();
    }

    /**
     * Show a loading screen waiting for other players connection
     * (using the current stage)
     */
    public void loadLobby() {
        this.clearController();
        this.changeScene(GUIResources.lobbyFXML, this.clientController);
    }

    /**
     * Ask the user to insert the number of players of the game
     * (using the current stage)
     */
    public void loadNumberOfPlayer() {
        this.clearController();
        this.numberOfPlayersScene = this.changeScene(GUIResources.numberOfPlayerFXML, this.clientController).getController();
    }

    /**
     * Load the game board
     * (closing the current stage and show in a new stage)
     */
    public void notMyTurn() {
        this.clearController();
        this.notMyTurnScene = this.changeStage(GUIResources.notMyTurnFXML, this.clientController).getController();
    }

    /**
     * Display victory
     * (using the current stage)
     */
    public void endGameWin() {
        this.clearController();
        this.endGameScene = this.changeScene(GUIResources.endGameWinFXML, this.clientController).getController();
    }

    /**
     * Display lost
     * (using the current stage)
     */
    public void endGameLose() {
        this.clearController();
        this.endGameScene = this.changeScene(GUIResources.endGameLoseFXML, this.clientController).getController();
    }

    private void clearController() {
        this.loginScene = null;
        this.numberOfPlayersScene = null;
        this.notMyTurnScene = null;
        this.takeCardsScene = null;
        this.putCardsScene = null;
        this.endGameScene = null;
        this.errorScene = null;
    }

    public LoginScene getLoginScene() {
        return loginScene;
    }

    public NumberOfPlayersScene getNumberOfPlayersScene() {
        return numberOfPlayersScene;
    }

    public NotMyTurnScene getNotMyTurnScene() {
        return notMyTurnScene;
    }

    public TakeCardsScene getTakeCardsScene() {
        return takeCardsScene;
    }

    public PutCardsScene getPutCardsScene() {
        return putCardsScene;
    }

    public EndGameScene getEndGameScene() {
        return endGameScene;
    }

    public void fatalError(String error) {
        this.clearController();
        this.errorScene = this.changeStage(GUIResources.errorFXML, this.clientController).getController();
        this.errorScene.printError(error);
    }
}
