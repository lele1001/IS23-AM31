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
    private final ArrayList<ComGoal> comGoalsReached;
    private final static int BOOKSHELF_LENGTH = 5;

    /**
     * The constructor of the class initializes the player's bookshelf and common goals reached.
     *
     * @param nickname of the player to be created.
     */
    public Player(String nickname) {
        this.nickname = nickname;
        myBookshelf = new Bookshelf();
        comGoalsReached = new ArrayList<>();
    }

    /**
     * Called at the beginning of the game, assigns a personal goal to the player.
     *
     * @param persGoal assigned to the player.
     */
    public void assignPersGoal(PersGoal persGoal) {
        this.persGoal = persGoal;
    }

    /**
     * Called at the end of the game
     *
     * @return the total score of the player (sum of ComGoal score (saved in variable 'score'), bookshelf's adjacency score, persGoal score).
     */
    public int calculateFinScore() {
        System.out.println(nickname + " has " + score + "points from CommonGoal");
        System.out.println(nickname + " has " + myBookshelf.calcScore() + "points from bookshelf");
        System.out.println(nickname + " has " + persGoal.calcScore(myBookshelf) + "points from Persgoal");
        return score + myBookshelf.calcScore() + persGoal.calcScore(myBookshelf);
    }

    /**
     * Called by the GameModel at the end of the turn of the player to check if it has reached ComGoals.
     *
     * @param comGoal available for the game
     * @return true if the player has reached the goal, false in other cases (goal not reached or already reached in other turns)
     */
    public boolean checkComGoal(ComGoal comGoal) {
        for (ComGoal cg : comGoalsReached) {
            if (cg == comGoal)
                return false;
        }
        int comGoalScore = comGoal.goalReached(myBookshelf);
        if (comGoalScore > 0) {
            score += comGoalScore;
            comGoalsReached.add(comGoal);
            return true;
        }
        return false;
    }

    /**
     * @return true if the bookshelf is full (the game has to run the last turn)
     */
    public boolean checkEnd() {
        for (int i = 0; i < 5; i++) {
            if (myBookshelf.checkSpace(i, 1))
                return false;
        }
        return true;
    }

    /**
     * Called by the GameModel, tries to insert cards in the player's bookshelf
     *
     * @param cards  to be inserted
     * @param column of the bookshelf where cards have to be inserted
     * @throws NoBookshelfSpaceException if there's no such space in the column of the bookshelf for the cards
     * @requires cards.size() <= 3
     */
    public boolean insertCard(List<ItemCard> cards, int column) throws NoBookshelfSpaceException {
        if (!myBookshelf.checkSpace(column, cards.size()))
            throw new NoBookshelfSpaceException();
        myBookshelf.insertCard(cards, column);
        for (int i = 0; i < BOOKSHELF_LENGTH; i++) {
            if (myBookshelf.checkSpace(i, 1))
                return false;
        }
        return true;
    }

    /**
     * Called by the GameModel, returns a representation of the player's bookshelf to be sent to all players.
     *
     * @return player's bookshelf as a matrix.
     */
    public ItemCard[][] getBookshelfAsMatrix() {
        return this.myBookshelf.getAsMatrix();
    }


    /**
     * Called by the GameModel when it needs to know player's score to send it to the player because he's just returned online.
     *
     * @return the actual score reached by the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Called by the GameModel when it needs to know player's personal goal to send it to the player because he's just returned online.
     *
     * @return a string that represents player's personal goal.
     */
    public String getPersGoal() {
        return this.persGoal.toString();
    }

}