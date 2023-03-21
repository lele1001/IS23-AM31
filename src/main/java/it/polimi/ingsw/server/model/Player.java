package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.model.comGoals.ComGoal;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String nickname;
    private final Bookshelf myBookshelf;
    private int score;
    private PersGoal persGoal;
    private boolean isOnline;
    private final ArrayList<ComGoal> comGoalsReached;

    public Player(String nickname) {
        this.nickname = nickname;
        myBookshelf = new Bookshelf();
        isOnline = true;
        comGoalsReached = new ArrayList<>();
    }

    /**
     * Called at the beginning of the game
     * @param persGoal assigned to the player.
     */
    public void assignPersGoal(PersGoal persGoal) {
        this.persGoal = persGoal;
    }

    /**
     * Called at the end of the game
     * @return the total score of the player (as the sum of ComGoal score (saved in variable 'score'), bookshelf's adjacencies score, persGoal score).
     */
    public int calculateFinScore() {
        return score + myBookshelf.calcScore() + persGoal.calcScore(myBookshelf);
    }

    /**
     * Called by the GameModel at the end of the turn of the player to check if it has reached ComGoals.
     * @param comGoal available for the game
     * @return true if the player has reached the goal, false in other cases (goal not reached or already reached in other turns)
     */
    public boolean checkComGoal(ComGoal comGoal) {
        for(ComGoal cg : comGoalsReached) {
            if(cg == comGoal)
                return false;
        }
        int comGoalScore = comGoal.goalReached(myBookshelf);
        if(comGoalScore > 0) {
            score += comGoalScore;
            comGoalsReached.add(comGoal);
            return true;
        }
        return false;
    }

    /**
     * @return true if the bookshelf is full (so the game has to run the last turn)
     */
    public boolean checkEnd() {
        for(int i=0; i<5; i++) {
            if(myBookshelf.checkSpace(i, 1))
                return false;
        }
        return true;
    }

    /**
     * Called by the GameModel, tries to insert cards in the player's bookshelf
     * @requires cards.size() <= 3
     * @param cards to be inserted
     * @param column of the bookshelf where cards have to be inserted
     * @throws NoBookshelfSpaceException if there's no such space in the column of the bookshelf for the cards
     */
    public void insertCard(List<ItemCard> cards, int column) throws NoBookshelfSpaceException {
        if(!myBookshelf.checkSpace(column, cards.size()))
            throw new NoBookshelfSpaceException();
        myBookshelf.insertCard(cards, column);
    }

    public void changePlayerStatus() {
        isOnline = !isOnline;
    }

    public boolean isOnline() {
        return isOnline;
    }
}