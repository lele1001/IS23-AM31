package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.ItemCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.HouseItem.*;
import static it.polimi.ingsw.server.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CG8Test {
    private final Bookshelf l1 = new Bookshelf();
    private final Bookshelf l2 = new Bookshelf();
    private final Bookshelf l3 = new Bookshelf();
    private final Bookshelf l4 = new Bookshelf();

    private final ItemCard c1 = new ItemCard(Cat, First);
    private final ItemCard c2 = new ItemCard(Cat, Second);
    private final ItemCard c3 = new ItemCard(Cat, Third);
    private final ItemCard f1 = new ItemCard(Frame, First);
    private final ItemCard f2 = new ItemCard(Frame, Second);
    private final ItemCard b1 = new ItemCard(Books, Third);
    private final ItemCard t1 = new ItemCard(Trophy, First);
    private final ItemCard t3 = new ItemCard(Trophy, Third);

    /**
     * Test with 2 players
     * 1st player - three equals tiles out of four: returns 0
     * 2nd player - two equals tiles out of four: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG8 comG8 = new CG8(2, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, f2, b1, f1, f1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, c2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, b1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        assertEquals(0, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - two equals tiles out of four: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG8 comG8 = new CG8(2, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, f2, b1, f1, c3));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, c2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, b1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - four equals tiles out of four: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG8 comG8 = new CG8(2, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, f2, b1, f1, c3));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(4, comG8.goalReached(l2));
    }

    /**
     * Test with 3 players
     * 1st player - three equals tiles out of four: returns 0
     * 2nd player - one equals tiles out of four: returns 0
     * 3rd player - two equals tiles out of four: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG8 comG8 = new CG8(3, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, f1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, c2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, f2));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, c1));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, b1));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        assertEquals(0, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(0, comG8.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - three equals tiles out of four: returns 8
     * 2nd player - two equals tiles out of four: returns 0
     * 3rd player - four equals tiles out of four: returns 0
     */
    @Test
    void threePlayers_one() {
        Bookshelf l1 = new Bookshelf();
        Bookshelf l2 = new Bookshelf();
        Bookshelf l3 = new Bookshelf();

        ItemCard c1 = new ItemCard(Cat, First);
        ItemCard c2 = new ItemCard(Cat, Second);
        ItemCard c3 = new ItemCard(Cat, Third);
        ItemCard f1 = new ItemCard(Frame, First);
        ItemCard f2 = new ItemCard(Frame, Second);
        ItemCard b1 = new ItemCard(Books, Third);

        CG8 comG8 = new CG8(3, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, f1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, c2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, b1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f2));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f1));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        assertEquals(0, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(8, comG8.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - two equals tiles out of four: returns 0
     * 3rd player - four equals tiles out of four: returns 6
     */
    @Test
    void threePlayers_two() {
        CG8 comG8 = new CG8(3, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, c1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, c2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, b1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f2));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f1));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(6, comG8.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - four equals tiles out of four: returns 6
     * 3rd player - four equals tiles out of four: returns 4
     */
    @Test
    void threePlayers_three() {
        CG8 comG8 = new CG8(3, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, c1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, b1));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, b1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f2));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f1));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(6, comG8.goalReached(l2));
        assertEquals(4, comG8.goalReached(l3));
    }

    /**
     * Test with 4 players
     * 1st player - three equals tiles out of four: returns 0
     * 2nd player - one equals tiles out of four: returns 0
     * 3rd player - two equals tiles out of four: returns 0
     * 4th player - two equals tiles out of four: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG8 comG8 = new CG8(4, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, f1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, c2));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, f2));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, c1));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, b1));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        List<ItemCard> col4_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, t1));
        List<ItemCard> col4_2 = new ArrayList<>(List.of(t3, c2, b1, b1, f1, b1));
        l4.insertCard(col4_1, 0);
        l4.insertCard(col4_2, 4);

        assertEquals(0, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(0, comG8.goalReached(l3));
        assertEquals(0, comG8.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - three equals tiles out of four: returns 0
     * 2nd player - two equals tiles out of four: returns 0
     * 3rd player - four equals tiles out of four: returns 8
     * 4th player - two equals tiles out of four: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG8 comG8 = new CG8(4, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, f1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, b1));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, f2));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f1));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f2));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        List<ItemCard> col4_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, t1));
        List<ItemCard> col4_2 = new ArrayList<>(List.of(t3, c2, b1, b1, f1, b1));
        l4.insertCard(col4_1, 0);
        l4.insertCard(col4_2, 4);

        assertEquals(0, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(8, comG8.goalReached(l3));
        assertEquals(0, comG8.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - two equals tiles out of four: returns 0
     * 3rd player - four equals tiles out of four: returns 6
     * 4th player - two equals tiles out of four: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG8 comG8 = new CG8(4, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, c1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, b1));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, f2));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f1));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f2));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        List<ItemCard> col4_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, t1));
        List<ItemCard> col4_2 = new ArrayList<>(List.of(t3, c2, b1, b1, f1, b1));
        l4.insertCard(col4_1, 0);
        l4.insertCard(col4_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(6, comG8.goalReached(l3));
        assertEquals(0, comG8.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - two equals tiles out of four: returns 0
     * 3rd player - four equals tiles out of four: returns 6
     * 4th player - four equals tiles out of four: returns 4
     */
    @Test
    void fourPlayers_three() {
        Bookshelf l1 = new Bookshelf();
        Bookshelf l2 = new Bookshelf();
        Bookshelf l3 = new Bookshelf();
        Bookshelf l4 = new Bookshelf();

        ItemCard c1 = new ItemCard(Cat, First);
        ItemCard c2 = new ItemCard(Cat, Second);
        ItemCard c3 = new ItemCard(Cat, Third);
        ItemCard f1 = new ItemCard(Frame, First);
        ItemCard f2 = new ItemCard(Frame, Second);
        ItemCard b1 = new ItemCard(Books, Third);
        ItemCard t1 = new ItemCard(Trophy, First);
        ItemCard t3 = new ItemCard(Trophy, Third);

        CG8 comG8 = new CG8(4, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, c1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, b1));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, f2));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f1));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f2));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        List<ItemCard> col4_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, t1));
        List<ItemCard> col4_2 = new ArrayList<>(List.of(t3, c2, b1, b1, f1, t1));
        l4.insertCard(col4_1, 0);
        l4.insertCard(col4_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(0, comG8.goalReached(l2));
        assertEquals(6, comG8.goalReached(l3));
        assertEquals(4, comG8.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - four equals tiles out of four: returns 8
     * 2nd player - four equals tiles out of four: returns 6
     * 3rd player - four equals tiles out of four: returns 4
     * 4th player - four equals tiles out of four: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG8 comG8 = new CG8(4, 8);

        List<ItemCard> col1_1 = new ArrayList<>(List.of(c1, c2, b1, b1, f1, c2));
        List<ItemCard> col1_2 = new ArrayList<>(List.of(c3, c2, b1, b1, f1, c1));
        l1.insertCard(col1_1, 0);
        l1.insertCard(col1_2, 4);

        List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, b1));
        List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, c2, b1, b1, f1, b1));
        l2.insertCard(col2_1, 0);
        l2.insertCard(col2_2, 4);

        List<ItemCard> col3_1 = new ArrayList<>(List.of(f1, c2, b1, b1, f1, f1));
        List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, c2, b1, b1, f1, f2));
        l3.insertCard(col3_1, 0);
        l3.insertCard(col3_2, 4);

        List<ItemCard> col4_1 = new ArrayList<>(List.of(t1, c2, b1, b1, f1, t1));
        List<ItemCard> col4_2 = new ArrayList<>(List.of(t3, c2, b1, b1, f1, t1));
        l4.insertCard(col4_1, 0);
        l4.insertCard(col4_2, 4);

        assertEquals(8, comG8.goalReached(l1));
        assertEquals(6, comG8.goalReached(l2));
        assertEquals(4, comG8.goalReached(l3));
        assertEquals(2, comG8.goalReached(l4));
    }
}