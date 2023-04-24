package it.polimi.ingsw.client.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectionSocket extends ConnectionClient {
    ClientController controller;
    private PrintWriter socketOut = null;
    private BufferedReader in;
    private Socket socket;

    /**
     * Initialize the Socket connection to the server
     *
     * @param controller ClientController, on which it will call all the methods after the server request
     * @param address    IP address of the server
     * @param port       IP port of the server
     */
    public ConnectionSocket(ClientController controller, String address, int port) throws IOException {
        super(controller, address, port);
    }

    /**
     * Starts socket's connection, opening the socket and the streams.
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
        System.out.println("Connection established.");
        System.out.println("Sending nickname...");
        send(generateStandardMessage("nickname", getController().getMyNickname()));
        new Thread(this::listen).start();
    }

    /**
     * Called when the client wants to select cards from board.
     * @param nickname      this client
     * @param cardsSelected Tiles selected by the client
     */
    @Override
    public void selectCard(String nickname, ArrayList<Integer> cardsSelected) {
        Gson gson = new Gson();
        send(generateStandardMessage("selectCards", gson.toJson(cardsSelected.toArray())));
    }

    /**
     * Called when the client wants to insert cards in his bookshelf.
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
     * @param nickname this client
     * @param message  String to send to all the connected players
     */
    @Override
    public void chatToAll(String nickname, String message) {
        send(generateStandardMessage("chatToAll", message));
    }

    /**
     * Called to send a message via chat to a specific player.
     * @param sender   this client
     * @param receiver the client receiver of the message.
     * @param message  String to be sent as a message
     */
    @Override
    public void chatToPlayer(String sender, String receiver, String message) {
        JsonObject toSend = generateStandardMessage("chatToPlayer", message);
        toSend.addProperty("receiver", receiver);
    }

    /**
     * Called when client wants to set players' number.
     * @param players: players' number to be set.
     */
    @Override
    public void setPlayersNumber(int players) {
        send(generateStandardMessage("playersNumber", Integer.toString(players)));
    }

    /**
     * A private method used to send throw the socket connection a json message.
     * @param json to be sent.
     */
    private void send(JsonObject json) {
        socketOut.println(json.toString());
        socketOut.flush();
    }

    /**
     * A private method used to generate standard messages with two fields.
     * @param type of the message to be sent.
     * @param value: the string to be set as "Value" in the json message (not always used).
     * @return the jsonObject created with the two specified fields.
     */
    private JsonObject generateStandardMessage(String type, String value) {
        JsonObject json = new JsonObject();
        json.addProperty("Type", type);
        json.addProperty("Value", value);
        return json;
    }

    /**
     * The infinitive loop that always listens throw socket connection until an error occurs.
     * This method notifies the client if server has disconnected for some reason.
     */
    public void listen() {
        while (true) {
            try {
                String line = in.readLine();
                // System.out.println("Received: " + line);
                onMessageReceived(line);
            } catch (IOException e) {
                System.out.println("Server disconnected.");
                break;
            }
        }
        socketOut.close();
        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing socket interface.");
        }
        getController().disconnectMe();
    }

    /**
     * A private method used to de-parse json messages received from the server and to do the actions requested.
     * @param json to be parsed.
     */
    private void onMessageReceived(String json) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        try {
            jsonObject = gson.fromJson(json, jsonObject.getClass());
            switch (jsonObject.get("Type").getAsString()) {
                case "askPlayersNumber" ->
                    getController().onPlayerNumber();

                case "disconnect" -> in.close();

                case "askSelect" -> getController().onSelect();

                case "askInsert" -> getController().onInsert();

                case "boardChanged" ->
                        getController().onBoardChanged(gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[][].class));

                case "bookshelfChanged" ->
                        getController().onBookshelfChanged(jsonObject.get("nickname").getAsString(), gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[][].class));

                case "error" -> getController().onError(jsonObject.get("Value").getAsString());

                case "comGoalCreated" ->
                        getController().onCommonGoalCreated(jsonObject.get("Value").getAsInt(), jsonObject.get("score").getAsInt());

                case "persGoalCreated" -> getController().onPersGoalCreated(jsonObject.get("Value").getAsString());

                case "changeTurn" -> getController().onChangeTurn(jsonObject.get("Value").getAsString());

                case "winner" -> {
                    String[] winners = gson.fromJson(jsonObject.get("Value"), String[].class);
                    if (winners.length == 1)
                        System.out.println("Winner is " + winners[0]);
                    else
                        System.out.println("Parity: winners are " + Arrays.toString(winners));
                }

                case "commonGoalDone" -> getController().onCommonGoalDone(jsonObject.get("source").getAsString(), gson.fromJson(jsonObject.get("Value"), int[].class));

                case "gameStarted" -> {
                    System.out.println("gameStarting");
                    getController().gameStarted(new ArrayList<>(Arrays.asList(gson.fromJson(jsonObject.get("Value").getAsString(), String[].class))), true);
                }

                case "gameNotAvailable" -> System.out.println("GameNotAvailable");

                case "chatToMe" -> getController().chatToMe(jsonObject.get("sender").getAsString(), jsonObject.get("Value").getAsString());

                default -> System.out.println("Unknown message from server.");

            }
        } catch (IOException e) {
            System.out.println("Error de-parsing server's message.");
        }
    }
}
