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

    public abstract void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles);

    public abstract void comGoal(Map<Integer, Integer> playerCommonGoal);

    public abstract void persGoal(String newValue);

    public abstract void printPoints(int myPoint);

    public abstract void setPlayers(int playersNumber);
}
