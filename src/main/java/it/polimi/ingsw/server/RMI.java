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
     * @param nickname of the client
     * @param client   is a pointer to the client connectionRMI
     * @return if the login was successful or not
     * @throws RemoteException if an error occurred calling the server RMI
     */
    boolean login(String nickname, RMIClientConnection client) throws RemoteException;

    /**
     * Method called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname of this client
     * @param cards    are the Tiles selected by the client in order
     * @param column   where to put the Tiles
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException;

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname  of this client
     * @param positions of Tiles selected by the client
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void selectCard(String nickname, ArrayList<Integer> positions) throws RemoteException;

    /**
     * Method called by the client only if he is the first connected to the server
     * Check that he is the first and then set the number of players for the game
     *
     * @param client that sends the request
     * @param number of players in the game
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void setPlayerNumber(String client, int number, String gameName) throws RemoteException;

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server sends to all the other clients the message
     *
     * @param nickname of this client
     * @param message  to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void chatToAll(String nickname, String message) throws RemoteException;

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server sends to all the other clients the message
     *
     * @param sender   of the message
     * @param receiver of the message
     * @param message  to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void chatToPlayer(String sender, String receiver, String message) throws RemoteException;

    /**
     * Method called by the client to see if the server is online
     *
     * @throws RemoteException if an error occurred calling the server RMI
     */
    void pong() throws RemoteException;

    void setSavedGames(boolean wantToSave, String gameName) throws RemoteException;
}
