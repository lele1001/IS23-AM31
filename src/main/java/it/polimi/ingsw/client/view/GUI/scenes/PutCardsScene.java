package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class PutCardsScene extends GUIScene {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    private ClientController clientController;
    private GridPane boardPane;

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

    public void orderTiles() {
    }

    public void updateCurrPlayer(String player) {
        //TODO: print the input string
    }

    @Override
    public void updateBoard(ItemCard[][] board) {
        for(int i = 0; i < DIM_BOARD; i++){
            for(int j = 0; j < DIM_BOARD; j++){
                String itemName = board[i][j].getMyItem().toString().toLowerCase();
                String itemNumber = board[i][j].getMyNum().toString();
                String myItem = itemName + itemNumber;
                boardPane.add(new ImageView(GUIResources.getItem(myItem)), i, j);
            }
        }

    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

        for(int i = 0; i < BOOKSHELF_HEIGHT; i++){
            for(int j = 0; j < BOOKSHELF_LENGTH ; j++){
                String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                String itemNumber = bookshelf[i][j].getMyNum().toString();
                String myItem = itemName + itemNumber;
                //Creare funzione per scegliere
            //    boardPane.add(new ImageView(GUIResources.getItem(myItem)), i, j);
            }
        }
    }
}
