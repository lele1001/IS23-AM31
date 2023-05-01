package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMI extends Remote {
    /**
     * Method called by the client to start the connection to the server
     * Create a ClientHandlerRMI for the client and start the checking procedure, sending a request to the connection client
     *
     * @param nickname the nickname of the client
     * @param client   A pointer to the client connectionRMI
     * @return If the login was successful or not
     * @throws RemoteException if an error occurred calling the server RMI
     */
    boolean login(String nickname, RMIClientConnection client) throws RemoteException;

    /**
     * Method called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname this client
     * @param cards    Tiles selected by the client in order
     * @param column   column where to put the Tiles
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException;

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname  this client
     * @param positions Tiles selected by the client
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void selectCard(String nickname, ArrayList<Integer> positions) throws RemoteException;

    /**
     * Method called by the client only if he is the first connected to the server
     * Check that he is the first and then set the number of player for the game
     *
     * @param client the client that send the request
     * @param number number of players in the game
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void setPlayerNumber(String client, int number) throws RemoteException;

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server send to all the other clients the message
     *
     * @param nickname this client
     * @param message  String to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void chatToAll(String nickname, String message) throws RemoteException;

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server send to all the other clients the message
     *
     * @param sender   the player that send the message
     * @param receiver the player that has to receive the message
     * @param message  String to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void chatToPlayer(String sender, String receiver, String message) throws RemoteException;

    /**
     * Method called by the client to see if the server is online
     *
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void pong() throws RemoteException;
}
