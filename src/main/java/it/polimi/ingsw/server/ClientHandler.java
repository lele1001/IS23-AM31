package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Optional;

public abstract class ClientHandler {
    protected volatile String nickname;
    protected ConnectionControl connectionControl;

    public abstract void askSelect();

    public abstract void askInsert();

//    public abstract void addToGame();

    public abstract Optional<Integer> askPlayerNumber();

    public abstract void sendErrorGameNotAvailable();

    public abstract void sendGameIsStarting();

    public abstract void sendError(String error);

    public abstract void SendCommonGoalDone(String source, int[] details);

    public abstract void SendPersGoalCreated(String nickname, String persGoal);

    public abstract void SendCommonGoalCreated(Integer comGoalID, Integer score);

    public abstract void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf);

    public abstract void SendBoardChanged(ItemCard[][] newBoard);

    public abstract void sendWinner (String winner);

}
