package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Library;

// Find two separate groups of 4 tiles of the same item and creates a 2x2 square.
// The tiles of the two groups should represent the same item.
public class CG1 extends ComGoal {
    public CG1(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        int r = 0;
        int c = 0;
        int groupsFound = 0;
        int toReturn = 0;
        HouseItem myItem, it1,it2, it3;

        while ((c < 4) && (groupsFound < 2)) {
            while ((r < 5) && (groupsFound < 2)) {
                if (l[r][c] != null) {
                    myItem = l[r][c].get(r, c).getMyItem();
                    it1 = l[r+1][c].get(r+1, c).getMyItem();

                    if (l[r+1][c+1] != null) {
                        it2 = l[r+1][c+1].get(r+1, c+1).getMyItem();

                        if (l[r][c+1] != null) {
                            it3 = l[r][c+1].get(r, c+1).getMyItem();

                            if ((it1 == myItem) && (it2 == myItem) && (it3 == myItem)) {
                                groupsFound++;
                                r += 2;
                            }
                            else {
                                r++;
                            }
                        }
                        else {
                            r++;
                        }
                    }
                    else {
                        r += 2;
                    }
                }
                else {
                    r++;
                }
            }
            c++;
        }

        if (groupsFound == 2) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
