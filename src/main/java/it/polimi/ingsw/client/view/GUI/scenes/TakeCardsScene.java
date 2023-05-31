package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;
import it.polimi.ingsw.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.model.Position.getColumn;
import static it.polimi.ingsw.server.model.Position.getRow;


public class TakeCardsScene extends GUIScene {
    @FXML
    TextField writtenMessage;
    @FXML
    MenuButton destinationMenu;
    @FXML
    AnchorPane takeCardsPane;
    @FXML
    GridPane boardPane, comGoals, bookshelfPane, persGoal, youSelectedThis, score_0, score_1;
    @FXML
    Label errorArea, yourPoints;
    @FXML
    Button selectTiles, undoSelection, sendMessage;
    @FXML
    TextArea chatHistory;
    private ClientController clientController;
    private ArrayList<Integer> selectedTiles;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");
        errorArea.setVisible(false);
        selectedTiles = new ArrayList<>();
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
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat());
    }

    /**
     * Shows the name(s) of the saved game(s)
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    @Override
    public void updateSavedGames(List<String> savedGames) {
    }

    /**
     * Sets the scene based on the number of players in the game
     */
    @Override
    public void setPlayers() {
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
    @Override
    public void updateCurrPlayer(String player) {
        selectedTiles.clear();
        selectTiles.setDisable(false);
    }

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
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
                        tileImage.setFitHeight(24);
                        tileImage.setFitWidth(24);

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
                String itemName = tilesToAdd.get(i).getMyItem().toString().toLowerCase();
                String itemNumber = tilesToAdd.get(i).getMyNum().toString();
                String myItem = itemName + itemNumber;

                ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                tileImage.setPreserveRatio(true);
                tileImage.setFitHeight(24);
                tileImage.setFitWidth(24);

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
        int n = 0;
        for (Integer i : playerCommonGoal.keySet()) {
            ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + playerCommonGoal.get(i).toString()));
            scoreImage.setFitHeight(60);
            scoreImage.setFitWidth(60);

            if (n == 0) {
                score_0.add(scoreImage, 0, 0);
            } else {
                score_1.add(scoreImage, 0, 0);
            }

            String cgNum = i.toString();
            if (i < 10) {
                cgNum = "0" + cgNum;
            }

            ImageView comGoalImage = new ImageView(GUIResources.getComGoal("cg" + cgNum));
            comGoalImage.setFitHeight(150);
            comGoalImage.setFitWidth(200);

            comGoals.add(comGoalImage, n, 0);
            n++;
        }
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
        int n = 0;

        for (Integer cgNum : clientController.getPlayerComGoal().keySet()) {
            if (cgNum == comGoalDoneID) {
                ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + newValue));
                scoreImage.setFitHeight(60);
                scoreImage.setFitWidth(60);

                if (n == 0) {
                    score_0.add(scoreImage, 0, 0);
                } else {
                    score_1.add(scoreImage, 0, 0);
                }
            }

            n++;
        }
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


    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);

        revert();
        selectTiles.setDisable(false);
    }

    /**
     * Checks the message destination and eventually sends the message
     */
    private void sendChat() {
        String[] checkChatMessage = {"@chat", destinationMenu.getText(), writtenMessage.getText()};
        InputController inputController = new InputController(clientController);

        if (inputController.checkChat(checkChatMessage) != 0) {
            String destination = checkChatMessage[1];
            String message = writtenMessage.getText();

            if (destination.equalsIgnoreCase("everybody")) {
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
     * Checks the selected Tiles from the Board and eventually communicates the change to the server
     */
    private void selectTiles() {
        InputController inputController = new InputController(clientController);

        if (!inputController.checkSelection(selectedTiles)) {
            printError("ERROR: wrong selection");
        }

        clientController.setSelectedTiles(selectedTiles);

        try {
            clientController.selectCard();
            selectTiles.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            printError("ERROR: server error");
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
        if (errorArea.isVisible()) {
            errorArea.setVisible(false);
            errorArea.setText("");
        }

        Node clickedNode = event.getPickResult().getIntersectedNode();
        int clickedColumn = GridPane.getColumnIndex(clickedNode);
        int clickedRow = GridPane.getRowIndex(clickedNode);

        if (boardPane.getChildren().contains(clickedNode)) {
            ImageView imageView = (ImageView) clickedNode;

            if (imageView != null && selectedTiles.size() < 3) {
                int coord = Position.getNumber(clickedColumn, clickedRow);
                selectedTiles.add(coord);

                imageView.setPreserveRatio(true);
                imageView.setFitWidth(50);
                imageView.setFitWidth(50);

                youSelectedThis.add(imageView, selectedTiles.size() - 1, 0);
            }
        }
    }

    /**
     * Removes all the images from the SelectedTiles pane and puts them back in the Bord pane
     */
    private void revert() {
        for (int i = youSelectedThis.getChildren().size() - 1; i >= 0; i--) {
            ImageView imageView = (ImageView) youSelectedThis.getChildren().get(i);

            if (imageView != null && !selectedTiles.isEmpty()) {
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);

                boardPane.add(imageView, Position.getColumn(selectedTiles.get(i)), Position.getRow(selectedTiles.get(i)));
                selectedTiles.remove(i);
            }
        }
    }
}