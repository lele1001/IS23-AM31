package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ComGoal {
    private ArrayList<Integer> score = new ArrayList<>();

    public ComGoal(int playerNum) {
        switch(playerNum) {
            case 2:
                score.add(0, 8);
                score.add(1, 4);
            case 3:
                score.add(0, 8);
                score.add(1, 6);
                score.add(2, 4);
            case 4:
                score.add(0, 8);
                score.add(1, 6);
                score.add(2, 4);
                score.add(3, 2);
        }
    }

/*    public int goalReached(Library l) {

    }*/
}
