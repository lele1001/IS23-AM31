package it.polimi.ingsw.server.controller;

import org.junit.jupiter.api.Test;

class GameControllerTest {

    @Test
    void createGame() {

        System.out.println("Case: 2 players");

        GameController gameController2 = new GameController(new ConnectionControl());
        gameController2.addPlayer("Giacomo");
        gameController2.addPlayer("Nicolas");
        gameController2.createGame();

        System.out.println("Case: 3 players");

        GameController gameController3 = new GameController(new ConnectionControl());
        gameController3.addPlayer("Andrea");
        gameController3.addPlayer("Luca");
        gameController3.addPlayer("Alice");
        gameController3.createGame();


        System.out.println("Case: 4 players");

        GameController gameController4 = new GameController(new ConnectionControl());
        gameController4.addPlayer("Topolino");
        gameController4.addPlayer("Eleonora");
        gameController4.addPlayer("Luigi");
        gameController4.addPlayer("Niccolò");
        gameController4.createGame();

        System.out.println("Case: 5 players");

        GameController gameController5 = new GameController(new ConnectionControl());
        gameController5.addPlayer("Riccardo");
        gameController5.addPlayer("Eleonora");
        gameController5.addPlayer("Alice");
        gameController5.addPlayer("Topolino");
        gameController5.addPlayer("Andrea");
    }
}