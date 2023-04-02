package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface ModelInterface {


    void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException;

    void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection; //chiedo conferma che sia la stessa eccezione


    void setListener(PropertyChangeListener listener);

    boolean isPlayerOnline(String nickname);

    String calcFinalScore();

    void ChangePlayerStatus(String nickname);
    void EndTurn(String nickname);

    void CreateGame(ArrayList<String> playersList);
}
