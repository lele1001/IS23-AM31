package it.polimi.ingsw.model;

import it.polimi.ingsw.ModelExceptions.EmptyCardBagException;
import it.polimi.ingsw.ModelExceptions.NoRightItemCardSelection;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    /**
     * Testing the creation of the board, all the Cells have to be null ad the player pattern has to be correct
     */
    @Test
    public void TestBoardcreation() {
        Board boardtest = new Board(4);
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
        for (int i = 0; i < boardtest.board.length; i++) {
            for (int j = 0; j < boardtest.board[0].length; j++) {
                assertNull(boardtest.board[i][j].itemCard);
            }
        }
        for (int i = 0; i < boardtest.board.length; i++) {
            for (int j = 0; j < boardtest.board[0].length; j++) {
                assertEquals(player[i][j], boardtest.board[i][j].numPlayermin);
            }
        }
        assertNotNull(boardtest.cardBag);
        assertEquals(132, boardtest.cardBag.size());
    }

    /**
     * Testing the Filling of the board for all possible number of players
     */
    @Test
    void fillBoardfromscratch() throws EmptyCardBagException {
        int numplayer;
        for (numplayer = 2; numplayer < 5; numplayer++) {
            Board boardtest1 = new Board(numplayer);
            boardtest1.fillBoard();
            for (int i = 0; i < boardtest1.board.length; i++) {
                for (int j = 0; j < boardtest1.board[0].length; j++) {
                    if (boardtest1.board[i][j].numPlayermin <= numplayer)
                        assertNotNull(boardtest1.board[i][j].itemCard);
                    else assertNull(boardtest1.board[i][j].itemCard);
                }
            }
            if (numplayer == 2) assertEquals(103, boardtest1.cardBag.size());
            if (numplayer == 3) assertEquals(95, boardtest1.cardBag.size());
            if (numplayer == 4) assertEquals(87, boardtest1.cardBag.size());
        }
    }

    /**
     * Testing if the algorithm see that the board need a refill if the last Tile is on the side
     */

    @Test
    void checksideRefill() throws EmptyCardBagException {
        int numplayer = 3;
        ItemCard prima;
        Board boardtest1 = new Board(numplayer);
        boardtest1.board[0][3].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull(boardtest1.board[0][3].itemCard);
        prima = boardtest1.board[0][3].itemCard;
        boardtest1.checkRefill();
        assertEquals(prima, boardtest1.board[0][3].itemCard);
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                if (boardtest1.board[i][j].numPlayermin <= numplayer) assertNotNull(boardtest1.board[i][j].itemCard);
                else assertNull(boardtest1.board[i][j].itemCard);
            }
        }
    }

    /**
     * Testing if the algorithm see the exception to the cardbag
     */

    @Test
    void checkCardBagException() throws EmptyCardBagException {
        int numplayer = 4;
        Board boardtest1 = new Board(numplayer);
        while (boardtest1.cardBag.size() > 10) boardtest1.cardBag.remove(0);
        boardtest1.board[0][3].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertThrows(EmptyCardBagException.class, () -> boardtest1.checkRefill());
    }

    /**
     * Testing if the algorithm see that the board doesn't need a refill if the last Tiles are on the side
     */
    @Test
    void checksidenotRefill() throws EmptyCardBagException {
        int numplayer = 3;
        Board prima = new Board(numplayer);
        Board boardtest1 = new Board(numplayer);
        boardtest1.board[0][3].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        boardtest1.board[1][3].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull(boardtest1.board[0][3].itemCard);
        assertNotNull(boardtest1.board[1][3].itemCard);
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                prima.board[i][j].itemCard = boardtest1.board[i][j].itemCard;
            }
        }
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard, boardtest1.board[i][j].itemCard);
            }
        }
        boardtest1.checkRefill();
        //The board has not changed
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard, boardtest1.board[i][j].itemCard);
            }
        }
    }

    /**
     * Testing if the algorithm see that the board need a refill if the last Tile is in the centre
     */

    @Test
    void checkcenterRefill() throws EmptyCardBagException {
        int numplayer = 2;
        Board prima;
        Board boardtest1 = new Board(numplayer);
        boardtest1.board[3][3].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull(boardtest1.board[3][3].itemCard);
        prima = boardtest1;
        assertEquals(boardtest1, prima);
        boardtest1.checkRefill();
        assertEquals(prima.board[3][3].itemCard, boardtest1.board[3][3].itemCard);
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                if (boardtest1.board[i][j].numPlayermin <= numplayer) assertNotNull(boardtest1.board[i][j].itemCard);
                else assertNull(boardtest1.board[i][j].itemCard);
            }
        }
    }

    /**
     * Testing if the algorithm see that the board doesn't need a refill if the last Tiles are in the centre
     */
    @Test
    void checkcenternotRefill() throws EmptyCardBagException {
        int numplayer = 2;
        Board prima;
        Board boardtest1 = new Board(numplayer);
        boardtest1.board[3][3].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        boardtest1.board[3][4].itemCard = boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull(boardtest1.board[3][3].itemCard);
        assertNotNull(boardtest1.board[3][4].itemCard);
        prima = boardtest1;
        assertEquals(boardtest1, prima);
        boardtest1.checkRefill();
        //The board has not changed
        assertEquals(prima.board, boardtest1.board);
    }

    /**
     * Testing the check for the possibility that the chosen 2 tiles are near each other or not
     */
    @Test
    void checkSelection2integer() throws EmptyCardBagException {
        int numofplayers = 4;
        Board boardtest1 = new Board(numofplayers);
        boardtest1.fillBoard();
        boolean res;
        ArrayList<Integer> pos = new ArrayList<>(List.of(20, 30));
        res = boardtest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(21, 20));
        res = boardtest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(31, 20));
        res = boardtest1.checkSelection(pos);
        assertFalse(res);
    }

    /**
     * Testing the check for the possibility that the chosen 3 tiles are near each other or not
     */
    @Test
    void checkSelection3integer() {
        Board boardtest1 = new Board(4);
        boolean res;
        ArrayList<Integer> pos = new ArrayList<>(List.of(20, 30, 40));
        res = boardtest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(21, 20, 19));
        res = boardtest1.checkSelection(pos);
        assertFalse(res);
        pos = new ArrayList<>(List.of(31, 20, 21));
        res = boardtest1.checkSelection(pos);
        assertFalse(res);
    }

    /**
     * Testing if the method deletes the chosen tiles and put them in the return arraylist
     * I don't need to check the Tiles, this part of the check is done in the Check selection method
     */
    @Test
    void deleteSelectionTestGoodselection() throws NoRightItemCardSelection, EmptyCardBagException {
        Board boardtest1 = new Board(2);
        Board prima = new Board(2);
        int position;
        int numplayer = 2;
        ArrayList<Integer> pos = new ArrayList<>(List.of(74, 75));
        ArrayList<ItemCard> delete;
        boardtest1.fillBoard();
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                prima.board[i][j].itemCard = boardtest1.board[i][j].itemCard;
            }
        }
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard, boardtest1.board[i][j].itemCard);
            }
        }
        delete = boardtest1.deleteSelection(pos);
        assertEquals(2, delete.size());
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                position = 10 * i + j;
                if (!pos.contains(position)) {
                    assertEquals(prima.board[i][j].itemCard, boardtest1.board[i][j].itemCard);
                } else {
                    assertTrue(delete.contains(prima.board[i][j].itemCard));
                }
            }
        }

    }

    /**
     * Testing if the method throws the NoRightItemCardSelection if called with wrong set of Tiles
     * Caused by the not straight tiles position or the fact that the Tiles has not a free side from other Tiles
     */
    @Test
    void deleteSelectionTestBadselection() throws NoRightItemCardSelection, EmptyCardBagException {
        int numplayer = 2;
        Board boardtest1 = new Board(numplayer);
        Board prima = new Board(numplayer);
        ArrayList<Integer> pos = new ArrayList<>(List.of(64, 74));
        ArrayList<Integer> pos1 = new ArrayList<>(List.of(33, 34, 44));
        ArrayList<Integer> pos2 = new ArrayList<>(List.of(33, 43, 44));
        boardtest1.fillBoard();
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                prima.board[i][j].itemCard = boardtest1.board[i][j].itemCard;
            }
        }
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard, boardtest1.board[i][j].itemCard);
            }
        }
        assertThrows(NoRightItemCardSelection.class, () -> boardtest1.deleteSelection(pos));
        assertThrows(NoRightItemCardSelection.class, () -> boardtest1.deleteSelection(pos1));
        assertThrows(NoRightItemCardSelection.class, () -> boardtest1.deleteSelection(pos2));

    }

    @Test
    void createcardBag() {
    }

}