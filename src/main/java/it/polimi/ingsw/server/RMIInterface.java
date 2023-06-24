package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIInterface implements RMI {
    private ConnectionControl connectionControl;

    /**
     * Set up the RMI interface
     *
     * @param connectionControl the connection control of the game
     */
    public RMIInterface(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }

    /**
     * Set up the connection control of the game
     *
     * @param connectionControl the connection control of the game
     */
    public void setConnectionControl(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname  of this client
     * @param positions of the Tiles selected by the client
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
     * @param client that sends the request
     * @param number of players in the game
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void setPlayerNumber(String client, int number, String gameName) throws RemoteException {
        connectionControl.setAvailablePlayers(client, number, gameName);
    }

    /**
     * Method called from the client that pass to the server the chat message for all connected players of the game
     * The server sends to all the other clients the message
     *
     * @param nickname of this client
     * @param message  to send to all the connected players
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
     * @param sender   is the player that sends the message
     * @param receiver is the player that has to receive the message
     * @param message  to send to all the connected players
     * @throws RemoteException if an error occurred calling the server RMI
     */
    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws RemoteException {
        connectionControl.chatToPlayer(sender, receiver, message);
    }

    /**
     * Used only by the client to check if the server in Online
     *
     * @throws RemoteException if the client cannot connect to the server
     */
    @Override
    public void pong() throws RemoteException {
    }

    /**
     * Called when the player wants to resume a saved game.
     *
     * @param wantToSave: true if he wants to resume a game.
     * @param gameName:   the name of the game to be resumed.
     */
    @Override
    public void setSavedGames(boolean wantToSave, String gameName) throws RemoteException {
        connectionControl.setSavedGame(wantToSave, gameName);
    }

    /**
     * Method called from the client that pass to the server the position of the Tiles selected by the client
     * Send a request for the checking procedure in the gameController and update the Model if the controls are successfully done
     *
     * @param nickname of the client
     * @param cards    selected by the client that will be inserted in the bookshelf
     * @param column   where the client wants to put the selected ItemCards
     */
    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws RemoteException {
        connectionControl.insertCard(nickname, cards, column);
    }

    /**
     * Waits for the client's nickname and RMI interface, then, tries to insert it into the game.
     * If the game is not available, sends the error and disconnects it.
     *
     * @param nickname of the client
     * @param client   is the client interface
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
