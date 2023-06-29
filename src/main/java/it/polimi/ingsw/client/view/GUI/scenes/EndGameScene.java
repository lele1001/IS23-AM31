package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.controller.ClientController;
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

/**
 * Defines the controller for the EndGameScene
 */
public class EndGameScene extends GUIScene {
    @FXML
    private ImageView winnerToken;
    @FXML
    private Button closeButton;
    @FXML
    private AnchorPane rankingPane;
    @FXML
    private Label victoryMessage;

    /**
     * Initialize the endGameScene
     *
     * @param clientController created for the GUI app
     */
    @Override
    public void initialize(ClientController clientController) {
        super.initialize(clientController);
        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
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

        rankingTable.add(new Label("pos"), 0, 0);
        rankingTable.add(new Label("nickname"), 0, 1);
        rankingTable.add(new Label("score"), 0, 2);

        for (int i = 0; i < clientController.getPlayersBookshelves().size(); i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(30.0);
            rankingTable.getRowConstraints().add(row);
        }
    }

    /**
     * Displays the final ranking of the game, showing the score of each player
     *
     * @param finalScores contains the players' nicknames and their score
     */
    @Override
    public void finalScores(LinkedHashMap<String, Integer> finalScores) {
        int pos = 0, max = -1, index = 1;
        GridPane rankingTable = (GridPane) rankingPane.getChildren().get(0);
        for (String s : finalScores.keySet()) {
            if (finalScores.get(s) != max) {
                pos++;
            }
            if (finalScores.get(s) > max) {
                max = finalScores.get(s);
            }

            rankingTable.add(new Label(String.valueOf(pos)), 0, index);
            rankingTable.add(new Label(s), 1, index);
            rankingTable.add(new Label(finalScores.get(s).toString()), 2, index);
            index++;
        }
        if (finalScores.get(clientController.getMyNickname()) == max) {
            winnerToken.setVisible(true);
            victoryMessage.setText("YOU'RE THE WINNER");
        } else {
            winnerToken.setVisible(false);
        }
    }
}
