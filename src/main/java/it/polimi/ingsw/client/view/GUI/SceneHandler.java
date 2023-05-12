package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;

public interface SceneHandler {
    void initialize(ClientController clientController);

    void printError(String error);

    /* TODO:
    void updateBoard(ItemCard[][] board);

    void updateBookshelves(Map<String, ItemCard[][]> bookshelves);

    void updateCommonGoal(Map<Integer, Integer> playerComGoal);

    void updatePoints(int points);

    ...

     */
}
