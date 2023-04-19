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

    public void askPlayerNumber(String nickname) {
        System.out.println("Asking players number to " + nickname);
        this.clientHandlerMap.get(nickname).askPlayerNumber();
    }

    public boolean tryAddInQueue(ClientHandler clientHandler, String nickname) {
        if (gameController.isGameIsActive()) {
            if (clientStatusMap.containsKey(nickname)) {
                if (clientStatusMap.get(nickname))
                    // ce n'è uno dentro e sta giocando
                    return false;
                // è già dentro ed è tornato online
                clientHandlerMap.put(nickname, clientHandler);
                clientStatusMap.replace(nickname, true);
                System.out.println(nickname + " is back!");
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

    public void removeClient(String nickname) {
        this.clientHandlerMap.get(nickname).disconnectPlayer();
        this.clientHandlerMap.remove(nickname);
        this.clientStatusMap.remove(nickname);
    }


    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void sendGameIsStarting(ArrayList<String> playersList) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.sendGameIsStarting(playersList);
        }
        System.out.println("Game is starting... sending it to clients.");
    }

    public void changePlayerStatus(String nickname) {
        synchronized (clientStatusMap) {
            this.clientStatusMap.put(nickname, false);
        }
        server.removeFromQueue(nickname);   // se era in coda, lo rimuovo
        if (clientHandlerMap.containsKey(nickname)) {
            this.clientHandlerMap.get(nickname).disconnectPlayer();
            this.clientHandlerMap.remove(nickname);
        }
        sendErrorToEveryone(nickname + " is disconnected from the game.");
        //gameController.changePlayerStatus(nickname);

    }

    public void selectCard(String nickname, ArrayList<Integer> positions) {
        System.out.println(nickname + " wants to select cards in position: " + positions);
        gameController.selectCard(nickname, positions);
    }

    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        System.out.println(nickname + " wants to insert cards: " + cards + " in column: " + column);
        gameController.insertCard(nickname, cards, column);
    }

    public void chatToAll(String nickname, String message) {
        System.out.println(nickname + " send to everyone: " + message);
        for (String player : clientHandlerMap.keySet()) {
            if (!player.equals(nickname))
                this.clientHandlerMap.get(player).chatToMe(nickname, message);
        }
    }

    public void chatToPlayer(String sender, String receiver, String message) {
        System.out.println(sender + " send to " + receiver + ": " + message);
        if (clientHandlerMap.containsKey(receiver))
            this.clientHandlerMap.get(receiver).chatToMe(sender, message);
    }

    public void askSelect(String nickname) {
        System.out.println("Asking " + nickname + " to select cards.");
        if (clientHandlerMap.containsKey(nickname))
            this.clientHandlerMap.get(nickname).askSelect();
        else
            System.out.println("No connection available for " + nickname);
    }

    public void askInsert(String nickname) {
        System.out.println("Asking " + nickname + " to insert cards.");
        if (clientHandlerMap.containsKey(nickname))
            this.clientHandlerMap.get(nickname).askInsert();
        else
            System.out.println("No connection available for " + nickname);

    }

    public void SendBoardChanged(ItemCard[][] newBoard) {
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

        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendBoardChanged(newBoard);
        }
    }

/*    public void sendBoard (ItemCard[][] board, String receiver) {
        System.out.println("Sending board to " + receiver);
        if (clientHandlerMap.containsKey(receiver))
            this.clientHandlerMap.get(receiver).SendBoardChanged(board);
    }

    public void sendBookshelf (ItemCard[][] bookshelf, String owner, String receiver) {
        System.out.println("Sending " + owner + "'s bookshelf to " + receiver);
        if (clientHandlerMap.containsKey(receiver))
            this.clientHandlerMap.get(receiver).SendBookshelfChanged(owner, bookshelf);
    }*/

    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        System.out.println("Player " + nickname + " has changed the bookshelf");
        c.printMyBookshelf(newBookshelf);
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendBookshelfChanged(nickname, newBookshelf);
        }
    }

    public void SendError(String error, String nickname) {
        System.out.println("Sending error: " + error + " to " + nickname);
        if (clientHandlerMap.containsKey(nickname))
            this.clientHandlerMap.get(nickname).sendError(error);
        else
            System.out.println("No connection available for " + nickname);
    }

    public synchronized void sendErrorToEveryone(String error) {
        System.out.println("Sending error: " + error + " to everyone.");
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(error);
        }
    }

    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendCommonGoalCreated(comGoalID, score);
        }
    }

    public void SendEmptyCardBag() {
        System.out.println("Cardbag is empty.");
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError("Cardbag is empty");
        }
    }

    public void SendCommonGoalDone(String source, int[] details) {
        System.out.println(source + " has reached Common Goal number: " + details[0]);
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendCommonGoalDone(source, details);
        }
    }

    public void SendPersGoalCreated(String nickname, String persGoal) {
        if (clientHandlerMap.containsKey(nickname))
            clientHandlerMap.get(nickname).SendPersGoalCreated(persGoal);
        else
            System.out.println("No connection available for " + nickname);
    }

    public void SendBookshelfCompleted(String nickname) {
        System.out.println("Sending bookshelf completed to everyone");
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(nickname + " has completed his Bookshelf. Let's go on for the last turn of the game.");
        }
    }

    public void sendWinner(String winner) {
        System.out.println("Sending winner's nickname...");
        for (ClientHandler c : clientHandlerMap.values()) {
            c.sendWinner(winner);
        }
        onEndGame();
    }

    public void sendPlayerTurn(String nickname) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.sendPlayerTurn(nickname);
        }
    }

    public boolean isOnline(String nickname) {
        synchronized (clientStatusMap) {
            return clientStatusMap.getOrDefault(nickname, true);
        }
    }


    private void onEndGame() {
        disconnectAll();
        this.clientStatusMap.clear();
        this.clientHandlerMap.clear();
        server.onEndGame();
    }

    public void disconnectAll() {
        System.out.println("Connection control: disconnecting all players...");
        for (ClientHandler c : clientHandlerMap.values()) {
            c.disconnectPlayer();
        }
    }
}
