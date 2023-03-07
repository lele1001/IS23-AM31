package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.HouseItem.*;
import static it.polimi.ingsw.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    void insertCard() {
        Library l = new Library(); int i;
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second))), 2);
        assertEquals(l.get(5,2), new ItemCard(Cat, First));
        assertEquals(l.get(4, 2), new ItemCard(Frame, Second));

        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third))), 2);
        assertEquals(l.get(5,2), new ItemCard(Cat, First));
        assertEquals(l.get(4, 2), new ItemCard(Frame, Second));
        assertEquals(l.get(3, 2), new ItemCard(Books, Third));

        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third))), 1);
        assertEquals(l.get(5, 1), new ItemCard(Books, Third));
        for (i = 4; i>=0; i--)
            assertNull(l.get(i, 1));

        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Books, First))), 4);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Books, Third))), 4);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Plants, First))), 4);
        assertEquals(l.get(5, 4), new ItemCard(Cat, Second));
        assertEquals(l.get(4, 4), new ItemCard(Books, First));
        assertEquals(l.get(3, 4), new ItemCard(Frame, Second));
        assertEquals(l.get(2, 4), new ItemCard(Books, Third));
        assertEquals(l.get(1, 4), new ItemCard(Cat, First));
        assertEquals(l.get(0, 4), new ItemCard(Plants, First));

    }


    // Full library, always returns false
    @Test
    void checkSpace1() {
        Library l = new Library();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 4);
        assertEquals(l.checkSpace(0, 1), false);
        assertEquals(l.checkSpace(0, 3), false);
        assertEquals(l.checkSpace(1, 1), false);
        assertEquals(l.checkSpace(1, 3), false);
        assertEquals(l.checkSpace(2, 1), false);
        assertEquals(l.checkSpace(2, 2), false);
        assertEquals(l.checkSpace(3, 1), false);
        assertEquals(l.checkSpace(3, 6), false);
        assertEquals(l.checkSpace(4, 1), false);
        assertEquals(l.checkSpace(4, 3), false);
        assertEquals(l.checkSpace(5, 1), false);
        assertEquals(l.checkSpace(5, 2), false);
    }

    // Empty library, always returns true
    @Test
    void checkSpace2() {
        Library l = new Library();
        assertEquals(l.checkSpace(0, 1), true);
        assertEquals(l.checkSpace(0, 3), true);
        assertEquals(l.checkSpace(1, 1), true);
        assertEquals(l.checkSpace(1, 2), true);
        assertEquals(l.checkSpace(2, 1), true);
        assertEquals(l.checkSpace(2, 3), true);
        assertEquals(l.checkSpace(3, 2), true);
        assertEquals(l.checkSpace(3, 3), true);
        assertEquals(l.checkSpace(4, 1), true);
        assertEquals(l.checkSpace(4, 2), true);
    }

    @Test
    void get() {
    }


}