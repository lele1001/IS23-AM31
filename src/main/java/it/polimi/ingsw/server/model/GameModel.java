package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.gameExceptions.EmptyCardBagException;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import it.polimi.ingsw.server.model.comGoals.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import static it.polimi.ingsw.utils.ModelPropertyChange.*;

/**
 * Class that defines all the possible action on the Model and send response to the GameController
 */
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
     * @param players is the list with all the players' nicknames
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
            evt = new PropertyChangeEvent("null", BOARD_CHANGED, null, board.getAsArrayList());
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
        evt = new PropertyChangeEvent(firstGoal, COM_GOAL_CREATED, null, comGoals.get(0).getCurrScore());
        this.listener.propertyChange(evt);

        comGoals.add(selectComGoal(secondGoal, players.size()));
        evt = new PropertyChangeEvent(secondGoal, COM_GOAL_CREATED, null, comGoals.get(1).getCurrScore());
        this.listener.propertyChange(evt);

        ArrayList<PersGoal> persGoals = new ArrayList<>(Arrays.asList(PersGoal.values()));
        Collections.shuffle(persGoals);

        for (String s : players) {
            this.playerMap.put(s, new Player(s));
            playerMap.get(s).assignPersGoal(persGoals.get(0));
            System.out.println("PersGoal " + persGoals.get(0) + " assigned to " + s);
            evt = new PropertyChangeEvent(s, PERS_GOAL_CREATED, null, persGoals.get(0).toString());
            this.listener.propertyChange(evt);
            persGoals.remove(0);
        }

        this.gameJson.addProperty("lastPlayer", players.get(players.size() - 1));
        saveJson(true);
    }

    /**
     * Called at the beginning of the game when the first player wants to resume one of the game he's into.
     *
     * @param onlinePlayers: the list of the players of this game already online and ready to play.
     * @param json:          the jsonObject with all the details of the game (taken from the file).
     * @param gameFilePath:  the path of the file with all game's details.
     */
    public void resumeGame(ArrayList<String> onlinePlayers, JsonObject json, String gameFilePath) {
        this.gameJson = json;
        this.gameFilePath = gameFilePath;
        Gson gson = new Gson();

        //Gets the players from the file and saves them in a list of Strings
        List<String> players = Arrays.asList(gson.fromJson(json.get("nicknames").getAsString(), String[].class));

        this.board = new Board(gson.fromJson(json.get("board").getAsString(), ItemCard[][].class), new ArrayList<>(Arrays.asList(gson.fromJson(json.get("cardBag").getAsString(), ItemCard[].class))), players.size());
        System.out.println("Board restored");

        comGoals.add(gson.fromJson(json.get("firstComGoal").getAsString(), selectComGoal(gson.fromJson(json.get("firstComGoal").getAsString().replace("\\", ""), JsonObject.class).get("CGID").getAsInt(), 3).getClass()));
        comGoals.add(gson.fromJson(json.get("secondComGoal").getAsString(), selectComGoal(gson.fromJson(json.get("secondComGoal").getAsString().replace("\\", ""), JsonObject.class).get("CGID").getAsInt(), 3).getClass()));

        System.out.println("ComGoals restored: " + comGoals.get(0).getCGID() + " punti: " + comGoals.get(0).getCurrScore() + "," + comGoals.get(1).getCGID() + " punti: " + comGoals.get(1).getCurrScore() + ".");

        for (String s : players) {
            this.playerMap.put(s, gson.fromJson(json.get(s).getAsString(), Player.class));
        }

        try {
            this.winner = gameJson.get("winner").getAsString();
        } catch (Exception e) {
            System.out.println("Restored game doesn't have a winner yet.");
        }

        for (String s : onlinePlayers) {
            this.sendGameDetails(s);
        }
    }

    /**
     * Tries to insert cards in a nickname's bookshelf.
     *
     * @param nickname of the owner of the bookshelf
     * @param cards    to be inserted into the bookshelf
     * @param column   of the bookshelf to insert cards into
     * @throws NoBookshelfSpaceException if there's no space in the column indicated
     * @throws NotSameSelectedException  if the player wants to insert cards different from the ones selected.
     */
    public void InsertCard(String nickname, ArrayList<ItemCard> cards, int column) throws NoBookshelfSpaceException, NotSameSelectedException {
        //Checks if the player wants to insert the previously selected cards
        if (!((cards.containsAll(selected)) && (selected.containsAll(cards)) && (cards.size() == selected.size()))) {
            sendBoard(nickname);
            sendBookshelf(nickname);
            throw new NotSameSelectedException();
        }

        PropertyChangeEvent evt;
        boolean a;
        try {
            a = playerMap.get(nickname).insertCard(cards, column);
        } catch (NoBookshelfSpaceException e) {
            sendBookshelf(nickname);
            throw new NoBookshelfSpaceException();
        }
        evt = new PropertyChangeEvent(nickname, BOOKSHELF_RENEWED, column, cards.toArray(new ItemCard[0]));
        this.listener.propertyChange(evt);

        if (a && winner == null) {
            winner = nickname;
            gameJson.addProperty("winner", winner);
            evt = new PropertyChangeEvent(nickname, BOOKSHELF_COMPLETED, null, null);
            this.listener.propertyChange(evt);
        }

        //Insertion completed
        board.backupBoard();
    }

    /**
     * Tries to select cards from the board.
     *
     * @param positions of the cards to be selected.
     * @throws NoRightItemCardSelection  if the selection is not valid.
     * @throws NoBookshelfSpaceException if there's no enough space in player's bookshelf for the number of tiles he selected.
     */
    public void selectCard(String player, ArrayList<Integer> positions) throws NoRightItemCardSelection, NoBookshelfSpaceException {
        PropertyChangeEvent evt;
        if (!this.playerMap.get(player).checkBookshelfSpace(positions.size())) {
            sendBoard(player);
            sendBookshelf(player);
            throw new NoBookshelfSpaceException();
        }
        try {
            selected = board.deleteSelection(positions);
        } catch (NoRightItemCardSelection e) {
            sendBoard(player);
            throw new NoRightItemCardSelection();
        }
        evt = new PropertyChangeEvent("null", BOARD_RENEWED, null, positions.toArray(new Integer[0]));
        this.listener.propertyChange(evt);
    }

    /**
     * Set the controller as a GameModel's listener.
     */
    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Calculates all the players' final score and sends it to each of them
     *
     * @return a set (whose size is > 1 only in case of parity) with all the winners.
     */
    public LinkedHashMap<String, Integer> calcFinalScore() { //su tutti i player sulla mappa devo chiamare il metodo per calcolare il punteggio
        int temp;
        Map<String, Integer> finalScores = new HashMap<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        for (String s : playerMap.keySet()) {
            temp = playerMap.get(s).calculateFinScore();
            finalScores.put(s, s.equals(winner) ? temp + 1 : temp);
        }
        for (Integer i : finalScores.values().stream().sorted(Comparator.reverseOrder()).toList()) {
            for (String s : finalScores.keySet()) {
                if ((Objects.equals(finalScores.get(s), i)) && (!sortedMap.containsKey(s))) {
                    sortedMap.put(s, i);
                    break;
                }
            }
        }
        System.out.println("Final classify : " + sortedMap);
        return sortedMap;
    }

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
            if (playerMap.get(nickname).checkComGoal(c)) {
                ComGoalDone = true;
                int[] toSend = {c.getCGID(), c.getCurrScore()};
                evt = new PropertyChangeEvent(nickname, COM_GOAL_DONE, null, toSend);
                this.listener.propertyChange(evt);
            }
        }

        try {
            if (board.checkRefill()) {
                evt = new PropertyChangeEvent("null", BOARD_CHANGED, null, board.getAsArrayList());
                this.listener.propertyChange(evt);
            }
        } catch (EmptyCardBagException e) {
            evt = new PropertyChangeEvent("null", EMPTY_CARD_BAG, null, null); // posso anche unirlo a change board
            this.listener.propertyChange(evt);

            evt = new PropertyChangeEvent("null", BOARD_CHANGED, null, board.getAsArrayList()); // faccio sempre anche se non modifica fa nulla
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
        PropertyChangeEvent evt = new PropertyChangeEvent("null", BOARD_CHANGED, null, board.getAsArrayList());
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
        PropertyChangeEvent evt;
        sendBoard(nickname);

        //Sending all bookshelves...
        for (String s : playerMap.keySet()) {
            evt = new PropertyChangeEvent(s, BOOKSHELF_CHANGED, nickname, playerMap.get(s).getBookshelfAsMatrix());
            this.listener.propertyChange(evt);
        }

        //Sending his personal goal
        evt = new PropertyChangeEvent(nickname, PERS_GOAL_CREATED, null, playerMap.get(nickname).getPersGoal());
        this.listener.propertyChange(evt);

        //Sending common goals
        for (ComGoal c : comGoals) {
            evt = new PropertyChangeEvent(c.getCGID(), COM_GOAL_CREATED, nickname, c.getCurrScore());
            this.listener.propertyChange(evt);
        }

        //Sending player's actual score
        evt = new PropertyChangeEvent(nickname, PLAYER_SCORE, null, (nickname.equalsIgnoreCase(winner) ? playerMap.get(nickname).getScore() + 1 : playerMap.get(nickname).getScore()));
        this.listener.propertyChange(evt);
    }

    /**
     * A private method called at the end of the turn to save game's details on the json backup file.
     *
     * @param comGoalDone: a boolean that indicates if a common goal has been done and needs to be saved or not.
     */
    private void saveJson(boolean comGoalDone) {
        Gson gson = new Gson();
        gameJson.addProperty("board", gson.toJson(board.getAsArrayList()));
        gameJson.addProperty("cardBag", gson.toJson(board.getCardBag().toArray()));

        if (comGoalDone) {
            gameJson.addProperty("firstComGoal", gson.toJson(comGoals.get(0), ComGoal.class));
            gameJson.addProperty("secondComGoal", gson.toJson(comGoals.get(1), ComGoal.class));
        }

        for (String s : playerMap.keySet()) {
            gameJson.addProperty(s, gson.toJson(playerMap.get(s), Player.class));
        }

        try {
            PrintWriter gameFile = new PrintWriter(new BufferedWriter(new FileWriter(gameFilePath)));
            gameFile.print(gameJson);
            gameFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to write on game's file.");
        }
    }

    /**
     * A private method used to send all the board when the player returns in the game or there's an error in select/insert phase from him.
     *
     * @param player to send board to.
     */
    private void sendBoard(String player) {
        PropertyChangeEvent evt = new PropertyChangeEvent("null", BOARD_CHANGED, player, board.getAsArrayList());
        this.listener.propertyChange(evt);

    }

    /**
     * A private method used to send all the bookshelf when the player returns in the game or there's an error in select/insert phase from him.
     *
     * @param player to send bookshelf to.
     */
    private void sendBookshelf(String player) {
        PropertyChangeEvent evt = new PropertyChangeEvent(player, BOOKSHELF_CHANGED, player, playerMap.get(player).getBookshelfAsMatrix());
        this.listener.propertyChange(evt);
    }
}


