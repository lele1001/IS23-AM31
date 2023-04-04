package it.polimi.ingsw.server.controller;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GameControllerTest {

    @Test
    void createGame() {

        System.out.println("Case: 2 players");

        GameController gameController2 = new GameController(new ConnectionControl());
        gameController2.createGame(new ArrayList<>(List.of("Giacomo", "Nicolas")));

        System.out.println("Case: 3 players");

        GameController gameController3 = new GameController(new ConnectionControl());
        gameController3.createGame(new ArrayList<>(List.of("Andrea", "Luca", "Alice")));


        System.out.println("Case: 4 players");

        GameController gameController4 = new GameController(new ConnectionControl());
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")));

        System.out.println("Case: 5 players");

        GameController gameController5 = new GameController(new ConnectionControl());
        gameController5.createGame(new ArrayList<>(List.of("Riccardo", "Eleonora", "Alice", "Topolino", "Andrea")));
    }

    @Test
    void run() {
        GameController gameController4 = new GameController(new ConnectionControl());
        gameController4.createGame(new ArrayList<>(List.of("Topolino", "Eleonora", "Luigi", "Niccolò")));
        new Thread(() -> {
            gameController4.run();
        }
        ).start();
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));
        gameController4.selectCard("Topolino", new ArrayList<>(List.of(3,4)));





    }
}
