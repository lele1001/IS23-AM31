package it.polimi.ingsw.server.model;


import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.model.comGoals.*;

import java.beans.PropertyChangeListener;
import java.util.*;

public class GameModel {

    private final Map<String, Player> playerMap = new HashMap<>();
    private final Board board;

    PropertyChangeListener listener;

    private final ArrayList<ComGoal> comGoals = new ArrayList<>();
    /**
     * Builds the game
     * @param players list of nicknames
     */
    public GameModel(ArrayList<String> players) {

        board = new Board(players.size());
        Random random = new Random();
        int firstGoal= 0;
        int secondGoal = 0;
        do{
            firstGoal= random.nextInt(1, 12);
            secondGoal = random.nextInt(1, 12);
        } while(firstGoal != secondGoal);

        comGoals.add(selectComGoal(firstGoal, players.size()));
        comGoals.add(selectComGoal(secondGoal, players.size()));

        ArrayList<PersGoal> persGoals = new ArrayList<>(Arrays.asList(PersGoal.values()));
        Collections.shuffle(persGoals);
        for(String s: players) {
            this.playerMap.put(s, new Player(s));
            playerMap.get(s).assignPersGoal(persGoals.get(0));
            persGoals.remove(0);
        }
    }

    /**
     * tries to insert cards in nickname's bookshelf
     * @param nickname
     * @param cards
     * @param column where cards have to be insert
     * @throws NoBookshelfSpaceException if there's no space
     */
    public void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException { //l'eccezione verrà gestita nel controller
            playerMap.get(nickname).insertCard(cards, column);
            //PropertyChangeEvent evt = new PropertyChangeEvent(//da mettere);
            //this.listener.properyChange(evt);

    }

    /**
     * Select cards from the board
     * @param positions cards' positions
     * @throws NoRightItemCardSelection if not all of the positions are available
     */
    public void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection {  //anche questa eccezione verrà gestita nel controller

        ArrayList<ItemCard> deleted;
           deleted = board.deleteSelection(positions);
            //PropertyChangeEvent evt = new PropertyChangeEvent(//da mettere);
            //this.listener.properyChange(evt);
    }

    /**
     * set the controller as listener
     * @param listener
     */
    public void setListener(PropertyChangeListener listener){
        this.listener = listener;
    }

    /**
     *
     * @param nickname nickname of the player to be checked
     * @return true if the player is online
     */
    public boolean isPlayerOnline(String nickname){
       return playerMap.get(nickname).isOnline();
    }

    /**
     * calculates all the players' final score and sends it to each of them
     * @return returns the nickname of the winner
     */
    public String calcFinalScore() { //su tutit i player sulla mappa devo chiamare il metodo per calcolare il punteggio
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

    public void changePlayerStatus(String nickname) {
        playerMap.get(nickname).changePlayerStatus();

    }

    private ComGoal selectComGoal(int numComGoal, int numPlayers){

        switch (numComGoal){
            case 1:
                return new CG1(numPlayers);
            case 2:
                return new CG2_5(numPlayers, 6);
            case 3:
                return new CG3_4(numPlayers, 2);
            case 4:
                return new CG3_4(numPlayers, 1);
            case 5:
                return new CG2_5(numPlayers, 3);
            case 6:
                return new CG6_7(numPlayers, 5);
            case 7:
                return new CG6_7(numPlayers, 3);
            case 8:
                return new CG8(numPlayers);
            case 9:
                return new CG9(numPlayers);
            case 10:
                return new CG10(numPlayers);
            case 11:
                return new CG11_12(numPlayers, true);
            case 12:
                return new CG11_12(numPlayers, false);
            default:
                return null;
        }
    }

    public void EndTurn(String nickname) throws EmptyCardBagException {
        boolean ComGoalDone;
        for(ComGoal c: comGoals) {
            ComGoalDone=playerMap.get(nickname).checkComGoal(c);
            if(ComGoalDone); //if true listener update
        }
        board.checkRefill();
    }
}


