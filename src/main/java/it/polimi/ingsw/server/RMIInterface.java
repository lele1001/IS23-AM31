package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIInterface implements RMI {
    private final Server server;
    private ConnectionControl connectionControl;

    public RMIInterface(Server server, ConnectionControl connectionControl) {
        this.server = server;
        this.connectionControl = connectionControl;
    }

    public void setConnectionControl(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }

    @Override
    public void selectCard(String nickname, ArrayList<Integer> positions) {
        connectionControl.selectCard(nickname, positions);
    }

    @Override
    public void setPlayerNumber(int number) throws RemoteException {
        System.out.println("Settato player number a " + number);
        server.setAvailablePlayers(number);
    }

    @Override
    public void chatToAll(String nickname, String message) throws RemoteException {
        connectionControl.chatToAll(nickname, message);
    }

    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws RemoteException {
        connectionControl.chatToPlayer(sender, receiver, message);
    }

    /**
     * Used only by the client to check if the server in Online
     * @throws RemoteException If the client cannot connect to the server
     */
    @Override
    public void pong() throws RemoteException {

    }

    /**
     *
     * @param nickname nickname of the client
     * @param cards The Item cards selected by the client
     * @param column The column where the client wants to put the Item cards
     */
    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        connectionControl.insertCard(nickname, cards, column);
    }
    /**
     * Waits for client's nickname and RMI interface, then, tries to insert it into the game.
     * If the game is not available, sends the error and disconnects it.
     * @param nickname nickname of the client
     * @param client The client interface
     */
    @Override
    public boolean login(String nickname, RMIClientConnection client) {        //deve prendere anche la classe del client
        ClientHandlerRmi clientHandlerRmi = new ClientHandlerRmi(connectionControl, nickname, client); //deve passargli la classe dell'interfaccia rmi client
        if (!connectionControl.tryAddInQueue(clientHandlerRmi, nickname)) {
            clientHandlerRmi.sendError("Game not available.");
            clientHandlerRmi.disconnectPlayer();
            return false;
        }
        return true;
    }
}
