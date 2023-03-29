package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import java.util.ArrayList;

/**
 * Used for cards that looks for differences in rows (common goal number 6 and 7)
 */
public class CG6_7 extends ComGoal {
    int numDiff;

    public CG6_7(int playerNum, int CGID) {
        super(playerNum, CGID);

        if (CGID == 6) {
            this.numDiff = 5;
        } else if (CGID == 7) {
            this.numDiff = 3;
        }
    }

    @Override
    public int goalReached(Bookshelf l) {
        int toReturn = 0;

        if (((numDiff == 5) && (lookDiff(l, numDiff) >= 2)) || ((numDiff == 3) && (lookDiff(l, numDiff) >= 4))) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }

    /**
     * Finds how many rows have numDiff >= this.numDiff
     *
     * @return number of rows found
     */
    private int lookDiff(Bookshelf l, int numDiff) {
        int r, c, i, initSize;
        long diffFound;
        int groupsFound = 0;
        boolean nullCell;
        HouseItem myItem;
        ArrayList<HouseItem> items = new ArrayList<>();

        for (r = 0; r < BookshelfHeight; r++) {
            nullCell = false;

            if (!items.isEmpty()) {
                initSize = items.size();

                for (i = 0; i < initSize; i++) {
                    items.remove(0);
                }
            }

            for (c = 0; c < BookshelfWidth; c++) {
                if (l.get(r, c) != null) {
                    myItem = l.get(r, c).getMyItem();

                    if (!items.contains(myItem)) {
                        items.add(myItem);
                    }
                } else {
                    nullCell = true;
                }
            }

            diffFound = items.stream().distinct().count();

            if (((numDiff == 5) && (diffFound == numDiff)) || (!nullCell && (numDiff == 3) && (diffFound <= numDiff))) {
                groupsFound++;
            }
        }

        return groupsFound;
    }
}