package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import java.util.ArrayList;

/**
 * Two separate groups each containing four tiles of the same type in a 2×2 square.
 * The tiles of one square can be different from those of the other square.
 */
public class CG1 extends ComGoal {
    public CG1(int playerNum, int CGID) {
        super(playerNum, CGID);
    }

    @Override
    public int goalReached(Bookshelf l) {
        int r, c;
        long groupsFound;
        int toReturn = 0;
        HouseItem myItem, it1, it2, it3, it4;
        int[][] fakeLib = new int[BookshelfHeight][BookshelfWidth];
        ArrayList<HouseItem> items = new ArrayList<>();

        for (c = 0; c < BookshelfWidth - 1; c++) {
            for (r = 0; r < BookshelfHeight - 1; r++) {
                if ((l.get(r, c) != null) && (fakeLib[r][c] != 1)) {
                    myItem = l.get(r, c).getMyItem();

                    if ((myItem == l.get(r + 1, c).getMyItem()) && (l.get(r, c + 1) != null)) {
                        if ((myItem == l.get(r, c + 1).getMyItem()) && (myItem == l.get(r + 1, c + 1).getMyItem())) {
                            fakeLib[r][c] = 1;
                            fakeLib[r][c + 1] = 1;
                            fakeLib[r + 1][c] = 1;
                            fakeLib[r + 1][c + 1] = 1;

                            // ensures that there
                            if ((r < BookshelfHeight - 2) && (c < BookshelfWidth - 2)) {
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
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
