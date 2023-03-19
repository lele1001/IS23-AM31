package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.HouseItem;

/**
 * Find 5 tiles, which form a diagonal (and sometimes represent the same item).
 */
public class CG11_12 extends ComGoal {
    boolean color;

    public CG11_12(int playerNum, boolean color) {
        super(playerNum);
        this.color = color;
    }

    @Override
    public int goalReached(Bookshelf l) {
        boolean found = false;
        int direction = 0;  // if = 0 from left to right, if = 1 from right to left.
        int offset = 0;
        int toReturn = 0;
        int diagCount = 0; // number of diagonals that I check

        while (diagCount < 4) {
            if (!found) {
                found = checkDiagonal(l, direction, offset);

                if (offset == 0) {
                    offset = 1;
                } else {
                    offset = 0;
                    direction = 1;
                }
            }

            diagCount++;
        }

        if (found) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }

    private boolean checkDiagonal(Bookshelf l, int direction, int offset) {
        HouseItem myItem;
        int i;
        int itemCount = 0; // number of colors equal to the first one

        if ((direction == 0) && (l.get(offset, 0) != null)) {
            myItem = l.get(offset, 0).getMyItem();

            for (i = 0; i < 5; i++) {
                if (l.get(i + offset, i) != null) {
                    if (color && l.get(i + offset, i).getMyItem().equals(myItem)) {
                        itemCount++;
                    } else if (!color) {
                        if ((i + offset == 0)) itemCount++;
                        else if (l.get(i + offset - 1, i) == null) {
                            itemCount++;
                        }
                    }
                }
            }
        } else if ((direction == 1) && (l.get(5 - offset, 0) != null)) {
            myItem = l.get(5 - offset, 0).getMyItem();

            for (i = 0; i < 5; i++) {
                if (l.get(5 - i - offset, i) != null) {
                    if (color && l.get(5 - i - offset, i).getMyItem().equals(myItem)) {
                        itemCount++;
                    } else if (!color) {
                        if ((5 - i - offset == 0)) itemCount++;
                        else if (l.get(5 - i - offset - 1, i) == null) {
                            itemCount++;
                        }
                    }
                }
            }
        }

        if (itemCount == 5) {
            return true;
        } else {
            return false;
        }
    }
}
