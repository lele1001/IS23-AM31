package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Library;

// Find two columns each formed by 6 different item tiles.
public class CG2 extends ComGoal {
    public CG2(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        int toReturn = 0;
        int r = 0;
        int c = 0;
        int columnsFound = 0;
        boolean foundEqual = false;
        int i;

        while ((c <= 4) && (columnsFound < 2)) {
            while ((r < 5) && !foundEqual) {
                i = r + 1;

                while (i <= 5) {
                    if (l[i][c].get(i, c).getMyItem() == l[r][c].get(r, c).getMyItem()) {
                        foundEqual = true;
                        break;
                    }

                    i++;
                }

                r++;
            }

            if (!foundEqual) {
                columnsFound++;
            }

            c++;
        }

        if (columnsFound == 2) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }
        return toReturn;
    }
}
