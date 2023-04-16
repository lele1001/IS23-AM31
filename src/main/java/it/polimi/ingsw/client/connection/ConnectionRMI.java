package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.RMI;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionRMI extends ConnectionClient implements RMIClientConnection {
    private RMI server;

    /**
     * Initialize the RMI connection to the server
     *
     * @param controller ClientController on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     */
    public ConnectionRMI(ClientController controller, String address, int port) throws RemoteException{
        super(controller, address, port);
    }

    /**
     * @throws IOException       throws if it has an error with input or output
     * @throws NotBoundException throws if it has an error with the connection
     */
    @Override
    public void startConnection() throws IOException, NotBoundException {
        boolean i = true;
        System.out.println("locateregistry");
        Registry registry = LocateRegistry.getRegistry(getAddress(), getPort());
        System.out.println("locatelookup");
        server = (RMI) registry.lookup("MyShelfieServer");
        //i=server.login(getController().getMyNickname());
        if (i) {
            System.out.println("login done");
        }
    }

    @Override
    public void onPlayerNumber() throws RemoteException {

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

    }

    @Override
    public void onGameIsStarting() throws RemoteException {

    }

    @Override
    public void onErrorGameNotAvailable() throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }
}
