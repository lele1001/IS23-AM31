package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandlerSocket extends ClientHandler implements Runnable {
    private final Server server;
    private boolean playerNumberAsked;
    private boolean isConnected;
    private final Socket socket;
    PrintWriter socketOut;


    public ClientHandlerSocket(Socket socket, Server server, ConnectionControl connectionControl) {
        this.socket = socket;
        this.server = server;
        this.connectionControl = connectionControl;
        playerNumberAsked = false;
        isConnected = true;
        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void run() {
        nickname = null;
        new Thread(this::listen).start();
        //aspetto nickname
        while (nickname == null) {
            Thread.onSpinWait();

        }

        if (!connectionControl.tryAddInQueue(this, nickname)) {
            // c'è già un gioco attivo e non sei dentro
            // invio messaggio e chiudo
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket of client: " + nickname);
            }
            return;
        }

    }


    public void listen() {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Unable to read from client: " + nickname);
            return;
        }

        // Accetta messaggi dal client e, eventualmente, si accorge della sua disconnessione.
        while (true) {
            try {
                String line = in.readLine();
                //System.out.println("Received: " + line);
                onMessageReceived(line);
            } catch (IOException e) {
                System.out.println("Client: " + nickname + " disconnected.");
                connectionControl.changePlayerStatus(nickname);
                break;
            }
        }

        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing socket of client: " + nickname);
        }
    }


    @Override
    public void askPlayerNumber() {
        if (!playerNumberAsked) {// non gliel'ho ancora chiesto: glielo chiedo
            playerNumberAsked = true;
            socketOut.println("Please, give me player number.");
            socketOut.flush();
        }
        //return Optional.empty();
    }

    @Override
    public void sendPlayerTurn(String nickname) {

    }

    @Override
    public void disconnectPlayer() {

    }

    private void onMessageReceived(String JSONMessage) {
        // switch per parsare i messaggi e chiamare i metodi corretti sul connectionControl
        // attenzione ad accettare due tipologie di messaggi: nickname all'inizio se ancora non ce l'ho (nickname==null)
        if (JSONMessage.contains("nickname")) {
            nickname = JSONMessage;
            System.out.println(nickname + " is trying to enter the game.");
        }

        //e numGiocatori perché glie l'ho chiesto (playerNumberAsked==true)
/*        server.setAvailablePlayers(2);
        playerNumberAsked = false;*/

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
    public void SendPersGoalCreated(String persGoal) {

    }

    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {

    }

    @Override
    public void sendWinner(String winner) {

    }

    @Override
    public void sendGameIsStarting() {

    }

    @Override
    public void sendErrorGameNotAvailable() {

    }
}
