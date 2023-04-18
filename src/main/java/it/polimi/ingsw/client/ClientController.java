package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.connection.ConnectionClient;
import it.polimi.ingsw.client.connection.ConnectionRMI;
import it.polimi.ingsw.client.connection.ConnectionSocket;
import it.polimi.ingsw.client.view.Cli;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.controller.TurnPhase;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.server.controller.TurnPhase.*;

public class ClientController {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    private String myNickname;
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
// new methods for failed login

    /**
     * Method called from the server to ask for the selection of the itemcards to the client
     * Change turn phase and print the board to the client's view
     */
    public void onSelect() {
        phase = SELECTCARDS;
        selectedTiles.clear();
        // to fix
        view.print("Choose the Tiles you want to get from the Board");
        view.printBoard(board);
    }

    /**
     * Method called from the server to ask for the insertion of the itemcards to the client
     * Change turn phase and print the client's bookshelf to the client's view
     */
    public void onInsert() {
        phase = INSERTCARDS;
        // to fix
        view.print("Choose int which order and where you want to put the Tiles");
        view.printMyBookshelf(playersBookshelf.get(myNickname));
    }

    /**
     * Method called from the view to update the Tiles selected from the player (After the input check)
     * @param coords C oordinates of the Tiles selected from the player
     */

    public void setSelectedTiles(ArrayList<Integer> coords) {
        for (Integer i : coords) {
            selectedTiles.put(i, board[Position.getRow(i)][Position.getColumn(i)]);
        }
    }

    /**
     * Method to get the selected tiles from the view for the insert method
     * @return the Tiles selected by the players during the
     */
    public Map<Integer, ItemCard> getSelectedTiles() {
        return selectedTiles;
    }

    /**
     * Method called from the server when the board change
     * Save the new Board and print it to the client's view
     *
     * @param newBoard is the updated board from the server
     */
    public void onBoardChanged(ItemCard[][] newBoard) {
        this.board = newBoard;
        view.printBoard(board);
    }

