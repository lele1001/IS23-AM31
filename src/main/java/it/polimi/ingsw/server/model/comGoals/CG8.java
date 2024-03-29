package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.commons.HouseItem;

/**
 * Four tiles of the same type in the four corners of the bookshelf.
 */
public class CG8 extends ComGoal {
    /**
     * Set the Common Goal based on the number of players
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public CG8(int playerNum, int CGID) {
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
        int toReturn = 0;
        HouseItem myItem, it1, it2, it3;
        boolean cornersFound = false;

        if ((l.get(0, 0) != null) && (l.get(0, 4) != null)) {
            //Gets the items in all the corners of the Bookshelf
            myItem = l.get(0, 0).getMyItem();
            it1 = l.get(0, 4).getMyItem();
            it2 = l.get(5, 0).getMyItem();
            it3 = l.get(5, 4).getMyItem();

            if ((it1 == myItem) && (it2 == myItem) && (it3 == myItem)) {
                cornersFound = true;
            }
        }

        if (cornersFound) {
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
