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
import java.util.List;
import java.util.Map;

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
    Button sendMessage;
    @FXML
    MenuButton destinationMenu;
    private ClientController clientController;
    private ArrayList<String> players;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");
        bindEvents();
    }

    @Override
    public void printError(String error) {
    }

    public void bindEvents() {
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat());
    }

    public void updateCurrPlayer(String currPlayer) {
        if (!clientController.getMyNickname().equals(currPlayer)) {
            userTurn.setText("It is " + currPlayer + "'s turn");
        } else {
            userTurn.setText("It is your turn");
        }
    }

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

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        int index = players.indexOf(nickname);
        Tab tabToModify = bookshelvesPane.getTabs().get(index);

        if (tabToModify.getText().equals(nickname)) {
            AnchorPane myAnchor = (AnchorPane) tabToModify.getContent();
            GridPane bookshelfToModify = (GridPane) myAnchor.getChildren().get(1);

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

    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        int n = 0;

        for (Integer i : playerCommonGoal.keySet()) {
            ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + playerCommonGoal.get(i).toString()));
            scoreImage.setFitHeight(45);
            scoreImage.setFitWidth(45);

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
            comGoalImage.setFitHeight(100);
            comGoalImage.setFitWidth(175);

            comGoals.add(comGoalImage, 0, n);
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
    public void setPlayers() {
        MenuItem msgEverybody = new MenuItem("everybody");
        msgEverybody.setId("msgEverybody");
        destinationMenu.getItems().add(msgEverybody);
        msgEverybody.setOnAction(event -> {
            destinationMenu.setDisable(false);
            sendMessage.setDisable(false);
            destinationMenu.setText(msgEverybody.getText());
        });

        players = new ArrayList<>(clientController.getPlayersBookshelves().keySet());

        for (int i = 0; i < players.size(); i++) {
            setBookshelf(i);
            setChat(players.get(i));
        }
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

    /**
     * Initializes the chat destination options, setting the players' names
     */
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
    }

    private void sendChat() {
        String[] checkChatMessage = {"@chat", destinationMenu.getText(), writtenMessage.getText()};
        InputController inputController = new InputController(clientController);

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
}