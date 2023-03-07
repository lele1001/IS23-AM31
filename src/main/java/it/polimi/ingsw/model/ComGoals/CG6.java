package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Library;

public class CG6 extends ComGoal{
    public CG6(int playerNum) {
        super(playerNum);
    }

    @Override
    public int goalReached(Library[][] l) {
        return 0;
    }
}
