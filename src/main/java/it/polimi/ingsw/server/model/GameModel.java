package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameModel {

    private final Map<String, Player> playerMap = new HashMap<>();
    private final Board board;

    PropertyChangeListener listener;

    /**
     * Builds the game
     * @param players list of nicknames
     */
    public GameModel(ArrayList<String> players) {
        for(String s: players) {
            this.playerMap.put(s, new Player(s));
        }
        board = new Board(players.size());
    }

    /**
     * tries to insert cards in nickname's bookshelf
     * @param nickname
     * @param cards
     * @param column where cards have to be insert
     * @throws NoBookshelfSpaceException if there's no space
     */
    void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException { //l'eccezione verrà gestita nel controller
            playerMap.get(nickname).insertCard(cards, column);
            //PropertyChangeEvent evt = new PropertyChangeEvent(//da mettere);
            //this.listener.properyChange(evt);

    }

    /**
     * Select cards from the board
     * @param positions cards' positions
     * @throws NoRightItemCardSelection if not all of the positions are available
     */
    void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection {  //anche questa eccezione verrà gestita nel controller

        ArrayList<ItemCard> deleted;
           deleted = board.deleteSelection(positions);
            //PropertyChangeEvent evt = new PropertyChangeEvent(//da mettere);
            //this.listener.properyChange(evt);
    }

    /**
     * set the controller as listener
     * @param listener
     */
    void setListener(PropertyChangeListener listener){
        this.listener = listener;
    }

    /**
     *
     * @param nickname nickname of the player to be checked
     * @return true if the player is online
     */
    boolean isPlayerOnline(String nickname){
       return playerMap.get(nickname).isOnline();
    }

    /**
     * calculates all the players' final score and sends it to each of them
     * @return returns the nickname of the winner
     */
    String calcFinalScore() { //su tutit i player sulla mappa devo chiamare il metodo per calcolare il punteggio
        //salvo punteggio e confronto quello più alto
        //return nickname di quello col punteggio più alto
        int temp;
        int max= 0;
        String nome = null;

        for(String s: playerMap.keySet()){//ciclo per ogni giocatore {
            temp = playerMap.get(s).calculateFinScore(); //da inviare a ogni singolo player
            if (temp > max) {
                max = temp;
                nome = s;
            }
        }
        return nome;
    }

    void changePlayerStatus(String nickname) {
        playerMap.get(nickname).changePlayerStatus();

    }

}


