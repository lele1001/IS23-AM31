package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.InputController;
import it.polimi.ingsw.commons.TurnPhase;
import it.polimi.ingsw.commons.HouseItem;
import it.polimi.ingsw.commons.ItemCard;
import it.polimi.ingsw.utils.Utils;

import java.util.*;

import static it.polimi.ingsw.utils.Utils.*;

/**
 * Class that defines all the characteristics and methods of the CLI view
 */
public class CLI implements View {
    private static boolean stopListening;
    final Scanner in = new Scanner(System.in);
    final InputController checkInput;
    final ClientController clientController;
    String username, address;
    int port = -1, select = -1;
    boolean disconnected = false;

    /**
     * Prints the MyShelfie Logo
     */
    private void printLogo() {
        System.out.println("""
                 __       __  __      __         ______   __    __  ________  __        ________  ______  ________\s
                /  \\     /  |/  \\    /  |       /      \\ /  |  /  |/        |/  |      /        |/      |/        |
                $$  \\   /$$ |$$  \\  /$$/       /$$$$$$  |$$ |  $$ |$$$$$$$$/ $$ |      $$$$$$$$/ $$$$$$/ $$$$$$$$/\s
                $$$  \\ /$$$ | $$  \\/$$/        $$ \\__$$/ $$ |__$$ |$$ |__    $$ |      $$ |__      $$ |  $$ |__   \s
                $$$$  /$$$$ |  $$  $$/         $$      \\ $$    $$ |$$    |   $$ |      $$    |     $$ |  $$    |  \s
                $$ $$ $$/$$ |   $$$$/           $$$$$$  |$$$$$$$$ |$$$$$/    $$ |      $$$$$/      $$ |  $$$$$/   \s
                $$ |$$$/ $$ |    $$ |          /  \\__$$ |$$ |  $$ |$$ |_____ $$ |_____ $$ |       _$$ |_ $$ |_____\s
                $$ | $/  $$ |    $$ |          $$    $$/ $$ |  $$ |$$       |$$       |$$ |      / $$   |$$       |
                $$/      $$/     $$/            $$$$$$/  $$/   $$/ $$$$$$$$/ $$$$$$$$/ $$/       $$$$$$/ $$$$$$$$/\s
                                                                                                                  \s
                __________________________________________________________________________________________________
                """);
    }


    /**
     * Initialization of the client profile asking: username, type of connection, ip and port
     *
     * @param clientController defines the direct contact with all the object containers sen from the server
     */
    public CLI(ClientController clientController) {
        this.clientController = clientController;
        printLogo();
        clientController.setView(this);
        checkInput = new InputController(clientController);
        askLoginParameters();
        clientController.startConnection(select, username, address, port);
        new Thread(this::listen).start();
    }

    /**
     * Prepared initialization of the client profile asking: username
     *
     * @param clientController defines the direct contact with all the object containers sen from the server
     * @param conn             defines the type of connection
     * @param port             defines the port to connect to
     */
    public CLI(ClientController clientController, int port, String conn) {
        this.clientController = clientController;
        printLogo();
        clientController.setView(this);
        checkInput = new InputController(clientController);

        // Asks the username
        do {
            System.out.print(grey + "Select Username: ");
            if (in.hasNext()) {
                username = in.next();
                if (username.length() > maxNameLength) {
                    System.out.println(red + "Name too long (max 18 characters)");
                    username = "";
                }
            }
        } while (username.equals(""));

        in.nextLine();

        if (conn.equals("rmi")) {
            select = 0;
        } else {
            select = 1;
        }
        address = "127.0.0.1";

        clientController.startConnection(select, username, address, port);


        new Thread(this::listen).start();
    }

