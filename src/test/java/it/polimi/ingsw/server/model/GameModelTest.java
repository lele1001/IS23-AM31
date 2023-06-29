package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.commons.ItemCard;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GameModelTest {

    /**
     * Testing insertCard method, with its exceptions, taking the board from the test.json file (where the game model saves game's details).
     */
    @Test
    void insertCard() throws IOException, NoBookshelfSpaceException, NoRightItemCardSelection, NotSameSelectedException {
        File file = new File("target/MyShelfieSavedGames/test.json");
        GameModel gameModel = new GameModel();
        ConnectionControl connectionControl = new ConnectionControl(new Server());
        GameController gameController = new GameController(connectionControl);

        gameModel.setListener(gameController);
        gameModel.CreateGame(new ArrayList<>(List.of("Luca", "Filippo", "Giovanni", "Topolino")), "target/MyShelfieSavedGames/test.json");

        ItemCard[][] board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        gameModel.selectCard("Luca", new ArrayList<>(List.of(3, 4)));
        assertThrows(NotSameSelectedException.class, () -> gameModel.InsertCard("Luca", new ArrayList<>(List.of(board[0][3])), 0));
        gameModel.InsertCard("Luca", new ArrayList<>(List.of(board[0][3], board[0][4])), 0);
        gameModel.selectCard("Filippo", new ArrayList<>(List.of(22)));
        gameModel.InsertCard("Filippo", new ArrayList<>(List.of(board[2][2])), 2);
        gameModel.selectCard("Luca", new ArrayList<>(List.of(13, 14, 15)));
        gameModel.InsertCard("Luca", new ArrayList<>(List.of(board[1][3], board[1][4], board[1][5])), 0);
        gameModel.selectCard("Filippo", new ArrayList<>(List.of(23)));
        assertThrows(NotSameSelectedException.class, () -> gameModel.InsertCard("Luca", new ArrayList<>(List.of(board[2][4])), 0));
        gameModel.InsertCard("Filippo", new ArrayList<>(List.of(board[2][3])), 1);
        gameModel.selectCard("Luca", new ArrayList<>(List.of(40, 50)));
        assertThrows(NoBookshelfSpaceException.class, () -> gameModel.InsertCard("Luca", new ArrayList<>(List.of(board[4][0], board[5][0])), 0));
        gameModel.InsertCard("Luca", new ArrayList<>(List.of(board[4][0], board[5][0])), 1);
    }

    /**
     * Tests selectCard method with its exceptions.
     */
    @Test
    void selectCard() throws NoBookshelfSpaceException, NoRightItemCardSelection {
        GameModel gameModel = new GameModel();
        ConnectionControl connectionControl = new ConnectionControl(new Server());
        GameController gameController = new GameController(connectionControl);

        gameModel.setListener(gameController);
        gameModel.CreateGame(new ArrayList<>(List.of("Luca", "Filippo", "Giovanni", "Topolino")), "target/MyShelfieSavedGames/test.json");

        assertThrows(NoRightItemCardSelection.class, () -> gameModel.selectCard("Luca", new ArrayList<>(List.of(32, 33))));
        assertThrows(NoRightItemCardSelection.class, () -> gameModel.selectCard("Luca", new ArrayList<>(List.of(32, 33, 34, 35))));

        gameModel.selectCard("Luca", new ArrayList<>(List.of(3)));
        assertThrows(NoRightItemCardSelection.class, () -> gameModel.selectCard("Giovanni", new ArrayList<>(List.of(55))));
        assertThrows(NoRightItemCardSelection.class, () -> gameModel.selectCard("Luca", new ArrayList<>(List.of(42, 43))));
        assertThrows(NoRightItemCardSelection.class, () -> gameModel.selectCard("Luca", new ArrayList<>(List.of(40, 41))));
    }
}