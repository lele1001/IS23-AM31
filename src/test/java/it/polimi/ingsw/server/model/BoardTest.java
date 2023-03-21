package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
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
        for (int i = 0; i < boardTest.board.length; i++) {
            for (int j = 0; j < boardTest.board[0].length; j++) {
                assertNull(boardTest.board[i][j]);
            }
        }
        for (int i = 0; i < boardTest.board.length; i++) {
            for (int j = 0; j < boardTest.board[0].length; j++) {
                assertEquals(player[i][j], boardTest.numMinPlayer[i][j]);
            }
        }
        assertNotNull(boardTest.cardBag);
        assertEquals(132, boardTest.cardBag.size());
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
            for (int i = 0; i < boardTest1.board.length; i++) {
                for (int j = 0; j < boardTest1.board[0].length; j++) {
                    if (boardTest1.numMinPlayer[i][j] <= numPlayer)
                        assertNotNull(boardTest1.board[i][j]);
                    else assertNull(boardTest1.board[i][j]);
                }
            }
            if (numPlayer == 2) assertEquals(103, boardTest1.cardBag.size());
            if (numPlayer == 3) assertEquals(95, boardTest1.cardBag.size());
            if (numPlayer == 4) assertEquals(87, boardTest1.cardBag.size());
        }
    }

    /**
     * Testing if the algorithm see that the board need a refill if the last Tile is on the side
     */

    @Test
    void checkSideRefill() throws EmptyCardBagException {
        int numPlayer = 3;
        ItemCard prima;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.board[0][3] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        assertNotNull(boardTest1.board[0][3]);
        prima = boardTest1.board[0][3];
        boardTest1.checkRefill();
        assertEquals(prima, boardTest1.board[0][3]);
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                if (boardTest1.numMinPlayer[i][j] <= numPlayer) assertNotNull(boardTest1.board[i][j]);
                else assertNull(boardTest1.board[i][j]);
            }
        }
    }

    /**
     * Testing if the algorithm see the exception to the cardBag
     */

    @Test
    void checkCardBagException() {
        int numPlayer = 4;
        Board boardTest1 = new Board(numPlayer);
        while (boardTest1.cardBag.size() > 10) boardTest1.cardBag.remove(0);
        boardTest1.board[0][3] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        assertThrows(EmptyCardBagException.class, () -> boardTest1.checkRefill());
    }

    /**
     * Testing if the algorithm see that the board doesn't need a refill if the last Tiles are on the side
     */
    @Test
    void checkSideNotRefill() throws EmptyCardBagException {
        int numPlayer = 3;
        Board prima = new Board(numPlayer);
        Board boardTest1 = new Board(numPlayer);
        boardTest1.board[0][3] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        boardTest1.board[1][3] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        assertNotNull(boardTest1.board[0][3]);
        assertNotNull(boardTest1.board[1][3]);
        for (int i = 0; i < boardTest1.board.length; i++) {
            System.arraycopy(boardTest1.board[i], 0, prima.board[i], 0, boardTest1.board[0].length);
        }
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                assertEquals(prima.board[i][j], boardTest1.board[i][j]);
            }
        }
        boardTest1.checkRefill();
        //The board has not changed
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                assertEquals(prima.board[i][j], boardTest1.board[i][j]);
            }
        }
    }

    /**
     * Testing if the algorithm see that the board need a refill if the last Tile is in the centre
     */

    @Test
    void checkCenterRefill() throws EmptyCardBagException {
        int numPlayer = 2;
        Board prima;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.board[3][3] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        assertNotNull(boardTest1.board[3][3]);
        prima = boardTest1;
        assertEquals(boardTest1, prima);
        boardTest1.checkRefill();
        assertEquals(prima.board[3][3], boardTest1.board[3][3]);
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                if (boardTest1.numMinPlayer[i][j] <= numPlayer) assertNotNull(boardTest1.board[i][j]);
                else assertNull(boardTest1.board[i][j]);
            }
        }
    }

    /**
     * Testing if the algorithm see that the board doesn't need a refill if the last Tiles are in the centre
     */
    @Test
    void checkCenterNotRefill() throws EmptyCardBagException {
        int numPlayer = 2;
        Board prima;
        Board boardTest1 = new Board(numPlayer);
        boardTest1.board[3][3] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        boardTest1.board[3][4] = boardTest1.cardBag.get(0);
        boardTest1.cardBag.remove(0);
        assertNotNull(boardTest1.board[3][3]);
        assertNotNull(boardTest1.board[3][4]);
        prima = boardTest1;
        assertEquals(boardTest1, prima);
        boardTest1.checkRefill();
        //The board has not changed
        assertEquals(prima.board, boardTest1.board);
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
        Board prima = new Board(numPlayer);
        int position;

        ArrayList<Integer> pos = new ArrayList<>(List.of(74, 75));
        ArrayList<ItemCard> delete;
        boardTest1.fillBoard();
        for (int i = 0; i < boardTest1.board.length; i++) {
            System.arraycopy(boardTest1.board[i], 0, prima.board[i], 0, boardTest1.board[0].length);
        }
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                assertEquals(prima.board[i][j], boardTest1.board[i][j]);
            }
        }
        delete = boardTest1.deleteSelection(pos);
        assertEquals(2, delete.size());
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                position = 10 * i + j;
                if (!pos.contains(position)) {
                    assertEquals(prima.board[i][j], boardTest1.board[i][j]);
                } else {
                    assertTrue(delete.contains(prima.board[i][j]));
                }
            }
        }

    }

    /**
     * Testing if the method throws the NoRightItemCardSelection if called with wrong set of Tiles
     * Caused by the not straight tiles position or because the Tiles have not a free side from other Tiles
     */
    @Test
    void deleteSelectionTestBadSelection() throws  EmptyCardBagException {
        int numPlayer = 2;
        Board boardTest1 = new Board(numPlayer);
        Board prima = new Board(numPlayer);
        ArrayList<Integer> pos = new ArrayList<>(List.of(64, 74));
        ArrayList<Integer> pos1 = new ArrayList<>(List.of(33, 34, 44));
        ArrayList<Integer> pos2 = new ArrayList<>(List.of(33, 43, 44));
        boardTest1.fillBoard();
        for (int i = 0; i < boardTest1.board.length; i++) {
            System.arraycopy(boardTest1.board[i], 0, prima.board[i], 0, boardTest1.board[0].length);
        }
        for (int i = 0; i < boardTest1.board.length; i++) {
            for (int j = 0; j < boardTest1.board[0].length; j++) {
                assertEquals(prima.board[i][j], boardTest1.board[i][j]);
            }
        }
        assertThrows(NoRightItemCardSelection.class, () -> boardTest1.deleteSelection(pos));
        assertThrows(NoRightItemCardSelection.class, () -> boardTest1.deleteSelection(pos1));
        assertThrows(NoRightItemCardSelection.class, () -> boardTest1.deleteSelection(pos2));

    }

    @Test
    void createCardBag() {  //Da fare io: controllo che le carte siano effettivamente 22 e uguali
        Board board = new Board(4);
        ItemCard test;
        int[] prova = new int[6];
        for (int i = 0; i < 6; i++) {
            prova[i] = 0;
        }
        for (int i = 0; i < 132; i++) {
            assertNotNull(board.cardBag.get(0));
            test = board.cardBag.get(0);
            board.cardBag.remove(0);
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