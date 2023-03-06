package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    public void TestBoardcreation() {
        Board boardtest=new Board();
        int [][] player;
        player= new int[][]{
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
        for(int i=0;i< boardtest.board.length;i++) {
            for (int j = 0; j < boardtest.board[0].length; j++) {
                assertEquals(null, boardtest.board[i][j].itemCard);
            }
        }
        for(int i=0;i< boardtest.board.length;i++) {
            for (int j = 0; j < boardtest.board[0].length; j++) {
                assertEquals(player[i][j], boardtest.board[i][j].numPlayermin);
            }
        }
        assertNotNull(boardtest.cardBag);
        assertEquals(132,boardtest.cardBag.size());
    }

    @Test
    void fillBoardfromscratch() {
        int numplayer;
        for (numplayer = 2; numplayer < 5; numplayer++) {
            Board boardtest1=new Board();
            boardtest1.fillBoard(numplayer);
            for (int i = 0; i < boardtest1.board.length; i++) {
                for (int j = 0; j < boardtest1.board[0].length; j++) {
                    if (boardtest1.board[i][j].numPlayermin <= numplayer)
                        assertNotNull(boardtest1.board[i][j].itemCard);
                    else
                        assertNull(boardtest1.board[i][j].itemCard);
                }
            }
            if (numplayer == 2)
                assertEquals(103, boardtest1.cardBag.size());
            if (numplayer == 3)
                assertEquals(95, boardtest1.cardBag.size());
            if (numplayer == 4)
                assertEquals(87, boardtest1.cardBag.size());
        }
    }
    @Test
    void checksideRefill() {
        int numplayer=3;
        ItemCard prima;
        Board boardtest1=new Board();
        boardtest1.board[0][3].itemCard=boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull( boardtest1.board[0][3].itemCard);
        prima=boardtest1.board[0][3].itemCard;
        boardtest1.checkRefill(numplayer);
        assertEquals(prima,boardtest1.board[0][3].itemCard);
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                if (boardtest1.board[i][j].numPlayermin <= numplayer)
                    assertNotNull(boardtest1.board[i][j].itemCard);
                else
                    assertNull(boardtest1.board[i][j].itemCard);
            }
        }
    }
    @Test
    void checksidenotRefill() {
        int numplayer=3;
        Board prima=new Board();
        Board boardtest1=new Board();
        boardtest1.board[0][3].itemCard=boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        boardtest1.board[1][3].itemCard=boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull( boardtest1.board[0][3].itemCard);
        assertNotNull( boardtest1.board[1][3].itemCard);
        for(int i=0;i< boardtest1.board.length;i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                prima.board[i][j].itemCard=boardtest1.board[i][j].itemCard;
            }
        }
        for(int i=0;i< boardtest1.board.length;i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard,boardtest1.board[i][j].itemCard);
            }
        }
        boardtest1.checkRefill(numplayer);
        //The board has not changed
        for(int i=0;i< boardtest1.board.length;i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard,boardtest1.board[i][j].itemCard);
            }
        }
    }

    @Test
    void checkcenterRefill() {
        int numplayer=2;
        Board prima;
        Board boardtest1=new Board();
        boardtest1.board[3][3].itemCard=boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull( boardtest1.board[3][3].itemCard);
        prima=boardtest1;
        assertEquals(boardtest1,prima);
        boardtest1.checkRefill(numplayer);
        assertEquals(prima.board[3][3].itemCard,boardtest1.board[3][3].itemCard);
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                if (boardtest1.board[i][j].numPlayermin <= numplayer)
                    assertNotNull(boardtest1.board[i][j].itemCard);
                else
                    assertNull(boardtest1.board[i][j].itemCard);
            }
        }
    }
    @Test
    void checkcenternotRefill() {
        int numplayer=2;
        Board prima;
        Board boardtest1=new Board();
        boardtest1.board[3][3].itemCard=boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        boardtest1.board[3][4].itemCard=boardtest1.cardBag.get(0);
        boardtest1.cardBag.remove(0);
        assertNotNull( boardtest1.board[3][3].itemCard);
        assertNotNull( boardtest1.board[3][4].itemCard);
        prima=boardtest1;
        assertEquals(boardtest1,prima);
        boardtest1.checkRefill(numplayer);
        //The board has not changed
        assertEquals(prima.board,boardtest1.board);
    }
    @Test
    void checkSelection2integer() {
        Board boardtest1=new Board();
        boolean res;
        ArrayList<Integer> pos= new ArrayList<>(List.of(20,30));
        res=boardtest1.checkSelection(pos);
        assertEquals(true,res);
        pos= new ArrayList<>(List.of(21,20));
        res=boardtest1.checkSelection(pos);
        assertEquals(true,res);
        pos= new ArrayList<>(List.of(31,20));
        res=boardtest1.checkSelection(pos);
        assertEquals(false,res);
    }
    @Test
    void checkSelection3integer() {
        Board boardtest1=new Board();
        boolean res;
        ArrayList<Integer> pos= new ArrayList<>(List.of(20,30,40));
        res=boardtest1.checkSelection(pos);
        assertEquals(true,res);
        pos= new ArrayList<>(List.of(21,20,19));
        res=boardtest1.checkSelection(pos);
        assertEquals(true,res);
        pos= new ArrayList<>(List.of(31,20,21));
        res=boardtest1.checkSelection(pos);
        assertEquals(false,res);
    }

    @Test
    void deleteSelection() {
        Board boardtest1=new Board();
        Board prima=new Board();
        int position;
        int numplayer=2;
        ArrayList<Integer> pos= new ArrayList<>(List.of(34,33));
        ArrayList<ItemCard> delete;
        boardtest1.fillBoard(numplayer);
        for(int i=0;i< boardtest1.board.length;i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                prima.board[i][j].itemCard=boardtest1.board[i][j].itemCard;
            }
        }
        for(int i=0;i< boardtest1.board.length;i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                assertEquals(prima.board[i][j].itemCard,boardtest1.board[i][j].itemCard);
            }
        }
        delete=boardtest1.deleteSelection(pos);
        assertEquals(2,delete.size());
        for (int i = 0; i < boardtest1.board.length; i++) {
            for (int j = 0; j < boardtest1.board[0].length; j++) {
                position=10*i+j;
                if (!pos.contains(position)){
                    assertEquals(prima.board[i][j].itemCard,boardtest1.board[i][j].itemCard);
                }
                else
                {
                    assertTrue(delete.contains(prima.board[i][j].itemCard));
                }
            }
        }

    }

    @Test
    void createcardBag() {
    }

    @Test
    void printBoard() {
    }
}