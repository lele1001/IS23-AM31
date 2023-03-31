package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.ItemCard;

public class ConnectionControl {
    public void askSelect(String nickname) {
        // to fix
    }

    public void askInsert(String nickname) {
        // to fix
    }

    public void SendBoardChanged(ItemCard[][] newBoard) {

    }

    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    public void SendError(String error, String nickname) {
        //genero il messaggio con la stringa di errore, che parserò lato client.
    }

    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
    }

    public void SendEmptyCardBag() {
    }

    public void SendPlayerPointUpdate(String source, int newValue) {
    }

    public void SendCommonGoalDone(String source, int[] newValue) {
    }

    public void SendPersGoalCreated(String newValue) {
    }

    public void SendBookshelfCompleted(String nickname) {

    }
}
