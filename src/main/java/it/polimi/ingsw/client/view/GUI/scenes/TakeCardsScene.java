package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


import java.awt.*;
import java.util.Map;
import java.util.Objects;


public class TakeCardsScene extends GUIScene {

    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;

    public GridPane boardPane;
    public GridPane comGoals;
    public GridPane bookshelfPane;

    private ClientController clientController;

    @Override
    public void updateBoard(ItemCard[][] board) {
        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (board[i][j] != null) {
                    String itemName = board[i][j].getMyItem().toString().toLowerCase();
                    String itemNumber = board[i][j].getMyNum().toString();
                    String myItem = itemName + itemNumber;
                    ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                    tileImage.setPreserveRatio(true);
                    tileImage.setFitHeight(50);
                    tileImage.setFitWidth(50);
                    boardPane.add(tileImage, j, i);
                }
            }
        }
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        if (clientController.getMyNickname().equals(nickname)) {
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;
                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(30);
                        tileImage.setFitWidth(30);
                        bookshelfPane.add(tileImage, j, i);
                    }
                }
            }
        }
    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        int n = 0;
        for (Integer i : playerCommonGoal.keySet()) {
            String cgNum = i.toString();
            if (i < 10) {
                cgNum = "0" + cgNum;
            }
            ImageView comGoalImage = new ImageView(GUIResources.getItem("cg"+cgNum));
            comGoalImage.setPreserveRatio(true);
            comGoalImage.setFitHeight(150);
            comGoalImage.setFitWidth(200);
            comGoals.add(comGoalImage, n, 0);
            n++;
        }
    }

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void bindEvents() {

    }

    public void highlightTiles() {
    }

    public void updateCurrPlayer(String player) {
        //TODO: print the input string
    }


}
