package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import it.polimi.ingsw.server.model.comGoals.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

public class GameModel implements ModelInterface {

    private final Map<String, Player> playerMap = new HashMap<>();
    public Board board;

    PropertyChangeListener listener;

    private final ArrayList<ComGoal> comGoals = new ArrayList<>();
    private ArrayList<ItemCard> selected = new ArrayList<>();
    String winner=null;

    /**
     * Builds the game
     */
    public GameModel() {

    }

    public void CreateGame(ArrayList<String> players) {
        PropertyChangeEvent evt;
        board = new Board(players.size());
        System.out.println("Board Created");
        try {
            board.fillBoard();
            System.out.println("Board Filled.");
            evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
            this.listener.propertyChange(evt);
        } catch (EmptyCardBagException e) {
            System.out.println("Impossible State");
        }

        Random random = new Random();
        int firstGoal;
        int secondGoal;
        do {
            firstGoal = random.nextInt(1, 12);
            secondGoal = random.nextInt(1, 12);
        } while (firstGoal == secondGoal);

        comGoals.add(selectComGoal(firstGoal, players.size()));
        System.out.println("ComGoals created: " + firstGoal + "," + secondGoal);
        evt = new PropertyChangeEvent(firstGoal, "COM_GOAL_CREATED", null, comGoals.get(0).getCurrScore());
        this.listener.propertyChange(evt);
        comGoals.add(selectComGoal(secondGoal, players.size()));
        evt = new PropertyChangeEvent(secondGoal, "COM_GOAL_CREATED", null, comGoals.get(1).getCurrScore());
        this.listener.propertyChange(evt);

        ArrayList<PersGoal> persGoals = new ArrayList<>(Arrays.asList(PersGoal.values()));
        Collections.shuffle(persGoals);
        for (String s : players) {
            this.playerMap.put(s, new Player(s));
            playerMap.get(s).assignPersGoal(persGoals.get(0));
            System.out.println("PersGoal " + persGoals.get(0) + " assigned to " + s);
            evt = new PropertyChangeEvent(s, "PERS_GOAL_CREATED", null, persGoals.get(0).toString());
            this.listener.propertyChange(evt);
            persGoals.remove(0);
        }
    }

    /**
     * Tries to insert cards in the bookshelf associated to the nickname
     *
     * @param nickname of the player
     * @param cards    to insert in the column
     * @param column   where cards have to be inserted
     *                 catches NoBookshelfSpaceException if there is no space
     */
    public void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException, NotSameSelectedException {
        // controllo se vuole inserire quelle che aveva selezionato
        if (!((cards.containsAll(selected)) && (selected.containsAll(cards)) && (cards.size() == selected.size())))
            throw new NotSameSelectedException();
        PropertyChangeEvent evt;
        boolean a;
        a = playerMap.get(nickname).insertCard(cards, column);
        evt = new PropertyChangeEvent(nickname, "BOOKSHELF_CHANGED", null, playerMap.get(nickname).getBookshelfAsMatrix());
        this.listener.propertyChange(evt);
        if (a && winner==null) {
            winner=nickname;
            evt = new PropertyChangeEvent(nickname, "BOOKSHELF_COMPLETED", null, null);
            this.listener.propertyChange(evt);
        }
        board.backupBoard();    // Inserimento andato a buon fine.
    }

    /**
     * Select cards from the board
     *
     * @param positions cards' positions
     */
    public void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection {  //anche questa eccezione verrà gestita nel controller

        PropertyChangeEvent evt;
        selected = board.deleteSelection(positions);
        evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
        this.listener.propertyChange(evt);
    }

    /**
     * Set the controller as listener
     */
    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    /**
     * @param nickname of the player to be checked
     * @return true if the player is online
     */
    public boolean isPlayerOnline(String nickname) {
        return playerMap.get(nickname).isOnline();
    }

