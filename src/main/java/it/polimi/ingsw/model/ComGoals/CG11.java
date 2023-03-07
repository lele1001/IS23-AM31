package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Library;

// Find 5 tiles, which represent the same item and form a diagonal.
public class CG11 extends ComGoal {
    public CG11(int playerNum) {
        super(playerNum);
    }
    @Override
    public int goalReached(Library[][] l) {
        boolean endCheck = false;
        boolean found = false;
        int direction = 0;  // if = 0 from left to right, if = 1 from right to left.
        int c = 0;
        int r = 0;
        HouseItem myItem;
        int i, j;
        int toReturn = 0;

        while (found || endCheck) {
            if (l[r][c] != null) {
                myItem = l[r][c].get(r, c).getMyItem();
                i = r;
                j = c;
                if (direction == 0) {
                    while ((l[i][j] != null) || ((i <= r + 4) && (j <= c + 4))) {
                        if (l[i][j].get(i, j).getMyItem() != myItem) {
                            break;
                        }
                        i++;
                        j++;
                    }

                    if ((i == r + 4) && (j == c + 4)) {
                        found = true;
                        break;
                    }
                }
                else if (direction == 1) {
                    while ((l[i][j] != null) || ((i >= r - 4) && (j >= c - 4))) {
                        if (l[i][j].get(i, j).getMyItem() != myItem) {
                            break;
                        }
                        i--;
                        j--;
                    }

                    if ((i == r - 4) && (j == c - 4)) {
                        found = true;
                        break;
                    }
                }
            }

            if (((r == 0) && (c == 0)) || ((r == 0) && (c == 4))) {
                r = 1;
            }
            else if ((r == 1) && (c == 0)) {
                r = 0;
                c = 4;
                direction = 1;
            }
            else {
                endCheck = true;
            }
        }

        if (found) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
