package it.polimi.ingsw.server;

import it.polimi.ingsw.server.model.ItemCard;

public abstract class ClientHandler {
    public abstract void askSelect();

    public abstract void askInsert();

    public abstract void sendError(String error);

    public abstract void SendCommonGoalDone(String source, int[] details);

    public abstract void SendPersGoalCreated(String nickname, String persGoal);

    public abstract void SendCommonGoalCreated(Integer comGoalID, Integer score);

    public abstract void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf);

    public abstract void SendBoardChanged(ItemCard[][] newBoard);

    public abstract void SendDetailsEndGame(String winner, int score);

}
