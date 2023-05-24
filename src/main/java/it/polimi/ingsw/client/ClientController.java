package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Exceptions.NotAskedException;
import it.polimi.ingsw.client.Exceptions.NotAvailableNameException;
import it.polimi.ingsw.client.Exceptions.NotExistingGameException;
import it.polimi.ingsw.client.connection.ConnectionClient;
import it.polimi.ingsw.client.connection.ConnectionRMI;
import it.polimi.ingsw.client.connection.ConnectionSocket;
import it.polimi.ingsw.client.view.CLI;
import it.polimi.ingsw.client.view.GUI.GUI;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.controller.TurnPhase;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.controller.TurnPhase.*;

public class ClientController {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_LENGTH = 5;
    private static final int BOOKSHELF_HEIGHT = 6;
    private String myNickname, myPersGoalNumber;
    private ItemCard[][] board = new ItemCard[DIM_BOARD][DIM_BOARD];
    private Map<String, ItemCard[][]> playersBookshelf = new HashMap<>();
    private Map<Integer, Integer> playerComGoal = new HashMap<>();
    private boolean myTurn = false;
    private Map<Integer, ItemCard> selectedTiles = new HashMap<>();
    private int myPoint = 0;
    private View view;
    private final Map<Integer, HouseItem> myPersGoal = new HashMap<>();
    private ConnectionClient connectionClient;
    private TurnPhase phase = NULL;
    private boolean selectNumberOfPlayers = false;
    private boolean gameStarted = false;
    private boolean selectSavedGame = false;
    private List<String> savedGames;
    private List<String> notAvailableNames;
// new methods for failed login

    /**
     * Method called by the server to ask for the selection of the item cards to the client
     * Change turn phase and print the board to the client's view
     */
    public void onSelect() {
        phase = SELECTCARDS;
        selectedTiles.clear();
        view.onSelect();
    }

    /**
     * Method called by the server to ask for the insertion of the item cards to the client
     * Change turn phase and print the client's bookshelf to the client's view
     */
    public void onInsert() {
        phase = INSERTCARDS;
        view.onInsert();
    }

    /**
     * Method called by the view to update the Tiles selected from the player (After the input check)
     *
     * @param coords Coordinates of the Tiles selected from the player
     */

    public void setSelectedTiles(ArrayList<Integer> coords) {
        for (Integer i : coords) {
            selectedTiles.put(i, board[Position.getRow(i)][Position.getColumn(i)]);
        }
    }

    /**
     * Method to get the selected tiles from the view for the insert method
     *
     * @return the Tiles selected by the players during the select phase
     */
    public Map<Integer, ItemCard> getSelectedTiles() {
        return selectedTiles;
    }

    /**
     * Method called by the server when the board change
     * Save the new Board and print it to the client's view
     *
     * @param newBoard is the updated board from the server
     */
    public void onBoardChanged(ItemCard[][] newBoard) {
        this.board = newBoard;
        view.printBoard(board);
    }

