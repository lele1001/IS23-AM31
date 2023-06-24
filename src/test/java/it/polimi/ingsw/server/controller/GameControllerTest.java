package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.ItemNumber;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class GameControllerTest {

    @Test
    void createGame() {
        File file = new File("target/MyShelfieSavedGames/testCreate.json");
        System.out.println("Case: 2 players");

        GameController gameController2 = new GameController(new ConnectionControl(new Server()));
        gameController2.createGame(new ArrayList<>(List.of("Giacomo", "Nicolas")), "target/MyShelfieSavedGames/testCreate.json");

        System.out.println("Case: 3 players");

        GameController gameController3 = new GameController(new ConnectionControl(new Server()));
        gameController3.createGame(new ArrayList<>(List.of("Andrea", "Luca", "Alice")), "target/MyShelfieSavedGames/testCreate.json");


        System.out.println("Case: 4 players");

        GameController gameController4 = new GameController(new ConnectionControl(new Server()));
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")), "target/MyShelfieSavedGames/testCreate.json");

        System.out.println("Case: 5 players");

        GameController gameController5 = new GameController(new ConnectionControl(new Server()));
        gameController5.createGame(new ArrayList<>(List.of("Riccardo", "Eleonora", "Alice", "Topolino", "Andrea")), "target/MyShelfieSavedGames/testCreate.json");

        if (!file.delete())
            System.out.println("Error deleting game's file...");
    }

    @Test
    void run() throws IOException {
        GameController gameController4 = new GameController(new ConnectionControl(new Server()));
        File file = new File("target/MyShelfieSavedGames/test.json");
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")), "target/MyShelfieSavedGames/test.json");
        new Thread(() -> gameController4.run(0)).start();
        JsonObject jsonObject = new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class);
        String[] nicknames = new Gson().fromJson(jsonObject.get("nicknames").getAsString(), String[].class);

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Taking board created from json boardtest file.
        ItemCard[][] board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        // Taking cards we want to select from board.
        ArrayList<ItemCard> selected = new ArrayList<>(List.of(board[0][3], board[0][4]));
        // Selecting them from board.
        //test: NO RIGHT SELECTION
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(1, 2))); //null case with 2 cards
        //wrong selection of 3 cards
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(0, 1, 2))); //null case with 3 cards
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(3, 13, 22)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(4, 15, 26)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(66, 57, 48)));

        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(13, 23, 33)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(31, 32, 33)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(26, 35, 44)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(66, 55, 44)));

        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(3, 13, 23)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(31, 41, 51)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(73, 74, 75)));

        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(24, 34, 44)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(33, 34, 35)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(42, 53, 64)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(64, 55, 46)));
        //wrong 2 cards selection
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(4, 15)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(24, 33)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(36, 37)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(34, 44)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(41, 51)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(42, 53)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(54, 55)));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(57, 66)));

        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(3, 4)));
        // Giving model time to do all the operations.
        try {
            TimeUnit.MICROSECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*  gameController4.insertCard(nicknames[0], selected, 6);  */ //wrong insertion in bookshelf
        gameController4.insertCard(nicknames[0], selected, 3);
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Inserting cards just selected from board.

        // Trying some casual insert.
        gameController4.insertCard(nicknames[0], new ArrayList<>(List.of(new ItemCard(HouseItem.Books, ItemNumber.Second))), 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameController4.insertCard(nicknames[0], new ArrayList<>(List.of(new ItemCard(HouseItem.Books, ItemNumber.Second))), 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Reading board (to do after every "board changed" event).
        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][8], board[4][8]));
        //test: NOT YOUR TURN - Topolino asks to select cards from the board
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(3)));

        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(38, 48)));
        gameController4.insertCard(nicknames[1], selected, 3);
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[8][4], board[8][5]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(84, 85)));
        gameController4.insertCard(nicknames[2], selected, 3);
        try {
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][7], board[4][7], board[5][7]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(37, 47, 57)));
        gameController4.insertCard(nicknames[3], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[1][3], board[1][4], board[1][5]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(13, 14, 15)));
        gameController4.insertCard(nicknames[0], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][6], board[3][6], board[4][6]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(26, 36, 46)));
        gameController4.insertCard(nicknames[1], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[4][5], board[3][5], board[2][5]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(45, 35, 25)));
        gameController4.insertCard(nicknames[2], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][4], board[3][4], board[4][4]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(24, 34, 44)));
        gameController4.insertCard(nicknames[3], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[4][0], board[5][0]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(40, 50)));
        gameController4.insertCard(nicknames[0], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][1], board[4][1], board[5][1]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(31, 41, 51)));
        gameController4.insertCard(nicknames[1], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][2], board[3][2], board[4][2]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(22, 32, 42)));
        gameController4.insertCard(nicknames[2], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][2], board[6][2]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(52, 62)));
        gameController4.insertCard(nicknames[3], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][3], board[3][3], board[4][3]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(23, 33, 43)));
        gameController4.insertCard(nicknames[0], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][6], board[6][6]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(56, 66)));
        gameController4.insertCard(nicknames[1], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][3], board[6][3], board[7][3]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(53, 63, 73)));
        gameController4.insertCard(nicknames[2], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][4], board[6][4], board[7][4]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(54, 64, 74)));
        gameController4.insertCard(nicknames[3], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][5], board[6][5], board[7][5]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(55, 65, 75)));
        gameController4.insertCard(nicknames[0], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[4][0], board[5][0]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(40, 50)));
        gameController4.insertCard(nicknames[1], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][8], board[4][8]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(38, 48)));
        gameController4.insertCard(nicknames[2], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][7], board[4][7]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(37, 47)));
        gameController4.insertCard(nicknames[3], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][7]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(57)));
        gameController4.insertCard(nicknames[0], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[0][3]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(3)));
        gameController4.insertCard(nicknames[1], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[1][3]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(13)));
        gameController4.insertCard(nicknames[2], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][1], board[4][1], board[5][1]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(31, 41, 51)));
        gameController4.insertCard(nicknames[3], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[0][4]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(4)));
        gameController4.insertCard(nicknames[0], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[1][5], board[1][4]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(15, 14)));
        gameController4.insertCard(nicknames[1], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][2], board[2][3], board[2][4]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(22, 23, 24)));
        gameController4.insertCard(nicknames[2], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][2], board[4][2], board[5][2]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(32, 42, 52)));
        gameController4.insertCard(nicknames[3], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][6]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(26)));
        gameController4.insertCard(nicknames[0], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[2][5]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(25)));
        gameController4.insertCard(nicknames[1], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][6], board[4][6]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(36, 46)));
        gameController4.insertCard(nicknames[2], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[8][4]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(84)));
        gameController4.insertCard(nicknames[3], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[8][5]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(85)));
        gameController4.insertCard(nicknames[0], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][6]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(56)));
        gameController4.insertCard(nicknames[1], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][3], board[3][4]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(33, 34)));
        gameController4.insertCard(nicknames[2], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[3][5], board[4][5]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(35, 45)));
        gameController4.insertCard(nicknames[3], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[7][3], board[7][4], board[7][5]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(73, 74, 75)));
        gameController4.insertCard(nicknames[0], selected, 1);
        gameController4.insertCard(nicknames[0], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[6][2], board[6][3], board[6][4]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(62, 63, 64)));
        gameController4.insertCard(nicknames[1], selected, 3);
        gameController4.insertCard(nicknames[1], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[6][5], board[6][6]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(65, 66)));
        gameController4.insertCard(nicknames[2], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][3], board[5][4], board[5][5]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(53, 54, 55)));
        gameController4.insertCard(nicknames[3], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[4][3], board[4][4]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(43, 44)));
        gameController4.insertCard(nicknames[0], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[7][3], board[7][4]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(73, 74)));
        gameController4.insertCard(nicknames[1], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[6][2], board[6][3], board[6][4]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(62, 63, 64)));
        gameController4.insertCard(nicknames[2], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[5][0], board[5][1], board[5][2]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(50, 51, 52)));
        gameController4.insertCard(nicknames[3], selected, 2);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[0][3], board[0][4]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(3, 4)));
        gameController4.insertCard(nicknames[0], selected, 3);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[1][3], board[1][4], board[1][5]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(13, 14, 15)));
        gameController4.insertCard(nicknames[1], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);
        selected = new ArrayList<>(List.of(board[4][0], board[4][1], board[4][2]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(40, 41, 42)));
        gameController4.insertCard(nicknames[2], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        selected = new ArrayList<>(List.of(board[5][7]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(57)));
        gameController4.insertCard(nicknames[3], selected, 0);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        selected = new ArrayList<>(List.of(board[2][2], board[2][3], board[2][4]));
        gameController4.selectCard(nicknames[0], new ArrayList<>(List.of(22, 23, 24)));
        gameController4.insertCard(nicknames[0], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        selected = new ArrayList<>(List.of(board[6][5], board[6][6]));
        gameController4.selectCard(nicknames[1], new ArrayList<>(List.of(65, 66)));
        gameController4.insertCard(nicknames[1], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        selected = new ArrayList<>(List.of(board[2][5]));
        gameController4.selectCard(nicknames[2], new ArrayList<>(List.of(25)));
        gameController4.insertCard(nicknames[2], selected, 4);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        selected = new ArrayList<>(List.of(board[2][6]));
        gameController4.selectCard(nicknames[3], new ArrayList<>(List.of(26)));
        gameController4.insertCard(nicknames[3], selected, 1);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void resumeGame() throws IOException {
        File file = new File("target/MyShelfieSavedGames/testResume.json");
        System.out.println("Case: 2 players");

        GameController gameController2 = new GameController(new ConnectionControl(new Server()));
        gameController2.createGame(new ArrayList<>(List.of("Giacomo", "Nicolas")), "target/MyShelfieSavedGames/testResume.json");
        gameController2.resumeGame(new ArrayList<>(List.of("Giacomo")), new ArrayList<>(List.of("Giacomo", "Nicolas")), new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class), "target/MyShelfieSavedGames/testResume.json");

        System.out.println("Case: 3 players");

        GameController gameController3 = new GameController(new ConnectionControl(new Server()));
        gameController3.createGame(new ArrayList<>(List.of("Andrea", "Luca", "Alice")), "target/MyShelfieSavedGames/testResume.json");
        gameController2.resumeGame(new ArrayList<>(List.of("Luca", "Alice")), new ArrayList<>(List.of("Andrea", "Luca", "Alice")), new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class), "target/MyShelfieSavedGames/testResume.json");

        System.out.println("Case: 4 players");

        GameController gameController4 = new GameController(new ConnectionControl(new Server()));
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")), "target/MyShelfieSavedGames/testResume.json");
        gameController2.resumeGame(new ArrayList<>(List.of("Topolino", "Eleonora")), new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")), new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class), "target/MyShelfieSavedGames/testResume.json");

        if (!file.delete())
            System.out.println("Error deleting game's file...");
    }

    @Test
    void sendGameDetails() throws IOException {
        File file = new File("target/MyShelfieSavedGames/testDetails.json");
        GameController gameController = new GameController(new ConnectionControl(new Server()));
        gameController.createGame(new ArrayList<>(List.of("Andrea", "Luca", "Alice", "Paolo")), "target/MyShelfieSavedGames/testDetails.json");
        new Thread(() -> gameController.run(0)).start();
        JsonObject jsonObject = new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class);
        String[] nicknames = new Gson().fromJson(jsonObject.get("nicknames").getAsString(), String[].class);

        gameController.sendGameDetails("Andrea");
        gameController.sendGameDetails("Luca");
        gameController.sendGameDetails("Alice");

        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Taking board created from json boardtest file.
        ItemCard[][] board = new Gson().fromJson(new Gson().fromJson(Files.readString(file.toPath()), JsonObject.class).get("board").getAsString(), ItemCard[][].class);

        gameController.selectCard(nicknames[0], new ArrayList<>(List.of(3, 4)));
        // Giving model time to do all the operations.
        try {
            TimeUnit.MICROSECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ArrayList<ItemCard> selected = new ArrayList<>(List.of(board[0][3], board[0][4]));
        gameController.insertCard(nicknames[0], selected, 3);
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameController.sendGameDetails(nicknames[0]);

        if (!file.delete())
            System.out.println("Error deleting test file.");
    }
}

