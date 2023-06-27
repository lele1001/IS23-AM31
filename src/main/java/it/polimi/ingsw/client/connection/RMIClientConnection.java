package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Defines all the possible methods the server can call on the client using RMI
 */
public interface RMIClientConnection extends Remote {
    /**
     * Method called by the server if the player is first in queue and ha to decide the number of players in the game
     *
     * @param notAvailableNames the name of the games that already exists
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onPlayerNumber(List<String> notAvailableNames) throws RemoteException;

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
     * Method called by the server when a game is starting
     *
     * @param playersList The list of players in the game
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onGameIsStarting(ArrayList<String> playersList) throws RemoteException;

    /**
     * Called when the client is the only one online,
     * and the game needs to be interrupted waiting for other players coming back.
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onGameInterrupted() throws RemoteException;

    /**
     * Method called by the server to ensure the player is online and connected
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void pong() throws RemoteException;

    /**
     * Method called by the server when a player wants to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void chatToMe(String sender, String message) throws RemoteException;

    /**
     * Method called by the server to disconnect the player when an error occurred, or the game is finished
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void disconnectMe() throws RemoteException;

    /**
     * Method called by the server to update the player score when a player reconnects
     *
     * @param score The score of the player
     * @throws RemoteException if an error occurred calling the RMI client
     */

    void onPlayerScore(int score) throws RemoteException;

    /**
     * Method called by the server when a player completes his bookshelf
     *
     * @param nickname of the player that has completed the bookshelf.
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onBookshelfCompleted(String nickname) throws RemoteException;

    /**
     * Called when there are some saved games with a client's nicknames, to ask him if he wants to resume one of them.
     *
     * @param savedGames the list of saved games' names.
     * @throws RemoteException if an error occurred calling the RMI client
     */
    void onSavedGame(List<String> savedGames) throws RemoteException;

    /**
     * Called when there's a bookshelf's update.
     *
     * @param tilesToAdd the tiles to be added in the bookshelf.
     * @param column     the column of the bookshelf to put tiles into.
     * @param player     the owner of the just updated bookshelf.
     * @throws RemoteException if an error occurred calling the RMI client.
     */
    void onBookshelfRenewed(ItemCard[] tilesToAdd, int column, String player) throws RemoteException;

    /**
     * Called when there's a board's update.
     *
     * @param tilesToRemove the positions of the tiles to be removed from the board.
     * @throws RemoteException if an error occurred calling the RMI client.
     */
    void onBoardRenewed(Integer[] tilesToRemove) throws RemoteException;

    /**
     * Called at the end of the game to send the final classification to the client.
     *
     * @param finalScores an ordered map with players' nicknames and final scores.
     * @throws RemoteException if an error occurred calling the RMI client.
     */
    void onFinalScores(LinkedHashMap<String, Integer> finalScores) throws RemoteException;
}
