package it.polimi.ingsw.server.model;

import it.polimi.ingsw.commons.ItemCard;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.model.comGoals.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.commons.HouseItem.*;
import static it.polimi.ingsw.commons.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    /**
     * This method tests the insertCard and all the exceptions that this method can throw.
     */
    @Test
    void insertCard() {
        Player p = new Player("Paolo");

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second))), 2);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third), new ItemCard(Frame, Second), new ItemCard(Cat, First))), 2);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Third), new ItemCard(Books, Second))), 2);
        } catch (NoBookshelfSpaceException e) {
            assertTrue(true); // To be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second))), 2);
        } catch (NoBookshelfSpaceException e) {
            assertEquals(1, 2); // Not to be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second))), 1);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third), new ItemCard(Frame, Second), new ItemCard(Cat, First))), 1);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Third))), 1);
        } catch (NoBookshelfSpaceException e) {
            assertEquals(1, 2); // Not to be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 1);
        } catch (NoBookshelfSpaceException e) {
            assertTrue(true);  // To be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 3);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First))), 3);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second))), 3);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 3);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 3);
        } catch (NoBookshelfSpaceException e) {
            assertEquals(1, 2);  // Not to be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third), new ItemCard(Frame, Second), new ItemCard(Cat, First))), 3);
        } catch (NoBookshelfSpaceException e) {
            assertTrue(true); // To be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second), new ItemCard(Cat, Second))), 4);
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third), new ItemCard(Frame, Second), new ItemCard(Cat, First))), 4);
        } catch (NoBookshelfSpaceException e) {
            assertEquals(1, 2); // Not to be executed
        }

        try {
            p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Third))), 4);
        } catch (NoBookshelfSpaceException e) {
            assertTrue(true); // To be executed
        }
    }

    /**
     * This method tests the checkComGoal, called at the end of a turn on each player.
     *
     * @throws NoBookshelfSpaceException if the insertCard throws this exception.
     */
    @Test
    void checkComGoal1() throws NoBookshelfSpaceException {
        // Creating the game
        Player p = new Player("Pippo");
        ArrayList<ComGoal> comGoals = new ArrayList<>(List.of(new CG2_5(3, 2), new CG1(3, 1)));
        p.assignPersGoal(PersGoal.pg06);

        // Creating player's bookshelf
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 4);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, First), new ItemCard(Plants, Third))), 4);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 3);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, First), new ItemCard(Plants, Third))), 3);
        assertTrue(p.checkComGoal(comGoals.get(0)));
        assertEquals(p.calculateFinScore(), 8);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 2);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, First), new ItemCard(Plants, Third))), 2);
        assertFalse(p.checkComGoal(comGoals.get(0)));   // Already done it

        assertEquals(20, p.calculateFinScore());

        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Third), new ItemCard(Plants, First), new ItemCard(Plants, Third))), 0);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Third), new ItemCard(Books, First), new ItemCard(Books, Third))), 0);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Third), new ItemCard(Plants, First), new ItemCard(Plants, Third))), 1);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Third), new ItemCard(Books, First), new ItemCard(Books, Third))), 1);
        assertTrue(p.checkComGoal(comGoals.get(1)));
        assertFalse(p.checkComGoal(comGoals.get(1)));

        assertEquals(34, p.calculateFinScore());
    }

    /**
     * This method tests the checkComGoal, called at the end of a turn on each player.
     *
     * @throws NoBookshelfSpaceException if the insertCard throws this exception.
     */
    @Test
    void checkComGoal2() throws NoBookshelfSpaceException {
        // Creating the game
        Player p = new Player("Luigi");
        Player p2 = new Player("Nico");
        ArrayList<ComGoal> comGoals = new ArrayList<>(List.of(new CG8(4, 8), new CG10(4, 10)));
        p.assignPersGoal(PersGoal.pg11);
        p2.assignPersGoal(PersGoal.pg07);

        // Creating player's bookshelf
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Third), new ItemCard(Games, First), new ItemCard(Trophy, Third))), 0);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Third), new ItemCard(Cat, First), new ItemCard(Cat, Third))), 0);
        assertFalse(p.checkComGoal(comGoals.get(0)));
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 4);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, First), new ItemCard(Cat, Third))), 4);
        assertTrue(p.checkComGoal(comGoals.get(0)));
        assertFalse(p.checkComGoal(comGoals.get(0)));
        assertFalse(p.checkComGoal(comGoals.get(0)));
        assertEquals(8, p.calculateFinScore());

        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Cat, Second), new ItemCard(Cat, First))), 3);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Games, Second), new ItemCard(Cat, First))), 2);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 4);
        assertTrue(p2.checkComGoal(comGoals.get(1)));
        assertFalse(p2.checkComGoal(comGoals.get(0)));
        assertFalse(p2.checkComGoal(comGoals.get(1)));
        assertEquals(11, p2.calculateFinScore());

        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Trophy, First), new ItemCard(Cat, Third))), 4);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Trophy, Third), new ItemCard(Trophy, Third))), 0);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Plants, Third), new ItemCard(Cat, Third))), 0);
        assertTrue(p2.checkComGoal(comGoals.get(0)));
        assertFalse(p2.checkComGoal(comGoals.get(0)));
        assertFalse(p2.checkComGoal(comGoals.get(1)));
        assertEquals(21, p2.calculateFinScore());

        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Third), new ItemCard(Games, First), new ItemCard(Plants, Third))), 1);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First))), 1);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Third), new ItemCard(Trophy, First), new ItemCard(Games, Third))), 2);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Third), new ItemCard(Games, First), new ItemCard(Books, First))), 3);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First))), 3);
        assertTrue(p.checkComGoal(comGoals.get(1)));
        assertFalse(p.checkComGoal(comGoals.get(1)));
        assertEquals(14, p.calculateFinScore());
    }

    /**
     * This method tests the checkComGoal, called at the end of a turn on each player.
     *
     * @throws NoBookshelfSpaceException if the insertCard throws this exception.
     */
    @Test
    void checkComGoal3() throws NoBookshelfSpaceException {
        // Creating the game
        Player p = new Player("Luigi");
        Player p2 = new Player("Nico");
        ArrayList<ComGoal> comGoals = new ArrayList<>(List.of(new CG6_7(2, 6), new CG9(2, 9)));
        p.assignPersGoal(PersGoal.pg01);
        p2.assignPersGoal(PersGoal.pg09);

        // Creating player's bookshelf
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Third), new ItemCard(Games, First), new ItemCard(Games, Third))), 0);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Third))), 0);
        assertFalse(p.checkComGoal(comGoals.get(0)));
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Games, Second), new ItemCard(Cat, First))), 4);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Plants, Second), new ItemCard(Plants, First))), 3);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Third))), 3);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Books, First), new ItemCard(Trophy, Second), new ItemCard(Trophy, First))), 2);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, First), new ItemCard(Trophy, Second), new ItemCard(Books, First))), 1);
        assertTrue(p.checkComGoal(comGoals.get(0)));
        assertFalse(p.checkComGoal(comGoals.get(0)));
        assertFalse(p.checkComGoal(comGoals.get(1)));
        assertEquals(10, p.calculateFinScore());


        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Books, Third), new ItemCard(Books, Third))), 0);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Third), new ItemCard(Games, First), new ItemCard(Games, Third))), 1);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Third))), 1);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Plants, Second), new ItemCard(Plants, First))), 2);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Trophy, Second), new ItemCard(Trophy, First))), 3);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Frame, Second), new ItemCard(Cat, First))), 3);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First), new ItemCard(Frame, Second), new ItemCard(Frame, First))), 4);
        assertTrue(p2.checkComGoal(comGoals.get(0)));
        assertFalse(p2.checkComGoal(comGoals.get(0)));
        assertFalse(p2.checkComGoal(comGoals.get(1)));
        assertEquals(9, p2.calculateFinScore());

        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Third))), 1);
        p2.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Third))), 2);
        assertTrue(p2.checkComGoal(comGoals.get(1)));
        assertFalse(p2.checkComGoal(comGoals.get(1)));
        assertEquals(17, p2.calculateFinScore());

        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Third))), 0);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Third), new ItemCard(Games, Second))), 2);
        p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Third))), 0);
        assertTrue(p.checkComGoal(comGoals.get(1)));
        assertFalse(p.checkComGoal(comGoals.get(1)));
        assertEquals(14, p.calculateFinScore());
    }

    /**
     * This method tests the return value of "insertCard" that is true if the player has completed his bookshelf.
     *
     * @throws NoBookshelfSpaceException if an error occurs during the insert phase.
     */
    @Test
    void checkEnd() throws NoBookshelfSpaceException {
        Player p = new Player("Nick");

        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 1);
        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1));
        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 3));

        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 2);

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, First))), 2));

        p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Books, Second), new ItemCard(Cat, First))), 4);
        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, First))), 4));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 0));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, First), new ItemCard(Plants, Third))), 0));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 0));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First), new ItemCard(Cat, First))), 3));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 0));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 1));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 3));

        assertFalse(p.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First))), 2));

        assertTrue(p.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, First))), 4));
    }
}