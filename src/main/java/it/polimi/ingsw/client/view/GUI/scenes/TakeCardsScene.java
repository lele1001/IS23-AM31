package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.InputController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.commons.ItemCard;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.utils.Position.getColumn;
import static it.polimi.ingsw.utils.Position.getRow;

/**
 * Defines the controller for the TakeCardsScene
 */
public class TakeCardsScene extends GUIScene {
    @FXML
    private TextField writtenMessage;
    @FXML
    private MenuButton destinationMenu;
    @FXML
    private GridPane boardPane, comGoals, bookshelfPane, persGoal, youSelectedThis, score_0, score_1, winnerScore;
    @FXML
    private Label yourPoints;
    @FXML
    private Button selectTiles, undoSelection, sendMessage, exitButton;
    @FXML
    private TextArea chatHistory;
    @FXML
    private Button helpButton;
    private ArrayList<Integer> selectedTiles;
    private InputController inputController;

    /**
     * Initialize the takeCardsScene
     *
     * @param clientController created for the GUI app
     */
    @Override
    public void initialize(ClientController clientController) {
        super.initialize(clientController);
        inputController = new InputController(clientController);
        yourPoints.setText("You have 0 points");
        selectedTiles = new ArrayList<>();
        setWinnerPointImage();
        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::remove);
        selectTiles.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectTiles());
        undoSelection.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> revert());
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat(inputController, clientController, destinationMenu, writtenMessage));
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
        helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> help());
    }

    /**
     * Put the image of the completed bookshelf point in the defined place
     */
    public void setWinnerPointImage() {
        ImageView scoreImage = new ImageView(GUIResources.getScore("sc01"));
        scoreImage.setFitHeight(55);
        scoreImage.setFitWidth(55);
        winnerScore.add(scoreImage, 0, 0);
    }

    /**
     * Sets the scene based on the number of players in the game
     */
    @Override
    public void setPlayers() {
        setPlayersShared(clientController, destinationMenu, sendMessage);
    }

    /**
     * Updates the current player
     *
     * @param player is the curren player
     */
    @Override
    public void updateCurrPlayer(String player) {
        selectedTiles.clear();
        selectTiles.setDisable(false);
    }


    /**
     * Prints the Board in the scene
     *
     * @param board is the updated Board
     */
    @Override
    public void updateBoard(ItemCard[][] board) {
        boardPane.getChildren().clear();

        for (int i = 0; i < Utils.DIM_BOARD; i++) {
            for (int j = 0; j < Utils.DIM_BOARD; j++) {
                if (board[i][j] != null) {
                    String itemName = board[i][j].getMyItem().toString().toLowerCase();
                    String itemNumber = board[i][j].getMyNum().toString();
                    String myItem = itemName + itemNumber;

                    ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                    tileImage.setPreserveRatio(true);
                    tileImage.setFitHeight(50);
                    tileImage.setFitWidth(50);
                    tileImage.setCursor(Cursor.HAND);

                    boardPane.add(tileImage, j, i);
                }
            }
        }
    }

    /**
     * Updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the positions of the tiles to be removed from the board
     */
    @Override
    public void changeBoard(Integer[] tilesToRemove) {
        for (Integer i : tilesToRemove)
            boardPane.getChildren().removeIf(n -> GridPane.getRowIndex(n) == getRow(i) && GridPane.getColumnIndex(n) == getColumn(i));
    }

    /**
     * Prints the player's Bookshelf in the scene
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        if (clientController.getMyNickname().equals(nickname)) {
            bookshelfPane.getChildren().clear();
            for (int i = 0; i < Utils.BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < Utils.BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(28);
                        tileImage.setFitWidth(28);

                        bookshelfPane.add(tileImage, j, i);
                    }
                }
            }
        }
    }

    /**
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
        if (clientController.getMyNickname().equals(player)) {
            for (Integer i : tilesToAdd.keySet()) {
                ImageView tileImage = new ImageView(GUIResources.getItem(tilesToAdd.get(i).getMyItem().toString().toLowerCase() + tilesToAdd.get(i).getMyNum().toString()));
                tileImage.setPreserveRatio(true);
                tileImage.setFitHeight(28);
                tileImage.setFitWidth(28);

                bookshelfPane.add(tileImage, Position.getColumn(i), Position.getRow(i));
            }
        }
    }

    /**
     * Prints the CommonGoals and its available score in the scene
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        comGoalCreated(playerCommonGoal, score_0, score_1, comGoals, true, 150, 200, 60);
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
        comGoalDone(comGoalDoneID, newValue, score_0, score_1, 60);
    }

    /**
     * Prints the PersonalGoal assigned to the player
     *
     * @param newValue is the PersonalGoal name
     */
    @Override
    public void persGoal(String newValue) {
        ImageView persGoalImage = new ImageView(GUIResources.getPersGoal(newValue));
        persGoalImage.setFitHeight(200);
        persGoalImage.setFitWidth(150);

        persGoal.add(persGoalImage, 0, 0);
    }

    /**
     * Prints the player's points in the scene
     *
     * @param myPoint are the points of the player
     */
    @Override
    public void printPoints(int myPoint) {
        yourPoints.setText("You have " + myPoint + " points");
    }

    /**
     * Prints the message in the chat field of the scene
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    @Override
    public void receiveMessage(String sender, String message) {
        chatHistory.appendText("> " + sender + ": " + message + "\n");
        writtenMessage.setText("");
    }

    /**
     * Checks the selected Tiles from the Board and eventually communicates the change to the server
     */
    private void selectTiles() {
        String[] checkToTake = {"@take"};
        for (Integer sel : selectedTiles) {
            int n = checkToTake.length;
            String[] newel = new String[n + 1];
            newel[n] = sel.toString();
            System.arraycopy(checkToTake, 0, newel, 0, n);
            checkToTake = newel;
        }
        ArrayList<Integer> tilesToTake = inputController.checkTake(checkToTake);
        if (tilesToTake == null) {
            printError("ERROR: wrong selection");
            revert();
            selectTiles.setDisable(false);
        } else {
            clientController.setSelectedTiles(selectedTiles);

            try {
                clientController.selectCard();
                //selectTiles.setDisable(true);
            } catch (Exception e) {
                e.printStackTrace();
                printError("ERROR: server error");
                revert();
                selectTiles.setDisable(false);
            }
        }
        selectedTiles.clear();
        youSelectedThis.getChildren().clear();
    }

    /**
     * Removes the clicked image from the Board pane and puts it in the SelectedTiles pane
     *
     * @param event is the click on the image
     */
    private void remove(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (GridPane.getColumnIndex(clickedNode) != null) {
            int clickedColumn = GridPane.getColumnIndex(clickedNode);
            int clickedRow = GridPane.getRowIndex(clickedNode);

            if (boardPane.getChildren().contains(clickedNode)) {
                ImageView imageView = (ImageView) clickedNode;
                imageView.setCursor(Cursor.DEFAULT);

                if (selectedTiles.size() < 3) {
                    int coord = Position.getNumber(clickedColumn, clickedRow);
                    selectedTiles.add(coord);
                    if (selectedTiles.size() == 3)
                        boardPane.getChildren().forEach(x -> x.setCursor(Cursor.DEFAULT));
                    imageView.setPreserveRatio(true);
                    imageView.setFitWidth(50);
                    imageView.setFitWidth(50);

                    youSelectedThis.add(imageView, selectedTiles.size() - 1, 0);
                }
            }
        }
    }

    /**
     * Removes all the images from the SelectedTiles pane and puts them back in the Board pane
     */
    private void revert() {
        youSelectedThis.getChildren().clear();
        ItemCard[][] board = clientController.getBoard();
        for (Integer i : selectedTiles) {
            ImageView tileImage = new ImageView(GUIResources.getItem(board[Position.getRow(i)][Position.getColumn(i)].getMyItem().toString().toLowerCase() + board[Position.getRow(i)][Position.getColumn(i)].getMyNum().toString()));
            tileImage.setPreserveRatio(true);
            tileImage.setFitHeight(50);
            tileImage.setFitWidth(50);
            tileImage.setCursor(Cursor.HAND);
            boardPane.add(tileImage, Position.getColumn(i), Position.getRow(i));
        }
        if (selectedTiles.size() == 3)
            boardPane.getChildren().forEach(x -> x.setCursor(Cursor.HAND));
        selectedTiles.clear();
    }

    /**
     * Remove the completion point image
     */
    @Override
    public void bookshelfCompleted() {
        winnerScore.getChildren().clear();
    }


    @Override
    public String getChatMessage() {
        return writtenMessage.getText();
    }

    @Override
    public String getChatReceiver() {
        return destinationMenu.getText();
    }

    @Override
    public void putChatMessage(String message) {
        writtenMessage.setText(message);
    }

    @Override
    public void putChatReceiver(String Receiver) {
        destinationMenu.setText(Receiver);
    }
}