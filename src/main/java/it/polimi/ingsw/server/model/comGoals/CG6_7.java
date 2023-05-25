package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * CG6: Two lines each formed by 5 different types of tiles.
 * One line can show the same or a different combination of the other line.
 * CG7: Four lines each formed by 5 tiles of maximum three different types.
 * One line can show the same or a different combination of another line.
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

        for (r = 0; r < BOOKSHELF_HEIGHT; r++) {
            nullCell = false;

            if (!items.isEmpty()) {
                initSize = items.size();

                for (i = 0; i < initSize; i++) {
                    items.remove(0);
                }
            }

            for (c = 0; c < BOOKSHELF_LENGTH; c++) {
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