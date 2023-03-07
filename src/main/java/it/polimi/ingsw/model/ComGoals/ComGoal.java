package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.Library;
import java.util.*;

public abstract class ComGoal {
    ArrayList<Integer> score = new ArrayList<>();

    public ComGoal(int playerNum) {
        this.score.add(0, 8);
        this.score.add(1, 4);

        switch(playerNum) {
            case 2:
            case 3:
                this.score.add(1, 6);
            case 4:
                this.score.add(1, 6);
                this.score.add(3, 2);
        }
    }

    public abstract int goalReached(Library[][] l);
}