    /**
     * Calculates all the players' final score and sends it to each of them
     *
     * @return a set (whose size is > 1 only in case of parity) with all the winners.
     */
    public Set<String> calcFinalScore() { //su tutti i player sulla mappa devo chiamare il metodo per calcolare il punteggio
        int temp;
        int max = 0;
        Map<String, Integer> finalScores = new HashMap<>();

        for (String s : playerMap.keySet()) {
            temp = playerMap.get(s).calculateFinScore();
            if(s.equals(winner)) {
                temp++;
            }
            finalScores.put(s, temp);
            PropertyChangeEvent evt = new PropertyChangeEvent(s, "FINAL_SCORE", null, temp);
            listener.propertyChange(evt);
            if (temp > max)
                max = temp;
        }

        for (String s : finalScores.keySet())
            if (finalScores.get(s) < max)
                finalScores.remove(s);
        return finalScores.keySet();
    }


    public void changePlayerStatus(String nickname) {
        playerMap.get(nickname).changePlayerStatus();

    }


    private ComGoal selectComGoal(int numComGoal, int numPlayers) {
        return switch (numComGoal) {
            case 1 -> new CG1(numPlayers, 1);
            case 2 -> new CG2_5(numPlayers, 2);
            case 3 -> new CG3_4(numPlayers, 3);
            case 4 -> new CG3_4(numPlayers, 4);
            case 5 -> new CG2_5(numPlayers, 5);
            case 6 -> new CG6_7(numPlayers, 6);
            case 7 -> new CG6_7(numPlayers, 7);
            case 8 -> new CG8(numPlayers, 8);
            case 9 -> new CG9(numPlayers, 9);
            case 10 -> new CG10(numPlayers, 10);
            case 11 -> new CG11_12(numPlayers, 11);
            case 12 -> new CG11_12(numPlayers, 12);
            default -> null;
        };
    }

    public void EndTurn(String nickname) {
        boolean ComGoalDone;
        //boolean playerPointUpdate = false;
        PropertyChangeEvent evt;
        for (ComGoal c : comGoals) {
            ComGoalDone = playerMap.get(nickname).checkComGoal(c);
            if (ComGoalDone) {
                int[] toSend = {c.getCGID(), c.getCurrScore()};
                evt = new PropertyChangeEvent(nickname, "COM_GOAL_DONE", null, toSend);
                this.listener.propertyChange(evt);
                //playerPointUpdate = true;
            }
        }
        try {
            if (board.checkRefill()) {
                evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
                this.listener.propertyChange(evt);
            }
        } catch (EmptyCardBagException e) {
            evt = new PropertyChangeEvent("null", "EMPTY_CARD_BAG", null, null); // posso anche unirlo a change board
            this.listener.propertyChange(evt);
            evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList()); // faccio sempre anche se non modifica fa nulla
            this.listener.propertyChange(evt);
        }

    }

    @Override
    public void resumeBoard() {
        board.resumeBoard();
        PropertyChangeEvent evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
        this.listener.propertyChange(evt);
    }
// TODO: (luigi) sending players points
    @Override
    public void sendGameDetails (String nickname) {
        // Sending board...
        PropertyChangeEvent evt = new PropertyChangeEvent("null", "BOARD_CHANGED", nickname, board.getAsArrayList());
        this.listener.propertyChange(evt);

        // Sending all bookshelves...
        for (String s : playerMap.keySet()) {
            evt = new PropertyChangeEvent(s, "BOOKSHELF_CHANGED", nickname, playerMap.get(s).getBookshelfAsMatrix());
            this.listener.propertyChange(evt);
        }

        // Sending his personal goal
        evt = new PropertyChangeEvent(nickname, "PERS_GOAL_CREATED", null, playerMap.get(nickname).getpersGoal());
        this.listener.propertyChange(evt);

        // Sending common goals
        for (ComGoal c : comGoals) {
            evt = new PropertyChangeEvent(c.getCGID(), "COM_GOAL_CREATED", nickname, c.getCurrScore());
            this.listener.propertyChange(evt);
        }
    }
}


