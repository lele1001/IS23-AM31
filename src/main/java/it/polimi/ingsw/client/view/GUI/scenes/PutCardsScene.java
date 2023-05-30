package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PutCardsScene extends GUIScene {
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;

    @FXML
    TextField writtenMessage;
    @FXML
    MenuButton destinationMenu;
    @FXML
    AnchorPane putCardsPane;
    @FXML
    GridPane bookshelfPane, comGoals, persGoal, score_0, score_1, youSelectedThis, youPutThis;
    @FXML
    TextArea chatHistory;
    @FXML
    Button undoSelection, selectTiles, sendMessage;
    @FXML
    Label yourPoints, errorArea;
    @FXML
    RadioButton col0, col1, col2, col3, col4;
    @FXML
    ToggleGroup columns;
    private ClientController clientController;
    private ArrayList<Integer> selectedTiles;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");

        columns = new ToggleGroup();
        col0.setToggleGroup(columns);
        col1.setToggleGroup(columns);
        col2.setToggleGroup(columns);
        col3.setToggleGroup(columns);
        col4.setToggleGroup(columns);

        selectedTiles = new ArrayList<>();

        bindEvents();
    }

    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    @Override
    public void bindEvents() {
        youSelectedThis.addEventHandler(MouseEvent.MOUSE_CLICKED, this::remove);
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat());
        undoSelection.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> revert());
        selectTiles.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> putTiles());

    }

    private void putTiles() {
        int i = 0;
        try {
            String column = ((RadioButton)columns.getSelectedToggle()).getId();
            i = Integer.parseInt(String.valueOf(column.charAt(column.length() - 1)));
        } catch (NumberFormatException e) {
            printError("ERROR: parse exception");
        }

        InputController inputController = new InputController(clientController);
        ArrayList<ItemCard> tilesToPut = inputController.checkPutGUI(selectedTiles);
        System.out.println(selectedTiles);
        if (tilesToPut != null) {
            try {
                clientController.insertCard(tilesToPut, i);
            } catch (Exception e) {
                printError("ERROR: serverError");
            }
        } else {
            printError("ERROR: wrong selection");
        }

        selectedTiles.clear();
        youPutThis.getChildren().clear();
    }

    public void updateCurrPlayer(String player) {
    }

    @Override
    public void updateBoard(ItemCard[][] board) {
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        if (clientController.getMyNickname().equals(nickname)) {
            bookshelfPane.getChildren().clear();

            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(46);
                        tileImage.setFitWidth(46);

                        bookshelfPane.add(tileImage, j, i);
                    }
                }
            }
        }
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

    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles){
        int i = 0;
        for(ItemCard itemCard : clientController.getSelectedTiles().values()){
            String itemName = itemCard.getMyItem().toString().toLowerCase();
            String itemNumber = itemCard.getMyNum().toString();
            String myItem = itemName + itemNumber;

            ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
            tileImage.setPreserveRatio(true);
            tileImage.setFitWidth(50);
            tileImage.setFitHeight(50);

            youSelectedThis.add(tileImage, i, 0);
            i++;
        }

    }

    public void sendChat() {
        destinationMenu.setDisable(false);

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
            }        }
    }

    private void remove(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();

        if (youSelectedThis.getChildren().contains(clickedNode)) {
            ImageView imageView = (ImageView) clickedNode;

            if (imageView != null && youPutThis.getChildren().size() < 3) {
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(50);
                imageView.setFitWidth(50);
                youPutThis.add(imageView, 0, 2 - youPutThis.getChildren().size());

                String[] splitString = imageView.getImage().getUrl().split("/");
                String itemIdentifiers = splitString[splitString.length -1];
                splitString = itemIdentifiers.split("(?=\\p{Upper})");
                splitString[1] = splitString[1].split("\\.")[0];

                for (Integer i : clientController.getSelectedTiles().keySet()) {
                    ItemCard itemCard = clientController.getSelectedTiles().get(i);

                    if (itemCard.getMyItem().toString().equalsIgnoreCase(splitString[0]) && itemCard.getMyNum().toString().equalsIgnoreCase(splitString[1])) {
                        if(!selectedTiles.contains(i)){
                            selectedTiles.add(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void revert() {
        for (int i = youPutThis.getChildren().size() - 1; i >= 0; i--) {
            ImageView imageView = (ImageView) youPutThis.getChildren().get(i);

            if (imageView != null && !youPutThis.getChildren().isEmpty()) {
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);

                youSelectedThis.add(imageView, i, 0);
            }
        }
        selectedTiles.clear();
    }
}