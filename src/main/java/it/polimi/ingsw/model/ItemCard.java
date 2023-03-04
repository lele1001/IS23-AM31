package it.polimi.ingsw.model;

public class ItemCard {
    private final HouseItem houseItem;
    private final ItemNumber itemNumber;

    public ItemCard (HouseItem myItem, ItemNumber myNum) {
        this.houseItem = myItem;
        this.itemNumber = myNum;
    }

    public HouseItem getMyItem() {
        return houseItem;
    }

    public ItemNumber getMyNum() {
        return itemNumber;
    }
}
