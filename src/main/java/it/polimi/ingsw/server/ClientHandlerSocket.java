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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHandlerSocket extends ClientHandler implements Runnable {
    private final Server server;
    private boolean playerNumberAsked;
    private boolean isConnected;
    private final Socket socket;
    private final PrintWriter socketOut;
    private Thread listeningThread;


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

    /**
     * Waits for the client's nickname and, then, tries to insert it into the game.
     * If the game is not available, sends the error and disconnects it.
     */
    public void run() {
        nickname = null;
        listeningThread = new Thread(this::listen);
        listeningThread.start();
        // Waiting for the nickname to be set.
        while (nickname == null) {
            Thread.onSpinWait();
        }

        if (!connectionControl.tryAddInQueue(this, nickname)) {
            // Game is not available.
            System.out.println("Sending " + nickname + " that game is not available.");
            sendError("Game not available.");
            disconnectPlayer();

        }

    }

    /**
     * The infinitive loop that listens from client's messages, until it is online.
     */
    public void listen() {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Unable to read from client: " + nickname);
            return;
        }

        // Always listens from the client's message and notifies the server of its disconnection.
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
                isConnected = false;
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

    /**
     * A private method used to send to the client the generated json.
     *
     * @param json to be converted in String and to be sent.
     */
    private void send(JsonObject json) {
        socketOut.println(json.toString());
        socketOut.flush();
    }

    /**
     * A private method used to prevent repeated code:
     * it generates standard messages used during the communication with the client.
     *
     * @param type  of the message to be sent.
     * @param value of the message to be sent.
     * @return the jsonObject to be sent.
     * It doesn't call directly the “send”
     * method because sometimes the game needs to add other fields in the jsonObject before sending it.
     */
    private JsonObject generateStandardMessage(String type, String value) {
        JsonObject json = new JsonObject();
        json.addProperty("Type", type);
        if (value != null)
            json.addProperty("Value", value);
        return json;
    }

    /**
     * Asks players' number to the client (if it hasn't done it already).
     */
    @Override
    public void askPlayerNumber() {
        if (!playerNumberAsked) {
            playerNumberAsked = true;
            send(generateStandardMessage("askPlayersNumber", null));
        }
    }

    /**
     * Sends an update of player's turn.
     *
     * @param nickname of the client is going to play.
     */
    @Override
    public void sendPlayerTurn(String nickname) {
        send(generateStandardMessage("changeTurn", nickname));
    }

    /**
     * Disconnects the player, due to server's error or game not available.
     */
    @Override
    public void disconnectPlayer() {
        if (isConnected) {
            isConnected = false;
            send(generateStandardMessage("disconnect", null));
            if (listeningThread.isAlive())
                listeningThread.interrupt();
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Unable to close socket interface.");
            }
        }
    }

    /**
     * Parses json messages received from the client.
     *
     * @param json string to be parsed.
     */
    private void onMessageReceived(String json) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        try {
            jsonObject = gson.fromJson(json, jsonObject.getClass());
        } catch (Exception e) {
            System.out.println("Unknown message from client.");
            return;
        }
        if (((jsonObject == null) || (jsonObject.isEmpty()) || !(jsonObject.has("Type")))) {
            System.out.println("Unknown message from client.");
        } else
            try {
                switch (jsonObject.get("Type").getAsString()) {
                    case "nickname" -> {
                        if (nickname == null) {
                            nickname = jsonObject.get("Value").getAsString();
                            System.out.println(nickname + " is trying to enter the game.");
                        }
                    }
                    case "playersNumber" -> {
                        if (playerNumberAsked) {
                            int given = Integer.parseInt(jsonObject.get("Value").getAsString());
                            if ((given < 2) || (given > 4)) {
                                sendError("Players' number not correct.");
                                playerNumberAsked = false;
                                askPlayerNumber();
                            } else {
                                server.setAvailablePlayers(given);
                                playerNumberAsked = false;
                            }
                        }
                    }
                    case "selectCards" ->
                            connectionControl.selectCard(nickname, new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), Integer[].class))));

                    case "insertCards" ->
                            connectionControl.insertCard(nickname, new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[].class))), jsonObject.get("column").getAsInt());

                    case "chatToAll" -> connectionControl.chatToAll(nickname, jsonObject.get("Value").getAsString());

                    case "chatToPlayer" ->
                            connectionControl.chatToPlayer(nickname, jsonObject.get("receiver").getAsString(), jsonObject.get("Value").getAsString());

                    default -> System.out.println("Unknown message from client.");
                }
            } catch (Exception e) {
                System.out.println("Error de-parsing client " + nickname + "'s message.");
            }

    }

    /**
     * Generates a message to ask the client to select cards from the board.
     */
    @Override
    public void askSelect() {
        send(generateStandardMessage("askSelect", null));
    }

    /**
     * Generates a message to ask the client to insert cards in his bookshelf.
     */
    @Override
    public void askInsert() {
        send(generateStandardMessage("askInsert", null));
    }

    /**
     * Generates an error message.
     *
     * @param error to be sent.
     */
    @Override
    public void sendError(String error) {
        send(generateStandardMessage("error", error));
    }

    /**
     * Generates a message to send board's update.
     *
     * @param newBoard: the updated board.
     */
    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        Gson gson = new Gson();
        send(generateStandardMessage("boardChanged", gson.toJson(newBoard)));
    }

    /**
     * Called to inform the client that a new commonGoal has been created (usually at the beginning of the game).
     *
     * @param comGoalID that has been created.
     * @param score     the maximum score that can be reached on the commonGoal.
     */
    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        JsonObject toSend = generateStandardMessage("comGoalCreated", comGoalID.toString());
        toSend.addProperty("score", score);
        send(toSend);
    }

    /**
     * Called to notify to the client his new PersonalGoal.
     *
     * @param persGoal the string that indicates the number of the card assigned.
     */
    @Override
    public void SendPersGoalCreated(String persGoal) {
        send(generateStandardMessage("persGoalCreated", persGoal));
    }

    /**
     * Generates a message to send the bookshelf's update.
     *
     * @param nickname     of the player the bookshelf is referred to.
     * @param newBookshelf the updated bookshelf.
     */
    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        Gson gson = new Gson();
        JsonObject toSend = generateStandardMessage("bookshelfChanged", gson.toJson(newBookshelf));
        toSend.addProperty("nickname", nickname);
        send(toSend);
    }

    /**
     * Called to inform that someone has reached a commonGoal.
     *
     * @param source  the nickname of the client that reached it.
     * @param details the ID of the commonGoal, and the score remained.
     */
    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        Gson gson = new Gson();
        JsonObject toSend = generateStandardMessage("commonGoalDone", gson.toJson(details));
        toSend.addProperty("source", source);
        send(toSend);
    }

    /**
     * It is the end of the game: there's a winner!
     *
     * @param winners: winners' nickname.
     */
    @Override
    public void sendWinner(List<String> winners) {
        Gson gson = new Gson();
        send(generateStandardMessage("winner", gson.toJson(winners.toArray())));
    }

    /**
     * Called when someone wants to send a message to this client.
     *
     * @param sender   the nickname of the client sender.
     * @param message: the message.
     */
    @Override
    public void chatToMe(String sender, String message) {
        JsonObject toSend = generateStandardMessage("chatToMe", message);
        toSend.addProperty("sender", sender);
        send(toSend);
    }

    /**
     * Generates a message to inform the client that the game is starting.
     */
    @Override
    public void sendGameIsStarting(ArrayList<String> playersList) {
        Gson gson = new Gson();
        send(generateStandardMessage("gameStarted", gson.toJson(playersList)));
    }

    /**
     * Generates a message to inform the client that game is not available.
     */
    @Override
    public void sendPlayerScore(int score) {
        send(generateStandardMessage("player_score", String.valueOf(score)));
    }

    @Override
    public void sendBookshelfCompleted() {
        send(generateStandardMessage("bookshelf_completed", null));
    }
}
