package it.polimi.ingsw.client.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 * Implementation of Clients Socket TCP connection
 */
public class ConnectionSocket extends ConnectionClient {
    private PrintWriter socketOut = null;
    private BufferedReader in;
    private Socket socket;
    private volatile boolean isConnected;
    private Timer ping;

    /**
     * Initialize the Socket connection to the server
     *
     * @param controller ClientController, on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     * @throws IOException if an error occurred during connection
     */
    public ConnectionSocket(ClientController controller, String address, int port) throws IOException {
        super(controller, address, port);
    }

    /**
     * Starts socket's connection, opening the socket and the streams.
     *
     * @throws Exception if something has gone wrong.
     */
    @Override
    public void startConnection() throws Exception {
        try {
            socket = new Socket(address, port);
            socketOut = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            throw new Exception("Error establishing socket connection.");
        }
        isConnected = true;
        System.out.println("Connection established.");
        System.out.println("Sending nickname...");
        send(generateStandardMessage("nickname", getController().getMyNickname()));
        new Thread(this::listen).start();
        new Thread(this::ping).start();
        ping = new Timer();
        ping.schedule(new TimerTask() {
            @Override
            public void run() {
                isConnected = false;
            }
        }, 10000);
    }

    /**
     * A private method that sends a ping to the server every 5 seconds to check if it is still online.
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
     * Called when the client wants to select cards from the board.
     *
     * @param nickname      this client
     * @param cardsSelected Tiles selected by the client
     */
    @Override
    public void selectCard(String nickname, ArrayList<Integer> cardsSelected) {
        send(generateStandardMessage("selectCards", cardsSelected.toString()));
    }

