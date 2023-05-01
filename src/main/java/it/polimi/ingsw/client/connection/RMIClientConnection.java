package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface RMIClientConnection extends Remote {
    /**
     * Method called by the server if the player is first in queue and ha to decide the number of players in the game
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onPlayerNumber() throws RemoteException;

    /**
     * Method called by the server in case of error on the server side
     *
     * @param error String that describes the type of error
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onError(String error) throws RemoteException;

    /**
     * Method called by the server to ask the player to select the Tiles
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onSelect() throws RemoteException;

    /**
     * Method called by the server to ask the player to insert the Tiles
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onInsert() throws RemoteException;

    /**
     * Method called by the server to update the client's side board
     *
     * @param newBoard the updated board
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onBoardChanged(ItemCard[][] newBoard) throws RemoteException;

    /**
     * Method called by the server to update the client's side bookshelf of player nickname
     *
     * @param nickname     the nickname of the player
     * @param newBookshelf the updated bookshelf of the player nickname
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) throws RemoteException;

    /**
     * Method called by the server when a Common goal is created
     *
     * @param comGoalID ID of the Common goal
     * @param score     Value if the client does the Common goal
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onCommonGoalCreated(Integer comGoalID, Integer score) throws RemoteException;

    /**
     * Method called by the server when a Common goal is completed by the player nickname
     *
     * @param nickname nickname of the player
     * @param newValue Array of Common goal ID and new value of common goal
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onCommonGoalDone(String nickname, int[] newValue) throws RemoteException;

    /**
     * Method called by the server when a Personal goal is created
     *
     * @param newValue String that determines the Personal goal from a Json file
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onPersGoalCreated(String newValue) throws RemoteException;

    /**
     * Method called by the server on a new Turn
     *
     * @param nickname the nickname of the players whose turn is
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onChangeTurn(String nickname) throws RemoteException;

    /**
     * Method called by the server when a player win
     *
     * @param winners is the player that won the game
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onWinner(List<String> winners) throws RemoteException;

    /**
     * Method called by the server when a game is starting
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onGameIsStarting(ArrayList<String> playersList) throws RemoteException;

    /**
     * Method called by the server to ensure the player is online and connected
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void pong() throws RemoteException;

    /**
     * Method called by the server when a player want to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void chatToMe(String sender, String message) throws RemoteException;

    /**
     * Method called by the server to disconnect the player when an error occurred or the game is finished
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void disconnectMe() throws RemoteException;

    /**
     * Method called by the server to update the player score when a player reconnect
     *
     * @param score The score of the player
     * @throws RemoteException if an error occurred calling the RMI client
     */

    void onPlayerScore(int score) throws RemoteException;

    /**
     * Method called by the server when a player completes his bookshelf
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onBookshelfCompleted() throws RemoteException;
}
