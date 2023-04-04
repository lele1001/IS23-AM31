package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;

public class RMIInterface implements RMI {
    private final Server server;
    private boolean isFirst;
    private final ConnectionControl connectionControl;

    public RMIInterface(Server server, ConnectionControl connectionControl) {
        this.server = server;
        this.connectionControl = connectionControl;
        isFirst = true;
    }

    public void selectCard(String nickname, ArrayList<Integer> positions) {
        connectionControl.selectCard(nickname, positions);
    }

    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        connectionControl.insertCard(nickname, cards, column);
    }

    public boolean login(String nickname) {        //deve prendere anche la classe del client
        synchronized (server) {
            if (server.getAvailablePlayers() == 0) {
                //non disponibile
                return false;
            }
            ClientHandlerRmi clientHandlerRmi = new ClientHandlerRmi(); //deve passargli la classe dell'interfaccia rmi client
            this.connectionControl.addClient(clientHandlerRmi, nickname);
            if ((isFirst) && (server.getAvailablePlayers() == 1)) {
                //chiedo num giocatori e lo setto
                server.setAvailablePlayers(2);
                isFirst = false;
            } else
                server.decrementAvailablePlayers();
        }
        return true;

    }

    public boolean loginExistingGame(String nickname) {
        // chiede al server se c'è una partita con quel nickname
        // se non c'è
        return false;

        //else
    }


}
