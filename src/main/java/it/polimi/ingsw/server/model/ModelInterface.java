package it.polimi.ingsw.server.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;

import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.ArrayList;

public interface ModelInterface {
    /**
     * Creates the game with all the necessary things (board, bookshelves, personal goals and common goals).
     *
     * @param playersList the list with all players' nicknames.
     */
    void CreateGame(ArrayList<String> playersList, String gameFilePath);

    void resumeGame(ArrayList<String> onlinePlayers, JsonObject json, String gameFilePath);

    /**
     * Tries to insert cards in a nickname's bookshelf.
     *
     * @param nickname the owner of the bookshelf.
     * @param cards     to be inserted into the bookshelf.
     * @param column    of the bookshelf to insert cards into.
     * @throws NoBookshelfSpaceException if there's no space in the column indicated.
     * @throws NotSameSelectedException  if the player wants to insert cards different from the ones selected.
     */
    void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException, NotSameSelectedException;

    /**
     * Tries to select cards from the board.
     *
     * @param positions of the cards to be selected.
     * @throws NoRightItemCardSelection if the selection is not valid.
     */
    void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection; //chiedo conferma che sia la stessa eccezione

    /**
     * Used to set GameController as a GameModel's listener.
     *
     * @param listener to be set.
     */
    void setListener(PropertyChangeListener listener);

    //boolean isPlayerOnline(String nickname);

    /**
     * Calculates all the players' final score and sends it to each of them
     *
     * @return a set (whose size is > 1 only in case of parity) with all the winners.
     */
    ArrayList<String> calcFinalScore();

    //void changePlayerStatus(String nickname);

    /**
     * Called at the end of a turn, checks if common goals have been reached from the current player,
     * and if the board needs to be refilled.
     *
     * @param nickname of the current player.
     */
    void EndTurn(String nickname);


    /**
     * Called to resume the board when someone has selected tiles from it
     * but has disconnected before inserting them in their bookshelf.
     */
    void resumeBoard();

    /**
     * Used when a player comes back in a game and needs to have all game's information.
     *
     * @param nickname of the just returned player.
     */
    void sendGameDetails(String nickname);
}
