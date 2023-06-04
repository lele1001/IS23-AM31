package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GameModelTest {

    @Test
    void insertCard() {
        GameModel gameModel = new GameModel();
        ConnectionControl connectionControl = new ConnectionControl(new Server());
        GameController gameController = new GameController(connectionControl);

        gameModel.setListener(gameController);
        gameModel.CreateGame(new ArrayList<>(List.of("Luca", "Filippo", "Giovanni", "Topolino")), null);
        System.out.println(Arrays.deepToString(gameModel.board.getAsArrayList()));

        try {
            gameModel.selectCard("Luca", new ArrayList<>(List.of(32, 33)));
        } catch (Exception e) {
            System.out.println("Eccezione");
        }
        try {
            gameModel.selectCard("Filippo", new ArrayList<>(List.of(3, 4)));
        } catch (Exception e) {
            System.out.println("Eccezione not Right");
        }

        try {
            gameModel.InsertCard("Luca", new ArrayList<>(List.of(new ItemCard(HouseItem.Games, ItemNumber.Third), new ItemCard(HouseItem.Books, ItemNumber.Second))), 2);
        } catch (NoBookshelfSpaceException e) {
            System.out.println("Eccezione Space");
        } catch (NotSameSelectedException e) {
            System.out.println("Eccezione not Same");
        }
    }


    @Test
    void selectCard() {
    }
}