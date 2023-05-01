package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientHandler {
    protected volatile String nickname;
    protected ConnectionControl connectionControl;

    /**
     * The server calls the client's method to ask the player to select the Tiles
     */
    public abstract void askSelect();

    /**
     * The server calls the client's method to ask the player to insert the Tiles
     */
    public abstract void askInsert();

    /**
     * The server calls the client's method if the player is first in queue and ha to decide the number of players in the game
     */
    public abstract void askPlayerNumber();

    /**
     * The server calls the client's method on a new Turn
     *
     * @param nickname the nickname of the players whose turn is
     */
    public abstract void sendPlayerTurn(String nickname);

    /**
     * The server calls the client's method to disconnect the player when an error occurred or the game is finished
     */
    public abstract void disconnectPlayer();

    /**
     * The server calls the client's method when a game is starting
     */
    public abstract void sendGameIsStarting(ArrayList<String> playersList);

    /**
     * The server calls the client's method in case of error on the server side
     *
     * @param error String that describes the type of error
     */
    public abstract void sendError(String error);

    /**
     * The server calls the client's method when a Common goal is completed by the player nickname
     *
     * @param source  nickname of the player
     * @param details Array of Common goal ID and new value of common goal
     */
    public abstract void SendCommonGoalDone(String source, int[] details);

    /**
     * The server calls the client's method when a Personal goal is created
     *
     * @param persGoal String that determines the Personal goal from a Json file
     */
    public abstract void SendPersGoalCreated(String persGoal);

    /**
     * The server calls the client's method when a Common goal is created
     *
     * @param comGoalID ID of the Common goal
     * @param score     Value if the client does the Common goal
     */
    public abstract void SendCommonGoalCreated(Integer comGoalID, Integer score);

    /**
     * The server calls the client's method to update the client's side bookshelf of player nickname
     *
     * @param nickname     the nickname of the player
     * @param newBookshelf the updated bookshelf of the player nickname
     */
    public abstract void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf);

    /**
     * The server calls the client's method to update the client's side board
     *
     * @param newBoard the updated board
     */
    public abstract void SendBoardChanged(ItemCard[][] newBoard);

    /**
     * The server calls the client's method when a player win
     *
     * @param winners is the player that won the game
     */
    public abstract void sendWinner(List<String> winners);

    /**
     * The server calls the client's method when a player want to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     */
    public abstract void chatToMe(String sender, String message);

    /**
     * The server calls the client's method to update the player score when a player reconnect
     *
     * @param score The score of the player
     */

    public abstract void sendPlayerScore(int score);

    /**
     * The server calls the client's method when a player completes his bookshelf
     */
    public abstract void sendBookshelfCompleted();
}
