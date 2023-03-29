package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.HouseItem;

import java.util.ArrayList;

/**
 * Finds eight tiles of the same type without any restriction on their position
 */
public class CG9 extends ComGoal {
    public CG9(int playerNum, int CGID) {
        super(playerNum, CGID);
    }

    @Override
    public int goalReached(Bookshelf l) {
        int r, c, i;
        int toReturn = 0;
        HouseItem myItem;
        int[] itemsCount = new int[BookshelfHeight];
        ArrayList<HouseItem> items = new ArrayList<>();

        for (r = BookshelfHeight - 1; r >= 0; r--) {
            for (c = 0; c < BookshelfWidth; c++) {
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
