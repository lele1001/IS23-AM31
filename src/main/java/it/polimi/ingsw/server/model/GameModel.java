package it.polimi.ingsw.server.model;


import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.model.comGoals.*;

import java.beans.PropertyChangeEvent;
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
        PropertyChangeEvent evt=null;

        board = new Board(players.size());
        try {
            board.fillBoard();
            evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
            this.listener.propertyChange(evt);
        }
        catch(EmptyCardBagException e){
            System.out.println("Impossible State");
        }

        Random random = new Random();
        int firstGoal;
        int secondGoal;
        do{
            firstGoal= random.nextInt(1, 12);
            secondGoal = random.nextInt(1, 12);
        } while(firstGoal != secondGoal);

        comGoals.add(selectComGoal(firstGoal, players.size()));
        evt = new PropertyChangeEvent("null", "COM_GOAL_CREATED", null, new HashMap<Integer,Integer>(firstGoal,comGoals.get(0).getCurrScore()));
        this.listener.propertyChange(evt);
        comGoals.add(selectComGoal(secondGoal, players.size()));
        evt = new PropertyChangeEvent("null", "COM_GOAL_CREATED", null, new HashMap<>(secondGoal,comGoals.get(1).getCurrScore()));
        this.listener.propertyChange(evt);

        ArrayList<PersGoal> persGoals = new ArrayList<>(Arrays.asList(PersGoal.values()));
        Collections.shuffle(persGoals);
        for(String s: players) {
            this.playerMap.put(s, new Player(s));
            playerMap.get(s).assignPersGoal(persGoals.get(0));
            evt = new PropertyChangeEvent(s, "PERS_GOAL_CREATED", null, persGoals.get(0).toString());
            this.listener.propertyChange(evt);
            persGoals.remove(0);
        }
    }

    /**
     * tries to insert cards in nickname's bookshelf
     * @param nickname
     * @param cards
     * @param column where cards have to be inserted
     * catch NoBookshelfSpaceException if there's no space
     */
    public void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException{
        PropertyChangeEvent evt = null;
            boolean a;
            a = playerMap.get(nickname).insertCard(cards, column);
            evt = new PropertyChangeEvent(nickname, "BOOKSHELF_CHANGED", null, playerMap.get(nickname).getBookshelfAsMatrix());
            this.listener.propertyChange(evt);
            if(a){
                evt = new PropertyChangeEvent(nickname, "BOOKSHELF_COMPLETED", null, null);
                this.listener.propertyChange(evt);
            }
    }

    /**
     * Select cards from the board
     * @param positions cards' positions
     */
    public void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection{  //anche questa eccezione verrà gestita nel controller

        PropertyChangeEvent evt = null;
            board.deleteSelection(positions);
            evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
            this.listener.propertyChange(evt);

/*        PropertyChangeEvent evt = new PropertyChangeEvent(, "BOARD_CHANGED", , board.getAsArrayList());
        this.listener.propertyChange(evt);*/
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
    public String calcFinalScore() { //su tutti i player sulla mappa devo chiamare il metodo per calcolare il punteggio
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
        return switch (numComGoal) {
            case 1 -> new CG1(numPlayers);
            case 2 -> new CG2_5(numPlayers, 6);
            case 3 -> new CG3_4(numPlayers, 2);
            case 4 -> new CG3_4(numPlayers, 1);
            case 5 -> new CG2_5(numPlayers, 3);
            case 6 -> new CG6_7(numPlayers, 5);
            case 7 -> new CG6_7(numPlayers, 3);
            case 8 -> new CG8(numPlayers);
            case 9 -> new CG9(numPlayers);
            case 10 -> new CG10(numPlayers);
            case 11 -> new CG11_12(numPlayers, true);
            case 12 -> new CG11_12(numPlayers, false);
            default -> null;
        };
    }

    public void EndTurn(String nickname) {
        boolean ComGoalDone;
        boolean playerPointUpdate=false;
        PropertyChangeEvent evt = null;
        for(ComGoal c: comGoals) {
            ComGoalDone=playerMap.get(nickname).checkComGoal(c);
            if(ComGoalDone){
                evt = new PropertyChangeEvent(nickname, "COM_GOAL_DONE", null, new HashMap<>(comGoals.indexOf(c),c.getCurrScore()));
                this.listener.propertyChange(evt);
                playerPointUpdate=true;
            }
        }
 /*
  if(playerPointUpdate){
            evt = new PropertyChangeEvent(nickname, "PLAYER_POINT_UPDATE", null, playerMap.get(nickname).getScore());
            this.listener.propertyChange(evt);
        } */
        try {
            if(board.checkRefill())
                evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
        } catch (EmptyCardBagException e) {
            evt = new PropertyChangeEvent("null", "EMPTY_CARD_BAG", null, null); // posso anche unirlo a change board
            this.listener.propertyChange(evt);
            evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList()); // faccio sempre anche se non modifica fa nulla
        }
        finally {
            this.listener.propertyChange(evt);
        }

    }
}


