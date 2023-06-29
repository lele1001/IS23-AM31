package it.polimi.ingsw.server.model.comGoals;

import it.polimi.ingsw.server.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the {@link ComGoal} methods
 */
public class ComGoalTest {

    @Test
    public void getCurrScore() {
        ComGoal comGoal = new CG1(2, 1);
        assertEquals(8, comGoal.getCurrScore());
        comGoal.score.remove(0);
        assertEquals(4, comGoal.getCurrScore());
        comGoal.score.remove(0);
        assertEquals(0, comGoal.getCurrScore());
    }
}