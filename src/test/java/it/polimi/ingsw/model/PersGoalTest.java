package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.HouseItem.*;
import static it.polimi.ingsw.model.ItemNumber.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersGoalTest {
    @Test
    public void calcScoreCard1() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 2);
        assertEquals(PersGoal.Card1.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Games, Third))), 1);
        assertEquals(PersGoal.Card1.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Games, First), new ItemCard(Frame, First), new ItemCard(Frame, First), new ItemCard(Plants, First))), 0);
        assertEquals(PersGoal.Card1.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Plants, Third), new ItemCard(Trophy, First), new ItemCard(Games, Second))), 2);
        assertEquals(PersGoal.Card1.calcScore(prova), 4);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second))), 2);
        assertEquals(PersGoal.Card1.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Plants, First))), 3);
        assertEquals(PersGoal.Card1.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Books, First))), 3);
        assertEquals(PersGoal.Card1.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Plants, First), new ItemCard(Trophy, Third))), 4);
        assertEquals(PersGoal.Card1.calcScore(prova), 9);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Cat, First))), 4);
        assertEquals(PersGoal.Card1.calcScore(prova), 12);
    }

    @Test
    public void calcScoreCard2() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card2.calcScore(prova), 0);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Cat, Second))), 0);
        assertEquals(PersGoal.Card2.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Games, Third))), 1);
        assertEquals(PersGoal.Card2.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Plants, First), new ItemCard(Games, Third))), 1);
        assertEquals(PersGoal.Card2.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second))), 4);
        assertEquals(PersGoal.Card2.calcScore(prova), 4);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Books, First))), 4);
        assertEquals(PersGoal.Card2.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Games, First), new ItemCard(Games, First), new ItemCard(Frame, First), new ItemCard(Plants, First))), 2);
        assertEquals(PersGoal.Card2.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, First))), 3);
        assertEquals(PersGoal.Card2.calcScore(prova), 12);
    }

    @Test
    public void calcScoreCard3() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second))), 0);
        assertEquals(PersGoal.Card3.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Cat, Second), new ItemCard(Cat, Third))), 0);
        assertEquals(PersGoal.Card3.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Cat, Third))), 1);
        assertEquals(PersGoal.Card3.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Plants, First), new ItemCard(Games, Third))), 1);
        assertEquals(PersGoal.Card3.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Games, First), new ItemCard(Plants, First), new ItemCard(Frame, First), new ItemCard(Plants, First))), 2);
        assertEquals(PersGoal.Card3.calcScore(prova), 4);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, First))), 3);
        assertEquals(PersGoal.Card3.calcScore(prova), 4);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, First), new ItemCard(Games, Second))), 3);
        assertEquals(PersGoal.Card3.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second))), 4);
        assertEquals(PersGoal.Card3.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Trophy, First))), 4);
        assertEquals(PersGoal.Card3.calcScore(prova), 9);
    }

    @Test
    public void calcScoreCard4() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Books, First), new ItemCard(Cat, Second), new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card4.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card4.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Books, First), new ItemCard(Cat, Third))), 1);
        assertEquals(PersGoal.Card4.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Games, Third))), 1);
        assertEquals(PersGoal.Card4.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Games, First), new ItemCard(Frame, First), new ItemCard(Plants, First))), 2);
        assertEquals(PersGoal.Card4.calcScore(prova), 4);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, First))), 3);
        assertEquals(PersGoal.Card4.calcScore(prova), 4);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second))), 3);
        assertEquals(PersGoal.Card4.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, Second), new ItemCard(Trophy, First))), 4);
        assertEquals(PersGoal.Card4.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Trophy, First), new ItemCard(Games, Third))), 4);
        assertEquals(PersGoal.Card4.calcScore(prova), 9);
    }

    @Test
    public void calcScoreCard5() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Cat, Second), new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card5.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card5.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Books, First), new ItemCard(Frame, Third))), 1);
        assertEquals(PersGoal.Card5.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, Third))), 1);
        assertEquals(PersGoal.Card5.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Books, First), new ItemCard(Frame, First), new ItemCard(Plants, First))), 2);
        assertEquals(PersGoal.Card5.calcScore(prova), 6);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Trophy, First))), 3);
        assertEquals(PersGoal.Card5.calcScore(prova), 9);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second))), 3);
        assertEquals(PersGoal.Card5.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Plants, Second))), 4);
        assertEquals(PersGoal.Card5.calcScore(prova), 12);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Games, Third))), 4);
        assertEquals(PersGoal.Card5.calcScore(prova), 12);
    }

    @Test
    public void calcScoreCard6() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second))), 0);
        assertEquals(PersGoal.Card6.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card6.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 1);
        assertEquals(PersGoal.Card6.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, Third))), 1);
        assertEquals(PersGoal.Card6.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Frame, First), new ItemCard(Books, First), new ItemCard(Frame, First), new ItemCard(Plants, First))), 2);
        assertEquals(PersGoal.Card6.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 2);
        assertEquals(PersGoal.Card6.calcScore(prova), 4);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Frame, First))), 3);
        assertEquals(PersGoal.Card6.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second), new ItemCard(Books, Third))), 3);
        assertEquals(PersGoal.Card6.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, Second))), 4);
        assertEquals(PersGoal.Card6.calcScore(prova), 9);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Frame, Second), new ItemCard(Games, Third), new ItemCard(Cat, First))), 4);
        assertEquals(PersGoal.Card6.calcScore(prova), 12);
    }

    @Test
    public void calcScoreCard7() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second), new ItemCard(Plants, Third), new ItemCard(Trophy, First))), 0);
        assertEquals(PersGoal.Card7.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 0);
        assertEquals(PersGoal.Card7.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second), new ItemCard(Cat, Third))), 0);
        assertEquals(PersGoal.Card7.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1);
        assertEquals(PersGoal.Card7.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, Third), new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card7.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Frame, First), new ItemCard(Plants, First))), 2);
        assertEquals(PersGoal.Card7.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 2);
        assertEquals(PersGoal.Card7.calcScore(prova), 6);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Frame, First))), 3);
        assertEquals(PersGoal.Card7.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second), new ItemCard(Books, Third), new ItemCard(Frame, Second))), 3);
        assertEquals(PersGoal.Card7.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Games, Second))), 4);
        assertEquals(PersGoal.Card7.calcScore(prova), 12);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Cat, First))), 4);
        assertEquals(PersGoal.Card7.calcScore(prova), 12);
    }

    @Test
    public void calcScoreCard8() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second), new ItemCard(Plants, Third), new ItemCard(Trophy, First))), 0);
        assertEquals(PersGoal.Card8.calcScore(prova), 0);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second))), 0);
        assertEquals(PersGoal.Card8.calcScore(prova), 0);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1);
        assertEquals(PersGoal.Card8.calcScore(prova), 0);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, Third), new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card8.calcScore(prova), 0);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second))), 1);
        assertEquals(PersGoal.Card8.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, First), new ItemCard(Books, First))), 2);
        assertEquals(PersGoal.Card8.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second))), 2);
        assertEquals(PersGoal.Card8.calcScore(prova), 2);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Books, First))), 3);
        assertEquals(PersGoal.Card8.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Games, Second))), 4);
        assertEquals(PersGoal.Card8.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Cat, First), new ItemCard(Books, Second), new ItemCard(Cat, Third))), 4);
        assertEquals(PersGoal.Card8.calcScore(prova), 6);
    }

    @Test
    public void calcScoreCard9() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Plants, Third), new ItemCard(Trophy, First))), 0);
        assertEquals(PersGoal.Card9.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1);
        assertEquals(PersGoal.Card9.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, Second), new ItemCard(Trophy, Third), new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card9.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, First), new ItemCard(Books, First))), 2);
        assertEquals(PersGoal.Card9.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second))), 2);
        assertEquals(PersGoal.Card9.calcScore(prova), 4);


        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, First))), 3);
        assertEquals(PersGoal.Card9.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Plants, Second))), 4);
        assertEquals(PersGoal.Card9.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Cat, Second), new ItemCard(Cat, Third))), 4);
        assertEquals(PersGoal.Card9.calcScore(prova), 9);
    }

    @Test
    public void calcScoreCard10() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Plants, Third), new ItemCard(Trophy, First))), 0);
        assertEquals(PersGoal.Card10.calcScore(prova), 0);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second))), 0);
        assertEquals(PersGoal.Card10.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1);
        assertEquals(PersGoal.Card10.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Trophy, Third), new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card10.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Trophy, Third))), 1);
        assertEquals(PersGoal.Card10.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, First), new ItemCard(Books, First))), 2);
        assertEquals(PersGoal.Card10.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First))), 3);
        assertEquals(PersGoal.Card10.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First), new ItemCard(Cat, Second))), 3);
        assertEquals(PersGoal.Card10.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Plants, Second))), 4);
        assertEquals(PersGoal.Card10.calcScore(prova), 9);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second), new ItemCard(Cat, Second), new ItemCard(Cat, Third), new ItemCard(Trophy, First))), 4);
        assertEquals(PersGoal.Card10.calcScore(prova), 12);
    }

    @Test
    public void calcScoreCard11() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Plants, Third), new ItemCard(Trophy, First))), 0);
        assertEquals(PersGoal.Card11.calcScore(prova), 0);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second), new ItemCard(Books, First))), 0);
        assertEquals(PersGoal.Card11.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 1);
        assertEquals(PersGoal.Card11.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Trophy, Third), new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card11.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Books, Second))), 1);
        assertEquals(PersGoal.Card11.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, First), new ItemCard(Frame, First))), 2);
        assertEquals(PersGoal.Card11.calcScore(prova), 4);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First))), 3);
        assertEquals(PersGoal.Card11.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First), new ItemCard(Cat, Second))), 3);
        assertEquals(PersGoal.Card11.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Cat, Second))), 4);
        assertEquals(PersGoal.Card11.calcScore(prova), 9);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Third), new ItemCard(Trophy, First))), 4);
        assertEquals(PersGoal.Card11.calcScore(prova), 9);
    }

    @Test
    public void calcScoreCard12() {
        Bookshelf prova = new Bookshelf();
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Trophy, First))), 0);
        assertEquals(PersGoal.Card12.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Games, Second))), 0);
        assertEquals(PersGoal.Card12.calcScore(prova), 1);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second))), 1);
        assertEquals(PersGoal.Card12.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Trophy, Third), new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card12.calcScore(prova), 1);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, Second))), 1);
        assertEquals(PersGoal.Card12.calcScore(prova), 2);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Frame, First), new ItemCard(Frame, First))), 2);
        assertEquals(PersGoal.Card12.calcScore(prova), 2);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Frame, Second), new ItemCard(Trophy, Third), new ItemCard(Books, Second))), 2);
        assertEquals(PersGoal.Card12.calcScore(prova), 6);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First))), 3);
        assertEquals(PersGoal.Card12.calcScore(prova), 6);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Plants, First), new ItemCard(Trophy, Second))), 3);
        assertEquals(PersGoal.Card12.calcScore(prova), 9);

        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, Second), new ItemCard(Games, Second))), 4);
        assertEquals(PersGoal.Card12.calcScore(prova), 12);
        prova.insertCard(new ArrayList<>(List.of(new ItemCard(Trophy, First))), 4);
        assertEquals(PersGoal.Card12.calcScore(prova), 12);
    }

}