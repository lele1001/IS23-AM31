package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.ItemCard;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Map;

public class GameController {
    private Map<String, ConnectionControl> playerList;
    private String currPlayer;

    public GameController(Map<String, ConnectionControl> playerList) {
        this.playerList = playerList;
    }

    private void createGame() {
    }

    public void run() {}

    public void runLastTurn() {}
    // todo for Mila
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {}
    // todo for Mila
    public void selectCard(ArrayList<Integer> position) {}
    // todo for Mila
    public void propertyChange(PropertyChangeEvent evt) {}
    // todo for Mila
    public void endTurn(String nickname) {}
}
