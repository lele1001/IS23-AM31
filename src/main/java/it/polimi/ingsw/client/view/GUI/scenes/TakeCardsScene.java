package it.polimi.ingsw.client.view.GUI.scenes;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.Objects;


public class TakeCardsScene extends GUIScene {

    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;

    public GridPane boardPane;

    private ClientController clientController;

    @Override
    public void updateBoard(ItemCard[][] board) {

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                String itemName = board[i][j].getMyItem().toString().toLowerCase();
                String itemNumber = board[i][j].getMyNum().toString();
                String myItem = itemName + itemNumber;
                boardPane.add(new ImageView(GUIResources.getItem(myItem)), i, j);
            }
        }

    }


    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

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
