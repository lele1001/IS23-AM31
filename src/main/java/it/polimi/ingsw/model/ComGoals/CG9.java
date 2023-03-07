package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Library;

public class CG9 extends ComGoal{
    public CG9(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        return 0;
    }
}
