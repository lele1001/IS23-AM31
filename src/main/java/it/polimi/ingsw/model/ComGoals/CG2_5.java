package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.HouseItem;

import java.util.ArrayList;

/**
 * Used for cards that looks for differences in columns (common goals number 2 and 5)
 */
public class CG2_5 extends ComGoal {
    int numDiff;
    public CG2_5(int playerNum, int numDiff) {
        super(playerNum);
        this.numDiff = numDiff;
    }

    @Override
    public int goalReached(Bookshelf l) {
        int toReturn = 0;

        if (((numDiff == 6) && (lookDiff(l, numDiff) >= 2)) || ((numDiff == 3) && (lookDiff(l, numDiff) >= 3))) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }

    /**
     * Used for finding how many rows have numDiff >= this.numDiff
     * @return number of rows found
     */
    private int lookDiff(Bookshelf l, int numDiff) {
        int r, c, i, initSize;
        long diffFound;
        int groupsFound = 0;
        HouseItem myItem;
        ArrayList<HouseItem> items = new ArrayList<>();

        for (c = 0; c < BookshelfWidth; c++) {
            if (!items.isEmpty()) {
                initSize = items.size();

                for (i = 0; i < initSize; i++) {
                    items.remove(0);
                }
            }

            for (r = 0; r < BookshelfHeight; r++) {
                if (l.get(r, c) != null) {
                    myItem = l.get(r, c).getMyItem();

                    if (!items.contains(myItem)) {
                        items.add(myItem);
                    }
                }
            }

            diffFound = items.stream().distinct().count();

            if (((numDiff == 6) && (diffFound == numDiff)) || ((numDiff == 3) && (0 < diffFound) && (diffFound <= numDiff))) {
                groupsFound++;
            }
        }

        return groupsFound;
    }
}