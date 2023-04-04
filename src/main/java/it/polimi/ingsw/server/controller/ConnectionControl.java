package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionControl {
    private Map<String, ClientHandler> clientHandlerMap = new HashMap<>();
    private GameController gameController;

    public void addClient(ClientHandler clientHandler, String nickname) {
        this.clientHandlerMap.put(nickname, clientHandler);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void selectCard(String nickname, ArrayList<Integer> positions) {
        gameController.selectCard(nickname, positions);
        System.out.println(nickname + " wants to select cards in position: " + positions);
    }

    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        gameController.insertCard(nickname, cards, column);
        System.out.println(nickname + " wants to insert cards: " + cards + " in column: " + column);
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
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendBoardChanged(newBoard);
        }
        System.out.println("Board has changed");
    }

    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendBookshelfChanged(nickname, newBookshelf);
        }
        System.out.println("Player " + nickname + " has changed the bookshelf");
    }

    public void SendError(String error, String nickname) {
        System.out.println("Sending error: " + error + " to " + nickname);
        if (clientHandlerMap.containsKey(nickname))
            this.clientHandlerMap.get(nickname).sendError(error);
        else
            System.out.println("No connection available for " + nickname);
    }

    public void sendErrorToEveryone(String error) {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(error);
        }
        System.out.println("Sending error: " + error + " to everyone.");
    }

    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendCommonGoalCreated(comGoalID, score);
        }
    }

    public void SendEmptyCardBag() {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError("Cardbag is empty");
        }
        System.out.println("Cardbag is empty.");
    }

    public void SendCommonGoalDone(String source, int[] details) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendCommonGoalDone(source, details);
        }
        System.out.println(source + " has reached Common Goal number: " + details[0]);
    }

    public void SendPersGoalCreated(String nickname, String persGoal) {
        if (clientHandlerMap.containsKey(nickname))
            clientHandlerMap.get(nickname).SendPersGoalCreated(nickname, persGoal);
        else
            System.out.println("No connection available for " + nickname);
    }

    public void SendBookshelfCompleted(String nickname) {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(nickname + " has completed his Bookshelf. Let's go on for the last turn of the game.");
        }
        System.out.println("Sending bookshelf completed to everyone");
    }

    public void SendDetailsEndGame(String winner, int score) {
        for (ClientHandler c : clientHandlerMap.values()) {
            c.SendDetailsEndGame(winner, score);
        }
        System.out.println("Sending end game scores");
    }
}
