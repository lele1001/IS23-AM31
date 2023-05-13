package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.RMI;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionRMI extends ConnectionClient implements RMIClientConnection {
    private RMI server;
    Timer timer = new Timer();

    /**
     * Initialize the RMI connection to the server
     *
     * @param controller ClientController, on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     */
    public ConnectionRMI(ClientController controller, String address, int port) throws RemoteException {
        super(controller, address, port);
    }

    /**
     * Start the connection and the login in RMI
     *
     * @throws IOException       throws if it has an error with input or output
     * @throws NotBoundException throws if it has an error with the connection
     */
    @Override
    public void startConnection() throws IOException, NotBoundException, NullPointerException {
        boolean i;
        Registry registry = LocateRegistry.getRegistry(getAddress(), getPort());
        server = (RMI) registry.lookup("MyShelfieServer");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000);
        System.out.println("Connection established.");
        i = server.login(getController().getMyNickname(), this);
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     *
     * @param nickname      this client
     * @param cardsSelected Tiles selected by the client
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void selectCard(String nickname, ArrayList<Integer> cardsSelected) throws RemoteException, NullPointerException {
        server.selectCard(nickname, cardsSelected);
    }

    /**
     * Method called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     *
     * @param nickname this client
     * @param cards    Tiles selected by the client in order
     * @param column   column where to put the Tiles
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException, NullPointerException {
        server.insertCard(nickname, cards, column);
    }

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     *
     * @param nickname this client
     * @param message  String to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void chatToAll(String nickname, String message) throws RemoteException, NullPointerException {
        server.chatToAll(nickname, message);
    }

    /**
     * Method called from the client that pass to the server the chat message for the receiver
     *
     * @param sender   this client
     * @param receiver player that receives the message
     * @param message  String to send to the receiver
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws RemoteException, NullPointerException {
        server.chatToPlayer(sender, receiver, message);
    }

    /**
     * Method called by the client only if he is the first connected to the server
     *
     * @param players number of players in the game
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void setPlayersNumber(int players, String gameName) throws RemoteException, NullPointerException {
        server.setPlayerNumber(getController().getMyNickname(), players);
    }

    @Override
    public void setSavedGame(boolean wantToSave, String gameName) throws Exception {
        server.setSavedGames(wantToSave, gameName);
    }


    /**
     * Method called by the server if the player is first in queue and ha to decide the number of players in the game
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onPlayerNumber() throws RemoteException {
        getController().onPlayerNumber(new ArrayList<>());
    }

    /**
     * Method called by the server in case of error on the server side
     *
     * @param error String that describes the type of error
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onError(String error) throws RemoteException {
        getController().onError(error);
    }

    /**
     * Method called by the server to ask the player to select the Tiles
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onSelect() throws RemoteException {
        getController().onSelect();
    }

    /**
     * Method called by the server to ask the player to insert the Tiles
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onInsert() throws RemoteException {
        getController().onInsert();
    }

    /**
     * Method called by the server to update the client's side board
     *
     * @param newBoard the updated board
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onBoardChanged(ItemCard[][] newBoard) throws RemoteException {
        getController().onBoardChanged(newBoard);
    }

    /**
     * Method called by the server to update the client's side bookshelf of player nickname
     *
     * @param nickname     the nickname of the player
     * @param newBookshelf the updated bookshelf of the player nickname
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) throws RemoteException {
        getController().onBookshelfChanged(nickname, newBookshelf);
    }

    /**
     * Method called by the server when a Common goal is created
     *
     * @param comGoalID ID of the Common goal
     * @param score     Value if the client does the Common goal
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onCommonGoalCreated(Integer comGoalID, Integer score) throws RemoteException {
        getController().onCommonGoalCreated(comGoalID, score);
    }

    /**
     * Method called by the server when a Common goal is completed by the player nickname
     *
     * @param nickname nickname of the player
     * @param newValue Array of Common goal ID and new value of common goal
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onCommonGoalDone(String nickname, int[] newValue) throws RemoteException {
        getController().onCommonGoalDone(nickname, newValue);
    }

    /**
     * Method called by the server when a Personal goal is created
     *
     * @param newValue String that determines the Personal goal from a Json file
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onPersGoalCreated(String newValue) throws RemoteException {
        getController().onPersGoalCreated(newValue);
    }

    /**
     * Method called by the server on a new Turn
     *
     * @param nickname the nickname of the players whose turn is
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onChangeTurn(String nickname) throws RemoteException {
        getController().onChangeTurn(nickname);
    }

    /**
     * Method called by the server when a player wins
     *
     * @param winners is the player that won the game
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onWinner(List<String> winners) throws RemoteException {
        getController().onWinner(winners);
    }

    /**
     * Method called by the server when a game is starting
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onGameIsStarting(ArrayList<String> playersList) throws RemoteException {
        getController().gameStarted(playersList, true);
    }

    /**
     * Method called by the server to ensure the player is online and connected
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void pong() throws RemoteException {

    }

    /**
     * Method used to see if the server is online, if an error occurred it disconnects the client
     */
    private void ping() {
        try {
            server.pong();
        } catch (Exception e) {
            // se si è disconnesso
            timer.cancel();
            server = null;
            getController().disconnectMe();
        }

    }

    /**
     * Method called by the server when a player wants to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void chatToMe(String sender, String message) throws RemoteException {
        getController().chatToMe(sender, message);
    }

    /**
     * Method called by the server to disconnect the player when an error occurred, or the game is finished
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void disconnectMe() throws RemoteException {
        server = null;
        getController().disconnectMe();
    }

    /**
     * Method called by the server to update the player score when a player reconnects
     *
     * @param score The score of the player
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onPlayerScore(int score) throws RemoteException {
        getController().onPlayerScore(score);
    }

    /**
     * Method called by the server when a player completes his bookshelf
     *
     * @throws RemoteException if an error occurred calling the RMI client
     */
    @Override
    public void onBookshelfCompleted() throws RemoteException {
        getController().onBookshelfCompleted();
    }

    @Override
    public void onSavedGame(List<String> savedGames) throws RemoteException {
        getController().onSavedGame(savedGames);
    }
}
