package it.polimi.ingsw.model;

import java.util.*;
public class Library {
    private final ItemCard[][] library = new ItemCard[6][5];


    // Inserimento carte
    //@ requires checkSpace(column, cards.size()) == TRUE && cards.size() <= 3
    public void insertCard (List<ItemCard> cards, int column) {
        int i;
        // Controllo il primo posto libero della colonna
        for (i=5; i>=0; i--) {
            if(library[i][column]==null)
                break;
        }

        // Inserisco, in ordine, le carte fornite dal basso verso l'alto
        for(ItemCard ic : cards) {
            library[i][column]  = ic;
            i--;
        }
    }

    public boolean checkSpace (int column, int num) {
        if((num>3)||(num<=0)||(column<0)||(column>4))
            return false;
        int i;
        for (i=5; i>=0; i--) {
            if(library[i][column]==null)
                break;
        }
        if((i<0)||(i-num<-1))
            return false;
        return true;
    }

    public ItemCard get(int x, int y) {
        if((x<0)||(y<0)||(x>5)||(y>4)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return library[x][y];
    }
}
