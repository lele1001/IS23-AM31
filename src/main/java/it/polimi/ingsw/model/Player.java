package it.polimi.ingsw.model;

import it.polimi.ingsw.ModelExceptions.NoBookshelfSpaceException;

import java.util.List;

public class Player {
    private final String nickname;
    private final Bookshelf myBookshelf;
    private int score;
    private PersGoal persGoal;



    public Player(String nickname) {
        this.nickname = nickname;
        myBookshelf = new Bookshelf();
    }

    public void assignPersGoal(PersGoal persGoal) {
        this.persGoal = persGoal;
    }

    public int calculateFinScore() {
        return score+ myBookshelf.calcScore()+ persGoal.calcScore(myBookshelf);
    }



    public void insertCard(List<ItemCard> cards, int column) throws NoBookshelfSpaceException {
        if(myBookshelf.checkSpace(column, cards.size()))
            throw new NoBookshelfSpaceException();
        myBookshelf.insertCard(cards, column);
        // Then prints the library to client interface
    }
}
