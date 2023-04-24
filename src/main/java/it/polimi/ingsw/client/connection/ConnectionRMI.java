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
    Timer timer=new Timer();

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
    public void startConnection() throws IOException, NotBoundException,NullPointerException {
        boolean i;
        System.out.println("locateRegistry");
        Registry registry = LocateRegistry.getRegistry(getAddress(), getPort());
        System.out.println("locateLookup");
        server = (RMI) registry.lookup("MyShelfieServer");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000);
        System.out.println("Connection established.");
        System.out.println("Sending nickname...");
        i = server.login(getController().getMyNickname(), this);
        if (i) {
            System.out.println("login done");
        }
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     *
     * @param nickname      this client
     * @param cardsSelected Tiles selected by the client
     * @throws RemoteException if an error occurred calling the server ( Socket or RMI )
     */
    @Override
    public void selectCard(String nickname, ArrayList<Integer> cardsSelected) throws RemoteException,NullPointerException {
        server.selectCard(nickname, cardsSelected);
    }

    /**
     * Method called from the client that pass to the server the Tiles inserted by the client and in which column he wants to put them
     *
     * @param nickname this client
     * @param cards    Tiles selected by the client in order
     * @param column   column where to put the Tiles
     * @throws RemoteException if an error occurred calling the server ( Socket or RMI )
     */
    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException,NullPointerException {
        server.insertCard(nickname, cards, column);
    }

    /**
     * Method called from the client that pass to the server the chat message for all connected player of the game
     *
     * @param nickname this client
     * @param message  String to send to all the connected players
     * @throws RemoteException if an error occurred calling the server ( Socket or RMI )
     */
    @Override
    public void chatToAll(String nickname, String message) throws RemoteException,NullPointerException {
        server.chatToAll(nickname, message);
    }

    /**
     * Method called from the client that pass to the server the chat message for the receiver
     *
     * @param sender   this client
     * @param receiver player that receive the message
     * @param message  String to send to the receiver
     * @throws RemoteException if an error occurred calling the server ( Socket or RMI )
     */
    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws RemoteException,NullPointerException {
        server.chatToPlayer(sender, receiver, message);
    }

    /**
     * Method called by the client only if he is the first connected to the server
     *
     * @param players number of players in the game
     * @throws RemoteException if an error occurred calling the RMI server
     */
    @Override
    public void setPlayersNumber(int players) throws RemoteException,NullPointerException {
        server.setPlayerNumber(getController().getMyNickname(),players);
    }

    @Override
    public void onPlayerNumber() throws RemoteException {
        getController().onPlayerNumber();
    }

    @Override
    public void onError(String error) throws RemoteException {
        getController().onError(error);
    }

    @Override
    public void onSelect() throws RemoteException {
        getController().onSelect();
    }

    @Override
    public void onInsert() throws RemoteException {
        getController().onInsert();
    }

    @Override
    public void onBoardChanged(ItemCard[][] newBoard) throws RemoteException {
        getController().onBoardChanged(newBoard);
    }

    @Override
    public void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) throws RemoteException {
        getController().onBookshelfChanged(nickname, newBookshelf);
    }

    @Override
    public void onCommonGoalCreated(Integer comGoalID, Integer score) throws RemoteException {
        getController().onCommonGoalCreated(comGoalID, score);
    }

    @Override
    public void onCommonGoalDone(String nickname, int[] newValue) throws RemoteException {
        getController().onCommonGoalDone(nickname, newValue);
    }

    @Override
    public void onPersGoalCreated(String newValue) throws RemoteException {
        getController().onPersGoalCreated(newValue);
    }

    @Override
    public void onChangeTurn(String nickname) throws RemoteException {
        getController().onChangeTurn(nickname);
    }

    @Override
    public void onWinner(List<String> winners) throws RemoteException {
        if (winners.size() == 1) {
            System.out.println("winner is" + winners.get(0));
        } else {
            System.out.println("Parity: winners are " + winners);
        }
    }

    @Override
    public void onGameIsStarting(ArrayList<String> playersList) throws RemoteException {
        getController().gameStarted(playersList,true);
    }

    @Override
    public void onErrorGameNotAvailable() throws RemoteException {
        System.out.println("GameNotAvailable");
    }

    @Override
    public void pong() throws RemoteException {

    }
    private void ping() {
        try {
            server.pong();
        } catch (Exception e) {
            // se si è disconnesso
            timer.cancel();
            server=null;
            getController().disconnectMe();
        }

    }

    @Override
    public void chatToMe(String sender, String message) throws RemoteException {
        getController().chatToMe(sender, message);
    }

    @Override
    public void disconnectMe() throws RemoteException {
        server=null;
        getController().disconnectMe();
    }

    @Override
    public void onPlayerScore(int score) throws RemoteException {
        getController().onPlayerScore(score);
    }

    @Override
    public void onBookshelfCompleted() throws RemoteException {
        getController().onBookshelfCompleted();
    }
}
