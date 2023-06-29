package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.commons.ItemCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.commons.HouseItem.*;
import static it.polimi.ingsw.commons.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CG1Test {
    private final Bookshelf l1 = new Bookshelf();
    private final Bookshelf l2 = new Bookshelf();
    private final Bookshelf l3 = new Bookshelf();
    private final Bookshelf l4 = new Bookshelf();
    private final ItemCard c1 = new ItemCard(Cat, First);
    private final ItemCard f2 = new ItemCard(Frame, Second);
    private final ItemCard b1 = new ItemCard(Books, First);
    private final ItemCard t3 = new ItemCard(Trophy, Third);
    private final ItemCard p2 = new ItemCard(Plants, Second);
    private final ItemCard g3 = new ItemCard(Games, Third);

    private final List<ItemCard> colA = new ArrayList<>(List.of(b1));
    private final List<ItemCard> colB = new ArrayList<>(List.of(b1, c1, f2, f2, t3));
    private final List<ItemCard> colC = new ArrayList<>(List.of(p2, g3, g3, f2, f2));
    private final List<ItemCard> colD = new ArrayList<>(List.of(c1, g3, g3));
    private final List<ItemCard> colE = new ArrayList<>(List.of(c1, c1, t3, p2, t3));
    private final List<ItemCard> colF = new ArrayList<>(List.of(b1, f2, t3, t3, c1, c1));
    private final List<ItemCard> colG = new ArrayList<>(List.of(p2, p2));
    private final List<ItemCard> colH = new ArrayList<>(List.of(g3, c1, b1, b1));
    private final List<ItemCard> colI = new ArrayList<>(List.of(b1, g3, g3, f2, g3, g3));
    private final List<ItemCard> colL = new ArrayList<>(List.of(t3, p2, p2, b1));

    /**
     * Test with 2 players
     * 1st player - one square: returns 0
     * 2nd player - zero squares: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG1 comG1 = new CG1(2, 1);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - three squares: returns 8
     * 2nd player - zero squares: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG1 comG1 = new CG1(2, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colA, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
    }

    /**
     * Test with 2 players
     * 1st player - three squares: returns 8
     * 2nd player - two squares: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG1 comG1 = new CG1(2, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colB, 1);
        l2.insertCard(colB, 2);
        l2.insertCard(colD, 3);
        l2.insertCard(colD, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(4, comG1.goalReached(l2));
    }

    /**
     * Test with 3 players
     * 1st player - one square: returns 0
     * 2nd player - zero squares: returns 0
     * 3rd player - one square in 3×3 config: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG1 comG1 = new CG1(3, 1);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colL, 0);
        l3.insertCard(colI, 1);
        l3.insertCard(colL, 2);
        l3.insertCard(colB, 3);
        l3.insertCard(colE, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(0, comG1.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - three squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - zero squares: returns 0
     */
    @Test
    void threePlayers_one() {
        CG1 comG1 = new CG1(3, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colL, 0);
        l3.insertCard(colI, 1);
        l3.insertCard(colL, 2);
        l3.insertCard(colB, 3);
        l3.insertCard(colE, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(0, comG1.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - three squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - two squares: returns 6
     */
    @Test
    void threePlayers_two() {
        CG1 comG1 = new CG1(3, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colE, 0);
        l3.insertCard(colE, 1);
        l3.insertCard(colH, 3);
        l3.insertCard(colH, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(6, comG1.goalReached(l3));
    }

    /**
     * Test with 3 players
     * 1st player - three squares: returns 8
     * 2nd player - two squares: returns 6
     * 3rd player - two squares: returns 4
     */
    @Test
    void threePlayers_three() {
        CG1 comG1 = new CG1(3, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colB, 1);
        l2.insertCard(colB, 2);
        l2.insertCard(colD, 3);
        l2.insertCard(colD, 4);

        l3.insertCard(colE, 0);
        l3.insertCard(colE, 1);
        l3.insertCard(colH, 3);
        l3.insertCard(colH, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(6, comG1.goalReached(l2));
        assertEquals(4, comG1.goalReached(l3));
    }

    /**
     * Test with 4 players
     * 1st player - one square: returns 0
     * 2nd player - zero squares: returns 0
     * 3rd player - zero squares: returns 0
     * 4th player - one square: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG1 comG1 = new CG1(4, 1);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colL, 0);
        l3.insertCard(colI, 1);
        l3.insertCard(colL, 2);
        l3.insertCard(colB, 3);
        l3.insertCard(colE, 4);

        l4.insertCard(colI, 0);
        l4.insertCard(colD, 1);
        l4.insertCard(colG, 2);
        l4.insertCard(colF, 3);
        l4.insertCard(colH, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(0, comG1.goalReached(l3));
        assertEquals(0, comG1.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - one square: returns 0
     * 2nd player - zero squares: returns 0
     * 3rd player - two squares: returns 8
     * 4th player - one square: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG1 comG1 = new CG1(4, 1);

        l1.insertCard(colA, 0);
        l1.insertCard(colB, 1);
        l1.insertCard(colC, 2);
        l1.insertCard(colD, 3);
        l1.insertCard(colE, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colE, 0);
        l3.insertCard(colE, 1);
        l3.insertCard(colH, 3);
        l3.insertCard(colH, 4);

        l4.insertCard(colI, 0);
        l4.insertCard(colD, 1);
        l4.insertCard(colG, 2);
        l4.insertCard(colF, 3);
        l4.insertCard(colH, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(8, comG1.goalReached(l3));
        assertEquals(0, comG1.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - three squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - two squares: returns 6
     * 4th player - one square: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG1 comG1 = new CG1(4, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colE, 0);
        l3.insertCard(colE, 1);
        l3.insertCard(colH, 3);
        l3.insertCard(colH, 4);

        l4.insertCard(colI, 0);
        l4.insertCard(colD, 1);
        l4.insertCard(colG, 2);
        l4.insertCard(colF, 3);
        l4.insertCard(colH, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(6, comG1.goalReached(l3));
        assertEquals(0, comG1.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - three squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - two squares: returns 6
     * 4th player - four squares: returns 4
     */
    @Test
    void fourPlayers_three() {
        CG1 comG1 = new CG1(4, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colF, 0);
        l2.insertCard(colC, 1);
        l2.insertCard(colG, 2);
        l2.insertCard(colH, 3);
        l2.insertCard(colA, 4);

        l3.insertCard(colE, 0);
        l3.insertCard(colE, 1);
        l3.insertCard(colH, 3);
        l3.insertCard(colH, 4);

        l4.insertCard(colI, 0);
        l4.insertCard(colI, 1);
        l4.insertCard(colA, 2);
        l4.insertCard(colC, 3);
        l4.insertCard(colC, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(6, comG1.goalReached(l3));
        assertEquals(4, comG1.goalReached(l4));
    }

    /**
     * Test with 4 players
     * 1st player - three squares: returns 8
     * 2nd player - two squares: returns 6
     * 3rd player - two squares: returns 4
     * 4th player - four squares: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG1 comG1 = new CG1(4, 1);

        l1.insertCard(colF, 0);
        l1.insertCard(colF, 1);
        l1.insertCard(colG, 2);
        l1.insertCard(colG, 3);
        l1.insertCard(colB, 4);

        l2.insertCard(colB, 1);
        l2.insertCard(colB, 2);
        l2.insertCard(colD, 3);
        l2.insertCard(colD, 4);

        l3.insertCard(colE, 0);
        l3.insertCard(colE, 1);
        l3.insertCard(colH, 3);
        l3.insertCard(colH, 4);

        l4.insertCard(colI, 0);
        l4.insertCard(colI, 1);
        l4.insertCard(colA, 2);
        l4.insertCard(colC, 3);
        l4.insertCard(colC, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(6, comG1.goalReached(l2));
        assertEquals(4, comG1.goalReached(l3));
        assertEquals(2, comG1.goalReached(l4));
    }
}