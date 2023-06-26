package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class GUIScene {
    private Scene myScene;
    private Alert alert;
    private boolean helpSceneOn = false;
    protected ClientController clientController;

    /**
     * Initialize the scene
     *
     * @param clientController created for the GUI app
     */
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * @return the scene called
     */
    public Scene getMyScene() {
        return myScene;
    }

    /**
     * Set the scene
     *
     * @param myScene the scene to set
     */
    public void setMyScene(Scene myScene) {
        this.myScene = myScene;
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    public abstract void bindEvents();

    /**
     * Shows the name(s) of the saved game(s)
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    public void updateSavedGames(List<String> savedGames) {

    }

    /**
     * Sets the scene based on the number of players in the game
     */
    public void setPlayers() {

    }

    /**
     * A shared method called by different scenes to initialize chat field.
     *
     * @param clientController: a reference to the client controller.
     * @param destinationMenu:  the button that allows player to select the recipient of the chat message.
     * @param sendMessage:      the button that allows player to send the message.
     */
    protected void setPlayersShared(ClientController clientController, MenuButton destinationMenu, Button sendMessage) {
        ArrayList<String> players = new ArrayList<>(clientController.getPlayersBookshelves().keySet());

        MenuItem msgEverybody = new MenuItem("everybody");
        msgEverybody.setId("msgEverybody");
        destinationMenu.getItems().add(msgEverybody);
        msgEverybody.setOnAction(event -> {
            destinationMenu.setDisable(false);
            sendMessage.setDisable(false);
            destinationMenu.setText(msgEverybody.getText());
        });

        for (String player : players) {
            if (!player.equals(clientController.getMyNickname())) {
                MenuItem msgPlayer = new MenuItem(player);
                msgPlayer.setId("msgTo" + player);
                destinationMenu.getItems().add(msgPlayer);

                msgPlayer.setOnAction(event -> {
                    destinationMenu.setDisable(false);
                    sendMessage.setDisable(false);
                    destinationMenu.setText(msgPlayer.getText());
                });
            }
        }
    }

    /**
     * Updates the current player
     *
     * @param player is the curren player
     */
    public void updateCurrPlayer(String player) {

    }

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {

    }

    /**
     * Prints the Board in the scene
     *
     * @param board is the updated Board
     */
    public void updateBoard(ItemCard[][] board) {

    }

    /**
     * Updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    public void changeBoard(Integer[] tilesToRemove) {

    }

    /**
     * Prints the player's Bookshelf in the scene
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

    }

    /**
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {

    }

    public void bookshelfCompleted() {

    }

    /**
     * Prints the CommonGoals and its available score in the scene
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {

    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    public void updateCommonGoal(int comGoalDoneID, int newValue) {

    }

    /**
     * A protected method shared by different scenes to update common goals' scores.
     *
     * @param comGoalDoneID:    the id ot the common goal to be updated.
     * @param newValue:         the remained score.
     * @param score_0:          the grid pane with the first common goal's score.
     * @param score_1:          the grid pane with the second common goal's score.
     * @param height:           the height of the score image.
     */
    protected void comGoalDone(int comGoalDoneID, int newValue, GridPane score_0, GridPane score_1, int height) {
        int n = 0;
        for (Integer cgNum : clientController.getPlayerComGoal().keySet()) {
            if (cgNum == comGoalDoneID) {
                ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + newValue));
                scoreImage.setFitHeight(height);
                scoreImage.setFitWidth(height);

                if (n == 0) {
                    score_0.getChildren().remove(0);
                    score_0.add(scoreImage, 0, 0);
                } else {
                    score_1.getChildren().remove(0);
                    score_1.add(scoreImage, 0, 0);
                }
            }

            n++;
        }
    }

    /**
     * A protected method shared by different scenes to create common goals' cards.
     *
     * @param playerCommonGoal: the common goals for the game.
     * @param score_0:          the grid pane for the first common goal's score.
     * @param score_1:          the grid pane for the second common goal's score.
     * @param comGoals:         the grid pane for the common goals' cards.
     * @param horizontal:       a boolean that is true if the scene has common goals cards in horizontal, false anyway.
     * @param height:           the height of the common goal image.
     * @param width:            thr width of the common goal image.
     * @param imgHeight:        the height of the score image.
     */
    protected void comGoalCreated(Map<Integer, Integer> playerCommonGoal, GridPane score_0, GridPane score_1, GridPane comGoals, boolean horizontal, int height, int width, int imgHeight) {
        int n = 0;
        for (Integer i : playerCommonGoal.keySet()) {
            ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + playerCommonGoal.get(i)));
            scoreImage.setFitHeight(imgHeight);
            scoreImage.setFitWidth(imgHeight);

            if (n == 0) {
                score_0.add(scoreImage, 0, 0);
                score_0.setOnMouseClicked(mouseEvent -> comGoalInfo(i));
                score_0.setOnMouseMoved(mouseEvent -> score_0.setCursor(Cursor.HAND));
            } else {
                score_1.add(scoreImage, 0, 0);
                score_1.setOnMouseClicked(mouseEvent -> comGoalInfo(i));
                score_1.setOnMouseMoved(mouseEvent -> score_1.setCursor(Cursor.HAND));
            }

            String cgNum = i.toString();
            if (i < 10) {
                cgNum = "0" + cgNum;
            }

            ImageView comGoalImage = new ImageView(GUIResources.getComGoal("cg" + cgNum));
            comGoalImage.setFitHeight(height);
            comGoalImage.setFitWidth(width);
            comGoalImage.setPickOnBounds(true);
            comGoalImage.setOnMouseClicked(mouseEvent -> comGoalInfo(i));
            comGoalImage.setOnMouseMoved(mouseEvent -> comGoalImage.setCursor(Cursor.HAND));

            if (horizontal)
                comGoals.add(comGoalImage, n, 0);
            else
                comGoals.add(comGoalImage, 0, n);
            n++;
        }
    }

    /**
     * Prints the PersonalGoal assigned to the player
     *
     * @param newValue is the PersonalGoal name
     */
    public void persGoal(String newValue) {

    }

    /**
     * A method shared by different scenes to send chat's messages.
     *
     * @param inputController:  a reference to the input controller.
     * @param clientController: a reference to the client controller.
     * @param destinationMenu:  the button that allows players to select the recipient of the message.
     * @param writtenMessage:   the text field that contains the message.
     */
    protected void sendChat(InputController inputController, ClientController clientController, MenuButton destinationMenu, TextField writtenMessage) {
        String[] checkChatMessage = {"@chat", destinationMenu.getText(), writtenMessage.getText()};
        if (inputController.checkChat(checkChatMessage) != 0) {
            String destination = checkChatMessage[1];
            String message = writtenMessage.getText();

            if (destination.equalsIgnoreCase("Everybody")) {
                try {
                    clientController.chatToAll(message);
                } catch (Exception e) {
                    printError("ERRROR: server error");
                }
            } else {
                try {
                    clientController.chatToPlayer(destination, message);
                } catch (Exception e) {
                    printError("ERROR: server error");
                }
            }

            try {
                clientController.chatToMe("you", message);
            } catch (Exception e) {
                printError("ERROR: server error");
            }
        }
    }

    /**
     * Prints the player's points in the scene
     *
     * @param myPoint are the points of the player
     */
    public void printPoints(int myPoint) {

    }

    /**
     * Prints the message in the chat field of the scene
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    public void receiveMessage(String sender, String message) {

    }

    /**
     * Prints an error message in the scene
     *
     * @param error is the error message to display
     */
    public void printError(String error) {
        if (this.alert == null) {
            this.alert = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(GUIResources.icon);
            alert.setTitle("MyShelfie");
            alert.setHeaderText("Attention!");
            alert.setContentText(error);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK || alert.getResult() == ButtonType.CLOSE) {
                this.alert = null;
            }
        } else {
            alert.setHeight(alert.getHeight() + 30);
            alert.setContentText(alert.getContentText() + "\n" + error);
        }
    }

    /**
     * Displays the final ranking of the game, showing the score of each player
     *
     * @param finalScores contains the players' nicknames and their score
     */
    public void finalScores(LinkedHashMap<String, Integer> finalScores) {

    }

    /**
     * Called in all the scenes when the player clicks on close button, asks him if he wants to be disconnected from the game and closes the connection in case of confirmation.
     *
     * @param clientController: a reference to the client controller.
     */
    public void closeGame(ClientController clientController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(GUIResources.icon);
        alert.setTitle("MyShelfie");
        alert.setHeaderText("You're about to exit the program...");
        alert.setContentText("Are you sure?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            clientController.disconnectMe();
            System.out.println("exit");
            System.exit(1);
        }
    }

    /**
     * Called when the user clicks on "help" button in TakeCardsScene and NotMyTurnScene, to show a new window with game's instructions.
     */
    protected void help() {
        if (!helpSceneOn) {
            helpSceneOn = true;
            Stage stage = new Stage();
            stage.getIcons().add(GUIResources.icon);
            stage.setTitle("My Shelfie");
            stage.setResizable(false);
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(TakeCardsScene.class.getClassLoader().getResource(GUIResources.helpFXML));

                if (loader.getLocation() == null) {
                    System.out.println("Not possible to set " + GUIResources.helpFXML + " scene.");
                }

                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest(windowEvent -> helpSceneOn = false);
            } catch (IOException e) {
                printError("Unable to load help scene.");
            }
        }
    }

    /**
     * Called on mouse clicked on a common goal to show its details.
     * @param comGoalID: the id of the common goal to be explained.
     */
    protected void comGoalInfo(int comGoalID) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(GUIResources.icon);
        alert.setTitle("MyShelfie");
        alert.setHeaderText("Common Goal n. " + comGoalID);
        alert.setContentText(Utils.comGoalDescription.valueOf("comGoal" + comGoalID).getDescription());
        alert.showAndWait();
    }
}
