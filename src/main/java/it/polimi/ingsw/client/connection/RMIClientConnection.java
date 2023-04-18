package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientConnection extends Remote {
    void onPlayerNumber() throws RemoteException;

    void onError(String error) throws RemoteException;

    void onSelect() throws RemoteException;

    void onInsert() throws RemoteException;

    void onBoardChanged(ItemCard[][] newBoard) throws RemoteException;

    void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) throws RemoteException;

    void onCommonGoalCreated(Integer comGoalID, Integer score) throws RemoteException;

    void onCommonGoalDone(String nickname, int[] newValue) throws RemoteException;

    void onPersGoalCreated(String newValue) throws RemoteException;

    void onChangeTurn(String nickname) throws RemoteException;

    void onWinner(String winner) throws RemoteException;

    void onGameIsStarting() throws RemoteException;

    void onErrorGameNotAvailable() throws RemoteException;

    void ping() throws RemoteException;

    void chatToMe(String sender, String message) throws RemoteException;

    void disconnectMe() throws RemoteException;
}
