package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;
import java.util.Scanner;
import java.util.TimerTask;

public class Cli implements View {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    private static boolean stopListening;
    Scanner in = new Scanner(System.in);
    InputController checkInput;
    ClientController clientController;
    String username;
    String address;
    boolean gameStarted = false;
    int port = -1;
    int select = -1;

    public Cli() {

    }

    /**
     * Initialization of the client profile asking: username,type of connection, ip and port
     *
     * @param clientController define the direct contact with all the object containers send from the server
     */
    public Cli(ClientController clientController) {
        this.clientController = clientController;
        clientController.setView(this);
        checkInput = new InputController(clientController);

        System.out.println("Adding pippo to the game");
        //clientController.onBookshelfChanged("pippo", null);

        askUsername();
        askConnection();
        askIP();
        askPort();

        /*try {
            clientController.startconnection(select, username, address, port);
        } catch (Exception e) {
            printError(e.getMessage());
            disconnectionError();
        }*/

        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }

                gameStarted = true;
            }
        };

        Thread menuThread = new Thread() {
            @Override
            public void run() {
                t.run();
            }
        };

        menuThread.start();
        waitForGame();
    }

    /**
     * Asking the type of connection: 0 for RMI, 1 for Socket
     */
    private void askConnection() {
        do {
            System.out.println("Write 0 for RMI, 1 for Socket");
            if (in.hasNextInt()) {
                select = in.nextInt();
            } else if (in.hasNextLine()) {
                in.nextLine();
                System.out.println("Input error");
            }
        } while (select != 0 && select != 1);
    }

    /**
     * Ask the port of the server, running controls on the inserted integer
     */
    private void askPort() {
        do {
            System.out.println("Select Ip Port");
            if (in.hasNextInt()) {
                port = in.nextInt();
            } else if (in.hasNextLine()) {
                in.nextLine();
                System.out.println("Input error");
            }
        } while (port == -1);

        in.nextLine();
        System.out.println("The server IP port chosen is: " + port);
    }

    /**
     * Ask the IP address of the server, running controls on the inserted String
     */
    private void askIP() {
        in.nextLine();
        do {
            System.out.println("Select Ip Address");
            if (in.hasNextLine()) {
                address = in.nextLine();
            }
        } while (address.equals(""));
        System.out.println("The server IP address chosen is: " + address);
    }

    /**
     * Ask the Username of the client
     */
    void askUsername() {
        do {
            System.out.println("Select Username");
            if (in.hasNextLine()) {
                username = in.nextLine();
            }
        } while (username.equals(""));
    }

    /**
     * Reads the choice of the user
     */
    public void listen() {
        String choice;
        String[] splitString;
        StringBuilder msg = new StringBuilder();
        String destNickname;

        printMenu();

        while (!stopListening) {
            if (in.hasNextLine()) {
                choice = in.nextLine();
                splitString = choice.split(" ");

                for (int i = 0; i < splitString.length; i++) {
                    splitString[i] = splitString[i].toLowerCase();
                }

                switch (splitString[0]) {
                    case "@menu" -> printMenu();
                    case "@board" -> printBoard(clientController.board);
                    case "@take" -> {
                        if (!checkInput.checkTake(splitString)) {
                            System.out.println("Errore take");
                        }
                        //chiamo il metodo selectCards

                    }
                    case "@myshelf" -> printMyBookshelf(clientController.playersBookshelf.get(clientController.getMyNickname()));
                    case "@allshelves" -> printBookshelves(clientController.playersBookshelf);
                    case "@put" -> {
                        if (!checkInput.checkPut(splitString)) {
                            System.out.println("Errore put");
                        }
                        //chiamo il metodo putCards

                    }
                    case "@chat" -> {
                        int dest = checkInput.checkChat(splitString);

                        if (dest == 1) {
                            destNickname = splitString[1];

                            for (int i = 2; i < splitString.length; i++) {
                                msg.append(splitString[i]).append(" ");
                            }

                            // metodo chat diretta
                        } else if (dest == 2) {
                            destNickname = "all";

                            for (int i = 2; i < splitString.length; i++) {
                                msg.append(splitString[i]).append(" ");
                            }

                            // metodo chat globale
                        } else {
                            System.out.println("Errore chat");
                            break;
                        }

                        System.out.println("Sending " + msg + "to " + destNickname);
                    }
                    case "@quit" -> {
                        System.out.println("Stopping CLI...");
                        stopListening = true;
                    }
                    default -> System.out.println("Input not recognised... try again");
                }
            }
        }

        disconnectionError();
    }

    /**
     * Allows the user only to quit the game while waiting for it to start
     */
    public void waitForGame() {
        String choice;
        String[] splitString;

        System.out.println("""
                \nWaiting for other players to connect...
                GAME MENU: type the corresponding command
                \t@MENU to show again this menu
                \t@QUIT to exit from the game""");

        while (!gameStarted && !stopListening) {
            if (in.hasNextLine()) {
                System.out.println("the timer is " + gameStarted);
                choice = in.nextLine();
                splitString = choice.split(" ");

                for (int i = 0; i < splitString.length; i++) {
                    splitString[i] = splitString[i].toLowerCase();
                }

                if (splitString[0].equals("@quit")) {
                    System.out.println("Stopping CLI...");
                    stopListening = true;
                } else if (splitString[0].equals("@menu")) {
                    System.out.println("""
                            GAME MENU: type the corresponding command
                            \t@MENU to show again this menu
                            \t@QUIT to exit from the game""");
                } else {
                    System.out.println("Input not recognised... try again");
                }
            }
        }

        if (stopListening) {
            disconnectionError();
        } else {
            gameStarted = true;
            listen();
        }
    }

    /**
     * Prints a menu on the screen to let the user choose what to do next
     */
    public void printMenu() {
        System.out.println("""
                GAME MENU: type the corresponding command
                \t@MENU to show again this menu
                \t@BOARD to print the game board
                \t@TAKE to choose from 1 to 3 tiles from the board, followed by the coordinates (xy) of the chosen tiles
                \t@MYSHELF to print you bookshelf
                \t@ALLSHELVES to print the bookshelf of all the players
                \t@PUT to choose a column for putting the cards, followed by the column number
                \t@CHAT to open the chat, followed by the nickname/all and the message
                \t@QUIT to exit from the game""");
    }

    /**
     * Prints the current board
     */
    @Override
    public void printBoard(ItemCard[][] board) {
        System.out.println("    0   1   2   3   4   5   6   7   8");
        printMatrix(board, DIM_BOARD, DIM_BOARD);
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
        if (bookshelf != null) {
            System.out.println("    0   1   2   3   4");
            printMatrix(bookshelf, BOOKSHELF_HEIGHT, BOOKSHELF_LENGTH);
        }
        else {
            System.out.println("    0   1   2   3   4");
            printMatrix(new ItemCard[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH], BOOKSHELF_HEIGHT, BOOKSHELF_LENGTH);
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
            System.out.print((char) 27 + "[39m" + " | ");
        } else {
            System.out.print(" ");
        }
    }

    /**
     * Private method that prints a cell from an ItemCard matrix (bookshelf or board)
     *
     * @param matrix is the representation of the bookshelf, or the board
     * @param height is the number of rows
     * @param length is the number of columns
     */
    private void printMatrix(ItemCard[][] matrix, int height, int length) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                if (j == 0) {
                    System.out.print((char) 27 + "[39m" + i + " | ");
                }

                printCell(matrix, i, j);
            }

            System.out.println();
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
                    System.out.println("""
                            Five columns of increasing or decreasing height.\s
                            Starting from the first column on the left or on the right, each next column must be made of exactly one more tile.\s
                            Tiles can be of any type.""");
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
        for (Integer i : myPersGoal.keySet()) {
            System.out.println(i + " " + myPersGoal.get(i));
        }
        System.out.println("    0   1   2   3   4");
        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                int k = i * 10 + j;
                if (j == 0) {
                    System.out.print((char) 27 + "[39m" + i + " | ");
                }
                if (myPersGoal.containsKey(k)) {
                    char itemChar = myPersGoal.get(k).toString().charAt(0);

                    System.out.print((char) 27 + chooseColorCode(itemChar) + itemChar);
                } else {
                    System.out.print(" ");
                }
                if (j < BOOKSHELF_LENGTH - 1) {
                    System.out.print((char) 27 + "[39m" + " | ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Tells the player if it is his turn
     */
    @Override
    public void print(String yourTurn) {
        System.out.println(yourTurn);
    }

    /**
     * Disconnection of the Cli after the client is disconnected from the server
     */
    private void disconnectionError() {
        System.out.println("\nPress ENTER to exit");
        in.nextLine();
        System.exit(1);
    }
}
