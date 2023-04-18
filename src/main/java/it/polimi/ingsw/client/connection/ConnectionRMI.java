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

public class ConnectionRMI extends ConnectionClient implements RMIClientConnection {
    private RMI server;

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
     * @throws IOException       throws if it has an error with input or output
     * @throws NotBoundException throws if it has an error with the connection
     */
    @Override
    public void startConnection() throws IOException, NotBoundException {
        boolean i = true;
        System.out.println("locateRegistry");
        Registry registry = LocateRegistry.getRegistry(getAddress(), getPort());
        System.out.println("locateLookup");
        server = (RMI) registry.lookup("MyShelfieServer");
        System.out.println("Connection established.");
        System.out.println("Sending nickname...");
        i = server.login(getController().getMyNickname(), this);
        if (i) {
            System.out.println("login done");
        }
    }

    @Override
    public void selectCard(String nickname, ArrayList<Integer> cardsSelected) throws RemoteException {
        server.selectCard(nickname, cardsSelected);
    }

    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException {
        server.insertCard(nickname, cards, column);
    }

    @Override
    public void chatToAll(String nickname, String message) throws Exception {
        server.chatToAll(nickname, message);
    }

    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws Exception {
        server.chatToPlayer(sender, receiver, message);
    }

    @Override
    public void setPlayersNumber(int players) throws Exception {
        server.setPlayerNumber(players);
    }

    @Override
    public void onPlayerNumber() throws RemoteException {
        getController().onPlayerNumber();
        /*Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    server.setPlayerNumber(2);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.out.println("settato a 2");
            }
        };
        t.start();
        System.out.println("chiesto");*/
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
    public void onWinner(String winner) throws RemoteException {
        System.out.println("winner is" + winner);
    }

    @Override
    public void onGameIsStarting() throws RemoteException {
        System.out.println("gameStarting");
        getController().setGameStarted(true);
    }

    @Override
    public void onErrorGameNotAvailable() throws RemoteException {
        System.out.println("GameNotAvailable");
    }

    @Override
    public void ping() throws RemoteException {
        System.out.println("Ping");
    }

    @Override
    public void chatToMe(String sender, String message) throws RemoteException {
        getController().chatToMe(sender, message);
    }
}
