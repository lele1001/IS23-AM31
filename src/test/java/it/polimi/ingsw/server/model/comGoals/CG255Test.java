package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.HouseItem.*;
import static it.polimi.ingsw.server.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CG255Test {
    private final Bookshelf l1 = new Bookshelf();
    private final Bookshelf l2 = new Bookshelf();
    private final Bookshelf l3 = new Bookshelf();
    private final Bookshelf l4 = new Bookshelf();
    private final ItemCard c1 = new ItemCard(Cat, First);
    private final ItemCard f2 = new ItemCard(Frame, Second);
    private final ItemCard b1 = new ItemCard(Books, First);
    private final ItemCard t3 = new ItemCard(Trophy, Third);
    private final ItemCard g1 = new ItemCard(Games, First);
    private final ItemCard p3 = new ItemCard(Plants, Third);
    private final List<ItemCard> col1 = new ArrayList<>(List.of(g1, p3, p3, f2, g1));// diff but not enough tiles to complete the column
    private final List<ItemCard> col2 = new ArrayList<>(List.of(c1, g1));
    private final List<ItemCard> col3 = new ArrayList<>(List.of(f2, t3, b1, c1, b1, t3));
    private final List<ItemCard> col4 = new ArrayList<>(List.of(c1, t3, t3, c1, t3, p3)); //diff
    private final List<ItemCard> col5 = new ArrayList<>(List.of(f2, g1, p3, f2, g1, p3)); //diff
    private final List<ItemCard> col6 = new ArrayList<>(List.of(f2, g1, f2, g1, g1, f2)); //diff

    /**
     * Test with 2 players
     * 1st player - zero columns: returns 0
     * 2nd player - one column: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG2_5 comG5 = new CG2_5(2, 5);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col2, 4);

        assertEquals(0, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - two columns: returns 8
     * 2nd player - one column: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG2_5 comG5 = new CG2_5(2, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col3, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - two columns: returns 8
     * 2nd player - three columns: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG2_5 comG5 = new CG2_5(2, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col3, 2);
        l2.insertCard(col6, 3);
        l2.insertCard(col1, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(4, comG5.goalReached(l2));
    }

    /**
     * Test with 3 players
     * 1st player - zero columns: returns 0
     * 2nd player - one column: returns 0
     * 3rd player - zero columns: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG2_5 comG5 = new CG2_5(3, 5);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        assertEquals(0, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(0, comG5.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - two columns: returns 8
     * 2nd player - one column: returns 0
     * 3rd player - zero columns: returns 0
     */
    @Test
    void threePlayers_one() {
        CG2_5 comG5 = new CG2_5(3, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col5, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(0, comG5.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - two columns: returns 8
     * 2nd player - one column: returns 0
     * 3rd player - five columns: returns 6
     */
    @Test
    void threePlayers_two() {
        CG2_5 comG5 = new CG2_5(3, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col5, 4);

        l3.insertCard(col6, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col6, 2);
        l3.insertCard(col4, 3);
        l3.insertCard(col4, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(6, comG5.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - two columns: returns 8
     * 2nd player - three columns: returns 6
     * 3rd player - five columns: returns 4
     */
    @Test
    void threePlayers_three() {
        CG2_5 comG5 = new CG2_5(3, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col3, 2);
        l2.insertCard(col6, 3);
        l2.insertCard(col1, 4);

        l3.insertCard(col6, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col6, 2);
        l3.insertCard(col4, 3);
        l3.insertCard(col4, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(6, comG5.goalReached(l2));
        assertEquals(4, comG5.goalReached(l3));
    }

    /**
     * Test with 4 players
     * 1st player - zero columns: returns 0
     * 2nd player - one column: returns 0
     * 3rd player - zero columns: returns 0
     * 4th player - zero columns: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG2_5 comG5 = new CG2_5(4, 5);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col2, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(0, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(0, comG5.goalReached(l3));
        assertEquals(0, comG5.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - zero columns: returns 0
     * 2nd player - one column: returns 0
     * 3rd player - five columns: returns 8
     * 4th player - zero columns: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG2_5 comG5 = new CG2_5(4, 5);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col6, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col6, 2);
        l3.insertCard(col4, 3);
        l3.insertCard(col4, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col2, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(0, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(8, comG5.goalReached(l3));
        assertEquals(0, comG5.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - two columns: returns 8
     * 2nd player - one column: returns 0
     * 3rd player - five columns: returns 6
     * 4th player - zero columns: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG2_5 comG5 = new CG2_5(4, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col6, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col6, 2);
        l3.insertCard(col4, 3);
        l3.insertCard(col4, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col2, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(6, comG5.goalReached(l3));
        assertEquals(0, comG5.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - two columns: returns 8
     * 2nd player - one column: returns 0
     * 3rd player - five columns: returns 6
     * 4th player - two columns: returns 4
     */
    @Test
    void fourPlayers_three() {
        CG2_5 comG5 = new CG2_5(4, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col6, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col6, 2);
        l3.insertCard(col4, 3);
        l3.insertCard(col4, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col4, 1);
        l4.insertCard(col4, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(0, comG5.goalReached(l2));
        assertEquals(6, comG5.goalReached(l3));
        assertEquals(4, comG5.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - two columns: returns 8
     * 2nd player - three column: returns 6
     * 3rd player - five columns: returns 4
     * 4th player - two columns: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG2_5 comG5 = new CG2_5(4, 5);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col3, 2);
        l2.insertCard(col6, 3);
        l2.insertCard(col1, 4);

        l3.insertCard(col6, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col6, 2);
        l3.insertCard(col4, 3);
        l3.insertCard(col4, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col4, 1);
        l4.insertCard(col4, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG5.goalReached(l1));
        assertEquals(6, comG5.goalReached(l2));
        assertEquals(4, comG5.goalReached(l3));
        assertEquals(2, comG5.goalReached(l4));
    }
}