package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMI extends Remote {
    boolean login(String nickname, RMIClientConnection client) throws RemoteException;

    void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException;

    void selectCard(String nickname, ArrayList<Integer> positions) throws RemoteException;

    void setPlayerNumber(int number) throws RemoteException;
}
