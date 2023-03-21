package it.polimi.ingsw.server.model;

import java.util.*;

public class Bookshelf {
    private final ItemCard[][] bookshelf = new ItemCard[6][5];


    /**
     * @requires checkSpace(column, cards.size ())==TRUE && cards.size()<=3
     * Insert cards in the bookshelf (at the indicated column)
     */
    public void insertCard(List<ItemCard> cards, int column) {
        int i;

        for (i = 5; i >= 0; i--) {
            if (bookshelf[i][column] == null) break;
        }
        for (ItemCard ic : cards) {
            bookshelf[i][column] = ic;
            i--;
        }
    }

    /**
     * @return a boolean that indicates if there is space in the indicated column for 'num' elements
     */
    public boolean checkSpace(int column, int num) {
        if ((num > 6) || (num <= 0) || (column < 0) || (column > 4)) return false;
        int i;
        for (i = 5; i >= 0; i--) {
            if (bookshelf[i][column] == null) break;
        }
        return (i >= 0) && (i - num >= -1);
    }

    /**
     * @return the element in x, y position
     */
    public ItemCard get(int x, int y) {
        if ((x < 0) || (y < 0) || (x > 5) || (y > 4)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return bookshelf[x][y];
    }

    /**
     * @return calculates the score of adjacency of this library
     */
    public int calcScore() {
        boolean[][] matrix = new boolean[6][5];
        int score = 0;
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
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
        if ((j != 4) && (bookshelf[i][j + 1] != null) && (bookshelf[i][j + 1].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i][j + 1]))
            num += calc(matrix, i, j + 1);
        if ((i != 5) && (bookshelf[i + 1][j] != null) && (bookshelf[i + 1][j].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i + 1][j]))
            num += calc(matrix, i + 1, j);
        if ((i != 0) && (bookshelf[i - 1][j] != null) && (bookshelf[i - 1][j].getMyItem() == bookshelf[i][j].getMyItem()) && (!matrix[i - 1][j]))
            num += calc(matrix, i - 1, j);
        return num;
    }


}
