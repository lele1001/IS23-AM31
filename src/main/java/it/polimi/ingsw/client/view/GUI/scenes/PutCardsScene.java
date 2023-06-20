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
import java.util.Map;

public class PutCardsScene extends GUIScene {
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
    Button undoSelection, selectTiles, sendMessage, exitButton;
    @FXML
    Label yourPoints, errorArea;
    @FXML
    RadioButton col0, col1, col2, col3, col4;
    @FXML
    ToggleGroup columns;
    private ClientController clientController;
    private InputController inputController;
    private ArrayList<Integer> selectedTiles;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        this.inputController = new InputController(clientController);
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

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        youSelectedThis.addEventHandler(MouseEvent.MOUSE_CLICKED, this::remove);
        sendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendChat(inputController, clientController, destinationMenu, writtenMessage));
        undoSelection.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> revert());
        selectTiles.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> putTiles());
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
    }

    /**
     * Sets the scene based on the number of players in the game
     */
    @Override
    public void setPlayers() {
        setPlayersShared(clientController, destinationMenu, sendMessage);
    }

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        int i = 0;
        for (ItemCard itemCard : selectedTiles.values()) {
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
                        tileImage.setFitHeight(46);
                        tileImage.setFitWidth(46);

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
                tileImage.setFitHeight(46);
                tileImage.setFitWidth(46);

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
        comGoalCreated(playerCommonGoal, score_0, score_1, comGoals, false, 150, 200, 60);
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
        comGoalDone(comGoalDoneID, newValue, score_0, score_1, clientController, 60);
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

/*
    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
        revert();
    }*/

    /**
     * Checks the Tiles to put in the Bookshelf and eventually communicates the change to the server
     */
    private void putTiles() {
        int i = 0;
        String column;
        try {
            column= ((RadioButton) columns.getSelectedToggle()).getId();
            i = Integer.parseInt(String.valueOf(column.charAt(column.length() - 1)));
        } catch (Exception e) {
            printError("ERROR: please select a valid column.");
            return;
        }

        ArrayList<ItemCard> tilesToPut = inputController.checkPutGUI(selectedTiles);
        //System.out.println(tilesToPut);
        if (tilesToPut != null) {
            try {
                clientController.insertCard(tilesToPut, i);
            } catch (Exception e) {
                printError("ERROR: serverError");
            }
        } else {
            printError("ERROR: wrong selection");
            youSelectedThis.getChildren().clear();
            updateSelectedTiles(clientController.getSelectedTiles());
        }

        selectedTiles.clear();
        youPutThis.getChildren().clear();

    }

    /**
     * Removes the clicked image from the SelectedTiles pane and puts it in the PutTiles pane
     *
     * @param event is the click on the image
     */
    private void remove(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();

        if (errorArea.isVisible()) {
            errorArea.setVisible(false);
            errorArea.setText("");
        }

        if (youSelectedThis.getChildren().contains(clickedNode)) {
            ImageView imageView = (ImageView) clickedNode;

            if (imageView != null && youPutThis.getChildren().size() < 3) {
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(50);
                imageView.setFitWidth(50);
                youPutThis.add(imageView, 0, 2 - youPutThis.getChildren().size());

                String[] splitString = imageView.getImage().getUrl().split("/");
                String itemIdentifiers = splitString[splitString.length - 1];
                splitString = itemIdentifiers.split("(?=\\p{Upper})");
                splitString[1] = splitString[1].split("\\.")[0];

                for (Integer i : clientController.getSelectedTiles().keySet()) {
                    ItemCard itemCard = clientController.getSelectedTiles().get(i);

                    if (itemCard.getMyItem().toString().equalsIgnoreCase(splitString[0]) && itemCard.getMyNum().toString().equalsIgnoreCase(splitString[1])) {
                        if (!selectedTiles.contains(i)) {
                            selectedTiles.add(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes all the images from the PutTiles pane and puts them back in the SelectedTiles pane
     */
    private void revert() {
        youSelectedThis.getChildren().clear();
        youPutThis.getChildren().clear();
        updateSelectedTiles(clientController.getSelectedTiles());
        selectedTiles.clear();
    }

}