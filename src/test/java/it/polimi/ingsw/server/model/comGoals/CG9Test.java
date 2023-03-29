package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.HouseItem.*;
import static it.polimi.ingsw.server.model.HouseItem.Trophy;
import static it.polimi.ingsw.server.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.*;

class CG9Test {
    private final Bookshelf l0 = new Bookshelf();
    private final Bookshelf l3 = new Bookshelf();
    private final Bookshelf l5 = new Bookshelf();
    private final Bookshelf l8 = new Bookshelf();
    private final Bookshelf l9 = new Bookshelf();
    private final ItemCard c1 = new ItemCard(Cat, First);
    private final ItemCard f1 = new ItemCard(Frame, First);
    private final ItemCard f2 = new ItemCard(Frame, Second);
    private final ItemCard b1 = new ItemCard(Books, First);
    private final ItemCard t3 = new ItemCard(Trophy, Third);
    private final List<ItemCard> col1 = new ArrayList<>(List.of(f2));
    private final List<ItemCard> col2 = new ArrayList<>(List.of(c1, t3));
    private final List<ItemCard> col4 = new ArrayList<>(List.of(c1, f1, b1, t3));
    private final List<ItemCard> col5 = new ArrayList<>(List.of(f2, t3, b1, f1, t3));
    private final List<ItemCard> col7 = new ArrayList<>(List.of(f2, t3, b1, f1, b1, t3));
    private final List<ItemCard> col8 = new ArrayList<>(List.of(c1, t3, b1, t3));
    private final List<ItemCard> col9 = new ArrayList<>(List.of(t3, t3, t3));

    /**
     * Test with 2 players
     * 1st player - zero occurrences: returns 0
     * 2nd player - five occurrences: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG9 comG9 = new CG9(2, 9);

        l0.insertCard(col1, 0);
        l0.insertCard(col1, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        assertEquals(0, comG9.goalReached(l0));
        assertEquals(0, comG9.goalReached(l5));
    }

    /**
     * Test with 2 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - five occurrences: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG9 comG9 = new CG9(2, 9);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l5));
    }

    /**
     * Test with 2 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - nine occurrences: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG9 comG9 = new CG9(2, 9);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l9.insertCard(col5, 0);
        l9.insertCard(col2, 1);
        l9.insertCard(col7, 2);
        l9.insertCard(col4, 3);
        l9.insertCard(col9, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(4, comG9.goalReached(l9));
    }

    /**
     * Test with 3 players
     * 1st player - zero occurrences: returns 0
     * 2nd player - five occurrences: returns 0
     * 3rd player - three occurrences: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG9 comG9 = new CG9(3, 9);

        l0.insertCard(col1, 0);
        l0.insertCard(col1, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l3.insertCard(col1, 1);
        l3.insertCard(col9, 3);

        assertEquals(0, comG9.goalReached(l0));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(0, comG9.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - five occurrences: returns 0
     * 3rd player - three occurrences: returns 0
     */
    @Test
    void threePlayers_one() {
        CG9 comG9 = new CG9(3, 9);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l3.insertCard(col1, 1);
        l3.insertCard(col9, 3);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(0, comG9.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - five occurrences: returns 0
     * 3rd player - nine occurrences: returns 6
     */
    @Test
    void threePlayers_two() {
        CG9 comG9 = new CG9(3, 9);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l9.insertCard(col5, 0);
        l9.insertCard(col2, 1);
        l9.insertCard(col7, 2);
        l9.insertCard(col4, 3);
        l9.insertCard(col9, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(6, comG9.goalReached(l9));
    }

    /**
     * Test with 3 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - eight occurrences: returns 6
     * 3rd player - nine occurrences: returns 4
     */
    @Test
    void threePlayers_three() {
        CG9 comG9 = new CG9(3, 9);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l9.insertCard(col5, 0);
        l9.insertCard(col2, 1);
        l9.insertCard(col7, 2);
        l9.insertCard(col4, 3);
        l9.insertCard(col9, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(6, comG9.goalReached(l8));
        assertEquals(4, comG9.goalReached(l9));
    }

    /**
     * Test with 4 players
     * 1st player - zero occurrences: returns 0
     * 2nd player - five occurrences: returns 0
     * 3rd player - three occurrences: returns 0
     * 4th player - zero occurrences: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG9 comG9 = new CG9(4, 9);

        l0.insertCard(col1, 0);
        l0.insertCard(col1, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l3.insertCard(col1, 1);
        l3.insertCard(col9, 3);

        assertEquals(0, comG9.goalReached(l0));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(0, comG9.goalReached(l3));
        assertEquals(0, comG9.goalReached(l0));
    }

    /**
     * Test with 4 players
     * 1st player - zero occurrences: returns 0
     * 2nd player - five occurrences: returns 0
     * 3rd player - eight occurrences: returns 8
     * 4th player - zero occurrences: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG9 comG9 = new CG9(4, 9);

        l0.insertCard(col1, 0);
        l0.insertCard(col1, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        assertEquals(0, comG9.goalReached(l0));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(8, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l0));
    }

    /**
     * Test with 4 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - five occurrences: returns 0
     * 3rd player - eight occurrences: returns 6
     * 4th player - zero occurrences: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG9 comG9 = new CG9(4, 9);

        l0.insertCard(col1, 0);
        l0.insertCard(col1, 4);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(6, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l0));
    }

    /**
     * Test with 4 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - five occurrences: returns 0
     * 3rd player - eight occurrences: returns 6
     * 4th player - nine occurrences: returns 4
     */
    @Test
    void fourPlayers_three() {
        CG9 comG9 = new CG9(4, 9);

        l5.insertCard(col5, 1);
        l5.insertCard(col2, 3);
        l5.insertCard(col8, 4);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l9.insertCard(col5, 0);
        l9.insertCard(col2, 1);
        l9.insertCard(col7, 2);
        l9.insertCard(col4, 3);
        l9.insertCard(col9, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(0, comG9.goalReached(l5));
        assertEquals(6, comG9.goalReached(l8));
        assertEquals(4, comG9.goalReached(l9));
    }

    /**
     * Test with 4 players
     * 1st player - eight occurrences: returns 8
     * 2nd player - nine occurrences: returns 6
     * 3rd player - eight occurrences: returns 4
     * 4th player - nine occurrences: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG9 comG9 = new CG9(4, 9);

        l8.insertCard(col5, 0);
        l8.insertCard(col2, 1);
        l8.insertCard(col8, 2);
        l8.insertCard(col7, 3);
        l8.insertCard(col2, 4);

        l9.insertCard(col5, 0);
        l9.insertCard(col2, 1);
        l9.insertCard(col7, 2);
        l9.insertCard(col4, 3);
        l9.insertCard(col9, 4);

        assertEquals(8, comG9.goalReached(l8));
        assertEquals(6, comG9.goalReached(l9));
        assertEquals(4, comG9.goalReached(l8));
        assertEquals(2, comG9.goalReached(l9));
    }
}