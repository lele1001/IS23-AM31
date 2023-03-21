package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;

import java.util.ArrayList;

public abstract class ComGoal {
    final static int BookshelfHeight = 6;
    final static int BookshelfWidth = 5;
    ArrayList<Integer> score = new ArrayList<>();

    public ComGoal(int playerNum) {
        if (playerNum == 2) {
            score.add(0, 8);
            score.add(1, 4);
        } else if (playerNum == 3) {
            score.add(0, 8);
            score.add(1, 6);
            score.add(2, 4);
        } else if (playerNum == 4) {
            score.add(0, 8);
            score.add(1, 6);
            score.add(2, 4);
            score.add(3, 2);
        }
    }

    /**
     * checks if the player reached the common goals
     *
     * @return score assigned to the player
     * the score is 0 if the goal is not reached, otherwise it is the max score available for the goal
     */
    public abstract int goalReached(Bookshelf l);

    public int getCurrScore(){
        try{
            return this.score.get(0);
        } catch(NullPointerException | IndexOutOfBoundsException e){
            return 0;
        }

    }

}
