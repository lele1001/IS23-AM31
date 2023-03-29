package it.polimi.ingsw.server.model;

import java.util.*;

public class Bookshelf {
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    private final ItemCard[][] bookshelf = new ItemCard[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH];


    /**
     * @requires checkSpace(column, cards.size ())==TRUE && cards.size()<=3
     * Insert cards in the bookshelf (at the indicated column)
     */
    public void insertCard(List<ItemCard> cards, int column) {
        int i;

        for (i = BOOKSHELF_HEIGHT - 1; i >= 0; i--) {
            if (bookshelf[i][column] == null) break;
        }
        for (ItemCard ic : cards) {
            bookshelf[i][column] = ic;
            i--;
        }
    }

    /**
     * @return a copy of the bookshelf.
     */
    public ItemCard[][] getAsMatrix() {
        ItemCard[][] toBeReturned = new ItemCard[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH];
        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            System.arraycopy(bookshelf[i], 0, toBeReturned[i], 0, BOOKSHELF_LENGTH);
        }
        return toBeReturned;
    }

    /**
     * @return a boolean which indicates if there is space in the indicated column for 'num' elements
     */
    public boolean checkSpace(int column, int num) {
        if ((num > BOOKSHELF_HEIGHT) || (num <= 0) || (column < 0) || (column > BOOKSHELF_LENGTH - 1)) return false;
        int i;
        for (i = BOOKSHELF_HEIGHT - 1; i >= 0; i--) {
            if (bookshelf[i][column] == null) break;
        }
        return (i >= 0) && (i - num >= -1);
    }

    /**
     * @return the element in x, y position
     */
    public ItemCard get(int x, int y) {
        if ((x < 0) || (y < 0) || (x > BOOKSHELF_HEIGHT - 1) || (y > BOOKSHELF_LENGTH - 1)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return bookshelf[x][y];
    }

    /**
     * @return calculates the score of adjacency of this library
     */
    public int calcScore() {
        boolean[][] matrix = new boolean[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH];
        int score = 0;
        for (int i = 0; i < BOOKSHELF_HEIGHT; i++)
            for (int j = 0; j < BOOKSHELF_LENGTH; j++)
                matrix[i][j] = false;
        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                if ((bookshelf[i][j] != null) && (!matrix[i][j])) {
                    switch (calc(matrix, i, j)) {
                        case 1, 2:
                            break;
                        case 3:
                            score += 2;
                            break;
                        case 4:
                            score += 3;
                            break;
                        case 5:
                            score += 5;
                            break;
                        default:
                            score += 8;
                    }
//                    System.out.println("Riga "+i+" Colonna "+j+": "+score);
                }
            }
        }
        return score;
    }

    /**
     * A private method, used by calcScore(), based on recursive calls to calculate adjacency
     */
    private int calc(boolean[][] matrix, int i, int j) {
        int num = 1;
        matrix[i][j] = true;
        if ((j != 0) && (bookshelf[i][j - 1] != null) && (bookshelf[i][j - 1].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i][j - 1]))
            num += calc(matrix, i, j - 1);
        if ((j != BOOKSHELF_LENGTH - 1) && (bookshelf[i][j + 1] != null) && (bookshelf[i][j + 1].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i][j + 1]))
            num += calc(matrix, i, j + 1);
        if ((i != BOOKSHELF_HEIGHT - 1) && (bookshelf[i + 1][j] != null) && (bookshelf[i + 1][j].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i + 1][j]))
            num += calc(matrix, i + 1, j);
        if ((i != 0) && (bookshelf[i - 1][j] != null) && (bookshelf[i - 1][j].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i - 1][j]))
            num += calc(matrix, i - 1, j);
        return num;
    }


}
