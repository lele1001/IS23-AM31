package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.HouseItem.*;
import static it.polimi.ingsw.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.*;

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

    private final List<ItemCard> col0 = new ArrayList<>(List.of(t3));
    private final List<ItemCard> col2_1 = new ArrayList<>(List.of(b1, p2, p2));
    private final List<ItemCard> col2_2 = new ArrayList<>(List.of(b1, g3, t3, g3, p2, p2));
    private final List<ItemCard> col3_1 = new ArrayList<>(List.of(g3, p2, p2, p2));
    private final List<ItemCard> col3_2 = new ArrayList<>(List.of(f2, f2, p2, p2, p2));
    private final List<ItemCard> col3_3 = new ArrayList<>(List.of(c1, f2, g3, p2, p2, p2));
    private final List<ItemCard> col4_1 = new ArrayList<>(List.of(c1, p2, p2, p2, p2));
    private final List<ItemCard> col4_2 = new ArrayList<>(List.of(f2, p2, p2, p2, p2));
    private final List<ItemCard> col5 = new ArrayList<>(List.of(f2, c1, t3, c1, c1));

    /** Test with 2 players
     * 1st player - one square in 3x2 config: returns 0
     * 2nd player - zero squares: returns 0
     */
    @Test
    void twoPlayers_zero() {
        CG1 comG1 = new CG1(2);

        l1.insertCard(col2_1, 0);
        l1.insertCard(col2_1, 1);
        l1.insertCard(col2_1, 2);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
    }

    /** Test with 2 players
     * 1st player - two squares: returns 8
     * 2nd player - zero squares: returns 0
     */
    @Test
    void twoPlayers_one() {
        CG1 comG1 = new CG1(2);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
    }

    /** Test with 2 players
     * 1st player - two squares: returns 8
     * 2nd player - four squares: returns 4
     */
    @Test
    void twoPlayers_two() {
        CG1 comG1 = new CG1(2);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col4_1, 0);
        l2.insertCard(col4_1, 1);
        l2.insertCard(col4_2, 2);
        l2.insertCard(col4_2, 3);
        l2.insertCard(col0, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(4, comG1.goalReached(l2));
    }

    /** Test with 3 players
     * 1st player - one square in 3x2 config: returns 0
     * 2nd player - zero squares: returns 0
     * 3rd player - one square in 3x3 config: returns 0
     */
    @Test
    void threePlayers_zero() {
        CG1 comG1 = new CG1(3);

        l1.insertCard(col2_1, 0);
        l1.insertCard(col2_1, 1);
        l1.insertCard(col2_1, 2);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col3_1, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col3_1, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(0, comG1.goalReached(l3));
    }

    /** Test with 3 players
     * 1st player - two squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - one square in 3x3 config: returns 0
     */
    @Test
    void threePlayers_one() {
        CG1 comG1 = new CG1(3);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col3_1, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col3_1, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(0, comG1.goalReached(l3));
    }

    /** Test with 3 players
     * 1st player - two squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - three squares: returns 6
     */
    @Test
    void threePlayers_two() {
        CG1 comG1 = new CG1(3);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col0, 0);
        l3.insertCard(col4_1, 1);
        l3.insertCard(col4_2, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col2_1, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(6, comG1.goalReached(l3));
    }

    /** Test with 3 players
     * 1st player - two squares: returns 8
     * 2nd player - four squares: returns 6
     * 3rd player - three squares: returns 4
     */
    @Test
    void threePlayers_three() {
        CG1 comG1 = new CG1(3);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col4_1, 0);
        l2.insertCard(col4_1, 1);
        l2.insertCard(col4_2, 2);
        l2.insertCard(col4_2, 3);
        l2.insertCard(col0, 4);

        l3.insertCard(col0, 0);
        l3.insertCard(col4_1, 1);
        l3.insertCard(col4_2, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col2_1, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(6, comG1.goalReached(l2));
        assertEquals(4, comG1.goalReached(l3));
    }

    /** Test with 4 players
     * 1st player - one square in 3x2 config: returns 0
     * 2nd player - zero squares: returns 0
     * 3rd player - one square in 3x3 config: returns 0
     * 4th player - zero squares: returns 0
     */
    @Test
    void fourPlayers_zero() {
        CG1 comG1 = new CG1(4);

        l1.insertCard(col2_1, 0);
        l1.insertCard(col2_1, 1);
        l1.insertCard(col2_1, 2);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col3_1, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col3_1, 4);

        l4.insertCard(col3_3, 1);
        l4.insertCard(col2_1, 2);
        l4.insertCard(col5, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(0, comG1.goalReached(l3));
        assertEquals(0, comG1.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - one square in 3x2 config: returns 0
     * 2nd player - zero squares: returns 0
     * 3rd player - three squares: returns 8
     * 4th player - zero squares: returns 0
     */
    @Test
    void fourPlayers_one() {
        CG1 comG1 = new CG1(4);

        l1.insertCard(col2_1, 0);
        l1.insertCard(col2_1, 1);
        l1.insertCard(col2_1, 2);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col0, 0);
        l3.insertCard(col4_1, 1);
        l3.insertCard(col4_2, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col2_1, 4);

        l4.insertCard(col3_3, 1);
        l4.insertCard(col2_1, 2);
        l4.insertCard(col5, 4);

        assertEquals(0, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(8, comG1.goalReached(l3));
        assertEquals(0, comG1.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - two squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - three squares: returns 6
     * 4th player - zero squares: returns 0
     */
    @Test
    void fourPlayers_two() {
        CG1 comG1 = new CG1(4);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col0, 0);
        l3.insertCard(col4_1, 1);
        l3.insertCard(col4_2, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col2_1, 4);

        l4.insertCard(col3_3, 1);
        l4.insertCard(col2_1, 2);
        l4.insertCard(col5, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(6, comG1.goalReached(l3));
        assertEquals(0, comG1.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - two squares: returns 8
     * 2nd player - zero squares: returns 0
     * 3rd player - three squares: returns 6
     * 4th player - two squares: returns 4
     */
    @Test
    void fourPlayers_three() {
        CG1 comG1 = new CG1(4);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col5, 0);
        l2.insertCard(col3_1, 2);
        l2.insertCard(col4_2, 4);

        l3.insertCard(col0, 0);
        l3.insertCard(col4_1, 1);
        l3.insertCard(col4_2, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col2_1, 4);

        l4.insertCard(col2_2, 0);
        l4.insertCard(col3_3, 1);
        l4.insertCard(col3_2, 2);
        l4.insertCard(col3_1, 3);
        l4.insertCard(col2_1, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(0, comG1.goalReached(l2));
        assertEquals(6, comG1.goalReached(l3));
        assertEquals(4, comG1.goalReached(l4));
    }

    /** Test with 4 players
     * 1st player - two squares: returns 8
     * 2nd player - four squares: returns 6
     * 3rd player - three squares: returns 4
     * 4th player - two squares: returns 2
     */
    @Test
    void fourPlayers_four() {
        CG1 comG1 = new CG1(4);

        l1.insertCard(col0, 0);
        l1.insertCard(col3_2, 1);
        l1.insertCard(col4_1, 2);
        l1.insertCard(col2_1, 3);
        l1.insertCard(col5, 4);

        l2.insertCard(col4_1, 0);
        l2.insertCard(col4_1, 1);
        l2.insertCard(col4_2, 2);
        l2.insertCard(col4_2, 3);
        l2.insertCard(col0, 4);

        l3.insertCard(col0, 0);
        l3.insertCard(col4_1, 1);
        l3.insertCard(col4_2, 2);
        l3.insertCard(col3_1, 3);
        l3.insertCard(col2_1, 4);

        l4.insertCard(col2_2, 0);
        l4.insertCard(col3_3, 1);
        l4.insertCard(col3_2, 2);
        l4.insertCard(col3_1, 3);
        l4.insertCard(col2_1, 4);

        assertEquals(8, comG1.goalReached(l1));
        assertEquals(6, comG1.goalReached(l2));
        assertEquals(4, comG1.goalReached(l3));
        assertEquals(2, comG1.goalReached(l4));
    }
}