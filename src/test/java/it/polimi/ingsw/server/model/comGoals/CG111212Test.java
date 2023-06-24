package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.ItemCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.HouseItem.*;
import static it.polimi.ingsw.server.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CG111212Test {
    private final Bookshelf l1 = new Bookshelf();
    private final Bookshelf l2 = new Bookshelf();
    private final Bookshelf l3 = new Bookshelf();
    private final Bookshelf l4 = new Bookshelf();
    private final ItemCard c1 = new ItemCard(Cat, First);
    private final ItemCard f1 = new ItemCard(Frame, First);
    private final ItemCard f2 = new ItemCard(Frame, Second);
    private final ItemCard b1 = new ItemCard(Books, First);
    private final ItemCard t3 = new ItemCard(Trophy, Third);
    private final List<ItemCard> col1 = new ArrayList<>(List.of(t3));
    private final List<ItemCard> col2 = new ArrayList<>(List.of(c1, t3));
    private final List<ItemCard> col3 = new ArrayList<>(List.of(f2, f2, f2));
    private final List<ItemCard> col4 = new ArrayList<>(List.of(c1, f1, b1, t3));
    private final List<ItemCard> col5 = new ArrayList<>(List.of(f1, t3, b1, f1, b1));
    private final List<ItemCard> col6 = new ArrayList<>(List.of(f2, t3, b1, f1, b1, c1));

    /**
     * Test with 2 players
     * 1st player - only two columns: returns 0
     * 2nd player - five mixed columns: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG11_12 comG11 = new CG11_12(2, 12);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col2, 0);
        l2.insertCard(col2, 1);
        l2.insertCard(col3, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col5, 4);

        assertEquals(0, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five mixed columns: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG11_12 comG11 = new CG11_12(2, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col1, 1);
        l2.insertCard(col3, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col5, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five ordered columns, offset 1, direction 0: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG11_12 comG11 = new CG11_12(2, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col3, 2);
        l2.insertCard(col2, 3);
        l2.insertCard(col1, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(4, comG11.goalReached(l2));
    }

    /**
     * Test with 3 players
     * 1st player - empty library: returns 0
     * 2nd player - five mixed columns: returns 0
     * 3rd player - only three columns: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG11_12 comG11 = new CG11_12(3, 12);


        l2.insertCard(col4, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col4, 2);
        l2.insertCard(col5, 3);
        l2.insertCard(col6, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        assertEquals(0, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(0, comG11.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five mixed columns: returns 0
     * 3rd player - only three columns: returns 0
     */
    @Test
    void threePlayers_one() {
        CG11_12 comG11 = new CG11_12(3, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col4, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col3, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(0, comG11.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five mixed column: returns 0
     * 3rd player - five ordered columns, offset 1, direction 0: returns 6
     */
    @Test
    void threePlayers_two() {
        CG11_12 comG11 = new CG11_12(3, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col6, 1);
        l2.insertCard(col6, 2);
        l2.insertCard(col6, 3);
        l2.insertCard(col6, 4);

        l3.insertCard(col5, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col3, 2);
        l3.insertCard(col2, 3);
        l3.insertCard(col1, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(6, comG11.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five ordered columns, offset 0, direction 0: returns 6
     * 3rd player - five ordered columns, offset 1, direction 0: returns 4
     */
    @Test
    void threePlayers_three() {
        CG11_12 comG11 = new CG11_12(3, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col4, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col5, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col3, 2);
        l3.insertCard(col2, 3);
        l3.insertCard(col1, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(6, comG11.goalReached(l2));
        assertEquals(4, comG11.goalReached(l3));
    }

    /**
     * Test with 4 players
     * 1st player - only two columns: returns 0
     * 2nd player - five mixed columns: returns 0
     * 3rd player - only three columns: returns 0
     * 4th player - five mixed columns: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG11_12 comG11 = new CG11_12(4, 12);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col2, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(0, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(0, comG11.goalReached(l3));
        assertEquals(0, comG11.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - only two columns: returns 0
     * 2nd player - five mixed columns: returns 0
     * 3rd player - five ordered columns, offset 1, direction 0: returns 8
     * 4th player - five mixed columns: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG11_12 comG11 = new CG11_12(4, 12);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col5, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col3, 2);
        l3.insertCard(col2, 3);
        l3.insertCard(col1, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col2, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(0, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(8, comG11.goalReached(l3));
        assertEquals(0, comG11.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five mixed columns: returns 0
     * 3rd player - five ordered columns, offset 1, direction 0: returns 6
     * 4th player - five mixed columns: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG11_12 comG11 = new CG11_12(4, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col5, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col3, 2);
        l3.insertCard(col2, 3);
        l3.insertCard(col1, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col2, 2);
        l4.insertCard(col5, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(6, comG11.goalReached(l3));
        assertEquals(0, comG11.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five mixed columns: returns 0
     * 3rd player - five ordered columns, offset 1, direction 0: returns 6
     * 4th player - five ordered columns, offset 1, direction 1: returns 4
     */
    @Test
    void fourPlayers_three() {
        CG11_12 comG11 = new CG11_12(4, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col2, 2);
        l2.insertCard(col4, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col5, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col3, 2);
        l3.insertCard(col2, 3);
        l3.insertCard(col1, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col2, 1);
        l4.insertCard(col3, 2);
        l4.insertCard(col4, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(0, comG11.goalReached(l2));
        assertEquals(6, comG11.goalReached(l3));
        assertEquals(4, comG11.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - five ordered columns, offset 0, direction 1: returns 8
     * 2nd player - five ordered columns, offset 0, direction 0: returns 6
     * 3rd player - five ordered columns, offset 1, direction 0: returns 4
     * 4th player - five ordered columns, offset 1, direction 1: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG11_12 comG11 = new CG11_12(4, 12);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col6, 4);

        l2.insertCard(col6, 0);
        l2.insertCard(col5, 1);
        l2.insertCard(col4, 2);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col5, 0);
        l3.insertCard(col4, 1);
        l3.insertCard(col3, 2);
        l3.insertCard(col2, 3);
        l3.insertCard(col1, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col2, 1);
        l4.insertCard(col3, 2);
        l4.insertCard(col4, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG11.goalReached(l1));
        assertEquals(6, comG11.goalReached(l2));
        assertEquals(4, comG11.goalReached(l3));
        assertEquals(2, comG11.goalReached(l4));
    }
}