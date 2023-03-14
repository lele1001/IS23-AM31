package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.HouseItem;
import it.polimi.ingsw.model.Library;

// Find four tiles of the same item at the corners of the bookshelf
public class CG8 extends ComGoal {
    public CG8(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library l) {
        int toReturn = 0;
        HouseItem myItem, it1, it2, it3;
        boolean cornersFound = false;

        if ((l.get(0, 0) != null) && (l.get(0, 4) != null)) {
            myItem = l.get(0, 0).getMyItem();
            it1 = l.get(0, 4).getMyItem();
            it2 = l.get(5, 0).getMyItem();
            it3 = l.get(5, 4).getMyItem();

            if ((it1 == myItem) && (it2 == myItem) && (it3 == myItem)) {
                cornersFound = true;
            }
        }

        if (cornersFound) {
            //Player.score += this.score.get(0);
            toReturn = this.score.get(0);
            this.score.remove(score.get(0));
        }

        return toReturn;
    }
}
