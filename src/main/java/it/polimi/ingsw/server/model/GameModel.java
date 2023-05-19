package it.polimi.ingsw.server.model;

import com.google.gson.*;
import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import it.polimi.ingsw.server.model.comGoals.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.*;

public class GameModel implements ModelInterface {

    private final Map<String, Player> playerMap = new HashMap<>();
    public Board board;

    PropertyChangeListener listener;

    private final ArrayList<ComGoal> comGoals = new ArrayList<>();
    private ArrayList<ItemCard> selected = new ArrayList<>();
    String winner = null;
    private String gameFilePath;
    private JsonObject gameJson;

    /**
     * Creates the game with all the necessary things (board, bookshelves, personal goals and common goals).
     *
     * @param players the list with all players' nicknames.
     */
    public void CreateGame(ArrayList<String> players, String gameFilePath) {
        this.gameJson = new JsonObject();
        this.gameFilePath = gameFilePath;
        this.gameJson.addProperty("nicknames", players.toString());

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
        this.gameJson.addProperty("lastPlayer", players.get(players.size() - 1));
        saveJson(true);
    }

    public void resumeGame(ArrayList<String> onlinePlayers, JsonObject json, String gameFilePath) {
        this.gameJson = json;
        this.gameFilePath = gameFilePath;
        Gson gson = new Gson();

        //Prendo i player dal file e li salvo in una lista di stringhe
        List<String> players = Arrays.asList(gson.fromJson(json.get("nicknames").getAsString(), String[].class));

        this.board = new Board(gson.fromJson(json.get("board").getAsString(), ItemCard[][].class), new ArrayList<>(Arrays.asList(gson.fromJson(json.get("cardBag").getAsString(), ItemCard[].class))), players.size());
        System.out.println("Board restored");

        comGoals.add(gson.fromJson(json.get("firstComGoal").getAsString(), selectComGoal(gson.fromJson(json.get("firstComGoal").getAsString().replace("\\", ""), JsonObject.class).get("CGID").getAsInt(), 3).getClass()));
        comGoals.add(gson.fromJson(json.get("secondComGoal").getAsString(), selectComGoal(gson.fromJson(json.get("secondComGoal").getAsString().replace("\\", ""), JsonObject.class).get("CGID").getAsInt(), 3).getClass()));

        System.out.println("ComGoals restored: " + comGoals.get(0).getCGID() + " punti: " + comGoals.get(0).getCurrScore() + "," + comGoals.get(1).getCGID() + " punti: " + comGoals.get(1).getCurrScore() + ".");

        for (String s : players)
            this.playerMap.put(s, gson.fromJson(json.get(s).getAsString(), Player.class));

        try {
            this.winner = gameJson.get("winner").getAsString();
        } catch (Exception e) {
            System.out.println("Restored game doesn't have a winner yet.");
        }

        for (String s : onlinePlayers)
            this.sendGameDetails(s);
    }

    /**
     * Tries to insert cards in a nickname's bookshelf.
     *
     * @param nickname the owner of the bookshelf.
     * @param cards    to be inserted into the bookshelf.
     * @param column   of the bookshelf to insert cards into.
     * @throws NoBookshelfSpaceException if there's no space in the column indicated.
     * @throws NotSameSelectedException  if the player wants to insert cards different from the ones selected.
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
        if (a && winner == null) {
            winner = nickname;
            gameJson.addProperty("winner", winner);
            evt = new PropertyChangeEvent(nickname, "BOOKSHELF_COMPLETED", null, null);
            this.listener.propertyChange(evt);
        }
        board.backupBoard();    // Inserimento andato a buon fine.
    }

    /**
     * Tries to select cards from the board.
     *
     * @param positions of the cards to be selected.
     * @throws NoRightItemCardSelection if the selection is not valid.
     */
    public void selectCard(ArrayList<Integer> positions) throws NoRightItemCardSelection {  //anche questa eccezione verrà gestita nel controller

        PropertyChangeEvent evt;
        selected = board.deleteSelection(positions);
        evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
        this.listener.propertyChange(evt);
    }

    /**
     * Set the controller as a GameModel's listener.
     */
    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

/*    public boolean isPlayerOnline(String nickname) {
        return playerMap.get(nickname).isOnline();
    }*/

