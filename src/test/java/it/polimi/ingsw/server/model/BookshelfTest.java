package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.HouseItem.*;
import static it.polimi.ingsw.server.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.*;

class BookshelfTest {

    @Test
    void insertCard() {
        Bookshelf l = new Bookshelf();
        int i;
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second))), 2);
        assertEquals(l.get(5, 2), new ItemCard(Cat, First));
        assertEquals(l.get(4, 2), new ItemCard(Frame, Second));

        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third))), 2);
        assertEquals(l.get(5, 2), new ItemCard(Cat, First));
        assertEquals(l.get(4, 2), new ItemCard(Frame, Second));
        assertEquals(l.get(3, 2), new ItemCard(Books, Third));

        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third))), 1);
        assertEquals(l.get(5, 1), new ItemCard(Books, Third));
        for (i = 4; i >= 0; i--)
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
        Bookshelf l = new Bookshelf();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 4);
        assertFalse(l.checkSpace(0, 1));
        assertFalse(l.checkSpace(0, 3));
        assertFalse(l.checkSpace(1, 1));
        assertFalse(l.checkSpace(1, 3));
        assertFalse(l.checkSpace(2, 1));
        assertFalse(l.checkSpace(2, 2));
        assertFalse(l.checkSpace(3, 1));
        assertFalse(l.checkSpace(3, 6));
        assertFalse(l.checkSpace(4, 1));
        assertFalse(l.checkSpace(4, 3));
        assertFalse(l.checkSpace(5, 1));
        assertFalse(l.checkSpace(5, 2));
    }

    // Empty library, always returns true
    @Test
    void checkSpace2() {
        Bookshelf l = new Bookshelf();
        assertTrue(l.checkSpace(0, 1));
        assertTrue(l.checkSpace(0, 3));
        assertTrue(l.checkSpace(1, 1));
        assertTrue(l.checkSpace(1, 2));
        assertTrue(l.checkSpace(2, 1));
        assertTrue(l.checkSpace(2, 3));
        assertTrue(l.checkSpace(3, 2));
        assertTrue(l.checkSpace(3, 3));
        assertTrue(l.checkSpace(4, 1));
        assertTrue(l.checkSpace(4, 2));
    }

    @Test
    void checkSpace3() {
        Bookshelf l = new Bookshelf();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Frame, Second))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Books, Third))), 4);


        assertTrue(l.checkSpace(0, 1));
        assertTrue(l.checkSpace(0, 3));
        assertFalse(l.checkSpace(0, 4));
        assertFalse(l.checkSpace(0, 5));

        assertTrue(l.checkSpace(1, 1));
        assertFalse(l.checkSpace(1, 2));
        assertFalse(l.checkSpace(1, 6));

        assertTrue(l.checkSpace(2, 2));
        assertTrue(l.checkSpace(2, 4));
        assertFalse(l.checkSpace(2, 6));

        assertTrue(l.checkSpace(4, 1));
        assertTrue(l.checkSpace(4, 2));
        assertFalse(l.checkSpace(4, 3));
        assertFalse(l.checkSpace(4, 5));

        assertFalse(l.checkSpace(7, 2));
        assertFalse(l.checkSpace(-25, 6));
        assertFalse(l.checkSpace(80, -9));
        assertFalse(l.checkSpace(1, -96));
        assertFalse(l.checkSpace(3, 7));

    }

    @Test
    void get() {
    }


    @Test
    void calcScore1() {
        Bookshelf l = new Bookshelf();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Games, First), new ItemCard(Cat, First))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Plants, First), new ItemCard(Games, First))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Trophy, First), new ItemCard(Cat, First))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Games, First), new ItemCard(Plants, First))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Cat, First), new ItemCard(Plants, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First), new ItemCard(Books, First), new ItemCard(Books, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Trophy, First), new ItemCard(Plants, First))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First), new ItemCard(Books, First), new ItemCard(Cat, First))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Trophy, First), new ItemCard(Plants, First))), 4);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First), new ItemCard(Cat, First), new ItemCard(Books, First))), 4);

        assertEquals(l.calcScore(), 18);

    }

    @Test
    void calcScore2() {
        Bookshelf l = new Bookshelf();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Books, First), new ItemCard(Frame, First))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Frame, First), new ItemCard(Books, First))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, First), new ItemCard(Books, First), new ItemCard(Books, First))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Games, First), new ItemCard(Games, First))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First), new ItemCard(Books, First), new ItemCard(Trophy, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Trophy, First), new ItemCard(Trophy, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Trophy, First), new ItemCard(Trophy, First))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First), new ItemCard(Cat, First), new ItemCard(Plants, First))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First), new ItemCard(Trophy, First), new ItemCard(Trophy, First))), 4);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Cat, First), new ItemCard(Cat, First))), 4);

        assertEquals(l.calcScore(), 18);
    }

    @Test
    void calcScore3() {
        Bookshelf l = new Bookshelf();
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Cat, First), new ItemCard(Cat, First))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 0);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Frame, First), new ItemCard(Frame, First))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 1);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Frame, First), new ItemCard(Frame, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First), new ItemCard(Cat, First))), 2);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Frame, First), new ItemCard(Frame, First))), 3);
        assertEquals(l.calcScore(), 13);

        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, First), new ItemCard(Books, First), new ItemCard(Books, First))), 3);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Cat, First), new ItemCard(Games, First))), 4);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, First))), 4);
        l.insertCard(new ArrayList<>(List.of(new ItemCard(Books, First))), 2);
        assertEquals(l.calcScore(), 18);
    }
}