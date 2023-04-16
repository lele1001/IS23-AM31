package it.polimi.ingsw.server.model;

import java.io.Serializable;

public class ItemCard implements Serializable {
    private final HouseItem houseItem;
    private final ItemNumber itemNumber;

    public ItemCard(HouseItem myItem, ItemNumber myNum) {
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

    @Override
    public String toString() {
        return "ItemCard{" +
                "houseItem=" + houseItem +
                ", itemNumber=" + itemNumber +
                '}';
    }
}
