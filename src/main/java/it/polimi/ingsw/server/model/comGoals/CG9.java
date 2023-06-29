package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.commons.HouseItem;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * Eight tiles of the same type. There is no restriction about the position of these tiles.
 */
public class CG9 extends ComGoal {
    /**
     * Set the Common Goal based on the number of players
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public CG9(int playerNum, int CGID) {
        super(playerNum, CGID);
    }

    /**
     * Checks if the player reached the common goals
     *
     * @return score assigned to the player
     * the score is 0 if the goal is not reached, otherwise it is the max score available for the goal
     */
    @Override
    public int goalReached(Bookshelf l) {
        int r, c, i;
        int toReturn = 0;
        HouseItem myItem;
        int[] itemsCount = new int[BOOKSHELF_HEIGHT];
        ArrayList<HouseItem> items = new ArrayList<>();

        for (r = BOOKSHELF_HEIGHT - 1; r >= 0; r--) {
            for (c = 0; c < BOOKSHELF_LENGTH; c++) {
                if (l.get(r, c) != null) {
                    myItem = l.get(r, c).getMyItem();

                    if (!items.contains(myItem)) {
                        items.add(myItem);
                    }

                    i = items.indexOf(myItem);
                    itemsCount[i]++;
                }
            }
        }

        for (i = 0; i < itemsCount.length; i++) {
            if (itemsCount[i] >= 8) {
                toReturn = this.score.get(0);
                this.score.remove(score.get(0));
            }
        }

        return toReturn;
    }
}
