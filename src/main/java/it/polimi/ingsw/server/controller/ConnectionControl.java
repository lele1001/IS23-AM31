package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.connection.ClientHandler;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.utils.Utils;

import java.util.*;

/**
 * Defines the actions and response to the client's requests
 */
public class ConnectionControl {
    private final Map<String, ClientHandler> clientHandlerMap = new HashMap<>();
    private final Map<String, Boolean> clientStatusMap = new HashMap<>();
    private GameController gameController;
    private final Server server;
    private boolean gameConfigured;

    /**
     * Set up the Connection Controller
     *
     * @param server the server instance
     */
    public ConnectionControl(Server server) {
        this.server = server;
        gameConfigured = false;
    }

    /**
     * A private method, used for synchronize on the clientHandlerMap and take her values.
     *
     * @return a copy of the clientHandlerMap to be used for sending objects.
     */
    private Map<String, ClientHandler> getClientHandlerMap() {
        synchronized (clientHandlerMap) {
            return new HashMap<>(clientHandlerMap);
        }
    }

    /**
     * Called by the server, asks players' number to a client.
     *
     * @param nickname          of the client.
     * @param notAvailableNames the names of the games already in existence
     */
    public void askPlayerNumber(String nickname, List<String> notAvailableNames) {
        System.out.println("Asking players number to " + nickname);
        this.getClientHandlerMap().get(nickname).askPlayerNumber(notAvailableNames);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!gameConfigured) {
                    getClientHandlerMap().get(nickname).sendError("Took too long to set... Please, try again.");
                    changePlayerStatus(nickname, false);
                }
            }
        }, Utils.timeOfReturning);
    }

    /**
     * Tries to add the clientHandler in the queue of the game
     *
     * @param clientHandler to be added.
     * @param nickname      of the client.
     * @return a boolean that indicates if the game is available.
     */
    public boolean tryAddInQueue(ClientHandler clientHandler, String nickname) {
        synchronized (clientHandlerMap) {
            if (gameController.isGameIsActive()) {
                if ((clientStatusMap.containsKey(nickname)) && (!clientStatusMap.get(nickname))) {
                    // Client's nickname is already in the map: it went offline during the game.
                    // Setting him as online...
                    clientHandler.sendError("Welcome back " + nickname + "!");
                    clientHandlerMap.put(nickname, clientHandler);
                    clientStatusMap.replace(nickname, true);
                    System.out.println(nickname + " is back!");

                    System.out.println("Sending him game details...");
                    this.sendGameIsStarting(new ArrayList<>(clientStatusMap.keySet()), nickname);
                    gameController.sendGameDetails(nickname);
                    return true;
                } else {
                    clientHandler.sendError("There's already an active game. Please, try later.");
                    return false;
                }
            }

            if (clientHandlerMap.containsKey(nickname)) {
                clientHandler.sendError("There's already a player with your nickname. Please, try again with another one.");
                return false;
            }

            this.clientHandlerMap.put(nickname, clientHandler);
            System.out.println("put in handlerMap");

            this.clientStatusMap.put(nickname, true);
            System.out.println("put in status");

            this.server.addInQueue(nickname);
            System.out.println(nickname + " added in queue.");
            return true;
        }
    }

    /**
     * Asks the client if he wants to resume one of the game he was into.
     *
     * @param nickname   the nickname of the client to be asked.
     * @param savedGames the list of the saved games the client is into.
     */
    public void askSavedGame(String nickname, List<String> savedGames) {
        this.getClientHandlerMap().get(nickname).askSavedGame(savedGames);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!gameConfigured) {
                    getClientHandlerMap().get(nickname).sendError("Took too long to set... Please, try again.");
                    changePlayerStatus(nickname, false);
                }
            }
        }, Utils.timeOfReturning);
    }

    /**
     * Allows the first player to set the number of available players.
     *
     * @param availablePlayers to set.
     * @param gameName         the name to be set for this game.
     * @param nickname         the nickname of the client that wants to set players.
     */
    public void setAvailablePlayers(String nickname, int availablePlayers, String gameName) {
        if (availablePlayers >= 2 && availablePlayers <= 4)
            gameConfigured = true;
        server.setAvailablePlayers(nickname, availablePlayers, gameName);
    }

    /**
     * Called when the player wants to resume a saved game.
     *
     * @param wantToSave true if he wants to resume a game.
     * @param gameName   the name of the game to be resumed.
     */
    public void setSavedGame(boolean wantToSave, String gameName) {
        if (wantToSave)
            gameConfigured = true;
        server.setSavedGame(wantToSave, gameName);
    }

    /**
     * Called to remove a client from the maps (when the game is not available for him).
     *
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
     *
     * @param gameController to be set.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Sends a message to the client with the game's list of the players, at the beginning of the game.
     *
     * @param playersList of the active players in the just created game.
     * @param receiver    the nickname of the receiver of the message (if null, the message has to be sent to all clients).
     */
    public void sendGameIsStarting(ArrayList<String> playersList, String receiver) {
        if (receiver == null) {
            // Send it to all clients
            System.out.println("Game is starting... sending it to clients.");

            for (ClientHandler c : getClientHandlerMap().values()) {
                c.sendGameIsStarting(playersList);
            }
        } else {
            // Send it only to receiver
            ClientHandler c_receiver = getClientHandlerMap().get(receiver);

            if (c_receiver != null) {
                c_receiver.sendGameIsStarting(playersList);
            }
        }

    }

    /**
     * Called to set a player as offline.
     *
     * @param nickname  of the player that has gone out from the game.
     * @param adviceAll define if an error is sent to everyone
     */
    public void changePlayerStatus(String nickname, boolean adviceAll) {
        synchronized (clientHandlerMap) {
            if (gameConfigured)
                this.clientStatusMap.put(nickname, false);
            else
                this.clientStatusMap.remove(nickname);
            server.removeFromQueue(nickname);

            if (clientHandlerMap.containsKey(nickname)) {
                this.clientHandlerMap.get(nickname).disconnectPlayer();
                this.clientHandlerMap.remove(nickname);
            }
        }

        if (adviceAll) {
            sendErrorToEveryone(nickname + " is disconnected from the game.");
        }
    }

    /**
     * Called by the client to select cards from the board.
     *
     * @param nickname  of the client that wants to select
     * @param positions a list of integer that indicates the positions of the tiles to be selected
     */
    public void selectCard(String nickname, ArrayList<Integer> positions) {
        System.out.println(nickname + " wants to select cards in position: " + positions);
        gameController.selectCard(nickname, positions);
    }

    /**
     * Called by the client to insert cards in his bookshelf.
     *
     * @param nickname of the client that wants to insert
     * @param cards    an ordered list of the ItemCards he wants to insert
     * @param column   to put ItemCards into
     */
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        System.out.println(nickname + " wants to insert cards: " + cards + " in column: " + column);
        gameController.insertCard(nickname, cards, column);
    }

    /**
     * Called by the client, send the message to all the other clients
     *
     * @param nickname The client that sent the message
     * @param message  THe message sent
     */
    public void chatToAll(String nickname, String message) {
        System.out.println(nickname + " sends to everyone: " + message);
        synchronized (clientHandlerMap) {
            for (String player : clientHandlerMap.keySet()) {
                if (!player.equals(nickname)) {
                    this.clientHandlerMap.get(player).chatToMe(nickname, message);
                }
            }
        }
    }

    /**
     * Called by a client when he wants to send a message via chat to a specific player.
     *
     * @param sender   the sender of the message.
     * @param receiver the receiver.
     * @param message  to be sent.
     */
    public void chatToPlayer(String sender, String receiver, String message) {
        System.out.println(sender + " sends to " + receiver + ": " + message);
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(receiver)) {
                this.clientHandlerMap.get(receiver).chatToMe(sender, message);
            }
        }
    }

    /**
     * Called by the GameController, notifies a client that it is his turn to select cards.
     *
     * @param nickname of the client that has to select.
     */
    public void askSelect(String nickname) {
        System.out.println("Asking " + nickname + " to select cards.");
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname)) {
                this.clientHandlerMap.get(nickname).askSelect();
            } else {
                System.out.println("No connection available for " + nickname);
            }
        }
    }

    /**
     * Called by the GameController, notifies a client that it is his turn to insert cards.
     *
     * @param nickname of the client that has to insert.
     */
    public void askInsert(String nickname) {
        System.out.println("Asking " + nickname + " to insert cards.");
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname)) {
                this.clientHandlerMap.get(nickname).askInsert();
            } else {
                System.out.println("No connection available for " + nickname);
            }
        }
    }

    /**
     * Called by the GameController, sends the board to the client because of an update of it.
     *
     * @param newBoard the board after the last update.
     * @param receiver the nickname of the receiver of the update. If null, the update has to be sent to all clients.
     */
    public void SendBoardChanged(ItemCard[][] newBoard, String receiver) {
        if (receiver == null) {
            // Send it to all the players
            System.out.println("Sending board changes to all...");

            for (ClientHandler c : getClientHandlerMap().values()) {
                c.SendBoardChanged(newBoard);
            }
        } else {
            // Send it only to receiver
            ClientHandler c_receiver = getClientHandlerMap().get(receiver);

            if (c_receiver != null) {
                System.out.println("Sending board update to " + receiver);
                c_receiver.SendBoardChanged(newBoard);
            }
        }
    }

    /**
     * Sends board's update to clients.
     *
     * @param tilesToRemove an array of the positions of the just removed tiles.
     */
    public void sendBoardRenewed(Integer[] tilesToRemove) {
        System.out.println("Sending board's update to all...");
        for (ClientHandler c : getClientHandlerMap().values())
            c.sendBoardRenewed(tilesToRemove);
    }

    /**
     * Sends a bookshelf's update to clients.
     *
     * @param tilesToAdd an ordered array of tiles to add into the bookshelf.
     * @param column     the column to put tiles into.
     * @param player     the player whose bookshelf has just changed.
     */
    public void sendBookshelfRenewed(ItemCard[] tilesToAdd, int column, String player) {
        System.out.println("Sending " + player + " bookshelf's update to all...");
        for (ClientHandler c : getClientHandlerMap().values())
            c.sendBookshelfRenewed(player, tilesToAdd, column);
    }

    /**
     * Called by the GameController, sends a player's bookshelf to the client because of an update of it.
     *
     * @param nickname     of the owner of the bookshelf.
     * @param newBookshelf the bookshelf after the last update.
     * @param receiver     the nickname of the receiver of the update. If null, the update has to be sent to all the clients.
     */
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf, String receiver) {
        if (receiver == null) {
            System.out.println("Player " + nickname + " has changed the bookshelf");

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
     *
     * @param error    to be sent.
     * @param nickname of the client to be sent to.
     */
    public void SendError(String error, String nickname) {
        System.out.println("Sending error: " + error + " to " + nickname);
        synchronized (clientHandlerMap) {
            if (clientHandlerMap.containsKey(nickname)) {
                this.clientHandlerMap.get(nickname).sendError(error);
            } else {
                System.out.println("No connection available for " + nickname);
            }
        }
    }

    /**
     * Sends an error to all the online players.
     *
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
     *
     * @param comGoalID the ID of the Common Goal.
     * @param score     the maximum score available for the Common Goal.
     * @param receiver  null if you want to send to everyone else the name of the player
     */
    public void SendCommonGoalCreated(Integer comGoalID, Integer score, String receiver) {
        if (receiver == null) {
            for (ClientHandler c : getClientHandlerMap().values()) {
                c.SendCommonGoalCreated(comGoalID, score);
            }
        } else {
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
     *
     * @param source  the nickname of the client just reached the goal.
     * @param details a two integers' vector: the first one indicates Common Goal's ID,
     *                while the other one is the maximum score still available after this event.
     */
    public void SendCommonGoalDone(String source, int[] details) {
        System.out.println(source + " has reached Common Goal number: " + details[0]);

        for (ClientHandler c : getClientHandlerMap().values()) {
            c.SendCommonGoalDone(source, details);
        }
    }

    /**
     * Notifies a client that a PersonalGoal has been assigned to him.
     *
     * @param nickname of the client to be notified.
     * @param persGoal assigned.
     */
    public void SendPersGoalCreated(String nickname, String persGoal) {
        synchronized (clientHandlerMap) {

            if (clientHandlerMap.containsKey(nickname)) {
                clientHandlerMap.get(nickname).SendPersGoalCreated(persGoal);
            } else {
                System.out.println("No connection available for " + nickname);
            }
        }
    }

    /**
     * Notifies the client that has completed his bookshelf.
     *
     * @param nickname of the client to be notified.
     */
    public void SendBookshelfCompleted(String nickname) {
        System.out.println("Sending bookshelf completed to everyone...");
        for (ClientHandler clientHandler : getClientHandlerMap().values()) {
            clientHandler.sendBookshelfCompleted(nickname);
        }
    }

    /**
     * Sends to all clients the nickname of the winners of the game.
     *
     * @param finalScores an ordered hash map with all final scores.
     */
    public void sendFinalScores(LinkedHashMap<String, Integer> finalScores) {
        System.out.println("Sending winners' nickname...");

        for (ClientHandler c : getClientHandlerMap().values()) {
            c.sendFinalScores(finalScores);
        }
    }

    /**
     * Notifies all clients about the current player that has to play now.
     *
     * @param nickname of the client that has to play.
     */
    public void sendPlayerTurn(String nickname) {
        for (ClientHandler c : getClientHandlerMap().values()) {
            c.sendPlayerTurn(nickname);
        }
    }

    /**
     * Used to send the player's score when he comes back to the game.
     *
     * @param nickname of the just returned player.
     * @param score    to be sent.
     */
    public void sendPlayerScore(String nickname, int score) {
        System.out.println("Sending actual score: " + score + " to " + nickname + ".");
        ClientHandler clientHandler = getClientHandlerMap().get(nickname);

        if (clientHandler != null) {
            clientHandler.sendPlayerScore(score);
        }
    }

    /**
     * Notifies the remaining client that the game has been interrupted because of too many absents for it.
     */
    public void sendGameInterrupted() {
        System.out.println("Sending that game has been interrupted because of too many absents..");
        for (ClientHandler c : getClientHandlerMap().values())
            c.sendGameInterrupted();
    }

    /**
     * Used by GameController to check if a client is still online.
     *
     * @param nickname of the players to be checked.
     * @return true if the client is online.
     */
    public boolean isOnline(String nickname) {
        synchronized (clientHandlerMap) {
            return clientStatusMap.getOrDefault(nickname, true);
        }
    }

    /**
     * Used at the end of a game to clear the maps and initialize the server's features.
     *
     * @param gameFilePath the path to the json file of the game
     */
    public void onEndGame(String gameFilePath) {
        disconnectAll();

        synchronized (clientHandlerMap) {
            this.clientStatusMap.clear();
            this.clientHandlerMap.clear();
        }

        server.onEndGame(gameFilePath);
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