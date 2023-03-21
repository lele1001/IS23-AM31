package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.HouseItem;

public class CG3_4 extends ComGoal {
    int multiplier;

    public CG3_4(int playerNum, int multiplier) {
        super(playerNum);
        this.multiplier = multiplier;
    }

    @Override
    public int goalReached(Bookshelf l) {
        int groupsFound = 0;
        int toReturn = 0;
        boolean[][] matrix = new boolean[6][5];

        for (int i = 0; i < BookshelfHeight; i++) {
            for (int j = 0; j < BookshelfWidth; j++) {
                matrix[i][j] = false;
            }
        }

        for (int i = 0; i < BookshelfHeight; i++) {
            for (int j = 0; j < BookshelfWidth; j++) {
                if (!matrix[i][j] && (l.get(i, j) != null)) {
                    groupsFound += (find(matrix, i, j, l) / (2 * multiplier));
//                    System.out.println("Riga "+i+" Colonna "+j+": "+score);
                }
            }
        }

        if (((multiplier == 1) && (groupsFound >= 6)) || ((multiplier == 2) && (groupsFound >= 4))) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }

    /**
     * Method used to cycle on all Cells near the selected one with the same Item-card using a DFS algorithm
     */
    private int find(boolean[][] matrix, int i, int j, Bookshelf l) {
        int num = 1;
        matrix[i][j] = true;
        HouseItem myItem = l.get(i, j).getMyItem();

        if ((j != 0) && (l.get(i, j - 1) != null) && (l.get(i, j - 1).getMyItem() == myItem) && (!matrix[i][j - 1])) {
            num += find(matrix, i, j - 1, l);
        }

        if ((j != 4) && (l.get(i, j + 1) != null) && (l.get(i, j + 1).getMyItem() == myItem) && (!matrix[i][j + 1])) {
            num += find(matrix, i, j + 1, l);
        }

        if ((i != 5) && (l.get(i + 1, j) != null) && (l.get(i + 1, j).getMyItem() == myItem) && (!matrix[i + 1][j])) {
            num += find(matrix, i + 1, j, l);
        }

        if ((i != 0) && (l.get(i - 1, j) != null) && (l.get(i - 1, j).getMyItem() == myItem) && (!matrix[i - 1][j])) {
            num += find(matrix, i - 1, j, l);
        }

        return num;
    }
}
