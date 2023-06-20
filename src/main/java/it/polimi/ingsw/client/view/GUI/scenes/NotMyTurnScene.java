package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.server.model.Position.getColumn;
import static it.polimi.ingsw.server.model.Position.getRow;

public class NotMyTurnScene extends GUIScene {
    @FXML
    TabPane bookshelvesPane;
    @FXML
    TextArea chatHistory;
    @FXML
    AnchorPane notYourPane;
    @FXML
    GridPane boardPane, comGoals, persGoal, score_0, score_1;
    @FXML
    Label yourPoints, userTurn;
    @FXML
    TextField writtenMessage;
    @FXML
    Button sendMessage, exitButton;
    @FXML
    MenuButton destinationMenu;
    private ClientController clientController;
    private InputController inputController;
    private ArrayList<String> players;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        this.inputController = new InputController(clientController);
        yourPoints.setText("You have 0 points");
        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat(inputController,clientController,destinationMenu,writtenMessage));
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
    }

    /**
     * Sets the scene based on the number of players in the game
     */
    @Override
    public void setPlayers() {
        setPlayersShared(clientController, destinationMenu, sendMessage);

       players = new ArrayList<>(clientController.getPlayersBookshelves().keySet());

        for (int i = 0; i < players.size(); i++) {
            setBookshelf(i);
            //setChat(players.get(i));
        }
    }

    /**
     * Updates the current player
     *
     * @param currPlayer is the curren player
     */
    public void updateCurrPlayer(String currPlayer) {
        if (!clientController.getMyNickname().equals(currPlayer)) {
            userTurn.setText("It is " + currPlayer + "'s turn");
        } else {
            userTurn.setText("It is your turn");
        }
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
                    tileImage.setFitHeight(40);
                    tileImage.setFitWidth(40);

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
        int index = players.indexOf(nickname);
        Tab tabToModify = bookshelvesPane.getTabs().get(index);

        if (tabToModify.getText().equals(nickname)) {
            AnchorPane myAnchor = (AnchorPane) tabToModify.getContent();
            GridPane bookshelfToModify = (GridPane) myAnchor.getChildren().get(1);
            bookshelfToModify.getChildren().clear();

            for (int i = 0; i < Utils.BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < Utils.BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(25);
                        tileImage.setFitWidth(25);

                        bookshelfToModify.add(tileImage, j, i);
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
        int index = players.indexOf(player);
        Tab tabToModify = bookshelvesPane.getTabs().get(index);

        if (tabToModify.getText().equals(player)) {
            AnchorPane myAnchor = (AnchorPane) tabToModify.getContent();
            GridPane bookshelfToModify = (GridPane) myAnchor.getChildren().get(1);

            for (Integer i : tilesToAdd.keySet()) {
                String itemName = tilesToAdd.get(i).getMyItem().toString().toLowerCase();
                String itemNumber = tilesToAdd.get(i).getMyNum().toString();
                String myItem = itemName + itemNumber;

                ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                tileImage.setPreserveRatio(true);
                tileImage.setFitHeight(25);
                tileImage.setFitWidth(25);
                bookshelfToModify.add(tileImage, getColumn(i), getRow(i));
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
        comGoalCreated(playerCommonGoal, score_0, score_1, comGoals, false, 100, 175, 45);
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
        comGoalDone(comGoalDoneID, newValue, score_0, score_1, clientController, 45);
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
     * Initializes the tabPane layout, setting the players' names, and the bookshelf image
     */
    private void setBookshelf(int i) {
        String tabId = "player" + i + "tab";

        GridPane bookshelfGrid = new GridPane();
        bookshelfGrid.setId("bookshelf" + i);
        bookshelfGrid.setFocusTraversable(true);
        bookshelfGrid.setHgap(8.0);
        bookshelfGrid.setVgap(4.0);
        bookshelfGrid.setLayoutX(54.0);
        bookshelfGrid.setLayoutY(41.0);
        bookshelfGrid.setPrefSize(172.0, 181.0);

        for (int c = 0; c < Utils.BOOKSHELF_LENGTH; c++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(40.0);
            bookshelfGrid.getColumnConstraints().add(colConst);
        }

        for (int r = 0; r < Utils.BOOKSHELF_HEIGHT; r++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(40.0);
            bookshelfGrid.getRowConstraints().add(rowConst);
        }

        ImageView bookshelfImage = new ImageView(GUIResources.bookshelfImage);
        bookshelfImage.setFitHeight(232.0);
        bookshelfImage.setFitWidth(228.0);
        bookshelfImage.setLayoutX(25.0);
        bookshelfImage.setLayoutY(25.0);
        bookshelfImage.setPickOnBounds(true);
        bookshelfImage.setPreserveRatio(true);

        AnchorPane bookshelfPane = new AnchorPane();
        bookshelfPane.setId("bookshelf" + i + "Pane");
        bookshelfPane.getChildren().add(bookshelfImage);
        bookshelfPane.getChildren().add(bookshelfGrid);

        Tab playerTab = new Tab(players.get(i), bookshelfPane);
        playerTab.setId(tabId);
        bookshelvesPane.getTabs().add(playerTab);
    }
/*

    private void setChat(String nickname) {
        if (!nickname.equals(clientController.getMyNickname())) {
            MenuItem msgPlayer = new MenuItem(nickname);
            msgPlayer.setId("msgTo" + nickname);
            destinationMenu.getItems().add(msgPlayer);

            msgPlayer.setOnAction(event -> {
                destinationMenu.setDisable(false);
                sendMessage.setDisable(false);
                destinationMenu.setText(msgPlayer.getText());
            });
        }
    }*/


}