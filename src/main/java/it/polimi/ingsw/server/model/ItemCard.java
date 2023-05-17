package it.polimi.ingsw.server.model;

import java.io.Serializable;

public class ItemCard implements Serializable {
    private final HouseItem houseItem;
    private final ItemNumber itemNumber;

    /**
     * The default constructor.
     *
     * @param myItem this ItemCard's HouseItem to be set.
     * @param myNum  this ItemCard's ItemNumber to be set.
     */
    public ItemCard(HouseItem myItem, ItemNumber myNum) {
        this.houseItem = myItem;
        this.itemNumber = myNum;
    }

    /**
     * The default getter of this ItemCard's HouseItem.
     *
     * @return the ItemCard's HouseItem.
     */
    public HouseItem getMyItem() {
        return houseItem;
    }

    /**
     * The default getter of this ItemCard's ItemNumber.
     *
     * @return the ItemCard's ItemNumber.
     */
    public ItemNumber getMyNum() {
        return itemNumber;
    }

    /**
     * The default implementation of the equal method to compare ItemCards.
     *
     * @param o the object to compare with this ItemCard.
     * @return true if o is the same ItemCard of this.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCard itemCard = (ItemCard) o;
        return houseItem == itemCard.houseItem && itemNumber == itemCard.itemNumber;
    }

    /**
     * The default method to get this ItemCard as a string.
     *
     * @return a string that represents this ItemCard with its two fields.
     */
    @Override
    public String toString() {
        return "ItemCard{" +
                "houseItem=" + houseItem +
                ", itemNumber=" + itemNumber +
                '}';
    }
}
