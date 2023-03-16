package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;

/**
 * Finds eight tiles of the same type without any restriction on their position
 */
public class CG9 extends ComGoal {
    public CG9(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Bookshelf l) {
        int r, c, i;
        int toReturn = 0;
        HouseItem myItem;
        int[] itemsCount = new int[6];
        ArrayList<HouseItem> items = new ArrayList<>();

        for (r = 5; r >= 0; r--) {
            for (c = 0; c <= 4; c++) {
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
                //Player.score += this.score.get(0);
                toReturn = this.score.get(0);
                this.score.remove(score.get(0));
            }
        }

        return toReturn;
    }
}
