package it.polimi.ingsw.server.connection;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Define methods used to communicate to the client
 */
public abstract class ClientHandler {
    /**
     * Nickname of the player
     */
    protected volatile String nickname;
    /**
     * Connection controller of the Game
     */
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
     *
     * @param notAvailableNames the name of the games already in existence
     */
    public abstract void askPlayerNumber(List<String> notAvailableNames);

    /**
     * The server calls the client's method on a new Turn
     *
     * @param nickname of the players whose turn is
     */
    public abstract void sendPlayerTurn(String nickname);

    /**
     * The server calls the client's method to disconnect the player when an error occurred, or the game is finished
     */
    public abstract void disconnectPlayer();

    /**
     * The server calls the client's method when a game is starting
     *
     * @param playersList the list that contains the name of the players
     */
    public abstract void sendGameIsStarting(ArrayList<String> playersList);

    /**
     * The server calls the client's method in case of error on the server side
     *
     * @param error is the String that describes the type of error
     */
    public abstract void sendError(String error);

    /**
     * The server calls the client's method when a CommonGoal is completed by the player nickname
     *
     * @param source  is the nickname of the player
     * @param details is an Array with the CommonGoal ID and its new value
     */
    public abstract void SendCommonGoalDone(String source, int[] details);

    /**
     * The server calls the client's method when a PersonalGoal is created
     *
     * @param persGoal is the String that determines the PersonalGoal from a Json file
     */
    public abstract void SendPersGoalCreated(String persGoal);

    /**
     * The server calls the client's method when a CommonGoal is created
     *
     * @param comGoalID is the ID of the CommonGoal
     * @param score     is the value assigned if the client reaches the CommonGoal
     */
    public abstract void SendCommonGoalCreated(Integer comGoalID, Integer score);

    /**
     * The server calls the client's method to update the client's side bookshelf of player nickname
     *
     * @param nickname     of the player
     * @param newBookshelf is the updated bookshelf of the player
     */
    public abstract void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf);

    /**
     * Sends a bookshelf's update to the client.
     *
     * @param nickname   the player whose bookshelf has changed.
     * @param tilesToAdd the ordered array of tiles to add in the player's bookshelf.
     * @param column     the column of the bookshelf to add tiles into.
     */
    public abstract void sendBookshelfRenewed(String nickname, ItemCard[] tilesToAdd, int column);

    /**
     * The server calls the client's method to update the client's side board
     *
     * @param newBoard is the updated board
     */
    public abstract void SendBoardChanged(ItemCard[][] newBoard);

    /**
     * Sends board's update to the client.
     *
     * @param tilesToRemove the array of board's positions to remove tiles from.
     */
    public abstract void sendBoardRenewed(Integer[] tilesToRemove);

    /**
     * The server calls the client's method when a player wins
     *
     * @param finalScores contains all the players' scores.
     */
    public abstract void sendFinalScores(LinkedHashMap<String, Integer> finalScores);

    /**
     * The server calls the client's method when a player wants to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     */
    public abstract void chatToMe(String sender, String message);

    /**
     * The server calls the client's method to update the player score when a player reconnects
     *
     * @param score of the player
     */

    public abstract void sendPlayerScore(int score);

    /**
     * The server calls the client's method when a player completes his bookshelf
     *
     * @param nickname of the player that has completed the bookshelf.
     */
    public abstract void sendBookshelfCompleted(String nickname);

    /**
     * Called when there are too many absents for the game: it needs to be interrupted waiting for them.
     */
    public abstract void sendGameInterrupted();

    /**
     * Asks the client if he wants to resume one of the game he was into.
     *
     * @param savedGames the list of saved games' names the client is into.
     */
    public abstract void askSavedGame(List<String> savedGames);
}
