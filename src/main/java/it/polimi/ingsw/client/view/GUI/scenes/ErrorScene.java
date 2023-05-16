package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

public class ErrorScene extends GUIScene{
    private ClientController clientController;
    @FXML
    private Label errorDescription_label;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void printError(String error) {
        this.errorDescription_label.setText(error);
    }

    @Override
    public void bindEvents() {

    }

    public void closeGame() {
        clientController.disconnectMe();
    }

    @Override
    public void updateBoard(ItemCard[][] board) {

    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

    }

    @Override
    public void updateCurrPlayer(String player) {

    }

}

