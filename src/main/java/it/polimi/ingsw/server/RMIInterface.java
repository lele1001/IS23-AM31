package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;

public class RMIInterface implements RMI {
    //private final Server server;
    private ConnectionControl connectionControl;

    public RMIInterface(ConnectionControl connectionControl) {
        //this.server = server;
        this.connectionControl = connectionControl;
    }

    public void setConnectionControl (ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }

    public void selectCard(String nickname, ArrayList<Integer> positions) {
        connectionControl.selectCard(nickname, positions);
    }

    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        connectionControl.insertCard(nickname, cards, column);
    }

    public boolean login(String nickname) {        //deve prendere anche la classe del client
        ClientHandlerRmi clientHandlerRmi = new ClientHandlerRmi(connectionControl, nickname); //deve passargli la classe dell'interfaccia rmi client
        if (!connectionControl.tryAddInQueue(clientHandlerRmi, nickname)) {
            // c'è già un gioco attivo e non sei dentro
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
