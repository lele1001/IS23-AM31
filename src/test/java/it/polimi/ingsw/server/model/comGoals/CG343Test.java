package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.commons.ItemCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.commons.HouseItem.*;
import static it.polimi.ingsw.commons.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the {@link CG3_4} methods
 */
public class CG343Test {
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
    private final List<ItemCard> colA = new ArrayList<>(List.of(p3, t3, p3, p3, p3, p3));
    private final List<ItemCard> colB = new ArrayList<>(List.of(t3, p3, g1, f2, t3, b1));
    private final List<ItemCard> colC = new ArrayList<>(List.of(g1, t3, t3, t3, t3));
    private final List<ItemCard> colD = new ArrayList<>(List.of(g1, g1, f2, f2, t3));
    private final List<ItemCard> colE = new ArrayList<>(List.of(f2, g1, t3));
    private final List<ItemCard> colF = new ArrayList<>(List.of(t3, p3, c1, c1, c1, c1));
    private final List<ItemCard> colG = new ArrayList<>(List.of(g1, g1, b1, t3));
    private final List<ItemCard> colH = new ArrayList<>(List.of(t3, t3, g1, f2, f2));
    private final List<ItemCard> colI = new ArrayList<>(List.of(f2, b1, b1, b1, b1));

    /**
     * Test with 2 players
     * 1st player - two groups: returns 0
     * 2nd player - two groups: returns 0
     */
    @Test
    public void twoPlayers_zero() {
        CG3_4 comG3 = new CG3_4(2, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        assertEquals(0, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - four groups: returns 8
     * 2nd player - two groups: returns 0
     */
    @Test
    public void twoPlayers_one() {
        CG3_4 comG3 = new CG3_4(2, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - four groups: returns 8
     * 2nd player - four groups: returns 4
     */
    @Test
    public void twoPlayers_two() {
        CG3_4 comG3 = new CG3_4(2, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colE, 2);
        l2.insertCard(colD, 3);
        l2.insertCard(colD, 4);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(4, comG3.goalReached(l2));
    }

    /**
     * Test with 3 players
     * 1st player - two groups: returns 0
     * 2nd player - two groups: returns 0
     * 3rd player - three groups: returns 0
     */
    @Test
    public void threePlayers_zero() {
        CG3_4 comG3 = new CG3_4(3, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colC, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colF, 2);
        l3.insertCard(colI, 3);

        assertEquals(0, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(0, comG3.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - four groups: returns 8
     * 2nd player - two groups: returns 0
     * 3rd player - three groups: returns 0
     */
    @Test
    public void threePlayers_one() {
        CG3_4 comG3 = new CG3_4(3, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colC, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colF, 2);
        l3.insertCard(colI, 3);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(0, comG3.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - four groups: returns 8
     * 2nd player - two groups: returns 0
     * 3rd player - six groups: returns 6
     */
    @Test
    public void threePlayers_two() {
        CG3_4 comG3 = new CG3_4(3, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colB, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colB, 2);
        l3.insertCard(colB, 3);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(6, comG3.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - four groups: returns 8
     * 2nd player - four groups: returns 6
     * 3rd player - six groups: returns 4
     */
    @Test
    public void threePlayers_three() {
        CG3_4 comG3 = new CG3_4(3, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colE, 2);
        l2.insertCard(colD, 3);
        l2.insertCard(colD, 4);

        l3.insertCard(colB, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colB, 2);
        l3.insertCard(colB, 3);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(6, comG3.goalReached(l2));
        assertEquals(4, comG3.goalReached(l3));
    }

    /**
     * Test with 4 players
     * 1st player - two groups: returns 0
     * 2nd player - two groups: returns 0
     * 3rd player - three groups: returns 0
     * 4th player - zero groups: returns 0
     */
    @Test
    public void fourPlayers_zero() {
        CG3_4 comG3 = new CG3_4(4, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colC, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colF, 2);
        l3.insertCard(colI, 3);

        l4.insertCard(colD, 0);
        l4.insertCard(colE, 1);
        l4.insertCard(colE, 3);
        l4.insertCard(colE, 4);

        assertEquals(0, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(0, comG3.goalReached(l3));
        assertEquals(0, comG3.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - two groups: returns 0
     * 2nd player - two groups: returns 0
     * 3rd player - six groups: returns 8
     * 4th player - zero groups: returns 0
     */
    @Test
    public void fourPlayers_one() {
        CG3_4 comG3 = new CG3_4(4, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colB, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colB, 2);
        l3.insertCard(colB, 3);

        l4.insertCard(colD, 0);
        l4.insertCard(colE, 1);
        l4.insertCard(colE, 3);
        l4.insertCard(colE, 4);

        assertEquals(0, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(8, comG3.goalReached(l3));
        assertEquals(0, comG3.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - four groups: returns 8
     * 2nd player - two groups: returns 0
     * 3rd player - six groups: returns 6
     * 4th player - zero groups: returns 0
     */
    @Test
    public void fourPlayers_two() {
        CG3_4 comG3 = new CG3_4(4, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colB, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colB, 2);
        l3.insertCard(colB, 3);

        l4.insertCard(colD, 0);
        l4.insertCard(colE, 1);
        l4.insertCard(colE, 3);
        l4.insertCard(colE, 4);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(6, comG3.goalReached(l3));
        assertEquals(0, comG3.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - four groups: returns 8
     * 2nd player - two groups: returns 0
     * 3rd player - six groups: returns 6
     * 4th player - four groups: returns 4
     */
    @Test
    public void fourPlayers_three() {
        CG3_4 comG3 = new CG3_4(4, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colG, 1);
        l2.insertCard(colH, 2);
        l2.insertCard(colI, 3);
        l2.insertCard(colE, 4);

        l3.insertCard(colB, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colB, 2);
        l3.insertCard(colB, 3);

        l4.insertCard(colH, 0);
        l4.insertCard(colH, 1);
        l4.insertCard(colF, 2);
        l4.insertCard(colD, 3);
        l4.insertCard(colD, 4);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(0, comG3.goalReached(l2));
        assertEquals(6, comG3.goalReached(l3));
        assertEquals(4, comG3.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - four groups: returns 8
     * 2nd player - four groups: returns 6
     * 3rd player - six groups: returns 4
     * 4th player - four groups: returns 2
     */
    @Test
    public void fourPlayers_four() {
        CG3_4 comG3 = new CG3_4(4, 3);

        l1.insertCard(colA, 0);
        l1.insertCard(colG, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colI, 3);
        l1.insertCard(colA, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colE, 2);
        l2.insertCard(colD, 3);
        l2.insertCard(colD, 4);

        l3.insertCard(colB, 0);
        l3.insertCard(colB, 1);
        l3.insertCard(colB, 2);
        l3.insertCard(colB, 3);

        l4.insertCard(colH, 0);
        l4.insertCard(colH, 1);
        l4.insertCard(colF, 2);
        l4.insertCard(colD, 3);
        l4.insertCard(colD, 4);

        assertEquals(8, comG3.goalReached(l1));
        assertEquals(6, comG3.goalReached(l2));
        assertEquals(4, comG3.goalReached(l3));
        assertEquals(2, comG3.goalReached(l4));
    }
}