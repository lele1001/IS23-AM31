package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Map;

public class ErrorScene extends GUIScene {
    private ClientController clientController;
    @FXML
    Label errorDescription, errorArea;
    @FXML
    Button closeButton;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void printError(String error) {
        errorDescription.setText(error);
        errorDescription.setVisible(true);
    }

    @Override
    public void bindEvents() {
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame());
    }

    public void closeGame() {
        clientController.disconnectMe();
        System.exit(1);
    }

    @Override
    public void updateBoard(ItemCard[][] board) {
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
    public void updateCurrPlayer(String player) {
    }
}