    /**
     * Reads the choice of the user and calls the correct method
     */
    private void listen() {
        String choice;
        String[] splitString;

        while (!stopListening) {
            if (in.hasNextLine()) {
                try {
                    choice = in.nextLine();
                    splitString = choice.split(" ");

                    // transforms only the command given by the user to its lower case version
                    splitString[0] = splitString[0].toLowerCase();

                    switch (splitString[0]) {
                        case "@players" -> {
                            if (clientController.isSelectNumberOfPlayers()) {
                                int players = checkInput.checkPlayers(splitString);
                                if (players != -1) {
                                    clientController.setPlayersNumber(players, splitString[2]);
                                }
                            } else {
                                System.out.println("You can not choose the number of players!");
                            }
                        }
                        case "@savedgame" -> {
                            if (splitString.length != 2)
                                printError("Input not recognised... try again.");
                            else {
                                if (splitString[1].equalsIgnoreCase("n") || splitString[1].equalsIgnoreCase("no"))
                                    clientController.setSavedGame(false, null);
                                else {
                                    clientController.setSavedGame(true, splitString[1]);
                                }
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
                        case "@persgoal" ->
                                printPersGoal(clientController.getMyPersGoal(), clientController.getPersGoalValue());
                        case "@score" -> printPoints(clientController.getMyPoint());
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
                                printBookshelf(clientController.getPlayersBookshelves().get(clientController.getMyNickname()), clientController.getMyNickname());
                        case "@allshelves" -> printBookshelves(clientController.getPlayersBookshelves());
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
                } catch (Exception e) {
                    if (!stopListening)
                        System.out.println("Input not recognised... try again");
                }
            }
        }

        disconnectionError();
    }

    /**
     * Asks the username, the connection type (RMI or Socket), the IP Address and the Port to the client
     */
    private void askLoginParameters() {
        // Asks the username
        do {
            System.out.print(grey + "Select Username: ");
            if (in.hasNextLine()) {
                username = in.nextLine();
                if (username.length() > maxNameLength) {
                    System.out.println(red + "Name too long (max 18 characters)");
                    username = "";
                }
            }
        } while (username.trim().equals(""));

        // Asks the connection type
        do {
            System.out.print("Write 0 for RMI, 1 for Socket: ");
            if (in.hasNextInt()) {
                select = in.nextInt();
            } else if (in.hasNextLine()) {
                in.nextLine();
                System.out.println(red + "Input error");
            }
        } while (select != 0 && select != 1);

        // Asks the IP
        in.nextLine();
        do {
            System.out.print(grey + "Select Ip Address: ");
            if (in.hasNextLine()) {
                address = in.nextLine();
                if (!checkInput.isValidInet4Address(address)) {
                    System.out.println(red + "Input error");
                }
            }
        } while (address.trim().equals("") || !checkInput.isValidInet4Address(address));

        // Asks the port
        do {
            System.out.print(grey + "Select Ip Port: ");
            if (in.hasNextInt()) {
                port = in.nextInt();
            } else if (in.hasNextLine()) {
                in.nextLine();
                System.out.println(red + "Input error");
            }
        } while (port == -1);

        in.nextLine();
    }

    /**
     * Implementation for CLI: prints the request to join or not a saved game
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    @Override
    public void askForSavedGame(List<String> savedGames) {
        System.out.println("These are the saved games with your nickname into: " + savedGames + ".");
        System.out.println("Do you want to resume one of them?");
        System.out.println("Type @savedgame and the name of the game you want to resume (or 'n' to start a new game).");
    }

    /**
     * Implementation for CLI: prints the request for the number of players
     */
    @Override
    public synchronized void printAskPlayerNumber() {
        System.out.println(grey + "Write @PLAYERS followed by the number of players and a name for this game.");
        System.out.println("Please, remember it in case of server crash (you can resume the game typing its name.)");
    }

    /**
     * Implementation for CLI: prints a waiting message while other players connect
     */
    @Override
    public synchronized void printLobby() {
        if (!clientController.isGameStarted()) {
            System.out.println("Waiting for other players to connect...");
            waitForGameMenu();
        }
    }

    /**
     * Allows the user only to quit the game while waiting for it to start
     */
    private synchronized void waitForGameMenu() {
        System.out.println("GAME MENU: type the corresponding command \n\t@MENU to show again this menu \n\t@QUIT to exit from the game");
    }

    /**
     * Implementation for CLI: prints the message that signals the start of the game
     */
    @Override
    public synchronized void printStartGame() {
        synchronized (this) {
            System.out.print(grey + "Welcome " + clientController.getMyNickname() + "! ");
            System.out.println("play in a " + clientController.getPlayersBookshelves().keySet().size() + " players game.");
            System.out.println("Type @MENU to see the game menu.\n");
        }
    }

    /**
     * Prints a menu on the screen to let the user choose what to do next
     */
    private synchronized void printMenu() {
        System.out.println(grey + "GAME MENU: type the corresponding command \n\t@MENU to show again this menu \n\t@COMGOAL to print the Common Goal of this game \n\t@PERSGOAL to print your Personal Goal \n\t@SCORE to print your score \n\t@BOARD to print the game board \n\t@TAKE to choose from 1 to 3 tiles from the board, followed by their coordinates (xy) of the chosen tiles \n\t@MYSHELF to print you bookshelf \n\t@ALLSHELVES to print the bookshelf of all the players \n\t@PUT to choose a column for putting the cards, followed by the column number and the board coordinates of the tiles (from bottom to top) \n\t@CHAT to open the chat, followed by the nickname/all and the message \n\t@QUIT to exit from the game");
    }

    /**
     * Implementation for CLI: prints the current player each time somebody ends its turn
     *
     * @param currPlayer is the nickname of the current player
     */
    @Override
    public void onChangeTurn(String currPlayer) {
        if (currPlayer.equals(username)) {
            print(grey + "It is your turn\n");
        } else {
            print(grey + "It is " + currPlayer + "'s turn\n");
        }
    }

    /**
     * Implementation for CLI: prints a selection message or scene,
     * then calls the methods to check and communicate the selection to the server
     */
    @Override
    public void onSelect() {
        print(grey + "Type @TAKE to choose from 1 to 3 tiles from the board, followed by their coordinates (xy)");
        printBoard(clientController.getBoard());
    }

    /**
     * Checks the positions of the chosen tiles and, if they are accepted, sends them to the client controller
     */
    private void take(String[] splitString) {
        ArrayList<Integer> coords = checkInput.checkTake(splitString);

        if (coords != null) {
            clientController.setSelectedTiles(coords);
                clientController.selectCard();
        }
    }

    /**
     * Implementation for CLI: prints the Tiles chosen by the player in the last Phase of the turn
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    @Override
    public synchronized void printSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        System.out.print("You selected: ");
        int cardNumber = selectedTiles.size();

        for (Integer i : selectedTiles.keySet()) {
            char itemChar = selectedTiles.get(i).getMyItem().toString().charAt(0);
            System.out.print(grey + "(");
            System.out.print(chooseColorCode(itemChar) + itemChar);
            System.out.print(grey + " - " + i + ")");

            if (cardNumber > 1) {
                System.out.print(" and ");
            }
            cardNumber--;
        }

        System.out.println();
    }

    /**
     * Implementation for CLI: prints an insertion message or scene,
     * then calls the methods to check and communicate the insertion to the server
     */
    @Override
    public void onInsert() {
        print(grey + "Type @PUT followed by the column number and the board coordinates of the tiles (from bottom to top)");
        printSelectedTiles(clientController.getSelectedTiles());
        printPersGoal(clientController.getMyPersGoal(), clientController.getPersGoalValue());
        printBookshelf(clientController.getPlayersBookshelves().get(clientController.getMyNickname()), clientController.getMyNickname());
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
            clientController.insertCard(tilesToPut, column);
        }
    }

    /**
     * Implementation for CLI: prints the Board
     *
     * @param board is the updated Board
     */
    @Override
    public synchronized void printBoard(ItemCard[][] board) {
        System.out.println(grey + "    0   1   2   3   4   5   6   7   8");
        printMatrix(board, DIM_BOARD, DIM_BOARD);
    }

    /**
     * Implementation for CLI: updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    @Override
    public void changeBoard(Integer[] tilesToRemove) {
        printBoard(clientController.getBoard());
    }

    /**
     * Prints all the bookshelves of the players in the current game
     *
     * @param bookshelves contains the nickname of the player and its bookshelf
     */
    private synchronized void printBookshelves(Map<String, ItemCard[][]> bookshelves) {
        /*if (!bookshelves.isEmpty()) {
            for (String s : bookshelves.keySet()) {
                printBookshelf(bookshelves.get(s), s);
            }
        }*/
        System.out.println();
        for (String s : bookshelves.keySet()) {
            System.out.print(s + "'s bookshelf");
            for (int i = 0; i < 18 - s.length(); i++) {
                System.out.print(" ");
            }
        }
        System.out.println();
        if (!bookshelves.isEmpty()) {
            for (int i = 0; i < bookshelves.keySet().size(); i++) {
                System.out.print("    0   1   2   3   4         ");
            }
            System.out.println();
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (String s : bookshelves.keySet()) {
                    for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                        if (j == 0) {
                            System.out.print(grey + i + " | ");
                        }

                        printCell(bookshelves.get(s), i, j);
                    }
                    System.out.print("      ");
                }
                System.out.println();
            }

        }
    }

