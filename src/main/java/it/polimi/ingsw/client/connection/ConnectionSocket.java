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

    @Override
    public void selectCard(String nickname, ArrayList<Integer> cardsSelected) throws Exception {

    }

    @Override
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) throws Exception {

    }

    @Override
    public void chatToAll(String nickname, String message) throws Exception {

    }

    @Override
    public void chatToPlayer(String sender, String receiver, String message) throws Exception {

    }

    @Override
    public void setPlayersNumber(int players) throws Exception {

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

    public void listen() {
        while (true) {
            try {
                String line = in.readLine();
                System.out.println("Received: " + line);
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
        //chiamare disconnect
    }

    private void onMessageReceived(String json) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        try {
            jsonObject = gson.fromJson(json, jsonObject.getClass());
            switch (jsonObject.get("Type").getAsString()) {
                case "askPlayersNumber" -> {
                }
                case "disconnect" -> {

                }
                case "askSelect" -> getController().onSelect();

                case "askInsert" -> getController().onInsert();

                case "boardChanged" ->
                        getController().onBoardChanged(gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[][].class));

                case "bookshelfChanged" ->
                        getController().onBookshelfChanged(jsonObject.get("nickname").toString(), gson.fromJson(jsonObject.get("Value").getAsString(), ItemCard[][].class));

                case "error" -> getController().onError(jsonObject.get("Value").getAsString());

                case "comGoalCreated" ->
                        getController().onCommonGoalCreated(jsonObject.get("Value").getAsInt(), jsonObject.get("score").getAsInt());

                case "persGoalCreated" -> getController().onPersGoalCreated(jsonObject.get("Value").getAsString());

                case "changeTurn" -> getController().onChangeTurn(jsonObject.get("Value").getAsString());

                case "gameStarted" -> {
                    System.out.println("gameStarting");
                    getController().setGameStarted(true);
                }
                default -> System.out.println("Unknown message from server.");

            }
        } catch (Exception e) {
            System.out.println("Unknown message from server.");
        }
    }
}
