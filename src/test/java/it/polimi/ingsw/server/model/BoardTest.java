package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.utils.Utils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final int[][] numMinPlayer = new int[][]{
            {5, 5, 5, 3, 4, 5, 5, 5, 5},
            {5, 5, 5, 2, 2, 4, 5, 5, 5},
            {5, 5, 3, 2, 2, 2, 3, 5, 5},
            {5, 4, 2, 2, 2, 2, 2, 2, 3},
            {4, 2, 2, 2, 2, 2, 2, 2, 4},
            {3, 2, 2, 2, 2, 2, 2, 4, 5},
            {5, 5, 3, 2, 2, 2, 3, 5, 5},
            {5, 5, 5, 4, 2, 2, 5, 5, 5},
            {5, 5, 5, 5, 4, 3, 5, 5, 5}
    };

    /**
     * Testing the creation of the board:
     * all the Cells have to be null, and the player pattern has to be correct
     */
    @Test
    public void TestBoardCreation() {
        Board boardTest = new Board(4);
        int[][] player;
        player = new int[][]{
                {5, 5, 5, 3, 4, 5, 5, 5, 5},
                {5, 5, 5, 2, 2, 4, 5, 5, 5},
                {5, 5, 3, 2, 2, 2, 3, 5, 5},
                {5, 4, 2, 2, 2, 2, 2, 2, 3},
                {4, 2, 2, 2, 2, 2, 2, 2, 4},
                {3, 2, 2, 2, 2, 2, 2, 4, 5},
                {5, 5, 3, 2, 2, 2, 3, 5, 5},
                {5, 5, 5, 4, 2, 2, 5, 5, 5},
                {5, 5, 5, 5, 4, 3, 5, 5, 5}};
        for (int i = 0; i < boardTest.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest.getAsMatrix()[0].length; j++) {
                assertNull(boardTest.getAsMatrix()[i][j]);
            }
        }
        for (int i = 0; i < boardTest.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest.getAsMatrix()[0].length; j++) {
                assertEquals(player[i][j], numMinPlayer[i][j]);
            }
        }
        assertNotNull(boardTest.getCardBag());
        assertEquals(132, boardTest.getCardBag().size());
    }

    /**
     * Testing the Filling of the board for all possible number of players
     */
    @Test
    void fillBoardFromScratch() throws EmptyCardBagException {
        int numPlayer;
        for (numPlayer = 2; numPlayer < 5; numPlayer++) {
            Board boardTest1 = new Board(numPlayer);
            boardTest1.fillBoard();
            for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
                for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                    if (numMinPlayer[i][j] <= numPlayer)
                        assertNotNull(boardTest1.getAsMatrix()[i][j]);
                    else assertNull(boardTest1.getAsMatrix()[i][j]);
                }
            }
            if (numPlayer == 2) assertEquals(103, boardTest1.getCardBag().size());
            if (numPlayer == 3) assertEquals(95, boardTest1.getCardBag().size());
            if (numPlayer == 4) assertEquals(87, boardTest1.getCardBag().size());
            Board board = new Board(numPlayer);
            board.fillBoard();
            for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
                for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                    if (boardTest1.getAsMatrix()[i][j] != null) {
                        assertNotNull(board.getAsMatrix()[i][j].getMyItem());
                    } else {
                        assertNull(board.getAsMatrix()[i][j]);
                    }
                }

            }
        }
    }

    /**
     * Testing if the algorithm see that the board need a refill if the last Tile is on the side
     */

    @Test
    void checkSideRefill() throws EmptyCardBagException, NoRightItemCardSelection {
        int numPlayer = 3;
        ItemCard prima;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.fillBoard();
        boardTest1.deleteSelection(new ArrayList<>(List.of(13,14)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(22,23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25,26)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32,33,34)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(38)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41,42,43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(47)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(50,51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(56)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(62,63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65,66)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(74,75)));

        assertNotNull(boardTest1.getAsMatrix()[0][3]);
        prima = boardTest1.getAsMatrix()[0][3];
        boardTest1.checkRefill();
        assertEquals(prima, boardTest1.getAsMatrix()[0][3]);
        for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                if (numMinPlayer[i][j] <= numPlayer) assertNotNull(boardTest1.getAsMatrix()[i][j]);
                else assertNull(boardTest1.getAsMatrix()[i][j]);
            }
        }
    }

    /**
     * Testing if the algorithm see the exception to the cardBag
     */

    @Test
    void checkCardBagException() throws EmptyCardBagException, NoRightItemCardSelection {
        int numPlayer = 4;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.fillBoard();
        boardTest1.deleteSelection(new ArrayList<>(List.of(4)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(13,14, 15)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(22,23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25,26)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(31)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32,33,34)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(38)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41,42,43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(47)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(50,51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(56)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(62,63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65,66)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(74,75)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(85)));
        boardTest1.checkRefill();

        boardTest1.deleteSelection(new ArrayList<>(List.of(4)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(13,14, 15)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(22,23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25,26)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(31)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32,33,34)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(38)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41,42,43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(47)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(50,51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(56)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(62,63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65,66)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(74,75)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(85)));
        boardTest1.checkRefill();

        boardTest1.deleteSelection(new ArrayList<>(List.of(4)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(13,14, 15)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(22,23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25,26)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(31)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32,33,34)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(38)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41,42,43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(47)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(50,51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(56)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(62,63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65,66)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(74,75)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(85)));

        assertThrows(EmptyCardBagException.class, boardTest1::checkRefill);
    }

    /**
     * Testing if the algorithm see that the board doesn't need a refill if the last Tiles are on the side
     */
    @Test
    void checkSideNotRefill() throws EmptyCardBagException, NoRightItemCardSelection {
        int numPlayer = 3;
        ItemCard[][] prima;
        Board boardTest1 = new Board(numPlayer);

        boardTest1.fillBoard();
        boardTest1.deleteSelection(new ArrayList<>(List.of(14)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(22)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25,26)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32,33,34)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(38)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41,42,43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(47)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(50,51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(56)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(62,63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65,66)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(74,75)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(85)));

        assertNotNull(boardTest1.getAsMatrix()[0][3]);
        assertNotNull(boardTest1.getAsMatrix()[1][3]);
        prima = boardTest1.getAsMatrix();
        for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                assertEquals(prima[i][j], boardTest1.getAsMatrix()[i][j]);
            }
        }
        boardTest1.checkRefill();
        //The board has not changed
        for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                assertEquals(prima[i][j], boardTest1.getAsMatrix()[i][j]);
            }
        }
    }

    /**
     * Testing if the algorithm see that the board need a refill if the last Tile is in the centre
     */

    @Test
    void checkCenterRefill() throws EmptyCardBagException, NoRightItemCardSelection {
        int numPlayer = 2;
        ItemCard prima;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.fillBoard();
        boardTest1.deleteSelection(new ArrayList<>(List.of(13,14)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(34)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41, 42)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(75)));
        assertNotNull(boardTest1.getAsMatrix()[3][3]);
        prima = boardTest1.getAsMatrix()[3][3];
        boardTest1.checkRefill();
        assertEquals(prima, boardTest1.getAsMatrix()[3][3]);
        for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                if (numMinPlayer[i][j] <= numPlayer) assertNotNull(boardTest1.getAsMatrix()[i][j]);
                else assertNull(boardTest1.getAsMatrix()[i][j]);
            }
        }
    }

    /**
     * Testing if the algorithm see that the board doesn't need a refill if the last Tiles are in the centre
     */
    @Test
    void checkCenterNotRefill() throws EmptyCardBagException, NoRightItemCardSelection {
        int numPlayer = 2;
        ItemCard[][] prima;
        Board boardTest1 = new Board(numPlayer);

        boardTest1.fillBoard();
        boardTest1.deleteSelection(new ArrayList<>(List.of(13,14)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(23,24)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(25)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(32)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(35,36,37)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(41, 42)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(43)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(44,45,46)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(51,52)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(53,54,55)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(63,64)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(65)));
        boardTest1.deleteSelection(new ArrayList<>(List.of(75)));

        assertNotNull(boardTest1.getAsMatrix()[3][3]);
        assertNotNull(boardTest1.getAsMatrix()[3][4]);
        prima = boardTest1.getAsMatrix();
        boardTest1.checkRefill();
        //The board has not changed
        for (int i = 0; i < Utils.DIM_BOARD; i++)
            for (int j=0; j< Utils.DIM_BOARD; j++)
                assertEquals(prima[i][j], boardTest1.getAsMatrix()[i][j]);
    }

    /**
     * Testing the check for the possibility that the chosen 2 tiles are near each other or not
     */
    @Test
    void checkSelection2integer() throws EmptyCardBagException {
        int numPlayer = 4;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.fillBoard();
        boolean res;
        ArrayList<Integer> pos = new ArrayList<>(List.of(20, 30));
        res = boardTest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(21, 20));
        res = boardTest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(31, 20));
        res = boardTest1.checkSelection(pos);
        assertFalse(res);
    }

    /**
     * Testing the check for the possibility that the chosen 3 tiles are near each other or not
     */
    @Test
    void checkSelection3integer() {
        Board boardTest1 = new Board(4);
        boolean res;
        ArrayList<Integer> pos = new ArrayList<>(List.of(20, 30, 40));
        res = boardTest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(21, 20, 19));
        res = boardTest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(31, 20, 21));
        res = boardTest1.checkSelection(pos);
        assertFalse(res);
    }

    /**
     * Testing if the method deletes the chosen tiles and put them in the return arraylist
     * I don't need to check the Tiles, this part of the check is done in the Check selection method
     */
    @Test
    void deleteSelectionTestGoodSelection() throws NoRightItemCardSelection, EmptyCardBagException {
        int numPlayer = 2;
        Board boardTest1 = new Board(numPlayer);
        ArrayList<Integer> pos = new ArrayList<>(List.of(74, 75));
        int position;
        boardTest1.fillBoard();
        ItemCard[][] prima = boardTest1.getAsMatrix();
        for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                assertEquals(prima[i][j], boardTest1.getAsMatrix()[i][j]);
            }
        }
        boardTest1.deleteSelection(pos);
        for (int i = 0; i < boardTest1.getAsMatrix().length; i++) {
            for (int j = 0; j < boardTest1.getAsMatrix()[0].length; j++) {
                position = 10 * i + j;
                if (!pos.contains(position)) {
                    assertEquals(prima[i][j], boardTest1.getAsMatrix()[i][j]);
                } else {
                    assertNull(boardTest1.getAsMatrix()[i][j]);
                }
            }
        }

    }

    /**
     * Testing if the method throws the NoRightItemCardSelection if called with wrong set of Tiles
     * Caused by the not straight tiles position or because the Tiles have not a free side from other Tiles
     */
    @Test
    void deleteSelectionTestBadSelection() throws EmptyCardBagException {
        int numPlayer = 2;
        Board boardTest1 = new Board(numPlayer);
        ArrayList<Integer> pos = new ArrayList<>(List.of(64, 74));
        ArrayList<Integer> pos1 = new ArrayList<>(List.of(33, 34, 44));
        ArrayList<Integer> pos2 = new ArrayList<>(List.of(33, 43, 44));
        boardTest1.fillBoard();
        assertThrows(NoRightItemCardSelection.class, () -> boardTest1.deleteSelection(pos));
        assertThrows(NoRightItemCardSelection.class, () -> boardTest1.deleteSelection(pos1));
        assertThrows(NoRightItemCardSelection.class, () -> boardTest1.deleteSelection(pos2));

    }

    /**
     * Testing the creation of the card bag.
     */
    @Test
    void createCardBag() {
        Board board = new Board(4);
        ItemCard test;
        int[] prova = new int[6];
        for (int i = 0; i < 6; i++) {
            prova[i] = 0;
        }
        for (int i = 0; i < 132; i++) {
            assertNotNull(board.getCardBag().get(i));
            test = board.getCardBag().get(i);
            if (test.getMyItem() == HouseItem.Cat) prova[0]++;
            if (test.getMyItem() == HouseItem.Books) prova[1]++;
            if (test.getMyItem() == HouseItem.Frame) prova[2]++;
            if (test.getMyItem() == HouseItem.Plants) prova[3]++;
            if (test.getMyItem() == HouseItem.Games) prova[4]++;
            if (test.getMyItem() == HouseItem.Trophy) prova[5]++;
        }
        for (int i = 0; i < 6; i++) {
            assertEquals(22, prova[i]);
        }

    }
}