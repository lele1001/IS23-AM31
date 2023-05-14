package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;

public interface GUIScene {
    void initialize(ClientController clientController);

    void printError(String error);

    void bindEvents();

    /* TODO:
    void updateBoard(ItemCard[][] board);

    void updateBookshelves(Map<String, ItemCard[][]> bookshelves);

    void updateCommonGoal(Map<Integer, Integer> playerComGoal);

    void updatePoints(int points);

    ...

     */
}
