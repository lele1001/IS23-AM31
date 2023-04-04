package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;


public interface View {
    void printBoard(ItemCard[][] board);

    void printBookshelf(Map<String, ItemCard[][]> bookshelfs);

    void printError(String error);

    void printCommonGoal(Map<Integer, Integer> playerComGoal);

    void printPoints(int myPoint);

    void printPersGoal(String myPersGoal);

    void print(String yourTurn);

    void printMyBookshelf(ItemCard[][] book);
}