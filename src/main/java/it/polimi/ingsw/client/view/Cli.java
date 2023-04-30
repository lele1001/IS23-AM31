package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.server.controller.TurnPhase;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

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
    int port = -1;
    int select = -1;

    public Cli() {

    }

    /**
     * Initialization of the client profile asking: username, type of connection, ip and port
     *
     * @param clientController define the direct contact with all the object containers send from the server
     */

    public Cli(ClientController clientController) {
        this.clientController = clientController;
        clientController.setView(this);
        checkInput = new InputController(clientController);

        askUsername();
        askConnection();
        askIP();
        askPort();

        try {
            clientController.startConnection(select, username, address, port);
        } catch (Exception e) {
            printError(e.getMessage());
            disconnectionError();
        }

        new Thread(this::listen).start();
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
    private void listen() {
        String choice;
        String[] splitString;

        if (!clientController.isGameStarted()) {
            synchronized (this) {
                System.out.println("Waiting for other players to connect...");
                waitForGameMenu();
            }
        }

        while (!stopListening) {
            if (in.hasNextLine()) {
                choice = in.nextLine();
                splitString = choice.split(" ");

                // transforms only the command given by the user to its lower case version
                splitString[0] = splitString[0].toLowerCase();

                switch (splitString[0]) {
                    case "@players" -> {
                        if (clientController.isSelectNumberOfPlayers()) {
                            int players = checkInput.checkPlayers(splitString);

                            if (players != -1) {
                                try {
                                    clientController.setPlayersNumber(players);
                                } catch (Exception e) {
                                    System.out.println("Impossible to connect to the server");
                                }
                            }
                        } else {
                            System.out.println("You can not choose the number of players!");
                        }
                    }
                    case "@menu" -> {
                        if (clientController.isGameStarted()) {
                            printMenu();
                        } else {
                            waitForGameMenu();
                        }
                    }
                    case "@comgoal" -> printCommonGoal(clientController.getPlayerComGoal());
                    case "@persgoal" -> printPersGoal(clientController.getMyPersGoal());
                    case "@board" -> {
                        if (clientController.isGameStarted()) {
                            printBoard(clientController.getBoard());
                        }
                    }
                    case "@take" -> {
                        if (clientController.isMyTurn() && clientController.isGameStarted() && clientController.getPhase().equals(TurnPhase.SELECTCARDS)) {
                            take(splitString);
                        } else {
                            System.out.println("It is not your turn!");
                        }
                    }
                    case "@myshelf" ->
                            printBookshelf(clientController.getPlayersBookshelf().get(clientController.getMyNickname()), clientController.getMyNickname());
                    case "@allshelves" -> printBookshelves(clientController.getPlayersBookshelf());
                    case "@put" -> {
                        if (clientController.isMyTurn() && clientController.isGameStarted() && clientController.getPhase().equals(TurnPhase.INSERTCARDS)) {
                            put(splitString);
                        } else {
                            System.out.println("It is not your turn!");
                        }
                    }
                    case "@chat" -> chat(splitString);
                    case "@quit" -> {
                        System.out.println("Stopping CLI...");
                        stopListening = true;
                    }
                    default -> {
                        if (!stopListening)
                            System.out.println("Input not recognised... try again");
                    }
                }
            }
        }

        disconnectionError();
    }

    /**
     * Checks the positions of the chosen tiles and, if they are accepted, sends them to the client controller
     */
    private void take(String[] splitString) {
        ArrayList<Integer> coords = checkInput.checkTake(splitString);

        if (coords != null) {
            clientController.setSelectedTiles(coords);
        }

        try {
            clientController.selectCard();
        } catch (Exception e) {
            System.out.println("Impossible to connect to the server");
        }
    }

    /**
     * Checks the column index and if the cards to put are the same as the selected ones
     * If they are accepted, sends them to the client controller
     */
    private void put(String[] splitString) {
        ArrayList<ItemCard> tilesToPut = checkInput.checkPut(splitString);
        int column = 0;

        if (tilesToPut != null) {
            try {
                column = Integer.parseInt(splitString[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                clientController.insertCard(tilesToPut, column);
            } catch (Exception e) {
                System.out.println("Impossible to connect to the server");
            }

        }
    }

    /**
     * Checks the recipient, and the message
     * If accepted sends them to the client controller
     */
    private void chat(String[] splitString) {
        StringBuilder msg = new StringBuilder();
        String destNickname = null;
        int dest = checkInput.checkChat(splitString);

        // creating the message to send
        for (int i = 2; i < splitString.length; i++) {
            msg.append(splitString[i]).append(" ");
        }

        String message = msg.toString();

        if (dest == 1) {
            destNickname = splitString[1];
            try {
                clientController.chatToPlayer(destNickname, message);
            } catch (Exception e) {
                System.out.println("Impossible to connect to the server");
            }
        } else if (dest == 2) {
            destNickname = "all";

            try {
                clientController.chatToAll(message);
            } catch (Exception e) {
                System.out.println("Impossible to connect to the server");
            }
        }

        System.out.println("Sending " + msg + "to " + destNickname);
    }

    /**
     * Allows the user only to quit the game while waiting for it to start
     */
    public void waitForGameMenu() {
        System.out.println("""
                GAME MENU: type the corresponding command
                 \t@MENU to show again this menu
                 \t@QUIT to exit from the game""");
    }

    /**
     * Prints a menu on the screen to let the user choose what to do next
     */
    @Override
    public void printMenu() {
        System.out.println("""
                GAME MENU: type the corresponding command
                \t@MENU to show again this menu
                \t@COMGOAL to print the Common Goal of this game
                \t@PERSGOAL to print your Personal Goal
                \t@BOARD to print the game board
                \t@TAKE to choose from 1 to 3 tiles from the board, followed by the coordinates (xy) of the chosen tiles
                \t@MYSHELF to print you bookshelf
                \t@ALLSHELVES to print the bookshelf of all the players
                \t@PUT to choose a column for putting the cards, followed by the column number and the board coordinates of the tiles (from bottom to top)
                \t@CHAT to open the chat, followed by the nickname/all and the message
                \t@QUIT to exit from the game""");
    }

    /**
     * Prints the current board
     */
    @Override
    public synchronized void printBoard(ItemCard[][] board) {
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
                printBookshelf(bookshelves.get(s), s);

            }
        }
    }

    /**
     * Prints the bookshelf of a given player
     */
    @Override
    public synchronized void printBookshelf(ItemCard[][] bookshelf, String nickname) {
        System.out.println(nickname + "'s bookshelf:");
        if (bookshelf != null) {
            System.out.println("    0   1   2   3   4");
            printMatrix(bookshelf, BOOKSHELF_HEIGHT, BOOKSHELF_LENGTH);
        } else {
            System.out.println("    0   1   2   3   4");
            printMatrix(new ItemCard[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH], BOOKSHELF_HEIGHT, BOOKSHELF_LENGTH);
        }
    }

    @Override
    public void chatToMe(String sender, String message) {
        System.out.println("From" + sender + ": " + message);
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
            System.out.print((char) 27 + "[0;39m" + " | ");
        } else {
            System.out.print(" ");
            System.out.print((char) 27 + "[0;39m" + " | ");
        }
    }

    /**
     * If the player is the first, allows it to choose the number of players
     */
    @Override
    public void printAskPlayerNumber() {
        System.out.println((char) 27 + "[0;39m" + "Write @PLAYERS followed by the number of players for this game");
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
                    System.out.print((char) 27 + "[0;39m" + i + " | ");
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
            return "[1;92m";
        } else if (itemChar == 'F') {
            // Frames are blue
            return "[1;94m";
        } else if (itemChar == 'G') {
            // Games are yellow
            return "[1;93m";
        } else if (itemChar == 'B') {
            // Books are white
            return "[1;97m";
        } else if (itemChar == 'P') {
            // Plants are purple
            return "[1;95m";
        } else if (itemChar == 'T') {
            // Trophies are cyan
            return "[1;96m";
        } else {
            // Default color
            return "[0;39m";
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
                System.out.println((char) 27 + "[0;39m" + "Common Goal number " + i + ": ");
                if (i == 1) {
                    System.out.println("Two separate groups each containing four tiles of the same type in a 2x2 square.\n" + "The tiles of one square can be different from those of the other square.");
                } else if (i == 2) {
                    System.out.println("Two columns each formed by 6 different types of tiles.\n" + "One column can show the same or a different combination of the other column.");
                } else if (i == 3) {
                    System.out.println("Four groups each containing at least 4 tiles of the same type (not necessarily in the depicted shape).\n" + "The tiles of one group can be different from those of another group.");
                } else if (i == 4) {
                    System.out.println("Six groups each containing at least 2 tiles of the same type (not necessarily in the depicted shape).\n" + "The tiles of one group can be different from those of another group.");
                } else if (i == 5) {
                    System.out.println("Three columns each formed by 6 tiles of maximum three different types.\n" + "One column can show the same or a different combination of another column.");
                } else if (i == 6) {
                    System.out.println("Two lines each formed by 5 different types of tiles.\n" + "One line can show the same or a different combination of the other line.");
                } else if (i == 7) {
                    System.out.println("Four lines each formed by 5 tiles of maximum three different types.\n" + "One line can show the same or a different combination of another line.");
                } else if (i == 8) {
                    System.out.println("Four tiles of the same type in the four corners of the bookshelf.");
                } else if (i == 9) {
                    System.out.println("Eight tiles of the same type.\n" + "There is no restriction about the position of these tiles.");
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

                System.out.println("The maximum available score for this card is " + playerComGoal.get(i) + ".\n");
            }
        }
    }

    /**
     * Prints the points earned by the player
     */
    @Override
    public void printPoints(int myPoint) {
        System.out.println((char) 27 + "[0;39m" + "You currently have " + myPoint + "points.");
    }

    /**
     * Prints the personalGoal associated to the player
     *
     * @param myPersGoal is a map with key the position and value the houseItem in that position
     */
    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal) {
        System.out.println((char) 27 + "[0;39m" + "Your personal goal is: ");
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

    @Override
    public void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        System.out.println("Here are the cards you previously selected:");
        int cardNumber = selectedTiles.size();

        for(Integer i : selectedTiles.keySet()) {
            char itemChar = selectedTiles.get(i).getMyItem().toString().charAt(0);
            System.out.print((char) 27 + "[0;39m" + "Item card ");
            System.out.print((char) 27 + chooseColorCode(itemChar) + itemChar);
            System.out.print((char) 27 + "[0;39m" + " from position " + i);

            if (cardNumber > 1){
                System.out.print(" and ");
            }
            cardNumber--;
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
    @Override
    public void disconnectionError() {
        System.out.println("\nPress ENTER to exit");
        in.nextLine();
        System.exit(1);
    }

    @Override
    public void disconnectMe() {
        stopListening = true;
        System.out.println("You are being disconnected from the server, please press ENTER to exit");
    }

    @Override
    public void printStartGame() {
        synchronized (this) {
            System.out.println("Welcome " + clientController.getMyNickname() + "!");
            System.out.println("You will play in a " + clientController.getPlayersBookshelf().keySet().size() + " players game.");
        }
        printMenu();
    }
}
