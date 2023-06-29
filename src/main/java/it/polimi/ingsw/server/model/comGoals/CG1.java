package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.commons.HouseItem;

import java.util.ArrayList;

import static it.polimi.ingsw.utils.Utils.BOOKSHELF_HEIGHT;
import static it.polimi.ingsw.utils.Utils.BOOKSHELF_LENGTH;

/**
 * Two separate groups each containing four tiles of the same type in a 2×2 square.
 * The tiles of one square can be different from those of the other square.
 */
public class CG1 extends ComGoal {
    /**
     * Set the Common Goal based on the number of players
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public CG1(int playerNum, int CGID) {
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
        int r, c;
        long groupsFound;
        int toReturn = 0;
        HouseItem myItem, it1, it2, it3, it4;
        int[][] fakeLib = new int[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH];
        ArrayList<HouseItem> items = new ArrayList<>();

        for (c = 0; c < BOOKSHELF_LENGTH - 1; c++) {
            for (r = 0; r < BOOKSHELF_HEIGHT - 1; r++) {
                if ((l.get(r, c) != null) && (fakeLib[r][c] != 1)) {
                    myItem = l.get(r, c).getMyItem();

                    if ((myItem == l.get(r + 1, c).getMyItem()) && (l.get(r, c + 1) != null)) {
                        if ((myItem == l.get(r, c + 1).getMyItem()) && (myItem == l.get(r + 1, c + 1).getMyItem())) {
                            fakeLib[r][c] = 1;
                            fakeLib[r][c + 1] = 1;
                            fakeLib[r + 1][c] = 1;
                            fakeLib[r + 1][c + 1] = 1;

                            if ((r < BOOKSHELF_HEIGHT - 2) && (c < BOOKSHELF_LENGTH - 2)) {
                                it1 = l.get(r + 2, c).getMyItem();
                                it2 = l.get(r + 2, c + 1).getMyItem();

                                if ((myItem != it1) && (myItem != it2)) {
                                    if (l.get(r, c + 2) != null) {
                                        it3 = l.get(r, c + 2).getMyItem();
                                        it4 = l.get(r + 1, c + 2).getMyItem();

                                        if ((myItem != it3) && (myItem != it4)) {
                                            items.add(myItem);
                                        }
                                    } else if (l.get(r + 1, c + 2) != null) {
                                        it4 = l.get(r + 1, c + 2).getMyItem();

                                        if (myItem != it4) {
                                            items.add(myItem);
                                        }
                                    } else {
                                        items.add(myItem);
                                    }
                                }
                            } else {
                                items.add(myItem);
                            }
                        }
                    }
                }
            }
        }

        groupsFound = items.size();

        if (groupsFound >= 2) {
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
