package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class NumberOfPlayersScene extends GUIScene {
    @FXML
    GridPane firstPlayerPane;
    @FXML
    TextField playersNum, gameName;
    @FXML
    Button submitButton;
    @FXML
    AnchorPane playersScenePane;
    @FXML
    TextArea errorArea;
    private ClientController clientController;
    public Parent root;

    public void setRoot(Parent root) {
        this.root = root;
    }

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;

        bindEvents();
    }

    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    @Override
    public void bindEvents() {
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> submitAction());
    }

    private boolean checkText(TextField text) {
        return !text.getText().isEmpty();
    }

    /**
     * Form to select the number of players
     */
    public void submitAction() {
        int players = Integer.parseInt(playersNum.getText());

        if (players <= 0 || players >= 5) {
            printError("Error: insert a valid number of players");
            submitButton.setDisable(false);
        }

        if (!checkText(gameName)) {
            printError("Error: insert a valid name for the game");
            submitButton.setDisable(false);
        }
        try {
            submitButton.setDisable(true);
            clientController.setPlayersNumber(players, gameName.getText());
        } catch (Exception e) {
            printError("Impossible to connect to the server");
        }
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
    }

    @Override
    public void persGoal(String newValue) {

    }

    @Override
    public void printPoints(int myPoint) {

    }

    @Override
    public void updateBoard(ItemCard[][] board) {
    }

    @Override
    public void updateCurrPlayer(String player) {
    }
}