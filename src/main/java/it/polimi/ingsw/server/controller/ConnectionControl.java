package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;

import java.util.*;

public class ConnectionControl {
    public static ArrayList<Position> askSelect(String currPlayer) {
        // to fix
        return null;
    }

    public static Map<Integer, ItemCard> askInsert(String currPlayer) {
        // to fix
        return null;
    }

    public void onBoardChanged(List<ItemCard> newBoard) {

    }

    public void onBookshelfChanged(String nickname, List<ItemCard> newBookshelf) {

    }

    public void onError(String error) {
        //genero il messaggio con la stringa di errore, che parserò lato client.
    }

    public void onCommonGoalCreated(HashMap<Integer, Integer> newComgoal) {
    }
    public void onEmptyCardBag() {
    }

    public void onPlayerPointUpdate(String source, int newValue) {
    }

    public void onCommonGoalDone(String source, HashMap<Integer, Integer> newValue) {
    }

    public void onPersGoalCreated(String newValue) {
    }
}
