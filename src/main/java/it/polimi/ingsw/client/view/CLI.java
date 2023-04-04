package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;
import java.util.Scanner;

public class Cli implements View {
    private Scanner in = new Scanner(System.in);
    int select;

    public Cli() {
        System.out.println("Write 0 for RMI, 1 for Socket");
        do {
            if (in.hasNextInt()) {
                select = in.nextInt();
            }
        }
        while (select != 0 && select != 1);
        if (select == 0) {

        } else {

        }
    }

    @Override
    public void printBoard(ItemCard[][] board) {

    }

    @Override
    public void printBookshelf(Map<String, ItemCard[][]> bookshelfs) {

    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {

    }

    @Override
    public void printPoints(int myPoint) {

    }

    @Override
    public void printPersGoal(String myPersGoal) {

    }

    @Override
    public void print(String yourTurn) {

    }

    @Override
    public void printMyBookshelf(ItemCard[][] book) {

    }
}
