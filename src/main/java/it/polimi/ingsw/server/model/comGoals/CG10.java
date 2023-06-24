package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * Five tiles of the same type forming an X
 */
public class CG10 extends ComGoal {
    /**
     * Set the Common Goal based on the number of players
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public CG10(int playerNum, int CGID) {
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
        int r = 0;
        int c;
        boolean found = false;
        HouseItem myItem, it1, it2, it3;

        while ((r < BOOKSHELF_HEIGHT - 2) && !found) {
            c = 0;
            while ((c < BOOKSHELF_LENGTH - 2) && !found) {
                if (l.get(r, c) != null) {
                    myItem = l.get(r, c).getMyItem();

                    if (l.get(r + 2, c).getMyItem() == myItem) {
                        if ((l.get(r, c + 2) != null) && (l.get(r + 1, c + 1) != null)) {
                            it1 = l.get(r, c + 2).getMyItem();
                            it2 = l.get(r + 2, c + 2).getMyItem();
                            it3 = l.get(r + 1, c + 1).getMyItem();

                            if ((it1 == myItem) && (it2 == myItem) && (it3 == myItem)) {
                                found = true;
                            }
                        }
                    }
                }

                c++;
            }

            r++;
        }

        if (found) {
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
