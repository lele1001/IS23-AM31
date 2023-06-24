package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * CG2: Two columns each formed by 6 different types of tiles.
 * One column can show the same or a different combination of the other column.
 * CG5: Three columns each formed by 6 tiles of maximum three different types.
 * One column can show the same or a different combination of another column.
 */
public class CG2_5 extends ComGoal {
    int numDiff;

    /**
     * Set the Common Goal based on the number of players
     * Set up variables for controls
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public CG2_5(int playerNum, int CGID) {
        super(playerNum, CGID);

        if (CGID == 2) {
            this.numDiff = 6;
        } else if (CGID == 5) {
            this.numDiff = 3;
        }
    }

    /**
     * Checks if the player reached the common goals
     *
     * @return score assigned to the player
     * the score is 0 if the goal is not reached, otherwise it is the max score available for the goal
     */
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
     *
     * @return number of rows found
     */
    private int lookDiff(Bookshelf l, int numDiff) {
        int r, c;
        long diffFound;
        int groupsFound = 0;
        HouseItem myItem;
        ArrayList<HouseItem> items = new ArrayList<>();

        for (c = 0; c < BOOKSHELF_LENGTH; c++) {
            items.clear();
            if (l.get(0, c) != null) {
                for (r = 0; r < BOOKSHELF_HEIGHT; r++) {
                    if (l.get(r, c) != null) {
                        myItem = l.get(r, c).getMyItem();

                        if (!items.contains(myItem)) {
                            items.add(myItem);
                        }
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