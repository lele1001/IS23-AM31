package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;

public class NotMyTurnScene extends GUIScene {
    private ClientController clientController;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void bindEvents() {

    }

    public void show() {
    }

    public void updateCurrPlayer(String currPlayer) {
        //TODO: print the name of the current player
    }

    @Override
    public void updateBoard(ItemCard[][] board) {

    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

    }

}
