package it.polimi.ingsw.model;

import it.polimi.ingsw.ModelExceptions.EmptyCardBagException;
import it.polimi.ingsw.ModelExceptions.NoRightItemCardSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Board {
    private static final int DIM_BOARD = 9;
    Cell[][] board = new Cell[DIM_BOARD][DIM_BOARD];
    ArrayList<ItemCard> cardBag = new ArrayList<>();
    int numofplayers;

    //Arraylist used for creating the board using the minimum number of player to put an Itemcard in the Cell
    private final ArrayList<Integer> numplayers2 = new ArrayList<>((Arrays.asList(13, 14, 23, 24, 25, 32, 33, 34, 35, 36, 37, 41, 42, 43, 44, 45, 46, 47, 51, 52, 53, 54, 55, 56, 63, 64, 65, 74, 75)));
    private final ArrayList<Integer> Numplayers3 = new ArrayList<>(Arrays.asList(3, 22, 26, 38, 50, 62, 66, 85));
    private final ArrayList<Integer> Numplayers4 = new ArrayList<>(Arrays.asList(4, 15, 31, 40, 48, 57, 73, 84));

    /**
     * Creation of the Board with Cells block containing the ItemCard and the minimum number of players to insert an Itemcard during the refill of the board
     *
     * @param numofplayers number of players in the game
     */
    Board(int numofplayers) {
        this.numofplayers = numofplayers;
        int numcell;
        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                numcell = 10 * i + j;
                if (Numplayers4.contains(numcell)) board[i][j] = new Cell(4);
                else if (Numplayers3.contains(numcell)) board[i][j] = new Cell(3);
                else if (numplayers2.contains(numcell)) board[i][j] = new Cell(2);
                else board[i][j] = new Cell(5);
            }
        }
        this.createcardBag();
        Collections.shuffle(cardBag);
    }

    /**
     * Method that fills the board with Itemcard if there isn't in the correspondent Cell
     */
    public void fillBoard() throws EmptyCardBagException {

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (board[i][j].itemCard == null) {
                    if (board[i][j].numPlayermin <= numofplayers) {
                        board[i][j].itemCard = cardBag.get(0);
                        cardBag.remove(0);
                        if (cardBag.isEmpty()) {
                            throw new EmptyCardBagException();
                        }
                    }
                }
            }
        }
        if (cardBag.isEmpty()) System.out.println("Finished Itemcard");
    }

    /**
     * Check if the there are only isolated card on the board
     * Checking only if there is a card in the Cell
     * I cannot have an edge case because edges are always null
     */
    public void checkRefill() throws EmptyCardBagException {
        boolean refill = true;
        for (int i = 0; i < DIM_BOARD && refill; i++) {
            for (int j = 0; j < DIM_BOARD && refill; j++) {
                if (((i == 0 || i == 8) || (j == 0 || j == 8)) && board[i][j].itemCard != null && board[i][j].numPlayermin <= numofplayers)
                    refill = checkSide(i, j);
                else if (board[i][j].itemCard != null && board[i][j].numPlayermin <= numofplayers)
                    refill = checkPosition(i, j);
            }
        }
        if (refill && !cardBag.isEmpty()) {
            System.out.println("REFILLING THE BOARD");
            fillBoard();
        }
    }

    /**
     * Method that check the possible near Cells position
     *
     * @param i row
     * @param j column
     * @return false if it finds an Itemcard near the one passed
     */

    private boolean checkSide(int i, int j) {
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

    /**
     * Method that check the 4 near Cells position if it finds an Itemcard it returns false, used for the internal Cells
     *
     * @param i row
     * @param j column
     * @return false if it finds an Itemcard near the one passed, true otherwise
     */
    private boolean checkPosition(int i, int j) {
        return board[i + 1][j].itemCard == null && board[i - 1][j].itemCard == null && board[i][j - 1].itemCard == null && board[i][j + 1].itemCard == null;
    }

    /**
     * Check if the Itemcard can be deleted
     * First check controls that the positions number are contained in the board
     * Second check controls that in the selected Cells there are the Itemcards
     * Called two separate methods to do other controls
     *
     * @param position number from which we can extract row and column
     * @return
     */
    public boolean checkSelection(ArrayList<Integer> position) {
        Collections.sort(position);

        for (Integer integer : position) {
            if (Position.getrow(integer) >= DIM_BOARD || Position.getcolumn(integer) >= DIM_BOARD) {
                return false;
            }
        }
        for (Integer pos : position) {
            if (board[Position.getrow(pos)][Position.getcolumn(pos)].itemCard == null) {
                return false;
            }
        }

        return checkStraightSelection(position) && checkClearSideSelection(position);
    }

    /**
     * Private method called by checkSelection that controls that all the tiles inb position have at least 1 clear side from other Itemcards
     *
     * @param position number from which we can extract row and column
     * @return
     */
    private boolean checkClearSideSelection(ArrayList<Integer> position) {
        Collections.sort(position);
        int i;
        int j;
        boolean sideclear = true;
        for (int k = 0; k < position.size() && sideclear; k++) {
            i = Position.getrow(position.get(k));
            j = Position.getcolumn(position.get(k));
            if (i != 0 && i != 8 && j != 0 && j != 8) {
                sideclear = board[i + 1][j].itemCard == null || board[i - 1][j].itemCard == null || board[i][j - 1].itemCard == null || board[i][j + 1].itemCard == null;
            }
        }
        return sideclear;
    }

    /**
     * Private method called by checkSelection that controls that the selected Tiles for a straight Line (change of row_position or column_position)
     *
     * @param position number from which we can extract row and column
     * @return
     */
    private boolean checkStraightSelection(ArrayList<Integer> position) {
        if (position.size() == 3) {
            if ((Position.getrow(position.get(0)) == Position.getrow(position.get(1)) - 1) && (Position.getrow(position.get(1)) == Position.getrow(position.get(2)) - 1) && (Position.getcolumn(position.get(0)) == Position.getcolumn(position.get(1))) && (Position.getcolumn(position.get(1)) == Position.getcolumn(position.get(2))))
                return true;
            if ((Position.getcolumn(position.get(0)) == Position.getcolumn(position.get(1)) - 1) && (Position.getcolumn(position.get(1)) == Position.getcolumn(position.get(2)) - 1) && (Position.getrow(position.get(0)) == Position.getrow(position.get(1))) && (Position.getrow(position.get(1)) == Position.getrow(position.get(2))))
                return true;
        }
        if (position.size() == 2) {
            if ((Position.getrow(position.get(0)) == Position.getrow(position.get(1)) - 1) && (Position.getcolumn(position.get(0)) == Position.getcolumn(position.get(1))))
                return true;
            if ((Position.getcolumn(position.get(0)) == Position.getcolumn(position.get(1)) - 1) && (Position.getrow(position.get(0)) == Position.getrow(position.get(1))))
                return true;
        }
        if (position.size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * Delete Itemcards in the selected Cells only if all the checks are successful
     *
     * @param position number from which we can extract row and column
     * @return
     * @throws NoRightItemCardSelection if the selected itemcards don't pas the check selection
     */

    public ArrayList<ItemCard> deleteSelection(ArrayList<Integer> position) throws NoRightItemCardSelection {
        Collections.sort(position);
        ArrayList<ItemCard> chosencard = new ArrayList<>();
        if (!checkSelection(position)) {
            throw new NoRightItemCardSelection();
        }
        for (Integer pos : position) {
            chosencard.add(board[Position.getrow(pos)][Position.getcolumn(pos)].itemCard);
            board[Position.getrow(pos)][Position.getcolumn(pos)].itemCard = null;
        }
        return chosencard;
    }

    //Todo for Mila, already created ItemCard
    public void createcardBag() {
        for (int i = 0; i < 132; i++) {
            cardBag.add(new ItemCard(HouseItem.Cat, ItemNumber.First));
        }
    }
}
