package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Set;

public interface ModelInterface {


    void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException, NotSameSelectedException;

    void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection; //chiedo conferma che sia la stessa eccezione


    void setListener(PropertyChangeListener listener);

    boolean isPlayerOnline(String nickname);

    Set<String> calcFinalScore();

    void changePlayerStatus(String nickname);

    void EndTurn(String nickname);

    void CreateGame(ArrayList<String> playersList);

    void resumeBoard();

    void sendGameDetails (String nickname);
}
