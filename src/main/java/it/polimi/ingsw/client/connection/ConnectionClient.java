package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public abstract class ConnectionClient extends UnicastRemoteObject {
    ClientController controller;
    String address;
    int port;

    /**
     * Initialize the connection to the server
     *
     * @param controller ClientController, on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     */
    ConnectionClient(ClientController controller, String address, int port) throws RemoteException {
        this.controller = controller;
        this.address = address;
        this.port = port;
    }

    public abstract void startConnection() throws Exception;

    public ClientController getController() {
        return controller;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public abstract void selectCard(String nickname, ArrayList<Integer> cardsSelected) throws Exception;

    public abstract void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws Exception;

    public abstract void chatToAll(String nickname, String message) throws Exception;

    public abstract void chatToPlayer(String sender, String receiver, String message) throws Exception;
}
