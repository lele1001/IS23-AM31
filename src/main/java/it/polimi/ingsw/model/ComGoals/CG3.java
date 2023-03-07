package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Library;

// Find four separate groups of 4 adjacent tiles of the same item.
// The tiles of the one group may differ from the ones of another group.
public class CG3 extends ComGoal {
    public CG3(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        int toReturn = 0;
        int r = 0;
        int c = 0;
        int groupsFound = 0;
        boolean foundDiff = false;
        HouseItem myItem;
        int i;

        // check on columns
        while ((c <= 4) && (groupsFound < 4)) {
            while (r <= 2) {
                if (l[r][c] == null) {
                    r++;
                }
                else {
                    myItem = l[r][c].get(r, c).getMyItem();
                    i = r + 1;

                    while (i <= r + 3) {
                        if (l[i][c].get(i, c).getMyItem() != myItem) {
                            foundDiff = true;
                            r++;
                            break;
                        }
                        i++;
                    }

                    if (!foundDiff) {
                        groupsFound++;
                    }
                }
            }

            c++;
        }

        // check on rows
        r = 0;
        c = 0;
        while ((r <= 5) && (groupsFound < 4)) {
            while (c <= 1) {
                if (l[r][c] == null) {
                    c++;
                }
                else {
                    myItem = l[r][c].get(r, c).getMyItem();
                    i = c + 1;

                    while (i <= c + 3) {
                        if ((l[r][i] == null) || (l[r][i].get(r, i).getMyItem() != myItem)) {
                            foundDiff = true;
                            c++;
                            break;
                        }
                        i++;
                    }

                    if (!foundDiff) {
                        groupsFound++;
                    }
                }
            }

            r++;
        }

        if (groupsFound == 4) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
