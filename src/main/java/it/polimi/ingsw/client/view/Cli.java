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
    Scanner in = new Scanner(System.in);
    ClientController clientController;
    ConnectionClient connectionClient;
    String username;
    String address;
    int port;
    int select = -1;
    public Cli(){

    }

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

    /**
     * Prints the current board
     */
    @Override
    public void printBoard(ItemCard[][] board) {
        System.out.println("    0   1   2   3   4   5   6   7   8");

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (j == 0) {
                    System.out.print((char) 27 + "[39m" + i + " | ");
                }

                printCell(board, i, j);

                if (j < DIM_BOARD - 1) {
                    System.out.print((char) 27 + "[39m" + " | ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints all the bookshelves of the players in the current game
     *
     * @param bookshelves is a map with key the nickname of the player and value its bookshelf
     */
    @Override
    public void printBookshelves(Map<String, ItemCard[][]> bookshelves) {
        if (!bookshelves.isEmpty()) {
            for (String s : bookshelves.keySet()) {
                printMyBookshelf(bookshelves.get(s));
                System.out.println(s);
            }
        }
    }

    /**
     * Prints the bookshelf of a given player
     */
    @Override
    public void printMyBookshelf(ItemCard[][] bookshelf) {
        System.out.println("    0   1   2   3   4   5");

        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                printCell(bookshelf, i, j);

                if (j < BOOKSHELF_LENGTH - 1) {
                    System.out.print((char) 27 + "[39m" + " | ");
                }
            }

            System.out.println();
        }
    }

    /**
     * Private method that prints a cell from an ItemCard matrix (bookshelf or board)
     *
     * @param matrix is the representation of the bookshelf, or the board
     * @param i      is the row number
     * @param j      is the column number
     */
    private void printCell(ItemCard[][] matrix, int i, int j) {
        if (matrix[i][j] != null) {
            char itemChar = matrix[i][j].getMyItem().toString().charAt(0);

            System.out.print((char) 27 + chooseColorCode(itemChar) + itemChar);
        } else {
            System.out.print(" ");
        }
    }

    /**
     * Private method used for switching between colors based on the item card type
     *
     * @param itemChar is the first character of the item card type
     * @return the string associated with the color
     */
    private String chooseColorCode(char itemChar) {
        if (itemChar == 'C') {
            // Cats are green
            return "[32m";
        } else if (itemChar == 'F') {
            // Frames are blue
            return "[34m";
        } else if (itemChar == 'G') {
            // Games are yellow
            return "[33m";
        } else if (itemChar == 'B') {
            // Books are white
            return "[37m";
        } else if (itemChar == 'P') {
            // Plants are purple
            return "[35m";
        } else if (itemChar == 'T') {
            // Trophies are cyan
            return "[36m";
        } else {
            // Default color
            return "[39m";
        }
    }

    /**
     * Prints a generic error
     */
    @Override
    public void printError(String error) {
        System.out.println(error);
    }

    /**
     * Method used to switch between the 12 common goals and give their textual representation
     *
     * @param playerComGoal is a map with key the comGoal number and value the available score
     */
    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {
        if (!playerComGoal.isEmpty()) {
            for (Integer i : playerComGoal.keySet()) {
                if (i == 1) {
                    System.out.println("Two separate groups each containing four tiles of the same type in a 2x2 square.\n"
                            + "The tiles of one square can be different from those of the other square.");
                } else if (i == 2) {
                    System.out.println("Two columns each formed by 6 different types of tiles.\n"
                            + "One column can show the same or a different combination of the other column.");
                } else if (i == 3) {
                    System.out.println("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).\n"
                            + "The tiles of one group can be different from those of another group.");
                } else if (i == 4) {
                    System.out.println("Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape).\n"
                            + "The tiles of one group can be different from those of another group.");
                } else if (i == 5) {
                    System.out.println("Three columns each formed by 6 tiles of maximum three different types.\n"
                            + "One column can show the same or a different combination of another column.");
                } else if (i == 6) {
                    System.out.println("Two lines each formed by 5 different types of tiles.\n"
                            + "One line can show the same or a different combination of the other line.");
                } else if (i == 7) {
                    System.out.println("Four lines each formed by 5 tiles of maximum three different types.\n"
                            + "One line can show the same or a different combination of another line.");
                } else if (i == 8) {
                    System.out.println("Four tiles of the same type in the four corners of the bookshelf.");
                } else if (i == 9) {
                    System.out.println("Eight tiles of the same type.\n"
                            + "There is no restriction about the position of these tiles.");
                } else if (i == 10) {
                    System.out.println("Five tiles of the same type forming an X");
                } else if (i == 11) {
                    System.out.println("Five tiles of the same item forming a diagonal.");
                } else if (i == 12) {
                    System.out.println("Five columns of increasing or decreasing height. \n"
                            + "Starting from the first column on the left or on the right, each next column must be made of exactly one more tile. \n"
                            + "Tiles can be of any type.");
                }

                System.out.println("The maximum available score for this card is " + playerComGoal.get(i) + ".");
            }
        }
    }

    /**
     * Prints the points earned by the player
     */
    @Override
    public void printPoints(int myPoint) {
        System.out.println("You currently have " + myPoint + "points.");
    }

    /**
     * Prints the personalGoal associated to the player
     *
     * @param myPersGoal is a map with key the position and value the houseItem in that position
     */
    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal) {
        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                int k = i * 10 + j;

                if (myPersGoal.containsKey(k)) {
                    char itemChar = myPersGoal.get(k).toString().charAt(0);

                    System.out.print((char) 27 + chooseColorCode(itemChar) + itemChar);
                } else {
                    System.out.print(" ");
                }
            }
        }
    }

    @Override
    public void print(String yourTurn) {

    }
}
