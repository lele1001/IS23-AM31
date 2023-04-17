package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    //private boolean isConnected;
    private final Socket socket;
    private final PrintWriter socketOut;
    private Thread listeningThread;


    public ClientHandlerSocket(Socket socket, Server server, ConnectionControl connectionControl) {
        this.socket = socket;
        this.server = server;
        this.connectionControl = connectionControl;
        playerNumberAsked = false;
        //isConnected = true;
        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void run() {
        nickname = null;
        listeningThread = new Thread(this::listen);
        listeningThread.start();
        //aspetto nickname
        while (nickname == null) {
            Thread.onSpinWait();
        }

        if (!connectionControl.tryAddInQueue(this, nickname)) {
            // c'è già un gioco attivo e non sei dentro
            // invio messaggio e chiudo
            System.out.println("Sending " + nickname + " that game is not available.");
            sendError("Game not available.");
            disconnectPlayer();

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
                if (line != null) {
                    System.out.println("Received: " + line);
                    onMessageReceived(line);
                }
            } catch (IOException e) {
                if ((nickname == null) || (Thread.interrupted()))
                    return;
                System.out.println("Client: " + nickname + " disconnected.");
                connectionControl.changePlayerStatus(nickname);
                //this.disconnectPlayer();
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

    private void send(JsonObject json) {
        socketOut.println(json.toString());
        socketOut.flush();
    }

    private JsonObject generateStandardMessage(String type, String value) {
        JsonObject json = new JsonObject();
        json.addProperty("Type", type);
        json.addProperty("Value", value);
        return json;
    }

    @Override
    public void askPlayerNumber() {
        if (!playerNumberAsked) { // non gliel'ho ancora chiesto: glielo chiedo
            playerNumberAsked = true;
            send(generateStandardMessage("askPlayersNumber", null));
        }
        //return Optional.empty();
    }

    @Override
    public void sendPlayerTurn(String nickname) {
        send(generateStandardMessage("changeTurn", nickname));
    }

    @Override
    public void disconnectPlayer() {
        send(generateStandardMessage("disconnect", null));
        if (listeningThread.isAlive())
            listeningThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Unable to close socket interface.");
        }
    }

    private void onMessageReceived(String json) {
        // switch per parsare i messaggi e chiamare i metodi corretti sul connectioncontrol
        // attenzione ad accettare due tipologie di messaggi: nickname all'inizio se ancora non ce l'ho (nickname==null)
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        try {
            jsonObject = gson.fromJson(json, jsonObject.getClass());
        } catch (Exception e) {
            System.out.println("Error parsing client's response.");
            return;
        }
        if (((jsonObject == null) || (jsonObject.isEmpty()) || !(jsonObject.has("Type")))) {
            System.out.println("Unknown message from client.");
        } else
            switch (jsonObject.get("Type").getAsString()) {
                case "nickname" -> {
                    if (nickname == null) {
                        nickname = jsonObject.get("Value").getAsString();
                        System.out.println(nickname + " is trying to enter the game.");
                    }
                }
                case "playersNumber" -> {
                    if (playerNumberAsked) {
                        server.setAvailablePlayers(Integer.parseInt(jsonObject.get("Value").getAsString()));
                        playerNumberAsked = false;
                    }
                }
                default ->
                    System.out.println("Unknown message from client.");

            }

    }

    // In questi metodi bisogna generare i JSON e inviarli
    @Override
    public void askSelect() {
        send(generateStandardMessage("askSelect", null));
    }

    @Override
    public void askInsert() {
        send(generateStandardMessage("askInsert", null));
    }

    @Override
    public void sendError(String error) {
        send(generateStandardMessage("error", error));
    }

    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        Gson gson = new Gson();
        send(generateStandardMessage("boardChanged", gson.toJson(newBoard)));
    }

    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        JsonObject toSend = generateStandardMessage("comGoalCreated", comGoalID.toString());
        toSend.addProperty("score", score);
        send(toSend);
    }

    @Override
    public void SendPersGoalCreated(String persGoal) {
        send(generateStandardMessage("persGoalCreated", persGoal));
    }

    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        Gson gson = new Gson();
        JsonObject toSend = generateStandardMessage("bookshelfChanged", gson.toJson(newBookshelf));
        toSend.addProperty("nickname", nickname);
        send(toSend);
    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {

    }

    @Override
    public void sendWinner(String winner) {

    }

    @Override
    public void sendGameIsStarting() {
        send(generateStandardMessage("gameStarted", null));
    }

    @Override
    public void sendErrorGameNotAvailable() {
    }
}
