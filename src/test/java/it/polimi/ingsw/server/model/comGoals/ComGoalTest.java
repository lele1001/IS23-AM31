package it.polimi.ingsw.server.model.comGoals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComGoalTest {

    @Test
    void getCurrScore() {
        ComGoal comGoal = new CG1(2);
        assertEquals(8,comGoal.getCurrScore());
        comGoal.score.remove(0);
        assertEquals(4,comGoal.getCurrScore());
        comGoal.score.remove(0);
        assertEquals(0,comGoal.getCurrScore());
    }
}