package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.server.model.HouseItem.*;
import static it.polimi.ingsw.server.model.HouseItem.Plants;
import static it.polimi.ingsw.server.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.*;

class CG10Test {
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
    private final List<ItemCard> col1 = new ArrayList<>(List.of(c1, b1, c1, b1, f2, t3));
    private final List<ItemCard> col2 = new ArrayList<>(List.of(g1, c1, b1));
    private final List<ItemCard> col3 = new ArrayList<>(List.of(c1, p3, c1, p3));
    private final List<ItemCard> col4 = new ArrayList<>(List.of(b1, t3, p3, g1, g1, f2));
    private final List<ItemCard> col5 = new ArrayList<>(List.of(f2, p3, t3, p3, g1));

    /** Test with 2 players
     * 1st player - zero groups: returns 0
     * 2nd player - zero groups: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG10 comG10 = new CG10(2, 10);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        assertEquals(0, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
    }

    /** Test with 2 players
     * 1st player - one group: returns 8
     * 2nd player - zero groups: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG10 comG10 = new CG10(2, 10);

        l1.insertCard(col1, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
    }

    /** Test with 2 players
     * 1st player - one group: returns 8
     * 2nd player - one group: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG10 comG10 = new CG10(2, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col1, 2);
        l2.insertCard(col2, 3);
        l2.insertCard(col3, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(4, comG10.goalReached(l2));
    }

    /** Test with 3 players
     * 1st player - zero groups: returns 0
     * 2nd player - zero groups: returns 0
     * 3rd player - zero groups: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG10 comG10 = new CG10(3, 10);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        assertEquals(0, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(0, comG10.goalReached(l3));
    }

    /** Test with 3 players
     * 1st player - one group: returns 8
     * 2nd player - zero groups: returns 0
     * 3rd player - zero groups: returns 0
     */
    @Test
    void threePlayers_one() {
        CG10 comG10 = new CG10(3, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(0, comG10.goalReached(l3));
    }

    /** Test with 3 players
     * 1st player - one group: returns 8
     * 2nd player - zero groups: returns 0
     * 3rd player - one group: returns 6
     */
    @Test
    void threePlayers_two() {
        CG10 comG10 = new CG10(3, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col4, 0);
        l3.insertCard(col1, 1);
        l3.insertCard(col2, 2);
        l3.insertCard(col3, 3);
        l3.insertCard(col5, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(6, comG10.goalReached(l3));
    }

    /** Test with 3 players
     * 1st player - one group: returns 8
     * 2nd player - one group: returns 6
     * 3rd player - one group: returns 4
     */
    @Test
    void threePlayers_three() {
        CG10 comG10 = new CG10(3, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col1, 2);
        l2.insertCard(col2, 3);
        l2.insertCard(col3, 4);

        l3.insertCard(col4, 0);
        l3.insertCard(col1, 1);
        l3.insertCard(col2, 2);
        l3.insertCard(col3, 3);
        l3.insertCard(col5, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(6, comG10.goalReached(l2));
        assertEquals(4, comG10.goalReached(l3));
    }

    /** Test with 4 players
     * 1st player - zero groups: returns 0
     * 2nd player - zero groups: returns 0
     * 3rd player - zero groups: returns 0
     * 4th player - zero groups: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG10 comG10 = new CG10(4, 10);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col1, 0);
        l3.insertCard(col2, 2);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col5, 4);

        assertEquals(0, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(0, comG10.goalReached(l3));
        assertEquals(0, comG10.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - zero groups: returns 0
     * 2nd player - zero groups: returns 0
     * 3rd player - one group: returns 8
     * 4th player - zero groups: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG10 comG10 = new CG10(4, 10);

        l1.insertCard(col1, 0);
        l1.insertCard(col2, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col4, 0);
        l3.insertCard(col1, 1);
        l3.insertCard(col2, 2);
        l3.insertCard(col3, 3);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col5, 4);

        assertEquals(0, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(8, comG10.goalReached(l3));
        assertEquals(0, comG10.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - one group: returns 8
     * 2nd player - zero groups: returns 0
     * 3rd player - one group: returns 6
     * 4th player - zero groups: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG10 comG10 = new CG10(4, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col4, 0);
        l3.insertCard(col1, 1);
        l3.insertCard(col2, 2);
        l3.insertCard(col3, 3);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col1, 1);
        l4.insertCard(col5, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(6, comG10.goalReached(l3));
        assertEquals(0, comG10.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - one group: returns 8
     * 2nd player - zero groups: returns 0
     * 3rd player - one group: returns 6
     * 4th player - two groups: returns 4
     */
    @Test
    void fourPlayers_three() {
        CG10 comG10 = new CG10(4, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col1, 0);
        l2.insertCard(col3, 3);
        l2.insertCard(col2, 4);

        l3.insertCard(col4, 0);
        l3.insertCard(col1, 1);
        l3.insertCard(col2, 2);
        l3.insertCard(col3, 3);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col2, 1);
        l4.insertCard(col3, 2);
        l4.insertCard(col4, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(0, comG10.goalReached(l2));
        assertEquals(6, comG10.goalReached(l3));
        assertEquals(4, comG10.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - one group: returns 8
     * 2nd player - one group: returns 6
     * 3rd player - one group: returns 4
     * 4th player - two groups: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG10 comG10 = new CG10(4, 10);

        l1.insertCard(col2, 0);
        l1.insertCard(col3, 1);
        l1.insertCard(col4, 2);
        l1.insertCard(col5, 3);
        l1.insertCard(col1, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col4, 1);
        l2.insertCard(col1, 2);
        l2.insertCard(col2, 3);
        l2.insertCard(col3, 4);

        l3.insertCard(col4, 0);
        l3.insertCard(col1, 1);
        l3.insertCard(col2, 2);
        l3.insertCard(col3, 3);
        l3.insertCard(col5, 4);

        l4.insertCard(col1, 0);
        l4.insertCard(col2, 1);
        l4.insertCard(col3, 2);
        l4.insertCard(col4, 3);
        l4.insertCard(col5, 4);

        assertEquals(8, comG10.goalReached(l1));
        assertEquals(6, comG10.goalReached(l2));
        assertEquals(4, comG10.goalReached(l3));
        assertEquals(2, comG10.goalReached(l4));
    }
}