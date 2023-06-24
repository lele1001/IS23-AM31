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

    /**
     * Start the connection in RMI/Socket chosen by the client
     *
     * @throws Exception thrown by RMI or Socket during the calls
     */
    public abstract void startConnection() throws Exception;

    /**
     * @return the ClientController for all the methods called by RMI/Socket
     */
    public ClientController getController() {
        return controller;
    }

    /**
     * @return the server Address written by the player
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the server port written by the client
     */
    public int getPort() {
        return port;
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     *
     * @param nickname      this client
     * @param cardsSelected Tiles selected by the client
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public abstract void selectCard(String nickname, ArrayList<Integer> cardsSelected) throws Exception;

    /**
     * Method called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     *
     * @param nickname this client
     * @param cards    Tiles selected by the client in order
     * @param column   column where to put the Tiles
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public abstract void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws Exception;

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     *
     * @param nickname this client
     * @param message  String to send to all the connected players
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public abstract void chatToAll(String nickname, String message) throws Exception;

    /**
     * Method called from the client that pass to the server the chat message for the receiver
     *
     * @param sender   this client
     * @param receiver player that receives the message
     * @param message  String to send to the receiver
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public abstract void chatToPlayer(String sender, String receiver, String message) throws Exception;

    /**
     * Method called by the client only if he is the first connected to the server
     *
     * @param players number of players in the game
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public abstract void setPlayersNumber(int players, String gameName) throws Exception;

    /**
     * Called by the client after his decision about saved games.
     *
     * @param wantToSave: true if he wants to re-start from a saved game.
     * @param gameName:   the name of the game he wants to resume.
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public abstract void setSavedGame(boolean wantToSave, String gameName) throws Exception;

}
