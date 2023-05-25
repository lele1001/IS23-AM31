package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * CG11: Five tiles of the same item forming a diagonal.
 * CG12: Five columns of increasing or decreasing height.
 * Starting from the first column on the left or on the right, each next column must be made of exactly one more tile.
 * Tiles can be of any type.
 */
public class CG11_12 extends ComGoal {
    boolean color;

    public CG11_12(int playerNum, int CGID) {
        super(playerNum, CGID);

        if (CGID == 11) {
            this.color = true;
        } else if (CGID == 12) {
            this.color = false;
        }
    }

    @Override
    public int goalReached(Bookshelf l) {
        boolean found = false;
        int direction = 0;  // if = 0 from left to right, if = 1 from right to left.
        int offset = 0;
        int toReturn = 0;
        int diagCount = 0; // number of diagonals that are checked

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

            for (i = 0; i < BOOKSHELF_HEIGHT - 1; i++) {
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
        } else if ((direction == 1) && (l.get(BOOKSHELF_LENGTH - offset, 0) != null)) {
            myItem = l.get(BOOKSHELF_LENGTH - offset, 0).getMyItem();

            for (i = 0; i < BOOKSHELF_HEIGHT - 1; i++) {
                if (l.get(BOOKSHELF_LENGTH - i - offset, i) != null) {
                    if (color && l.get(BOOKSHELF_LENGTH - i - offset, i).getMyItem().equals(myItem)) {
                        itemCount++;
                    } else if (!color) {
                        if ((BOOKSHELF_LENGTH - i - offset == 0)) itemCount++;
                        else if (l.get(BOOKSHELF_LENGTH - i - offset - 1, i) == null) {
                            itemCount++;
                        }
                    }
                }
            }
        }

        return itemCount == 5;
    }
}
