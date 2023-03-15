package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.model.HouseItem.Cat;
import static it.polimi.ingsw.model.ItemNumber.First;

public class Board {
    Cell[][] board = new Cell[9][9];
    ArrayList<ItemCard> cardBag = new ArrayList<>();

    //Arraylist used for creating the board using the minimum number of player to put an ItemCard in the Cell.
    private final ArrayList<Integer> Numplayers2 = new ArrayList<>((Arrays.asList(13, 14, 23, 24, 25, 32, 33, 34, 35, 36, 37, 41, 42, 43, 44, 45, 46, 47, 51, 52, 53, 54, 55, 56, 63, 64, 65, 74, 75)));
    private final ArrayList<Integer> Numplayers3 = new ArrayList<>(Arrays.asList(3, 22, 26, 38, 50, 62, 66, 85));
    private final ArrayList<Integer> Numplayers4 = new ArrayList<>(Arrays.asList(4, 15, 31, 40, 48, 57, 73, 84));

    Board() {
        int numcell;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                numcell = 10 * i + j;
                if (Numplayers4.contains(numcell)) board[i][j] = new Cell(4);
                else if (Numplayers3.contains(numcell)) board[i][j] = new Cell(3);
                else if (Numplayers2.contains(numcell)) board[i][j] = new Cell(2);
                else board[i][j] = new Cell(5);
            }
        }
        this.createcardBag();
        Collections.shuffle(cardBag);
    }

    //S
    public void fillBoard(int NumofPlayers) {
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.itemCard == null) {
                    if (cell.numPlayermin <= NumofPlayers) {
                        cell.itemCard = cardBag.get(0);
                        cardBag.remove(0);
                    }
                }
            }
        }
    }

    // Check if the there are only isolated card on the board
    // Checking only if there is a card in the Cell
    // I cannot have an edge case because edges are always null
    public void checkRefill(int NumofPlayers) {
        boolean refill = true;
        for (int i = 0; i < board.length && refill; i++) {
            for (int j = 0; j < board[0].length && refill; j++) {
                if (((i == 0 || i == 8) || (j == 0 || j == 8)) && board[i][j].itemCard != null && board[i][j].numPlayermin <= NumofPlayers)
                    refill = checkSide(i, j);
                else if (board[i][j].itemCard != null && board[i][j].numPlayermin <= NumofPlayers)
                    refill = checkPosition(i, j);
            }
        }
        if (refill) {
            System.out.println("REFILLING THE BOARD");
            fillBoard(NumofPlayers);
        }
    }

    public boolean checkSide(int i, int j) {
        if (i == 0) {
            if (board[i + 1][j].itemCard != null || board[i][j + 1].itemCard != null || board[i][j - 1].itemCard != null)
                return false;
        }
        if (i == 8) {
            if (board[i - 1][j].itemCard != null || board[i][j + 1].itemCard != null || board[i][j - 1].itemCard != null)
                return false;
        }
        if (j == 0) {
            if (board[i + 1][j].itemCard != null || board[i - 1][j].itemCard != null || board[i][j + 1].itemCard != null)
                return false;
        }
        if (j == 8) {
            if (board[i + 1][j].itemCard != null || board[i - 1][j].itemCard != null || board[i][j - 1].itemCard != null)
                return false;
        }
        return true;
    }

    public boolean checkPosition(int i, int j) {
        return board[i + 1][j].itemCard == null && board[i - 1][j].itemCard == null && board[i][j - 1].itemCard == null && board[i][j + 1].itemCard == null;
    }

    // The check if there isn't an Itemcard is not done here
    public boolean checkSelection(ArrayList<Integer> Position) {
        Collections.sort(Position);
        if (Position.size() == 3) {
            if ((Position.get(0) == Position.get(1) - 10) && (Position.get(1) == Position.get(2) - 10)) return true;
            if ((Position.get(0) == Position.get(1) - 1) && (Position.get(1) == Position.get(2) - 1)) return true;
        }
        if (Position.size() == 2) {
            if (Position.get(0) == Position.get(1) - 10) return true;
            if (Position.get(0) == Position.get(1) - 1) return true;
        }
        return false;
    }

    public ArrayList<ItemCard> deleteSelection(ArrayList<Integer> Position) {
        Collections.sort(Position);
        ArrayList<ItemCard> chosencard = new ArrayList<>();
        for (int pos : Position) {
            if (board[pos / 10][pos % 10].itemCard == null)
                System.out.println("Not find card at" + pos / 10 + " " + pos % 10);
            chosencard.add(board[pos / 10][pos % 10].itemCard);
            board[pos / 10][pos % 10].itemCard = null;
        }
        return chosencard;
    }

    //Todo for Mila, already created ItemCard
    public void createcardBag() {
        for (int i = 0; i < 132; i++) {
            cardBag.add(new ItemCard(Cat, First));
        }
    }

    public void printBoard() {
        //Todo when ItemCards are created (print must be chosen on what time of card we have in the cell)
        // Print change base on what we use
    }
}
