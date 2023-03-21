package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;

import java.util.ArrayList;
import java.util.Collections;


public class Board {
    private static final int DIM_BOARD = 9;
    ItemCard[][] board = new ItemCard[DIM_BOARD][DIM_BOARD];
    ArrayList<ItemCard> cardBag = new ArrayList<>();
    int numPlayers;
    int[][] numMinPlayer = new int[][]{
        {5, 5, 5, 3, 4, 5, 5, 5, 5},
        {5, 5, 5, 2, 2, 4, 5, 5, 5},
        {5, 5, 3, 2, 2, 2, 3, 5, 5},
        {5, 4, 2, 2, 2, 2, 2, 2, 3},
        {4, 2, 2, 2, 2, 2, 2, 2, 4},
        {3, 2, 2, 2, 2, 2, 2, 4, 5},
        {5, 5, 3, 2, 2, 2, 3, 5, 5},
        {5, 5, 5, 4, 2, 2, 5, 5, 5},
        {5, 5, 5, 5, 4, 3, 5, 5, 5}};

    //Arraylist used for creating the board using the minimum number of player to put an Itemcard in the Cell

    /**
     * Creation of the Board with a matrix containing the minimum number of players to insert an Itemcard during the refill of the board
     *
     * @param numPlayers number of players in the game
     */
    public Board(int numPlayers) {
        this.numPlayers = numPlayers;
        this.createcardBag();
        Collections.shuffle(cardBag);
    }

    /**
     * Method that fills the board with Itemcard if there is not in the correspondent Cell
     */
    public void fillBoard() throws EmptyCardBagException {

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (board[i][j] == null) {
                    if (numMinPlayer[i][j] <= numPlayers) {
                        board[i][j] = cardBag.get(0);
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
     * Check if there are only isolated card on the board
     * I cannot have an edge case because edges are always null
     */
    public void checkRefill() throws EmptyCardBagException {
        boolean refill = true;
        for (int i = 0; i < DIM_BOARD && refill; i++) {
            for (int j = 0; j < DIM_BOARD && refill; j++) {
                if (((i == 0 || i == 8) || (j == 0 || j == 8)) && board[i][j] != null && numMinPlayer[i][j] <= numPlayers)
                    refill = checkSide(i, j);
                else if (board[i][j] != null && numMinPlayer[i][j] <= numPlayers)
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
            if (board[i + 1][j] != null || board[i][j + 1] != null || board[i][j - 1] != null)
                return false;
        }
        if (i == 8) {
            if (board[i - 1][j] != null || board[i][j + 1] != null || board[i][j - 1] != null)
                return false;
        }
        if (j == 0) {
            if (board[i + 1][j] != null || board[i - 1][j] != null || board[i][j + 1] != null)
                return false;
        }
        if (j == 8) {
            if (board[i + 1][j] != null || board[i - 1][j] != null || board[i][j - 1] != null)
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
        return board[i + 1][j] == null && board[i - 1][j] == null && board[i][j - 1] == null && board[i][j + 1] == null;
    }

    /**
     * Check if the Itemcard can be deleted
     * First check controls that the positions number are contained in the board
     * Second check controls that in the selected Cells there are the Itemcards
     * Called two separate methods to do other controls
     *
     * @param position number from which we can extract row and column
     * @return true if the Itemcard can be deleted
     */
    public boolean checkSelection(ArrayList<Integer> position) {
        Collections.sort(position);

        for (Integer integer : position) {
            if (Position.getRow(integer) >= DIM_BOARD || Position.getColumn(integer) >= DIM_BOARD) {
                return false;
            }
        }
        for (Integer pos : position) {
            if (board[Position.getRow(pos)][Position.getColumn(pos)] == null) {
                return false;
            }
        }

        return checkStraightSelection(position) && checkClearSideSelection(position);
    }

    /**
     * Private method called by checkSelection that controls that all the tiles inb position have at least 1 clear side from other Itemcards
     *
     * @param position number from which we can extract row and column
     * @return true if a side of an Itemcard is clear from others
     */
    private boolean checkClearSideSelection(ArrayList<Integer> position) {
        Collections.sort(position);
        int i;
        int j;
        boolean sideclear = true;
        for (int k = 0; k < position.size() && sideclear; k++) {
            i = Position.getRow(position.get(k));
            j = Position.getColumn(position.get(k));
            if (i != 0 && i != 8 && j != 0 && j != 8) {
                sideclear = board[i + 1][j] == null || board[i - 1][j] == null || board[i][j - 1] == null || board[i][j + 1] == null;
            }
        }
        return sideclear;
    }

    /**
     * Private method called by checkSelection that controls that the selected Tiles for a straight Line (change of row_position or column_position)
     *
     * @param position number from which we can extract row and column
     * @return true if the Itemcards are in a straight line
     */
    private boolean checkStraightSelection(ArrayList<Integer> position) {
        if (position.size() == 3) {
            if ((Position.getRow(position.get(0)) == Position.getRow(position.get(1)) - 1) && (Position.getRow(position.get(1)) == Position.getRow(position.get(2)) - 1) && (Position.getColumn(position.get(0)) == Position.getColumn(position.get(1))) && (Position.getColumn(position.get(1)) == Position.getColumn(position.get(2))))
                return true;
            if ((Position.getColumn(position.get(0)) == Position.getColumn(position.get(1)) - 1) && (Position.getColumn(position.get(1)) == Position.getColumn(position.get(2)) - 1) && (Position.getRow(position.get(0)) == Position.getRow(position.get(1))) && (Position.getRow(position.get(1)) == Position.getRow(position.get(2))))
                return true;
        }
        if (position.size() == 2) {
            if ((Position.getRow(position.get(0)) == Position.getRow(position.get(1)) - 1) && (Position.getColumn(position.get(0)) == Position.getColumn(position.get(1))))
                return true;
            if ((Position.getColumn(position.get(0)) == Position.getColumn(position.get(1)) - 1) && (Position.getRow(position.get(0)) == Position.getRow(position.get(1))))
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
     * @return an Arraylist with the selected ItemCards
     * @throws NoRightItemCardSelection if the selected itemcards don't pas the check selection
     */

    public ArrayList<ItemCard> deleteSelection(ArrayList<Integer> position) throws NoRightItemCardSelection {
        Collections.sort(position);
        ArrayList<ItemCard> chosencard = new ArrayList<>();
        if (!checkSelection(position)) {
            throw new NoRightItemCardSelection();
        }
        for (Integer pos : position) {
            chosencard.add(board[Position.getRow(pos)][Position.getColumn(pos)]);
            board[Position.getRow(pos)][Position.getColumn(pos)] = null;
        }
        return chosencard;
    }

    //Todo for Mila, already created ItemCard
    public void createcardBag() {

        for (HouseItem item : HouseItem.values()) {
            for (int i = 0; i < 8; i++) {
                cardBag.add(new ItemCard(item, ItemNumber.First));
            }
            for (int i = 0; i < 7; i++) {
                cardBag.add(new ItemCard(item, ItemNumber.Second));
            }
            for (int i = 0; i < 7; i++) {
                cardBag.add(new ItemCard(item, ItemNumber.Third));
            }
        }
    }
}