    /**
     * Method called from the server when the nickname's client bookshelf change
     * Save the new bookshelf and print all the bookshelves to the client's view
     *
     * @param nickname     nickname of the client whose bookshelf changed
     * @param newBookshelf The updated bookshelf
     */
    public void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        playersBookshelf.put(nickname, newBookshelf);
        view.printBookshelves(playersBookshelf);
    }

    /**
     * Method called to get the Client's view
     * @return the Client's view (GUI or TUI)
     */
    public View getView() {
        return view;
    }

    /**
     * Method called from the server when an error occurred,
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
    public void onPlayerNumber() {
        selectNumberOfPlayers = true;
        view.printAskPlayerNumber();
    }

    /**
     * Method called from the server when a CommonGoal is created
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
     * Method called from the server when one CommonGoal is done by the player nickname
     *
     * @param nickname name of the player
     * @param newValue new value of the CommonGoal after a player has done it
     */
    public void onCommonGoalDone(String nickname, int[] newValue) {
        if (myNickname.equals(nickname)) {
            myPoint += playerComGoal.get(newValue[0]);
            view.printPoints(myPoint);
        }

        playerComGoal.replace(newValue[0], newValue[1]);
        view.print(nickname + " has completed the CommonGoal n° " + newValue[0]);
        view.print("The new value of CommonGoal n° " + newValue[0] + " is " + newValue[1]);
    }

    /**
     * Method called from the server when creating the PersonalGoal of the player
     * Create from the json file the PersonalGoal and print it to the client's view
     *
     * @param newValue String that defines the PersonalGoal
     */
    public void onPersGoalCreated(String newValue) {
        Reader json;
        {
            try {
                json = new FileReader("src/main/java/it/polimi/ingsw/server/model/PersGoalConfiguration.json");
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: No such PersGoalConfiguration file.");
                return;
            }
        }

        Gson gson = new Gson();

        Type cardsType = new TypeToken<Map<String, ArrayList<Integer>>>() {
        }.getType();
        Map<String, ArrayList<Integer>> cards = gson.fromJson(json, cardsType);
        List<Integer> index = cards.get(newValue);

        // Conventional order in which we read items from JsonConfiguration file.
        List<HouseItem> items = new ArrayList<>(List.of(HouseItem.Frame, HouseItem.Cat, HouseItem.Books, HouseItem.Games, HouseItem.Trophy, HouseItem.Plants));
        for (int i = 0; i < items.size(); i++) {
            myPersGoal.put(index.get(i), items.get(i));
        }

        view.printPersGoal(myPersGoal);
    }

    /**
     * Method called from the server after the end of a player's turn
     * If it is my turn I update myTurn and print to the client's view that it is the client's turn
     *
     * @param nickname the nickname of the player whose turn is
     */
    public void onChangeTurn(String nickname) {
        if (nickname.equals(myNickname)) {
            myTurn = true;
            view.print("Your Turn");
        } else {
            phase = NULL;
            myTurn = false;
            view.print(nickname + "'s Turn");
        }
    }

    /**
     * Set the view to the client controller on which he will call all the methods
     *
     * @param view view on which the client controller will print on
     */
    public void setView(View view) {
        this.view = view;

        if (view instanceof Cli) {
            System.out.println("Cli added to Client controller");
        }
        //view.printMyBookshelf(playersBookshelf.get(myNickname));
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
        System.out.println("Your nickname is " + myNickname);

        if (select == 0) {
            connectionClient = new ConnectionRMI(this, address, port);
            System.out.println("Created RMI connection!");
        } else {
            connectionClient = new ConnectionSocket(this, address, port);
            System.out.println("Created Socket connection!");
        }

        connectionClient.startConnection();
    }

    /**
     * Methods called from the client that pass to the server the position of the Tiles selected by the client
     * @throws Exception if an error occurred calling the server ( Socket or RMI )
     */

    public void selectCard() throws Exception {
        ArrayList<Integer> integerSelected = new ArrayList<>(selectedTiles.keySet());
        connectionClient.selectCard(myNickname, integerSelected);
    }

    /**
     * Methods called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     * @param selectedCards Tiles selected by the client in order
     * @param column column where to put the Tiles
     * @throws Exception if an error occurred calling the server ( Socket or RMI )
     */
    public void insertCard(ArrayList<ItemCard> selectedCards, int column) throws Exception {
        connectionClient.insertCard(myNickname, selectedCards, column);
    }
    /**
     * Methods called from the client that pass to the server the chat message for all connected player of the game
     * @param message String to send to all the connected players
     * @throws Exception if an error occurred calling the server ( Socket or RMI )
     */
    public void chatToAll(String message) throws Exception {
        connectionClient.chatToAll(myNickname, message);
    }

    /**
     * Methods called from the client that pass to the server he chat message for the receiver
     * @param receiver player that receive the message
     * @param message String to send to the receiver
     * @throws Exception if an error occurred calling the server ( Socket or RMI )
     */
    public void chatToPlayer(String receiver, String message) throws Exception {
        connectionClient.chatToPlayer(myNickname, receiver, message);
    }

    /**
     * Method called from the server to print the message
     * @param sender Player that send the message
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
     * Method called by the server for the start of the game
     * @param gameStarted Boolean to set the gamesStarted as true for the control in the view
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        view.printMenu();
    }

    /**
     * @return true if game is started else false
     */
    public boolean getGameStarted() {
        return this.gameStarted;
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

    public Map<String, ItemCard[][]> getPlayersBookshelf() {
        return playersBookshelf;
    }

    /**
     * @return if it is my turn
     */

    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Metho called by the client only if he is the first connected to the server
     * @param players number of players in the game
     * @throws Exception if an error occurred calling the server ( Socket or RMI )
     */

    public void setPlayersNumber(int players) throws Exception {
        connectionClient.setPlayersNumber(players);
    }
}