    /**
     * Calculates all the players' final score and sends it to each of them
     *
     * @return a set (whose size is > 1 only in case of parity) with all the winners.
     */
    public ArrayList<String> calcFinalScore() { //su tutti i player sulla mappa devo chiamare il metodo per calcolare il punteggio
        int temp;
        int max = 0;
        Map<String, Integer> finalScores = new HashMap<>();
        ArrayList<String> winners;

        for (String s : playerMap.keySet()) {
            temp = playerMap.get(s).calculateFinScore();
            if (s.equals(winner)) {
                temp++;
            }
            finalScores.put(s, temp);
            PropertyChangeEvent evt = new PropertyChangeEvent(s, "FINAL_SCORE", null, temp);
            listener.propertyChange(evt);
            if (temp > max)
                max = temp;
        }

        winners = new ArrayList<>(finalScores.keySet().stream().toList());
        for (String s : finalScores.keySet())
            if (finalScores.get(s) < max)
                winners.remove(s);
        return winners;
    }

/*    public void changePlayerStatus(String nickname) {
        playerMap.get(nickname).changePlayerStatus();

    }*/

    /**
     * A private method used to select a CommonGoal from an integer.
     *
     * @param numComGoal the integer that represents the common goal.
     * @param numPlayers the players' number of the game.
     * @return the ComGoal just created.
     */
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

    /**
     * Called at the end of a turn, checks if common goals have been reached from the current player,
     * and if the board needs to be refilled.
     *
     * @param nickname of the current player.
     */
    public void EndTurn(String nickname) {
        boolean ComGoalDone = false;
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
        this.gameJson.addProperty("lastPlayer", nickname);
        saveJson(ComGoalDone);
    }

    /**
     * Called to resume the board when someone has selected tiles from it
     * but has disconnected before inserting them in their bookshelf.
     */
    @Override
    public void resumeBoard() {
        board.resumeBoard();
        PropertyChangeEvent evt = new PropertyChangeEvent("null", "BOARD_CHANGED", null, board.getAsArrayList());
        this.listener.propertyChange(evt);
    }

    /**
     * Used when a player comes back in a game and needs to have all game's information.
     *
     * @param nickname of the just returned player.
     */
    @Override
    public void sendGameDetails(String nickname) {
        // Sending board...
        PropertyChangeEvent evt = new PropertyChangeEvent("null", "BOARD_CHANGED", nickname, board.getAsArrayList());
        this.listener.propertyChange(evt);

        // Sending all bookshelves...
        for (String s : playerMap.keySet()) {
            evt = new PropertyChangeEvent(s, "BOOKSHELF_CHANGED", nickname, playerMap.get(s).getBookshelfAsMatrix());
            this.listener.propertyChange(evt);
        }

        // Sending his personal goal
        evt = new PropertyChangeEvent(nickname, "PERS_GOAL_CREATED", null, playerMap.get(nickname).getPersGoal());
        this.listener.propertyChange(evt);

        // Sending common goals
        for (ComGoal c : comGoals) {
            evt = new PropertyChangeEvent(c.getCGID(), "COM_GOAL_CREATED", nickname, c.getCurrScore());
            this.listener.propertyChange(evt);
        }

        // Sending player's actual score
        evt = new PropertyChangeEvent(nickname, "PLAYER_SCORE", null, (nickname.equalsIgnoreCase(winner) ? playerMap.get(nickname).getScore() + 1 : playerMap.get(nickname).getScore()));
        this.listener.propertyChange(evt);
    }

    private void saveJson(boolean comGoalDone) {
        Gson gson = new Gson();
        gameJson.addProperty("board", gson.toJson(board.getAsArrayList()));
        gameJson.addProperty("cardBag", gson.toJson(board.getCardBag().toArray()));

        if (comGoalDone) {
            gameJson.addProperty("firstComGoal", gson.toJson(comGoals.get(0), ComGoal.class));
            gameJson.addProperty("secondComGoal", gson.toJson(comGoals.get(1), ComGoal.class));
        }

        for (String s : playerMap.keySet())
            gameJson.addProperty(s, gson.toJson(playerMap.get(s), Player.class));

        try {
            PrintWriter gameFile = new PrintWriter(new BufferedWriter(new FileWriter(gameFilePath)));
            gameFile.print(gameJson);
            gameFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to write on game's file.");
        }
    }
}


