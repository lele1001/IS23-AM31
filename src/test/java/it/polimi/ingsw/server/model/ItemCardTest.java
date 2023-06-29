package it.polimi.ingsw.server.model;

import it.polimi.ingsw.commons.HouseItem;
import it.polimi.ingsw.commons.ItemCard;
import it.polimi.ingsw.commons.ItemNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ItemCardTest {

    /**
     * Just to test the correct creation of itemcards and its getter to get the item type.
     */
    @Test
    void getMyItem() {
        ItemCard itemCard = new ItemCard(HouseItem.Frame, ItemNumber.Third);
        assertEquals(itemCard.getMyItem(), HouseItem.Frame);
        itemCard = new ItemCard(HouseItem.Cat, ItemNumber.First);
        assertNotEquals(itemCard.getMyItem(), HouseItem.Frame);
        assertNotEquals(itemCard.getMyItem(), HouseItem.Plants);
        assertEquals(itemCard.getMyItem(), HouseItem.Cat);
        itemCard = new ItemCard(HouseItem.Plants, ItemNumber.Second);
        assertNotEquals(itemCard.getMyItem(), HouseItem.Frame);
        assertEquals(itemCard.getMyItem(), HouseItem.Plants);
    }

    /**
     * Just to test the correct creation of itemcards and its getter to get the item number.
     */
    @Test
    void getMyNum() {
        ItemCard itemCard = new ItemCard(HouseItem.Plants, ItemNumber.Second);
        assertEquals(itemCard.getMyNum(), ItemNumber.Second);
        assertNotEquals(itemCard.getMyNum(), ItemNumber.First);
        itemCard = new ItemCard(HouseItem.Frame, ItemNumber.First);
        assertNotEquals(itemCard.getMyNum(), ItemNumber.Third);
        assertEquals(itemCard.getMyNum(), ItemNumber.First);
        assertNotEquals(itemCard.getMyNum(), ItemNumber.Second);
        itemCard = new ItemCard(HouseItem.Cat, ItemNumber.Third);
        assertNotEquals(itemCard.getMyNum(), ItemNumber.Second);
        assertEquals(itemCard.getMyNum(), ItemNumber.Third);
    }

}