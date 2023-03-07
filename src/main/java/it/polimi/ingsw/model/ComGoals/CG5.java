package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Library;

import java.util.*;

// Find three columns of 6 tiles of maximum three different items.
// One column can show the same or a different combination of another column.
public class CG5 extends ComGoal{
    public CG5(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        int toReturn = 0;
        int r = 0;
        int c = 0;
        int columnsFound = 0;
        HouseItem[] items = new HouseItem[6];
        HouseItem myItem;
        int i;

        while ((c <= 4) && (columnsFound < 3)) {
            while (r < 5) {
                myItem = l[r][c].get(r, c).getMyItem();

                for (i = r; i < items.length; i++) {
                    if (items[i].equals(myItem)) {
                        items[i] = myItem;
                    }
                }

                r++;
            }

            if (Arrays.stream(items).distinct().count() <= 3) {
                columnsFound++;
            }

            c++;
        }

        if (columnsFound == 3) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }
        return toReturn;
    }
}