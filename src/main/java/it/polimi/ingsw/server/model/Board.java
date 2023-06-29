package it.polimi.ingsw.server.model;

import it.polimi.ingsw.commons.HouseItem;
import it.polimi.ingsw.commons.ItemCard;
import it.polimi.ingsw.commons.ItemNumber;
import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.utils.Utils.DIM_BOARD;

/**
 * Class that defines all the characteristics and methods of the game model Board
 */
public class Board {

    private ItemCard[][] board = new ItemCard[DIM_BOARD][DIM_BOARD];
    private final ItemCard[][] oldBoard = new ItemCard[DIM_BOARD][DIM_BOARD];
    private ArrayList<ItemCard> cardBag = new ArrayList<>();
    private final int numPlayers;

    //Matrix used for creating the board using the minimum number of players required to put an Itemcard in the Cell
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
     * Set the board, and the card bag from existing ones
     *
     * @param board      the board to copy
     * @param cardBag    the card bag to copy
     * @param numPlayers the number of players
     */
    public Board(ItemCard[][] board, ArrayList<ItemCard> cardBag, int numPlayers) {
        this.board = board;
        this.cardBag = cardBag;
        this.numPlayers = numPlayers;
    }

    /**
     * Method that fills the board with Itemcard if there is not in the correspondent Cell
     * If the exception is thrown, the Game controller has to be notified
     *
     * @throws EmptyCardBagException if the card bag is empty
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

        for (int i = 0; i < DIM_BOARD; i++) {
            System.arraycopy(board[i], 0, oldBoard[i], 0, DIM_BOARD);
        }

        if (cardBag.isEmpty()) System.out.println("Finished Itemcard, cannot be reached");
    }

    /**
     * Check if there are only isolated cards on the board
     * I cannot have an edge case because edges are always null
     *
     * @return true if the refill of the board is done correctly
     * @throws EmptyCardBagException if the card bag is empty
     */
    public synchronized boolean checkRefill() throws EmptyCardBagException {
        boolean refill = true;

        for (int i = 0; i < DIM_BOARD && refill; i++) {
            for (int j = 0; j < DIM_BOARD && refill; j++) {
                if (((i == 0 || i == 8) || (j == 0 || j == 8)) && board[i][j] != null && numMinPlayer[i][j] <= numPlayers) {
                    refill = checkSide(i, j);
                } else if (board[i][j] != null && numMinPlayer[i][j] <= numPlayers) {
                    refill = checkPosition(i, j);
                }
            }
        }

        if (refill && !cardBag.isEmpty()) {
            System.out.println("REFILLING THE BOARD");
            fillBoard();
        }

        return (refill && !cardBag.isEmpty());
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
            if (board[i + 1][j] != null || board[i][j + 1] != null || board[i][j - 1] != null) {
                return false;
            }
        }

        if (i == 8) {
            if (board[i - 1][j] != null || board[i][j + 1] != null || board[i][j - 1] != null) {
                return false;
            }
        }

        if (j == 0) {
            if (board[i + 1][j] != null || board[i - 1][j] != null || board[i][j + 1] != null) {
                return false;
            }
        }

