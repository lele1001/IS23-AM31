package it.polimi.ingsw.model;

import it.polimi.ingsw.ModelExceptions.NoBookshelfSpaceException;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface ModelInterface {



    void InsertCard(String nickname, ArrayList<ItemCard> cards,int column) throws NoBookshelfSpaceException;

    void selectCard(ArrayList<Integer> positions) throws NoBookshelfSpaceException; //chiedo conferma che sia la stessa eccezione



    void setListener(PropertyChangeListener listener);

    boolean isPlayerOnline(String nickname);

    String calcFinalScore();

    void ChangePlayerStatus(String nickname);

}
