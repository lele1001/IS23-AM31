package it.polimi.ingsw.model;

public class Cell {
    ItemCard itemCard;
    int numPlayermin;

    Cell(int numPlayermin) {
        itemCard = null;
        this.numPlayermin = numPlayermin;
    }

    public void addItemCard(ItemCard itemCard) {
        this.itemCard = itemCard;
    }

    public ItemCard getItemCard() {
        return itemCard;
    }

    public int getNumPlayermin() {
        return numPlayermin;
    }
}
