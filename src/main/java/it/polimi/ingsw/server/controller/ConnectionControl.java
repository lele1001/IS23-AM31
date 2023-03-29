package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;

import java.util.*;

public class ConnectionControl {
    public static ArrayList<Position> askSelect(String nickname) {
        // to fix
        return null;
    }

    public static Map<Integer, ItemCard> askInsert(String nickname) {
        // to fix
        return null;
    }

    public void SendBoardChanged(List<ItemCard> newBoard) {

    }

    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    public void SendError(String error) {
        //genero il messaggio con la stringa di errore, che parserò lato client.
    }

    public void SendCommonGoalCreated(HashMap<Integer, Integer> newComgoal) {
    }
    public void SendEmptyCardBag() {
    }

    public void SendPlayerPointUpdate(String source, int newValue) {
    }

    public void SendCommonGoalDone(String source, HashMap<Integer, Integer> newValue) {
    }

    public void SendPersGoalCreated(String newValue) {
    }

    public void SendBookshelfCompleted(String nickname){

    }
}
