package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.connection.ConnectionClient;
import it.polimi.ingsw.client.connection.ConnectionRMI;
import it.polimi.ingsw.client.connection.ConnectionSocket;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;
import java.util.Scanner;

public class Cli implements View {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    private Scanner in = new Scanner(System.in);
    ClientController clientController;
    ConnectionClient connectionClient;
    String username;
    String address;
    int port;
    int select = -1;

    public Cli(ClientController clientController) {


        this.clientController = clientController;
        askUsername();
        askConnection();
        System.out.println("Write 0 for RMI, 1 for Socket");
        do {
            if (in.hasNextInt()) {
                select = in.nextInt();
            }
        } while (select != 0 && select != 1);
        askIP();
        askPort();

        System.out.println("Select Ip Address");
        if (in.hasNextLine()) {
            address = in.nextLine();
        }
        System.out.println("Select Ip Port");
        if (in.hasNextInt()) {
            port = in.nextInt();
        }


        if (select == 0) {
            connectionClient = new ConnectionRMI(clientController);
        } else {
            connectionClient = new ConnectionSocket(clientController);
        }
    }

    private void askConnection() {

    }

    private void askPort() {
    }

    private void askIP() {

    }

    void askUsername() {


    }

    @Override
    public void printBoard(ItemCard[][] board) {
        System.out.println("    0   1   2   3   4   5   6   7   8");

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (j == 0) {
                    System.out.print((char) 27 + "[39m" + i + " | ");
                }

                printLine(board, i, j);

                if (j < DIM_BOARD - 1) {
                    System.out.print((char) 27 + "[39m" + " | ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void printBookshelves(Map<String, ItemCard[][]> bookshelves) {
        if (!bookshelves.isEmpty()) {
            for (String s : bookshelves.keySet()) {
                printMyBookshelf(bookshelves.get(s));
                System.out.println(s);
            }
        }
    }

    @Override
    public void printMyBookshelf(ItemCard[][] bookshelf) {
        System.out.println("    0   1   2   3   4   5");

        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                printLine(bookshelf, i, j);

                if (j < BOOKSHELF_LENGTH - 1) {
                    System.out.print((char) 27 + "[39m" + " | ");
                }
            }

            System.out.println();
        }
    }

    private void printLine(ItemCard[][] matrix, int i, int j) {
        if (matrix[i][j] != null) {
            char itemChar = matrix[i][j].getMyItem().toString().charAt(0);

            System.out.print((char) 27 + chooseColorCode(itemChar) + itemChar);
        } else {
            System.out.print(" ");
        }
    }

    private String chooseColorCode(char itemChar) {
        if (itemChar == 'C') {
            return "[32m";
        } else if (itemChar == 'F') {
            return "[34m";
        } else if (itemChar == 'G') {
            return "[33m";
        } else if (itemChar == 'B') {
            return "[37m";
        } else if (itemChar == 'P') {
            return "[35m";
        } else if (itemChar == 'T') {
            return "[36m";
        } else {
            return "[39m";
        }
    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {
        if (!playerComGoal.isEmpty()) {
            for (Integer i : playerComGoal.keySet()) {
                playerComGoal.get(i);
            }
        }
    }

    @Override
    public void printPoints(int myPoint) {

    }

    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal) {

    }

    @Override
    public void print(String yourTurn) {

    }
}
