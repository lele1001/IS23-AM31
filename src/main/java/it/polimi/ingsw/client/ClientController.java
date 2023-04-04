package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    private String myNickname;
    ItemCard[][] board = new ItemCard[DIM_BOARD][DIM_BOARD];
    Map<String, ItemCard[][]> playersBookshelf = new HashMap<>();
    Map<Integer, Integer> playerComGoal = new HashMap<>();
    boolean myTurn = false;
    List<ItemCard> selectedTiles = new ArrayList<>();
    int myPoint = 0;
    View view;
    String myPersGoal;

    public void onSelect(String currPlayer) {
        // to fix
        view.print("Choose the Tiles you want to get from the Board");
        view.printBoard(board);
    }

    public void onInsert(String currPlayer) {
        // to fix
        view.print("Choose int which order and where you want to put the Tiles");
        view.printMyBookshelf(playersBookshelf.get(myNickname));
    }

    public void onBoardChanged(ItemCard[][] newBoard) {
        this.board = newBoard;
        view.printBoard(board);
    }

    public void onBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        if(playersBookshelf.containsKey(nickname))
            playersBookshelf.replace(nickname,newBookshelf);
        else
            playersBookshelf.put(nickname, newBookshelf);
        view.printBookshelf(playersBookshelf);
    }

    public void onError(String error) {
        //genero il messaggio con la stringa di errore, che parserò lato client.
        view.printError(error);
    }

    public void onCommonGoalCreated(Integer comGoalID, Integer score) {
        playerComGoal.put(comGoalID,score);
        view.printCommonGoal(playerComGoal);
    }

    public void onEmptyCardBag() {
        view.printError("CardBag out of Cards");
    }

    public void onPlayerPointUpdate(String nickname, int newValue) {
        if (nickname.equals(myNickname)) {
            myPoint = newValue;
        }
        view.printPoints(myPoint);
    }

    public void onCommonGoalDone(String nickname, int[] newValue) {
        playerComGoal.replace(newValue[0],newValue[1]);

    }

    public void onPersGoalCreated(String newValue) {
        this.myPersGoal = newValue;
        view.printPersGoal(myPersGoal);
    }

    public void onChangeTurn(String nickname) {
        if (nickname.equals(myNickname)) {
            myTurn = true;
            view.print("Your Turn");
        } else {
            myTurn = false;
            view.print(nickname + "'s Turn");
        }
    }
}


