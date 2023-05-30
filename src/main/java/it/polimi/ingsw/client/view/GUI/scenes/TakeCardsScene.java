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

    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
    }

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

    @Override
    public void persGoal(String newValue) {
        ImageView persGoalImage = new ImageView(GUIResources.getPersGoal(newValue));
        persGoalImage.setFitHeight(200);
        persGoalImage.setFitWidth(150);

        persGoal.add(persGoalImage, 0, 0);
    }

    @Override
    public void printPoints(int myPoint) {
        yourPoints.setText("You have " + myPoint + " points");
    }

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

    @Override
    public void receiveMessage(String sender, String message) {
        chatHistory.appendText("> " + sender + ": " + message + "\n");
        writtenMessage.setText("");
    }

    @Override
    public void updateSavedGames(List<String> savedGames) {
    }

    /**
     * Updates the score of the CommonGoal
     *
     * @param comGoalDoneID is the ID of the CommonGoal
     * @param newValue      is its available score
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

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");
        errorArea.setVisible(false);
        selectedTiles = new ArrayList<>();
        bindEvents();
    }

    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);

        revert();
        selectTiles.setDisable(false);
    }

    @Override
    public void bindEvents() {
        boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::remove);
        selectTiles.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectTiles());
        undoSelection.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> revert());
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat());
    }

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

    public void updateCurrPlayer(String player) {
        selectedTiles.clear();
        selectTiles.setDisable(false);
    }

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
}