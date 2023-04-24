package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.Cli;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.ItemCard;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionControl {
    private final Map<String, ClientHandler> clientHandlerMap = new HashMap<>();
    private final Map<String, Boolean> clientStatusMap = new HashMap<>();
    private GameController gameController;
    private final Server server;
    Cli c; //added only for testing purpose

    public ConnectionControl(Server server) {
        c = new Cli();
        this.server = server;
    }

    /**
     * A private method, used for synchronize on the clientHandlerMap and take her values.
     * @return a copy of the clientHandlerMap to be used for sending objects.
     */
    private Map<String, ClientHandler> getClientHandlerMap() {
        synchronized (clientHandlerMap) {
            return new HashMap<>(clientHandlerMap);
        }
    }

    /**
     * Called by the server, asks players' number to a client.
     * @param nickname of the client.
     */
    public void askPlayerNumber(String nickname) {
        System.out.println("Asking players number to " + nickname);
        this.getClientHandlerMap().get(nickname).askPlayerNumber();
    }

    /**
     * Tries to add the clientHandler in the queue of the game
     * @param clientHandler to be added.
     * @param nickname of the client.
     * @return a boolean that indicates if the game is available.
     */
    public boolean tryAddInQueue(ClientHandler clientHandler, String nickname) {
        synchronized (clientHandlerMap) {
            if (gameController.isGameIsActive()) {
                if (clientStatusMap.containsKey(nickname)) {
                    if (clientStatusMap.get(nickname))
                        // There's a client in the game playing with the same nickname!
                        return false;
                    // Client's nickname is already in the map: it went offline during the game. Setting him as online...
                    clientHandlerMap.put(nickname, clientHandler);
                    clientStatusMap.replace(nickname, true);
                    System.out.println(nickname + " is back!");
                    System.out.println("Sending him game details...");
                    this.sendGameIsStarting(new ArrayList<>(getClientHandlerMap().keySet()), nickname);
                    gameController.sendGameDetails(nickname);
                    return true;
                } else
                    return false;
            }
            if (clientHandlerMap.containsKey(nickname)) {
                return false;
            }

            System.out.println(nickname + " added in queue.");
            this.clientHandlerMap.put(nickname, clientHandler);
            System.out.println("put in handlermap");
            this.clientStatusMap.put(nickname, true);
            System.out.println("put in status");
            server.addInQueue(nickname);
            System.out.println("ritornato da addqueue");
            return true;
        }
    }

    /**
     * Called to remove a client from the maps (when the game is not available for him).
     * @param nickname of the client to be deleted.
     */
    public void removeClient(String nickname) {
        synchronized (clientHandlerMap) {
            this.clientHandlerMap.get(nickname).disconnectPlayer();
            this.clientHandlerMap.remove(nickname);
            this.clientStatusMap.remove(nickname);
        }
    }

    /**
     * Used to set the gameController parameter.
     * @param gameController to be set.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Sends a message to the client with game's list of the players, at the beginning of the game.
     * @param playersList of the active players in the just created game.
     * @param receiver: the nickname of the receiver of the message (if null, the message has to be sent to all clients).
     */
    public void sendGameIsStarting(ArrayList<String> playersList, String receiver) {
        if (receiver == null) { // Send it to all clients
            System.out.println("Game is starting... sending it to clients.");
            for (ClientHandler c : getClientHandlerMap().values()) {
                c.sendGameIsStarting(playersList);
            }
        } else {    // Send it only to receiver
            ClientHandler c_receiver = getClientHandlerMap().get(receiver);
            if (c_receiver != null)
                c_receiver.sendGameIsStarting(playersList);
        }

    }

    /**
     * Called to set a player as offline.
     * @param nickname of the player that has gone out from the game.
     */
    public void changePlayerStatus(String nickname) {
        synchronized (clientHandlerMap) {
            this.clientStatusMap.put(nickname, false);
            server.removeFromQueue(nickname);   // Removes it if it was in the queue.
            if (clientHandlerMap.containsKey(nickname)) {
                this.clientHandlerMap.get(nickname).disconnectPlayer();
                this.clientHandlerMap.remove(nickname);
            }
        }
        sendErrorToEveryone(nickname + " is disconnected from the game.");
        //gameController.changePlayerStatus(nickname);

    }

    /**
     * Called by the client to select cards from the board.
     * @param nickname of the client that wants to select.
     * @param positions: a list of integer that indicates the positions of the tiles to be selected.
     */
    public void selectCard(String nickname, ArrayList<Integer> positions) {
        System.out.println(nickname + " wants to select cards in position: " + positions);
        gameController.selectCard(nickname, positions);
    }

    /**
     * Called by the client to insert cards in his bookshelf.
     * @param nickname of the client that wants to insert.
     * @param cards: an ordered list of the ItemCards he wants to insert.
     * @param column to put ItemCards into.
     */
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        System.out.println(nickname + " wants to insert cards: " + cards + " in column: " + column);
        gameController.insertCard(nickname, cards, column);
    }

    public void chatToAll(String nickname, String message) {
        System.out.println(nickname + " sends to everyone: " + message);
        synchronized (clientHandlerMap) {
            for (String player : clientHandlerMap.keySet()) {
                if (!player.equals(nickname))
                    this.clientHandlerMap.get(player).chatToMe(nickname, message);
            }
        }
    }

    /**
     * Called by a client when he wants to send a message via chat to a specific player.
     * @param sender: the sender of the message.
     * @param receiver: the receiver.
     * @param message to be sent.
     */
    public void chatToPlayer(String sender, String receiver, String message) {
        System.out.println(sender + " sends to " + receiver + ": " + message);
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(receiver))
                this.clientHandlerMap.get(receiver).chatToMe(sender, message);
        }
    }

    /**
     * Called by the GameController, notifies a client that it's his turn to select cards.
     * @param nickname of the client that has to select.
     */
    public void askSelect(String nickname) {
        System.out.println("Asking " + nickname + " to select cards.");
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname))
                this.clientHandlerMap.get(nickname).askSelect();
            else
                System.out.println("No connection available for " + nickname);
        }
    }

    /**
     * Called by the GameController, notifies a client that it's his turn to insert cards.
     * @param nickname of the client that has to insert.
     */
    public void askInsert(String nickname) {
        System.out.println("Asking " + nickname + " to insert cards.");
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname))
                this.clientHandlerMap.get(nickname).askInsert();
            else
                System.out.println("No connection available for " + nickname);
        }
    }

    /**
     * Called by the GameController, sends the board to the client because of an update of it.
     * @param newBoard: the board after the last update.
     * @param receiver the nickname of the receiver of the update. If null, the update has to be sent to all clients.
     */
    public void SendBoardChanged(ItemCard[][] newBoard, String receiver) {
        if (receiver == null) { // Send it to all the players
            System.out.println("Board has changed");
            c.printBoard(newBoard);

            // Just for tests: saving the board in a temporary file to check the insert.
            PrintStream printStream;
            try {
                printStream = new PrintStream("src/test/java/it/polimi/ingsw/server/controller/boardTest.json");
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                return;
            }
            Gson gson = new Gson();
            printStream.print(gson.toJson(newBoard));
            printStream.close();


            for (ClientHandler c : getClientHandlerMap().values())
                c.SendBoardChanged(newBoard);
        }   // Send it only to receiver
        else {
            ClientHandler c_receiver = getClientHandlerMap().get(receiver);
            if (c_receiver != null) {
                System.out.println("Sending board update to " + receiver);
                c_receiver.SendBoardChanged(newBoard);
            }
        }
    }


    /**
     * Called by the GameController, sends a player's bookshelf to the client because of an update of it.
     * @param nickname of the owner of the bookshelf.
     * @param newBookshelf: the bookshelf after the last update.
     * @param receiver the nickname of the receiver of the update. If null, the update has to be sent to all the clients.
     */
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf, String receiver) {
        if (receiver == null) {
            System.out.println("Player " + nickname + " has changed the bookshelf");
            c.printMyBookshelf(newBookshelf);
            for (ClientHandler c : getClientHandlerMap().values()) {
                c.SendBookshelfChanged(nickname, newBookshelf);
            }
        } else {
            ClientHandler c_receiver = getClientHandlerMap().get(receiver);
            if (c_receiver != null) {
                System.out.println("Sending bookshelf update to " + receiver);
                c_receiver.SendBookshelfChanged(nickname, newBookshelf);
            }
        }
    }

    /**
     * Called by the GameController, sends an error to a specific client.
     * @param error to be sent.
     * @param nickname of the client to be sent to.
     */
    public void SendError(String error, String nickname) {
        System.out.println("Sending error: " + error + " to " + nickname);
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname))
                this.clientHandlerMap.get(nickname).sendError(error);
            else
                System.out.println("No connection available for " + nickname);
        }
    }

    /**
     * Sends an error to all the online players.
     * @param error to be sent.
     */
    public synchronized void sendErrorToEveryone(String error) {
        System.out.println("Sending error: " + error + " to everyone.");
        for (ClientHandler clientHandler : getClientHandlerMap().values()) {
            clientHandler.sendError(error);
        }
    }

    /**
     * Called at the beginning of the game, sends the details of a Common Goal created.
     * @param comGoalID: the ID of the Common Goal.
     * @param score: the maximum score available for the Common Goal.
     */
    public void SendCommonGoalCreated(Integer comGoalID, Integer score, String receiver) {
        if (receiver == null)
            for (ClientHandler c : getClientHandlerMap().values())
                c.SendCommonGoalCreated(comGoalID, score);
        else {
            ClientHandler c_receiver = getClientHandlerMap().get(receiver);
            if (c_receiver != null) {
                System.out.println("Sending commonGoal update to " + receiver);
                c_receiver.SendCommonGoalCreated(comGoalID, score);
            }
        }
    }

    /**
     * Notifies clients that the card bag is empty.
     */
    public void SendEmptyCardBag() {
        System.out.println("Cardbag is empty.");
        for (ClientHandler clientHandler : getClientHandlerMap().values()) {
            clientHandler.sendError("Cardbag is empty");
        }
    }

    /**
     * Notifies clients that someone has reached a Common Goal.
     * @param source: the nickname of the client just reached the goal.
     * @param details: a two integers' vector: the first one indicates Common Goal's ID, while the other one is the maximum score still available after this event.
     */
    public void SendCommonGoalDone(String source, int[] details) {
        System.out.println(source + " has reached Common Goal number: " + details[0]);
        for (ClientHandler c : getClientHandlerMap().values()) {
            c.SendCommonGoalDone(source, details);
        }
    }

    /**
     * Notifies a client that a PersonalGoal has been assigned to him.
     * @param nickname of the client to be notified.
     * @param persGoal assigned.
     */
    public void SendPersGoalCreated(String nickname, String persGoal) {
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname))
                clientHandlerMap.get(nickname).SendPersGoalCreated(persGoal);
            else
                System.out.println("No connection available for " + nickname);
        }
    }

    /**
     * Notifies the client that has completed his bookshelf.
     * @param nickname of the client to be notified.
     */
    public void SendBookshelfCompleted(String nickname) {
        System.out.println("Sending bookshelf completed to everyone");
        for (ClientHandler clientHandler : getClientHandlerMap().values()) {
            clientHandler.sendError(nickname + " has completed his Bookshelf. Let's go on for the last turn of the game.");
        }
    }

    /**
     * Sends to all client the name of the winner of the game.
     * @param winners: winners' list.
     */
    public void sendWinner(List<String> winners) {
        System.out.println("Sending winners' nickname...");
        for (ClientHandler c : getClientHandlerMap().values()) {
            c.sendWinner(winners);
        }
    }

    /**
     * Notifies all clients about the current player that has to play now.
     * @param nickname of the client that has to play.
     */
    public void sendPlayerTurn(String nickname) {
        for (ClientHandler c : getClientHandlerMap().values()) {
            c.sendPlayerTurn(nickname);
        }
    }

    /**
     * Used by GameController to check if a client is still online.
     * @param nickname of the players to be checked.
     * @return true if the client is online.
     */
    public boolean isOnline(String nickname) {
        synchronized (clientHandlerMap) {
            return clientStatusMap.getOrDefault(nickname, true);
        }
    }

    /**
     * Used at the end of a game to clear the maps and initialize server's features.
     */
    public void onEndGame() {
        disconnectAll();
        synchronized (clientHandlerMap) {
            this.clientStatusMap.clear();
            this.clientHandlerMap.clear();
        }
        server.onEndGame();
    }

    /**
     * Used at the end of a game (or when server is going to stop itself) to disconnect all the clients.
     */
    public void disconnectAll() {
        System.out.println("Connection control: disconnecting all players...");
        for (ClientHandler c : getClientHandlerMap().values()) {
            c.disconnectPlayer();
        }
    }
}
