package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIInterface  implements RMI {
    private final Server server;
    private ConnectionControl connectionControl;

    public RMIInterface(Server server,ConnectionControl connectionControl)  {
        this.server = server;
        this.connectionControl = connectionControl;
    }

    public void setConnectionControl(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }
    @Override
    public void selectCard(String nickname, ArrayList<Integer> positions) {
        connectionControl.selectCard(nickname, positions);
    }

    @Override
    public void setPlayerNumber(int number) throws RemoteException {
        System.out.println("Settato player number a "+ number);
        server.setAvailablePlayers(number);
    }

    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        connectionControl.insertCard(nickname, cards, column);
    }

    @Override
    public boolean login(String nickname, RMIClientConnection client) {        //deve prendere anche la classe del client
        ClientHandlerRmi clientHandlerRmi = new ClientHandlerRmi(connectionControl, nickname, client); //deve passargli la classe dell'interfaccia rmi client
        if (!connectionControl.tryAddInQueue(clientHandlerRmi, nickname)) {
            // c'è già un gioco attivo e non sei dentrosd
            return false;
        }
        return true;
    }

/*    public boolean loginExistingGame(String nickname) {
        // chiede al server se c'è una partita con quel nickname
        // se non c'è
        return false;

        //else
    }*/


}