    /**
     * Called when the client wants to insert cards in his bookshelf.
     *
     * @param nickname this client
     * @param cards    Ordered tiles selected by the client
     * @param column   column to put Tiles into
     */
    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        Gson gson = new Gson();
        JsonObject toSend = generateStandardMessage("insertCards", gson.toJson(cards.toArray()));
        toSend.addProperty("column", column);
        send(toSend);
    }

    /**
     * Called to send a message via chat to all the players.
     *
     * @param nickname this client
     * @param message  String to send to all the connected players
     */
    @Override
    public void chatToAll(String nickname, String message) {
        send(generateStandardMessage("chatToAll", message));
    }

    /**
     * Called to send a message via chat to a specific player.
     *
     * @param sender   this client
     * @param receiver the client receiver of the message.
     * @param message  String to be sent as a message
     */
    @Override
    public void chatToPlayer(String sender, String receiver, String message) {
        JsonObject toSend = generateStandardMessage("chatToPlayer", message);
        toSend.addProperty("receiver", receiver);
        send(toSend);
    }

    /**
     * Called when the client wants to set players' number.
     *
     * @param players is the number of players to be set.
     */
    @Override
    public void setPlayersNumber(int players, String gameName) {
        JsonObject toSend = generateStandardMessage("playersNumber", Integer.toString(players));
        toSend.addProperty("gameName", gameName);
        send(toSend);
    }

    /**
     * Called after client's decision about saved games.
     *
     * @param wantToSave true if he wants to re-start from a saved game.
     * @param gameName   the name of the game he wants to resume.
     */
    @Override
    public void setSavedGame(boolean wantToSave, String gameName) {
        JsonObject toSend = generateStandardMessage("savedGameFound", String.valueOf(wantToSave));
        toSend.addProperty("gameName", gameName != null ? gameName : "null");
        send(toSend);
    }

    /**
     * A private method used to send throws the socket connection a json message.
     *
     * @param json to be sent.
     */
    private void send(JsonObject json) {
        socketOut.println(json.toString());
        socketOut.flush();
    }

    /**
     * A private method used to generate standard messages with two fields.
     *
     * @param type  of the message to be sent.
     * @param value the string to be set as “Value” in the json message (not always used).
     * @return the jsonObject created with the two specified fields.
     */
    private JsonObject generateStandardMessage(String type, String value) {
        JsonObject json = new JsonObject();
        json.addProperty("Type", type);
        json.addProperty("Value", value);
        return json;
    }

    /**
     * The infinitive loop that always listens throws socket connection until an error occurs.
     * This method notifies the client if the server has disconnected for some reason.
     */
    public void listen() {
        while (isConnected) {
            try {
                String line = in.readLine();
                if (line != null) {
                    //System.out.println("Received: " + line);
                    onMessageReceived(line);
                }
            } catch (IOException e) {
                isConnected = false;
            }
        }

        getController().onError("Server disconnected.");

        socketOut.close();
        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            getController().onError("Error closing socket interface.");
        }
        getController().disconnectMe();
    }

    /**
     * A private method used to de-parse json messages received from the server and to do the actions requested.
     *
     * @param json to be parsed.
     */
    private void onMessageReceived(String json) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        try {
            jsonObject = gson.fromJson(json, jsonObject.getClass());
            switch (jsonObject.get("Type").getAsString()) {
                case "askPlayersNumber" ->
                        getController().onPlayerNumber(new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), String[].class))));
                case "disconnect" -> in.close();
                case "savedGameFound" ->
                        getController().onSavedGame(new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), String[].class))));
                case "askSelect" -> getController().onSelect();
                case "askInsert" -> getController().onInsert();
                case "boardChanged" ->
                        getController().onBoardChanged(gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[][].class));
                case "bookshelfRenewed" ->
                        getController().onBookshelfRenewed(gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[].class), jsonObject.get("column").getAsInt(), jsonObject.get("player").getAsString());
                case "boardRenewed" ->
                        getController().onBoardRenewed(gson.fromJson(jsonObject.get("Value").getAsString(), Integer[].class));
                case "bookshelfChanged" ->
                        getController().onBookshelfChanged(jsonObject.get("nickname").getAsString(), gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[][].class));
                case "error" -> getController().onError(jsonObject.get("Value").getAsString());
                case "comGoalCreated" ->
                        getController().onCommonGoalCreated(jsonObject.get("Value").getAsInt(), jsonObject.get("score").getAsInt());
                case "persGoalCreated" -> getController().onPersGoalCreated(jsonObject.get("Value").getAsString());
                case "changeTurn" -> getController().onChangeTurn(jsonObject.get("Value").getAsString());
                case "winner" ->
                        getController().onWinner(new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), String[].class))));
                case "commonGoalDone" ->
                        getController().onCommonGoalDone(jsonObject.get("source").getAsString(), gson.fromJson(jsonObject.get("Value").getAsString(), int[].class));
                case "gameStarted" ->
                        getController().gameStarted(new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), String[].class))), true);
                case "player_score" -> getController().onPlayerScore(jsonObject.get("Value").getAsInt());
                case "bookshelf_completed" ->
                        getController().onBookshelfCompleted(jsonObject.get("Value").getAsString());
                case "chatToMe" ->
                        getController().chatToMe(jsonObject.get("sender").getAsString(), jsonObject.get("Value").getAsString());
                case "finalScores" -> {
                    String[] nicknames = gson.fromJson(jsonObject.get("Value").getAsString(), String[].class);
                    Integer[] scores = gson.fromJson(jsonObject.get("scores").getAsString(), Integer[].class);
                    LinkedHashMap<String, Integer> finalScores = new LinkedHashMap<>();
                    for (int i = 0; i < nicknames.length; i++)
                        finalScores.put(nicknames[i], scores[i]);
                    getController().onFinalScores(finalScores);
                }
                case "gameInterrupted" -> getController().gameInterrupted();
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
                default -> getController().onError("Unknown message from server.");

            }
        } catch (IOException e) {
            getController().onError("Error de-parsing server's message.");
        }
    }
}