package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandlerSocket extends ClientHandler implements Runnable {
    private final Server server;
    private final ConnectionControl connectionControl;  // per inviare messaggi ricevuti
    private String nickname;
    private boolean isFirst;
    private boolean stop;
    private final Socket socket;

    public ClientHandlerSocket(Socket socket, Server server, ConnectionControl connectionControl) {
        this.socket = socket;
        this.server = server;
        this.connectionControl = connectionControl;
        isFirst = true;
        stop = false;
    }


    public void run() {
        //chiedo nickname

        if (!initialize()) {
            //chiudo la connessione
            return;
        }

        //accettare messaggi in continuazione
        try {
            Scanner in = new Scanner(socket.getInputStream());
            while (!stop) {
                String line = in.nextLine();
                onMessageReceived(line);
            }
            // Chiudo gli stream e il socket
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public void stop() {
        stop = true;
    }

    private boolean initialize() {
        synchronized (server) {  //mila: il primo player blocca il serve, quindi non libererà serve finché non imposta il numero di giocatori, solo allora potranno uscire gli altri
            if (server.getAvailablePlayers() == 0) {
                //non disponibile
                sendError("Game not available.");
                return false;
            }
            if ((isFirst) && (server.getAvailablePlayers() == 1)) {
                // è il primo: chiedo il numero di giocatori

            } else
                server.decrementAvailablePlayers();
        }
        return true;
    }

    private void onMessageReceived(String JSONMessage) {
        // switch per parsare i messaggi e chiamare i metodi corretti sul connectioncontrol
        // attenzione ad accettare due tipologie di messaggi: nickname all'inizio se ancora non ce l'ho (nickname==null)
        connectionControl.addClient(this, "Pippo"); // nickname da prendere dal client

        //e numgiocatori perché gliel'ho chiesto (isFirst==true)
        server.setAvailablePlayers(2);
        isFirst = false;

    }

    // In questi metodi bisogna generare i JSON e inviarli
    @Override
    public void askSelect() {
        // genera il messaggio e lo invia
    }

    @Override
    public void askInsert() {

    }

    @Override
    public void sendError(String error) {

    }

    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {

    }

    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {

    }

    @Override
    public void SendPersGoalCreated(String nickname, String persGoal) {

    }

    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {

    }

    @Override
    public void SendDetailsEndGame(String winner, int score) {

    }
}
