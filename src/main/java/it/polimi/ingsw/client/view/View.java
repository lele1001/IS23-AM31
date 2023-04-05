package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;

public interface View {
    void printBoard(ItemCard[][] board);

    void printBookshelves(Map<String, ItemCard[][]> bookshelves);

    void printError(String error);

    void printCommonGoal(Map<Integer, Integer> playerComGoal);

    void printPoints(int myPoint);

    void printPersGoal(Map<Integer, HouseItem> myPersGoal);

    void print(String yourTurn);

    void printMyBookshelf(ItemCard[][] book);
}