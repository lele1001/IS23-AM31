package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.util.Map;

public class NotMyTurnScene extends GUIScene {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    public Tab player1tab, player2tab, player3tab, player4tab;
    public GridPane bookshelf1, bookshelf2, bookshelf3, bookshelf4;
    public GridPane boardPane;

    @FXML
    GridPane comGoals;
    private ClientController clientController;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        int i= 0;
        for(String s : clientController.getPlayersBookshelves().keySet()){
            if(i == 0){
                player1tab.setText(s);
            }else if(i == 1){
                player2tab.setText(s);
            }else if(i == 2){
                player3tab.setText(s);
                //ricordarsi di rendere o meno visibile in base a num giocatori
            }else if(i == 3){
                player4tab.setText(s);
            }
            i++;
        }
    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void bindEvents() {

    }

    public void updateCurrPlayer(String currPlayer) {
        //TODO: print the name of the current player
    }

    @Override
    public void updateBoard(ItemCard[][] board) {
        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if(board[i][j]!=null) {
                    String itemName = board[i][j].getMyItem().toString().toLowerCase();
                    String itemNumber = board[i][j].getMyNum().toString();
                    String myItem = itemName + itemNumber;
                    boardPane.add(new ImageView(GUIResources.getItem(myItem)), i, j);
                }
            }
        }
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        if(player1tab.getText().equals(nickname)){
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if(bookshelf[i][j]!=null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;
                        bookshelf1.add(new ImageView(GUIResources.getItem(myItem)), i, j);
                    }
                }
            }
        }
        else if(player2tab.getText().equals(nickname)){
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if(bookshelf[i][j]!=null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;
                        bookshelf2.add(new ImageView(GUIResources.getItem(myItem)), i, j);
                    }
                }
            }
        }
        else if(player3tab.getText().equals(nickname)){
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if(bookshelf[i][j]!=null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;
                        bookshelf3.add(new ImageView(GUIResources.getItem(myItem)), i, j);
                    }
                }
            }
        }
        else if(player4tab.getText().equals(nickname)){
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if(bookshelf[i][j]!=null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;
                        bookshelf4.add(new ImageView(GUIResources.getItem(myItem)), i, j);
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
            comGoals.add(new ImageView(GUIResources.getComGoal("cg" + cgNum)), n, 0);
            n++;
        }
    }
}