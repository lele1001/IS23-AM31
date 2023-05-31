package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EndGameScene extends GUIScene {
    private ClientController clientController;
    @FXML
    ImageView winnerToken;
    @FXML
    Button closeButton;
    @FXML
    AnchorPane rankingPane;
    @FXML
    Label endMessage;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame());
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
        GridPane rankingTable = new GridPane();

        ColumnConstraints placeCol = new ColumnConstraints();
        ColumnConstraints nameCol = new ColumnConstraints();
        ColumnConstraints scoreCol = new ColumnConstraints();
        placeCol.setPrefWidth(20.0);
        nameCol.setPrefWidth(180.0);
        scoreCol.setPrefWidth(50.0);

        RowConstraints headingRow = new RowConstraints();
        headingRow.setPrefHeight(30.0);

        rankingTable.getColumnConstraints().addAll(placeCol, nameCol, scoreCol);
        rankingTable.getRowConstraints().add(headingRow);
        rankingTable.setId("rankingTable");

        rankingTable.add(new Label("pos"),0 ,0);
        rankingTable.add(new Label("nickname"), 0, 1);
        rankingTable.add(new Label("score"), 0, 2);

        for (int i = 0; i < clientController.getPlayersBookshelves().size(); i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(30.0);
            rankingTable.getRowConstraints().add(row);
        }
    }

    /**
     * Updates the current player
     *
     * @param player is the curren player
     */
    @Override
    public void updateCurrPlayer(String player) {
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
    }

    /**
     * Updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    @Override
    public void changeBoard(Integer[] tilesToRemove) {
    }

    /**
     * Prints the player's Bookshelf in the scene
     *
     * @param nickname  is the owner of the Bookshelf
     * @param bookshelf is the player's updated Bookshelf
     */
    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
    }

    /**
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
    }

    /**
     * Prints the CommonGoals and its available score in the scene
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
    }

    /**
     * Prints the PersonalGoal assigned to the player
     *
     * @param newValue is the PersonalGoal name
     */
    @Override
    public void persGoal(String newValue) {
    }

    /**
     * Prints the player's points in the scene
     *
     * @param myPoint are the points of the player
     */
    @Override
    public void printPoints(int myPoint) {
    }

    /**
     * Prints the message in the chat field of the scene
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    @Override
    public void receiveMessage(String sender, String message) {
    }

    /**
     * Prints an error message in the scene
     *
     * @param error is the error message to display
     */
    @Override
    public void printError(String error) {
    }

    /**
     * Displays the final ranking of the game, showing the score of each player
     *
     * @param finalScores contains the players' nicknames and their score
     */
    @Override
    public void finalScores(LinkedHashMap<String, Integer> finalScores) {
        int pos = 0, max = -1, index = 1;
        String posString;
        GridPane rankingTable = (GridPane) rankingPane.getChildren().get(0);

        for (String s : finalScores.keySet()) {
            if (finalScores.get(s) != max) {
                pos++;
                max = finalScores.get(s);
            }

            posString = String.valueOf(pos);
            rankingTable.add(new Label(posString), index, 0);
            rankingTable.add(new Label(s), index, 1);
            rankingTable.add(new Label(finalScores.get(s).toString()), index, 2);
            index++;
        }

        System.out.println();
    }

    public void closeGame() {
        clientController.disconnectMe();
        System.exit(1);
    }
}
