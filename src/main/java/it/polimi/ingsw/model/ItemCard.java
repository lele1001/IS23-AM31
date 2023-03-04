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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCard itemCard = (ItemCard) o;
        return houseItem == itemCard.houseItem && itemNumber == itemCard.itemNumber;
    }


}
