package it.polimi.ingsw.server.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    void createGame() {
        GameController gameController = new GameController(new ConnectionControl());
        gameController.addPlayer("Andrea");
        gameController.addPlayer("Luca");
        gameController.addPlayer("Alice");
        gameController.createGame();
        //rifare stessa roba con più giocatori o meno
        //controllo che non inserisca più di 4 giocatori -> provo ad aggiungere più giocatori >4 e controllo che la stringa del metodo add player esca
    }
}