    /**
     * Implementation for CLI: prints the player's Bookshelf
     *
     * @param bookshelf is the player's updated Bookshelf
     * @param nickname  is the owner of the Bookshelf
     */
    @Override
    public synchronized void printBookshelf(ItemCard[][] bookshelf, String nickname) {
        System.out.println(grey + nickname + "'s bookshelf:");
        System.out.println("    0   1   2   3   4");
        printMatrix(Objects.requireNonNullElseGet(bookshelf, () -> new ItemCard[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH]), BOOKSHELF_HEIGHT, BOOKSHELF_LENGTH);
    }

    /**
     * Implementation for CLI: updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
        printBookshelf(clientController.getPlayersBookshelves().get(player), player);
    }

    /**
     * Prints a cell from an ItemCard matrix (Bookshelf or Board)
     *
     * @param matrix is the Bookshelf or the Board to print
     * @param height is the number of rows
     * @param length is the number of columns
     */
    private synchronized void printMatrix(ItemCard[][] matrix, int height, int length) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                if (j == 0) {
                    System.out.print(grey + i + " | ");
                }

                printCell(matrix, i, j);
            }

            System.out.println();
        }
    }

    /**
     * Prints a cell from an ItemCard matrix (Bookshelf or Board)
     *
     * @param matrix is the Bookshelf or the Board to print
     * @param i      is the row number
     * @param j      is the column number
     */
    private synchronized void printCell(ItemCard[][] matrix, int i, int j) {
        if (matrix[i][j] != null) {
            char itemChar = matrix[i][j].getMyItem().toString().charAt(0);

            System.out.print(chooseColorCode(itemChar) + itemChar);
            System.out.print(grey + " | ");
        } else {
            System.out.print(" ");
            System.out.print(grey + " | ");
        }
    }

    /**
     * Switches between CLI colors based on the ItemCard type
     *
     * @param itemChar is the first character of the ItemCard type
     * @return the string associated with the color
     */
    private String chooseColorCode(char itemChar) {
        if (itemChar == 'C') {
            return green;
        } else if (itemChar == 'F') {
            return blue;
        } else if (itemChar == 'G') {
            return yellow;
        } else if (itemChar == 'B') {
            return white;
        } else if (itemChar == 'P') {
            return purple;
        } else if (itemChar == 'T') {
            return cyan;
        } else {
            return grey;
        }
    }

    /**
     * Implementation for CLI: prints the CommonGoal for the game
     *
     * @param playerComGoal contains the CommonGoalID and its available score
     */
    @Override
    public synchronized void printCommonGoal(Map<Integer, Integer> playerComGoal) {
        if (!playerComGoal.isEmpty()) {
            for (Integer i : playerComGoal.keySet()) {
                System.out.println(grey + "Common Goal number " + i + ": ");
                System.out.println(Utils.comGoalDescription.valueOf("comGoal" + i).getDescription());
                System.out.println("The maximum available score for this card is " + playerComGoal.get(i) + ".\n");
            }
        }
    }

    /**
     * Implementation for CLI: updates the available score of a CommonGoal each time it is reached
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void onCommonGoalDone(int comGoalDoneID, int newValue) {
        print("The new value of CommonGoal n° " + comGoalDoneID + " is " + newValue);
    }

    /**
     * Implementation for CLI: prints the player's PersonalGoal
     *
     * @param myPersGoal represents the PersonalGoal of the player
     * @param newValue   is the string that defines the PersonalGoal
     */
    @Override
    public synchronized void printPersGoal(Map<Integer, HouseItem> myPersGoal, String newValue) {
        System.out.println(grey + "Your personal goal is: ");
        System.out.println("    0   1   2   3   4");

        for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
            for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                int k = i * 10 + j;

                if (j == 0) {
                    System.out.print(grey + i + " | ");
                }

                if (myPersGoal.containsKey(k)) {
                    char itemChar = myPersGoal.get(k).toString().charAt(0);

                    System.out.print(chooseColorCode(itemChar) + itemChar);
                } else {
                    System.out.print(" ");
                }
                System.out.print(grey + " | ");

            }

            System.out.println();
        }

        System.out.println();
    }

    /**
     * Implementation for CLI: prints the player's points
     *
     * @param myPoint are the points of the player
     */
    @Override
    public synchronized void printPoints(int myPoint) {
        System.out.println(grey + "You currently have " + myPoint + " points.");
    }

    /**
     * Implementation for CLI: prints a generic message from the server
     *
     * @param message is the string to print
     */
    @Override
    public synchronized void print(String message) {
        System.out.println(message);
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
            clientController.chatToPlayer(destNickname, message);
        } else if (dest == 2) {
            destNickname = "all";
                clientController.chatToAll(message);
        }

        System.out.println("Sending " + msg + "to " + destNickname);
    }

    /**
     * Implementation for CLI: prints the message on the chat
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    @Override
    public synchronized void chatToMe(String sender, String message) {
        System.out.println("From " + sender + ": " + message);
    }

    /**
     * Implementation for CLI: prints an error message
     *
     * @param error is the error message to display
     */
    @Override
    public synchronized void printError(String error) {
        System.err.println(error);
    }

    /**
     * Implementation for CLI: closes the view after an error occurs
     */
    @Override
    public void disconnectionError() {
        if (!disconnected) {
            disconnected = true;
            System.out.println("\nPress ENTER to exit");
            in.nextLine();
        }
        System.exit(1);
    }

    /**
     * Implementation for CLI: prints the name(s) of the winning player(s)
     *
     * @param winners is(are) the winner(s) of the game
     */
    @Override
    public synchronized void printWinners(List<String> winners) {
        if (winners.size() == 1) {
            System.out.println("The winner is " + winners.get(0));
        } else {
            System.out.println("Parity: the winners are " + winners);
        }
    }

    /**
     * Implementation for CLI: disconnects the client after a request done by the server
     */
    @Override
    public void disconnectMe() {
        stopListening = true;
        if (!disconnected) {
            disconnected = true;
            System.out.println(grey + "You are being disconnected from the server, please press ENTER to exit");
        }
    }

    /**
     * Implementation for CLI: prints the name of each player and its score
     *
     * @param finalScores contains the players' nicknames and their score
     */

    @Override
    public void finalScores(LinkedHashMap<String, Integer> finalScores) {
        print("Game is ended!");
        print("Final rank: ");
        int i = 1, max = -1;

        for (String s : finalScores.keySet()) {
            if (finalScores.get(s) == max)
                print(" parity with " + s);
            else {
                System.out.println();
                print(i + ") " + s);
                max = finalScores.get(s);
                i++;
            }
        }

        System.out.println();
    }

    /**
     * Implementation for CLI: does nothing, test already written from the Client controller
     */
    @Override
    public void bookshelfCompleted() {

    }

    /**
     * Called when the game has been interrupted because of too many absents for it.
     */
    @Override
    public void gameInterrupted() {
        print("Game has been interrupted because of too many absents.");
        print("Waiting for other players...");
    }
}
