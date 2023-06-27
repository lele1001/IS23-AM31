package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Bookshelf;

import java.util.ArrayList;

/**
 * Define the characteristics of the Common Goals
 */
public abstract class ComGoal {
    final int CGID;
    final ArrayList<Integer> score = new ArrayList<>();

    /**
     * Set the Common Goal based on the number of players
     *
     * @param playerNum THe number of players
     * @param CGID      the ID of th CommonGoal
     */
    public ComGoal(int playerNum, int CGID) {
        this.CGID = CGID;

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
     * Checks if the player reached the common goals
     *
     * @param l the bookshelf
     * @return score assigned to the player
     * the score is 0 if the goal is not reached, otherwise it is the max score available for the goal
     */
    public abstract int goalReached(Bookshelf l);

    /**
     * Getter methods for the score of the common goal
     *
     * @return the value of the Common Goal
     */
    public int getCurrScore() {
        try {
            return this.score.get(0);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return 0;
        }

    }

    /**
     * Getter methods to get the ID
     *
     * @return the Common Goal ID
     */
    public int getCGID() {
        return CGID;
    }
}
