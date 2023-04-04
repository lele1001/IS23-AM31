package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;
import java.util.Map;

public class ConnectionControl {
    private Map<String, ClientHandler> clientHandlerMap;
    private GameController gameController;

    public void addClient(ClientHandler clientHandler, String nickname) {
        this.clientHandlerMap.put(nickname, clientHandler);
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void selectCard(String nickname, ArrayList<Integer> positions) {
        gameController.selectCard(nickname, positions);
    }

    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        gameController.insertCard(nickname, cards, column);
    }

    public void askSelect(String nickname) {
        this.clientHandlerMap.get(nickname).askSelect();
    }

    public void askInsert(String nickname) {
        this.clientHandlerMap.get(nickname).askInsert();
    }

    public void SendBoardChanged(ItemCard[][] newBoard) {

    }

    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    public void SendError(String error, String nickname) {
        this.clientHandlerMap.get(nickname).sendError(error);
    }

    public void sendErrorToEveryone(String error) {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(error);
        }
    }

    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {

    }

    public void SendEmptyCardBag() {
    }

    public void SendPlayerPointUpdate(String source, int newValue) {
    }

    public void SendCommonGoalDone(String source, int[] details) {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(source + " has reached ComGoal n." + details[0] + ".");
        }
    }

    public void SendPersGoalCreated(String newValue) {
    }

    public void SendBookshelfCompleted(String nickname) {
        for (ClientHandler clientHandler : clientHandlerMap.values()) {
            clientHandler.sendError(nickname + " has completed his Bookshelf. Let's go on for the last turn of the game.");
        }
    }
}
