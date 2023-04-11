package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.ItemNumber;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class GameControllerTest {

    @Test
    void createGame() {

        System.out.println("Case: 2 players");

        GameController gameController2 = new GameController(new ConnectionControl(new Server()));
        gameController2.createGame(new ArrayList<>(List.of("Giacomo", "Nicolas")));

        System.out.println("Case: 3 players");

        GameController gameController3 = new GameController(new ConnectionControl(new Server()));
        gameController3.createGame(new ArrayList<>(List.of("Andrea", "Luca", "Alice")));


        System.out.println("Case: 4 players");

        GameController gameController4 = new GameController(new ConnectionControl(new Server()));
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")));

        System.out.println("Case: 5 players");

        GameController gameController5 = new GameController(new ConnectionControl(new Server()));
        gameController5.createGame(new ArrayList<>(List.of("Riccardo", "Eleonora", "Alice", "Topolino", "Andrea")));
    }

    @Test
    void run() throws IOException {


        GameController gameController4 = new GameController(new ConnectionControl(new Server()));
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")));
        new Thread(() -> {
            gameController4.run();
        }
        ).start();
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Path path = Paths.get("src/test/java/it/polimi/ingsw/server/controller/boardTest.json");

        Gson gson = new Gson();
        ItemCard[][] board = gson.fromJson(Files.readString(path), ItemCard[][].class);

        ArrayList<ItemCard> selected = new ArrayList<>(List.of(board[0][3]));

        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3)));

        try {
            TimeUnit.MICROSECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameController4.insertCard("Topolino", selected,1);

        gameController4.insertCard("Topolino", new ArrayList<>(List.of(new ItemCard(HouseItem.Books, ItemNumber.Second))),1);
        gameController4.insertCard("Topolino", new ArrayList<>(List.of(new ItemCard(HouseItem.Books, ItemNumber.Second))),1);

        board = gson.fromJson(Files.readString(path), ItemCard[][].class);

        selected = new ArrayList<>(List.of(board[0][4], board[1][4]));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3)));
        gameController4.selectCard("Eleonora", new ArrayList<>(List.of(13, 4)));
        gameController4.selectCard("Eleonora", new ArrayList<>(List.of(4)));
        gameController4.insertCard("Eleonora", selected,1);

    }
}
