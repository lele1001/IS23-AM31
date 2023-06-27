package it.polimi.ingsw.server.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Extension of Client Handler for Socket TCP connections
 */
public class ClientHandlerSocket extends ClientHandler implements Runnable {
    private boolean playerNumberAsked;
    private volatile boolean isConnected;
    private final Socket socket;
    private final PrintWriter socketOut;
    private Thread listeningThread;
    private Timer ping;
    private volatile Boolean savedGame;

    /**
     * Create the Client handler RMI every time a client connects to the server
     *
     * @param connectionControl The defined connection control for the game
     * @param socket            The connection of the client socket
     */
    public ClientHandlerSocket(Socket socket, ConnectionControl connectionControl) {
        this.socket = socket;
        this.connectionControl = connectionControl;
        playerNumberAsked = false;
        isConnected = true;

        try {
            socketOut = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        savedGame = null;
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

        new Thread(this::ping).start();

        ping = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                isConnected = false;
            }
        }, 10000);

        if (!connectionControl.tryAddInQueue(this, nickname)) {
            // Game is not available.
            System.out.println("Sending " + nickname + " that game is not available.");
            sendError("Game not available.");
            disconnectPlayer();
        }
    }

    /**
     * A private method that sends a ping to the client every 5 seconds to check if it is still online.
     */
    private void ping() {
        // Always sends a ping to the client.
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                send(generateStandardMessage("ping", null));
            }
        }, 0, 5000);
    }

    /**
     * The infinitive loop that listens from client's messages, until it is online.
     */
    private void listen() {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Unable to read from client: " + nickname);
            return;
        }

        // Always listens from the client's message and notifies the server of its disconnection.
        while (isConnected) {
            try {
                String line = in.readLine();
                if (line != null) {
                    //System.out.println("Received: " + line);
                    onMessageReceived(line);
                }
            } catch (IOException e) {
                if ((nickname == null) || (Thread.interrupted())) {
                    return;
                }
                isConnected = false;
            }
        }


        System.out.println("Client: " + nickname + " disconnected.");
        connectionControl.changePlayerStatus(nickname, true);

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
     * @param json to be converted in String and to be sent
     */
    private void send(JsonObject json) {
        socketOut.println(json.toString());
        socketOut.flush();
    }

    /**
     * A private method used to prevent repeated code:
     * it generates standard messages used during the communication with the client.
     * It doesn't call directly the “send”
     * method because sometimes the game needs to add other fields in the jsonObject before sending it.
     *
     * @param type  of the message to be sent
     * @param value of the message to be sent
     * @return the jsonObject to be sent
     */
    private JsonObject generateStandardMessage(String type, String value) {
        JsonObject json = new JsonObject();
        json.addProperty("Type", type);

        if (value != null) {
            json.addProperty("Value", value);
        }

        return json;
    }

    /**
     * Asks players' number to the client (if it hasn't done it already).
     */
    @Override
    public void askPlayerNumber(List<String> notAvailableNames) {
        if (!playerNumberAsked) {
            playerNumberAsked = true;
            send(generateStandardMessage("askPlayersNumber", notAvailableNames.toString()));
        }
    }

    /**
     * Sends an update of player's turn.
     *
     * @param nickname of the client is going to play
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

            if (listeningThread.isAlive()) {
                listeningThread.interrupt();
            }

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
     * @param json string to be parsed
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
        } else {
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
                            connectionControl.setAvailablePlayers(nickname, Integer.parseInt(jsonObject.get("Value").getAsString()), jsonObject.get("gameName").getAsString());
                            playerNumberAsked = false;
                        }
                    }
                    case "savedGameFound" -> {
                        if (savedGame) {
                            connectionControl.setSavedGame(jsonObject.get("Value").getAsBoolean(), jsonObject.get("gameName").getAsString());
                        }
                    }
                    case "selectCards" ->
                            connectionControl.selectCard(nickname, new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), Integer[].class))));
                    case "insertCards" ->
                            connectionControl.insertCard(nickname, new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[].class))), jsonObject.get("column").getAsInt());
                    case "chatToAll" -> connectionControl.chatToAll(nickname, jsonObject.get("Value").getAsString());
                    case "chatToPlayer" ->
                            connectionControl.chatToPlayer(nickname, jsonObject.get("receiver").getAsString(), jsonObject.get("Value").getAsString());
                    case "ping" -> {
                        ping.cancel();
                        ping = new Timer();
                        ping.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                isConnected = false;
                            }
                        }, 10000);
                    }
                    default -> System.out.println("Unknown message from client.");
                }
            } catch (Exception e) {
                System.out.println("Error de-parsing client " + nickname + "'s message.");
            }
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
     * @param error to be sent
     */
    @Override
    public void sendError(String error) {
        send(generateStandardMessage("error", error));
    }

    /**
     * Generates a message to send board's update.
     *
     * @param newBoard is the updated board
     */
    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        Gson gson = new Gson();
        send(generateStandardMessage("boardChanged", gson.toJson(newBoard)));
    }

    /**
     * Sends board's update to the client.
     *
     * @param tilesToRemove: the array of board's positions to remove tiles from.
     */
    @Override
    public void sendBoardRenewed(Integer[] tilesToRemove) {
        send(generateStandardMessage("boardRenewed", new Gson().toJson(tilesToRemove)));
    }

    /**
     * Called to inform the client that a new commonGoal has been created (usually at the beginning of the game).
     *
     * @param comGoalID that has been created
     * @param score     is the maximum score that can be reached on the commonGoal
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
     * @param persGoal indicates the number of the PersonalGoal assigned
     */
    @Override
    public void SendPersGoalCreated(String persGoal) {
        send(generateStandardMessage("persGoalCreated", persGoal));
    }

    /**
     * Generates a message to send the bookshelf's update.
     *
     * @param nickname     of the player the bookshelf is referred to
     * @param newBookshelf is the updated bookshelf
     */
    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        Gson gson = new Gson();
        JsonObject toSend = generateStandardMessage("bookshelfChanged", gson.toJson(newBookshelf));
        toSend.addProperty("nickname", nickname);
        send(toSend);
    }

    /**
     * Sends bookshelf's update to the client.
     *
     * @param nickname:   the player whose bookshelf has changed.
     * @param tilesToAdd: the ordered array of tiles to add in nickname's bookshelf.
     * @param column:     the column of the bookshelf to add tiles into.
     */
    @Override
    public void sendBookshelfRenewed(String nickname, ItemCard[] tilesToAdd, int column) {
        JsonObject toSend = generateStandardMessage("bookshelfRenewed", new Gson().toJson(tilesToAdd));
        toSend.addProperty("column", column);
        toSend.addProperty("player", nickname);
        send(toSend);
    }

    /**
     * Called to inform that someone has reached a commonGoal.
     *
     * @param source  is the nickname of the client that reached it
     * @param details are the ID of the commonGoal, and the maximum score
     */
    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        JsonObject toSend = generateStandardMessage("commonGoalDone", Arrays.toString(details));
        toSend.addProperty("source", source);
        send(toSend);
    }

    /**
     * The server calls the client's method when a player wins
     *
     * @param finalScores contains all the players' scores.
     */
    @Override
    public void sendFinalScores(LinkedHashMap<String, Integer> finalScores) {
        Gson gson = new Gson();
        JsonObject toSend = generateStandardMessage("finalScores", gson.toJson(finalScores.keySet()));
        toSend.addProperty("scores", gson.toJson(finalScores.values()));
        send(toSend);
    }

    /**
     * Called when there are too many absents for the game: it needs to be interrupted waiting for them.
     */
    @Override
    public void sendGameInterrupted() {
        send(generateStandardMessage("gameInterrupted", null));
    }

    /**
     * Called when someone wants to send a message to this client.
     *
     * @param sender  of the message
     * @param message sent to somebody else
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
        send(generateStandardMessage("gameStarted", playersList.toString()));
    }

    /**
     * Generates a message to inform the client that game is not available.
     *
     * @param score of the player
     */
    @Override
    public void sendPlayerScore(int score) {
        send(generateStandardMessage("player_score", String.valueOf(score)));
    }

    /**
     * Called when a player completes his bookshelf
     *
     * @param nickname of the player that has completed the bookshelf.
     */
    @Override
    public void sendBookshelfCompleted(String nickname) {
        send(generateStandardMessage("bookshelf_completed", nickname));
    }

    /**
     * Asks the client if he wants to resume one of the game he's into.
     *
     * @param savedGames: the list of saved games' names the client is into.
     */
    @Override
    public void askSavedGame(List<String> savedGames) {
        send(generateStandardMessage("savedGameFound", savedGames.toString()));
        savedGame = true;
    }
}
