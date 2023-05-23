package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.scene.Scene;

import java.util.Map;

public abstract class GUIScene {
    private Scene myScene;

    public abstract void initialize(ClientController clientController);

    public abstract void printError(String error);

    public abstract void bindEvents();

    public Scene getMyScene() {
        return myScene;
    }

    public void setMyScene(Scene myScene) {
        this.myScene = myScene;
    }

    public abstract void updateCurrPlayer(String player);

    public abstract void updateBoard(ItemCard[][] board);

    public abstract void updateBookshelf(String nickname, ItemCard[][] bookshelf);

    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
        //aggiornare tiles selezionate SOLO per la scena PutCardsScene
    }

    public abstract void comGoal(Map<Integer, Integer> playerCommonGoal);

    void updateBoard(ItemCard[][] board);


}
