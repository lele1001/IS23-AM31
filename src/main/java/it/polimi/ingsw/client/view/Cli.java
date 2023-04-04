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
        String colorCode = null;
        char itemChar;

        System.out.println("    0   1   2   3   4   5   6   7   8");

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (j == 0) {
                    System.out.print(i + " | ");
                }

                if (board[i][j] != null) {
                    itemChar = board[i][j].getMyItem().toString().charAt(0);

                    if (itemChar == 'C') {
                        colorCode = "[32m";
                    } else if (itemChar == 'F') {
                        colorCode = "[34m";
                    } else if (itemChar == 'G') {
                        colorCode = "[33m";
                    } else if (itemChar == 'B') {
                        colorCode = "[37m";
                    } else if (itemChar == 'P') {
                        colorCode = "[35m";
                    } else if (itemChar == 'T') {
                        colorCode = "[36m";
                    } else {
                        colorCode = "[39m";
                    }

                    System.out.print((char) 27 + colorCode + itemChar);
                } else {
                    System.out.print(" ");
                }

                if (j < DIM_BOARD - 1) {
                    System.out.print((char) 27 + "[39m" + " | ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void printBookshelves(Map<String, ItemCard[][]> bookshelves) {

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
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal) {

    }

    @Override
    public void print(String yourTurn) {

    }

    @Override
    public void printMyBookshelf(ItemCard[][] book) {

    }
}
