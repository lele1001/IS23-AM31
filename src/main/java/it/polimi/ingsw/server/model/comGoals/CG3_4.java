package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * CG3: Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape).
 * The tiles of one group can be different from those of another group.
 * CG4: Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).
 * The tiles of one group can be different from those of another group.
 */
public class CG3_4 extends ComGoal {
    int multiplier;

    /**
     * Set the Common Goal based on the number of players
     * Set up variables for controls
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public CG3_4(int playerNum, int CGID) {
        super(playerNum, CGID);

        if (CGID == 3) {
            this.multiplier = 2;
        } else if (CGID == 4) {
            this.multiplier = 1;
        }
    }

    /**
     * Checks if the player reached the common goals
     *
     * @return score assigned to the player
     * the score is 0 if the goal is not reached, otherwise it is the max score available for the goal
     */
    @Override
    public int goalReached(Bookshelf l) {
        int groupsFound = 0;
        int toReturn = 0;
        boolean[][] matrix = new boolean[6][5];

        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                matrix[i][j] = false;
            }
        }

        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                if (!matrix[i][j] && (l.get(i, j) != null)) {
                    if (find(matrix, i, j, l) >= (2 * multiplier)) {
                        groupsFound++;
                    }
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
