package it.polimi.ingsw.model;


public class Library {
    private ItemCard[][] library;



    public ItemCard get(int x, int y) {
        return library[x][y];
    }
}
