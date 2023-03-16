package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Bookshelf;

import java.util.ArrayList;

/**
 * Find two separate groups of 4 tiles of the same item and creates a 2x2 square.
 * The tiles of the two groups should represent the same item.
 */
public class CG1 extends ComGoal {
    public CG1(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Bookshelf l) {
        int r, c;
        long groupsFound;
        int toReturn = 0;
        HouseItem myItem, it1;
        int[][] fakeLib = new int[5][6];
        ArrayList<HouseItem> items = new ArrayList<>();

        for (c = 0; c < 4; c++) {
            for (r = 0; r < 5; r++) {
                if ((l.get(r, c) != null) && (fakeLib[r][c] != 1)) {
                    myItem = l.get(r, c).getMyItem();

                    if ((myItem == l.get(r + 1, c).getMyItem()) && (l.get(r, c + 1) != null)) {
                        it1 = l.get(r, c + 1).getMyItem();

                        if ((myItem == it1) && l.get(r, c + 1).getMyItem() == myItem) {
                            fakeLib[r][c] = 1;
                            fakeLib[r][c + 1] = 1;
                            fakeLib[r + 1][c] = 1;
                            fakeLib[r + 1][c + 1] = 1;
                            items.add(myItem);
                        }
                    }
                }
            }
        }

        for (HouseItem it : items) {
            groupsFound = items.stream().filter(x -> x.equals(it)).count();

            if (groupsFound >= 2) {
                //Player.score += this.score.get(0);
                toReturn = this.score.get(0);
                this.score.remove(score.get(0));

                return toReturn;
            }
        }

        return toReturn;
    }
}
