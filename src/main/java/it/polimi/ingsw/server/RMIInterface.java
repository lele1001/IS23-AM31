package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIInterface implements RMI {
    private final Server server;
    private ConnectionControl connectionControl;

    public RMIInterface(Server server, ConnectionControl connectionControl) {
        this.server = server;
        this.connectionControl = connectionControl;
    }

    public void setConnectionControl(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname  this client
     * @param positions Tiles selected by the client
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void selectCard(String nickname, ArrayList<Integer> positions) throws RemoteException {
        connectionControl.selectCard(nickname, positions);
    }

    /**
     * Method called by the client only if he is the first connected to the server
     * Check that he is the first and then set the number of players for the game
     *
     * @param client the client that sends the request
     * @param number number of players in the game
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void setPlayerNumber(String client, int number, String gameName) throws RemoteException {
        if ((number < 2) || (number > 4)) {
            connectionControl.SendError("Players' number not correct.", client);
            //connectionControl.askPlayerNumber(client);
        } else {
            server.setAvailablePlayers(number, gameName);
        }
    }

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server sends to all the other clients the message
     *
     * @param nickname this client
     * @param message  String to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void chatToAll(String nickname, String message) throws RemoteException {
        connectionControl.chatToAll(nickname, message);
    }

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server sends to all the other clients the message
     *
     * @param sender   the player that sends the message
     * @param receiver the player that has to receive the message
     * @param message  String to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws RemoteException {
        connectionControl.chatToPlayer(sender, receiver, message);
    }

    /**
     * Used only by the client to check if the server in Online
     *
     * @throws RemoteException If the client cannot connect to the server
     */
    @Override
    public void pong() throws RemoteException {

    }

    @Override
    public void setSavedGames(boolean wantToSave, String gameName) throws RemoteException {
        server.setSavedGame(wantToSave, gameName);
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname nickname of the client
     * @param cards    The Item cards selected by the client
     * @param column   The column where the client wants to put the Item cards
     */
    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException {
        connectionControl.insertCard(nickname, cards, column);
    }

    /**
     * Waits for the client's nickname and RMI interface, then, tries to insert it into the game.
     * If the game is not available, sends the error and disconnects it.
     *
     * @param nickname nickname of the client
     * @param client   The client interface
     */
    @Override
    public boolean login(String nickname, RMIClientConnection client) {        //deve prendere anche la classe del client
        ClientHandlerRmi clientHandlerRmi = new ClientHandlerRmi(connectionControl, nickname, client); //deve passargli la classe dell'interfaccia rmi client
        if (!connectionControl.tryAddInQueue(clientHandlerRmi, nickname)) {
            clientHandlerRmi.sendError("Game not available.");
            clientHandlerRmi.disconnectPlayer();
            return false;
        }
        return true;
    }
}