    /**
     * Method called by the server when the nickname's client bookshelf change
     * Save the new bookshelf and print all the bookshelves to the client's view
     *
     * @param nickname     nickname of the client whose bookshelf changed
     * @param newBookshelf The updated bookshelf
     */
    public void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        playersBookshelf.put(nickname, newBookshelf);
        view.printBookshelf(playersBookshelf.get(nickname), nickname);
    }

    /**
     * Method called by the server when an error occurred,
     * Print the error to the client's view
     *
     * @param error String in which is saved the error
     */
    public void onError(String error) {
        //creates the message with the error string that will be parsed by the client.
        view.printError(error);
    }

    /**
     * Methods called by the server to the first player of the game asking the number of players he wants in the game
     * Set selectNumberOfPlayers to true, so the Cli can accept the corresponding command
     */
    public void onPlayerNumber(List<String> notAvailableNames) {
        selectNumberOfPlayers = true;
        this.notAvailableNames = new ArrayList<>(notAvailableNames);
        view.printAskPlayerNumber();
    }

    public void onSavedGame(List<String> savedGames) {
        selectSavedGame = true;
        this.savedGames = savedGames;
        view.askForSavedGame(savedGames);
    }

    /**
     * Method called by the server when a CommonGoal is created
     * Print the CommonGoal to the client's view
     *
     * @param comGoalID ID that references to the defined CommonGoal
     * @param score     Value of the CommonGoal if completed
     */
    public void onCommonGoalCreated(Integer comGoalID, Integer score) {
        playerComGoal.put(comGoalID, score);
        if (playerComGoal.size() > 1)
            view.printCommonGoal(playerComGoal);
    }

    /**
     * Method called by the server when one CommonGoal is done by the player nickname
     *
     * @param nickname name of the player
     * @param newValue new value of the CommonGoal after a player has done it
     */
    public void onCommonGoalDone(String nickname, int[] newValue) {
        if (newValue != null && playerComGoal.containsKey(newValue[0])) {
            if (myNickname.equals(nickname)) {
                myPoint += playerComGoal.get(newValue[0]);
                view.printPoints(myPoint);
            }

            playerComGoal.replace(newValue[0], newValue[1]);
            view.print(nickname + " has completed the CommonGoal n° " + newValue[0]);
            view.onCommonGoalDone(newValue[0], newValue[1]);
        }
    }

    /**
     * Called when the client comes back to an existing game with him into.
     *
     * @param score done during the game by the client.
     */
    public void onPlayerScore(int score) {
        view.printPoints(score);
        myPoint = score;
    }

    /**
     * Just notifies the client that he has completed his bookshelf and earned an extra point.
     */
    public void onBookshelfCompleted() {
        view.print("Congrats! You have just completed your bookshelf.");
        view.print("Adding you an extra point...");
        myPoint++;
        view.printPoints(myPoint);
        view.print("Let's go on for the last turn of the game!");
    }

    /**
     * Method called by the server when creating the PersonalGoal of the player
     * Create from the json file the PersonalGoal and print it to the client's view
     *
     * @param newValue String that defines the PersonalGoal
     */
    public void onPersGoalCreated(String newValue) {
        String json;
        {
            try {
                json = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("PersGoalConfiguration.json")))).lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (Exception e) {
                System.out.println("ERROR: No such PersGoalConfiguration file.");
                return;
            }
        }

        Gson gson = new Gson();

        Type cardsType = new TypeToken<Map<String, ArrayList<Integer>>>() {}.getType();
        Map<String, ArrayList<Integer>> cards = gson.fromJson(json, cardsType);
        List<Integer> index = cards.get(newValue);

        // Conventional order in which we read items from JsonConfiguration file.
        List<HouseItem> items = new ArrayList<>(List.of(HouseItem.Frame, HouseItem.Cat, HouseItem.Books, HouseItem.Games, HouseItem.Trophy, HouseItem.Plants));
        for (int i = 0; i < items.size(); i++) {
            myPersGoal.put(index.get(i), items.get(i));
        }
        myPersGoalNumber = newValue;
        view.printPersGoal(myPersGoal, myPersGoalNumber);
    }

    /**
     * Method called by the server after the end of a player's turn
     * If it is my turn I update myTurn and print to the client's view that it is the client's turn
     *
     * @param nickname the nickname of the player whose turn is
     */
    public void onChangeTurn(String nickname) {
        if (nickname.equals(myNickname)) {
            myTurn = true;
        } else {
            phase = NULL;
            myTurn = false;
        }
        view.onChangeTurn(nickname);
    }

    /**
     * Set the view to the client controller on which he will call all the methods
     *
     * @param view view on which the client controller will print on
     */
    public void setView(View view) {
        this.view = view;

        if (view != null) {
            if (view instanceof CLI) {
                System.out.println("CLI added to ClientController");
            } else if (view instanceof GUI) {
                System.out.println("GUI added to ClientController");
            }
        }
        //view.printBookshelf(playersBookshelf.get(myNickname));
    }

    /**
     * Starting the connection with the server using all data from the view
     * Saving the player's username
     *
     * @param select   0 for RMI, 1 for Socket
     * @param username player's username
     * @param address  IP of the server
     * @param port     Port of the server
     * @throws Exception Called if there is a connection problem, close the client
     */
    public void startConnection(int select, String username, String address, int port) throws Exception {
        this.myNickname = username;
        //System.out.println("Your nickname is " + myNickname);

        if (select == 0) {
            connectionClient = new ConnectionRMI(this, address, port);
            //System.out.println("Created RMI connection!");
        } else {
            connectionClient = new ConnectionSocket(this, address, port);
            //System.out.println("Created Socket connection!");
        }

        connectionClient.startConnection();
        printWaitingForGame();
    }

    /**
     * Method called by the client that pass to the server the position of the Tiles selected by the client
     *
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public void selectCard() throws Exception {
        ArrayList<Integer> integerSelected = new ArrayList<>(selectedTiles.keySet());
        connectionClient.selectCard(myNickname, integerSelected);
    }

    /**
     * Method called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     *
     * @param selectedCards Tiles selected by the client in order
     * @param column        column where to put the Tiles
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public void insertCard(ArrayList<ItemCard> selectedCards, int column) throws Exception {
        connectionClient.insertCard(myNickname, selectedCards, column);
    }

    /**
     * Method called by the client that pass to the server the chat message for all connected players of the game
     *
     * @param message String to send to all the connected players
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public void chatToAll(String message) throws Exception {
        connectionClient.chatToAll(myNickname, message);
    }

    /**
     * Method called by the client that pass to the server the chat message for the receiver
     *
     * @param receiver player that receives the message
     * @param message  String to send to the receiver
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public void chatToPlayer(String receiver, String message) throws Exception {
        connectionClient.chatToPlayer(myNickname, receiver, message);
    }

    /**
     * Method called by the server to print the message
     *
     * @param sender  Player that send the message
     * @param message String sent by the sender
     */
    public void chatToMe(String sender, String message) {
        view.chatToMe(sender, message);
    }

    /**
     * @return the player's nickname
     */
    public String getMyNickname() {
        return myNickname;
    }

    /**
     * @return the Board
     */
    public ItemCard[][] getBoard() {
        return board;
    }

    /**
     * @return the Map of all the Bookshelves
     */
    public Map<String, ItemCard[][]> getPlayersBookshelves() {
        return playersBookshelf;
    }

    /**
     * @return if it is my turn
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Method called by the client only if he is the first connected to the server
     *
     * @param players number of players in the game
     * @throws Exception if an error occurred calling the server (Socket or RMI)
     */
    public void setPlayersNumber(int players, String gameName) throws Exception {
        if ((notAvailableNames.contains(gameName)) || gameName.contains("."))
            throw new NotAvailableNameException();
        selectNumberOfPlayers = false;
        connectionClient.setPlayersNumber(players, gameName);
        printWaitingForGame();
    }

    public void printWaitingForGame() {
        if (!gameStarted) {
            view.printLobby();
        }
    }

    public void setSavedGame(boolean wantToSave, String gameName) throws Exception {
        if (!selectSavedGame)
            throw new NotAskedException();
        if ((wantToSave) && (!savedGames.contains(gameName)))
            throw new NotExistingGameException();
        selectSavedGame = false;
        connectionClient.setSavedGame(wantToSave, gameName);
    }

    /**
     * @return the CommonGoals of the game
     */
    public Map<Integer, Integer> getPlayerComGoal() {
        return playerComGoal;
    }

    /**
     * @return the client's point
     */
    public int getMyPoint() {
        return myPoint;
    }

    /**
     * @return the client's personal goal
     */
    public Map<Integer, HouseItem> getMyPersGoal() {
        return myPersGoal;
    }

    /**
     * @return the game's turn phase if it is his turn, NULL all other times
     */
    public TurnPhase getPhase() {
        return phase;
    }

    /**
     * @return true if you can use the @player function on the Cli
     */
    public boolean isSelectNumberOfPlayers() {
        return selectNumberOfPlayers;
    }

    /**
     * @return true if the game is started else false
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Method called by the server to disconnect the player
     */
    public void disconnectMe() {
        view.disconnectMe();
    }

    /**
     * Method called by the server when the game starts
     *
     * @param playersList The List of the players connected to the game
     * @param gameStarted A Boolean used by the client to check the input of the user
     */
    public void gameStarted(ArrayList<String> playersList, boolean gameStarted) {
        for (String player : playersList) {
            playersBookshelf.put(player, new ItemCard[BOOKSHELF_HEIGHT][BOOKSHELF_LENGTH]);
        }
        this.gameStarted = gameStarted;
        view.printStartGame();
    }

    /**
     * Method called by the server to print the name(s) of the winner(s)
     *
     * @param winners The player(s) tha has/have done the highest number of points
     */
    public void onWinner(List<String> winners) {
        view.printWinners(winners);
    }

    public String getPersGoalValue(){return myPersGoalNumber;}
}