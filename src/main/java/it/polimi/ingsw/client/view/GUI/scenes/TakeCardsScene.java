package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;

public class TakeCardsScene extends GUIScene {
    private ClientController clientController;

    @Override
    public void updateBoard(ItemCard[][] board) {

    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

    }

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

    public void highlightTiles() {
    }

    public void updateCurrPlayer(String player) {
        //TODO: print the input string
    }


}
