package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.HouseItem.Cat;
import static it.polimi.ingsw.model.HouseItem.Frame;
import static it.polimi.ingsw.model.ItemNumber.First;
import static it.polimi.ingsw.model.ItemNumber.Second;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void insertCard() {
        Library l = new Library();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second))), 2);
        assertEquals(l.get(5,2), new ItemCard(Cat, First));
    }

    @Test
    void checkSpace() {
        Library l = new Library();

    }

    @Test
    void get() {
        Library l = new Library();

    }


}