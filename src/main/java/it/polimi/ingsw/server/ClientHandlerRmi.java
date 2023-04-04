package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.ItemCard;

public class ClientHandlerRmi extends ClientHandler {

    // come attributo serve il riferimento all'interfaccia client

    @Override
    public void askSelect() {
        //chiama il metodo sul client
    }

    @Override
    public void askInsert() {

    }

    @Override
    public void sendError(String error) {

    }

    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {

    }

    @Override
    public void SendPersGoalCreated(String nickname, String persGoal) {

    }

    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {

    }

    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {

    }

    @Override
    public void SendDetailsEndGame(String winner, int score) {

    }
}
