package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;

public class TakeCardsScene implements GUIScene {
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

    public void highlightTiles() {
    }

    public void updateCurrPlayer(String yourTurn) {
        //TODO: print the input string
    }
}
