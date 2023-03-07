package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Library;

// Find six separate groups of 2 adjacent tiles of the same item.
// The tiles of the one group may differ from the ones of another group.
public class CG4 extends ComGoal {
    public CG4(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        int toReturn = 0;
        int r = 0;
        int c = 0;
        int groupsFound = 0;
        HouseItem myItem;

        // check on columns
        while ((c <= 4) && (groupsFound < 6)) {
            while (r <= 4) {
                if (l[r][c] == null) {
                    r++;
                }
                else {
                    myItem = l[r][c].get(r, c).getMyItem();

                    if (l[r + 1][c].get(r + 1, c).getMyItem() != myItem) {
                        r++;
                    }
                    else {
                        groupsFound++;
                    }
                }
            }

            c++;
        }

        // check on rows
        r = 0;
        c = 0;
        while ((r <= 5) && (groupsFound < 6)) {
            while (c <= 3) {
                if (l[r][c] == null) {
                    c++;
                }
                else {
                    myItem = l[r][c].get(r, c).getMyItem();

                    if ((l[r][c + 1] == null) || (l[r][c + 1].get(r, c + 1).getMyItem() != myItem)) {
                        c++;
                    }
                    else {
                        groupsFound++;
                    }
                }
            }

            r++;
        }

        if (groupsFound == 6) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}