        if (j == 8) {
            return board[i + 1][j] == null && board[i - 1][j] == null && board[i][j - 1] == null;
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
     * First check controls that the position numbers are contained in the board
     * Second check controls that in the selected Cells there are the ItemCards
     * Called two separate methods to do other controls
     *
     * @param position is the number from which we can extract row and column
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
     * Private method called by checkSelection that controls that all the tiles
     * in the ArrayList position have at least 1 clear side from other ItemCards
     *
     * @param position is the number from which we can extract row and column
     * @return true if a side of an Itemcard is clear from others
     */
    private boolean checkClearSideSelection(ArrayList<Integer> position) {
        Collections.sort(position);
        int i;
        int j;
        boolean sideClear = true;

        for (int k = 0; k < position.size() && sideClear; k++) {
            i = Position.getRow(position.get(k));
            j = Position.getColumn(position.get(k));

            if (i != 0 && i != 8 && j != 0 && j != 8) {
                sideClear = board[i + 1][j] == null || board[i - 1][j] == null || board[i][j - 1] == null || board[i][j + 1] == null;
            }
        }

        return sideClear;
    }

    /**
     * Private method called by checkSelection that controls that the selected tiles
     * form a straight line (change of row_position or column_position)
     *
     * @param position is the number from which we can extract row and column
     * @return true if the ItemCards are in a straight line
     */
    private boolean checkStraightSelection(ArrayList<Integer> position) {
        if (position.size() == 3) {
            if ((Position.getRow(position.get(0)) == Position.getRow(position.get(1)) - 1) && (Position.getRow(position.get(1)) == Position.getRow(position.get(2)) - 1) && (Position.getColumn(position.get(0)) == Position.getColumn(position.get(1))) && (Position.getColumn(position.get(1)) == Position.getColumn(position.get(2)))) {
                return true;
            }

            if ((Position.getColumn(position.get(0)) == Position.getColumn(position.get(1)) - 1) && (Position.getColumn(position.get(1)) == Position.getColumn(position.get(2)) - 1) && (Position.getRow(position.get(0)) == Position.getRow(position.get(1))) && (Position.getRow(position.get(1)) == Position.getRow(position.get(2)))) {
                return true;
            }
        }

        if (position.size() == 2) {
            if ((Position.getRow(position.get(0)) == Position.getRow(position.get(1)) - 1) && (Position.getColumn(position.get(0)) == Position.getColumn(position.get(1)))) {
                return true;
            }

            if ((Position.getColumn(position.get(0)) == Position.getColumn(position.get(1)) - 1) && (Position.getRow(position.get(0)) == Position.getRow(position.get(1)))) {
                return true;
            }
        }

        return position.size() == 1;
    }

    /**
     * Delete ItemCards in the selected Cells only if all the checks are successful
     *
     * @param position number from which we can extract row and column
     * @return the ItemCards deleted
     * @throws NoRightItemCardSelection if the selected ItemCards do not pass the check selection
     */

    public synchronized ArrayList<ItemCard> deleteSelection(ArrayList<Integer> position) throws NoRightItemCardSelection {
        Collections.sort(position);

        if (!checkSelection(position)) {
            throw new NoRightItemCardSelection();
        }

        //Saves a copy of the old board
        for (int i = 0; i < DIM_BOARD; i++) {
            System.arraycopy(board[i], 0, oldBoard[i], 0, DIM_BOARD);
        }

        ArrayList<ItemCard> toBeReturned = new ArrayList<>();

        //Removes from the board the selected positions
        for (Integer pos : position) {
            toBeReturned.add(board[Position.getRow(pos)][Position.getColumn(pos)]);
            board[Position.getRow(pos)][Position.getColumn(pos)] = null;
        }

        return toBeReturned;
    }

    /**
     * Resume the board from the old one when a player disconnects during his turn
     */
    public synchronized void resumeBoard() {
        for (int i = 0; i < DIM_BOARD; i++) {
            System.arraycopy(oldBoard[i], 0, board[i], 0, DIM_BOARD);
        }
    }

    /**
     * Backup up the board in the old one
     */
    public synchronized void backupBoard() {
        for (int i = 0; i < DIM_BOARD; i++) {
            System.arraycopy(board[i], 0, oldBoard[i], 0, DIM_BOARD);
        }
    }

    /**
     * Create the card bag using a defined number of tiles of each type
     */
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

    /**
     * Returns a copy of the board as a matrix.
     *
     * @return the board as a matrix.
     */
    public synchronized ItemCard[][] getAsMatrix() {
        ItemCard[][] toBeReturned = new ItemCard[DIM_BOARD][DIM_BOARD];

        for (int i = 0; i < DIM_BOARD; i++) {
            System.arraycopy(board[i], 0, toBeReturned[i], 0, DIM_BOARD);
        }

        return toBeReturned;
    }

    /**
     * Returns a copy of the card bag.
     *
     * @return the card bag as an Arraylist.
     */
    public List<ItemCard> getCardBag() {
        return List.copyOf(cardBag);
    }
}