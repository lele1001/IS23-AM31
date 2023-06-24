package it.polimi.ingsw.server.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.ModelInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import static it.polimi.ingsw.utils.ModelPropertyChange.*;
import static it.polimi.ingsw.utils.Utils.*;

public class GameController implements PropertyChangeListener {
    private final ConnectionControl connectionControl;
    private final ArrayList<String> playersList = new ArrayList<>();
    private String currPlayer;
    private boolean winner;
    private ModelInterface gameModel;
    private TurnPhase turnPhase;
    private boolean gameIsActive;
    private boolean timeout = false;
    private String gameFilePath;

    /**
     * A simple constructor also used to set the reference for the ConnectionControl.
     *
     * @param connectionControl of the game.
     */
    public GameController(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
        gameIsActive = false;
        winner = false;
    }

    /**
     * Creates the game with all the necessary (board, bookshelves, ...) and starts it
     *
     * @param gameFilePath: the file's path to save game into.
     * @param playersList   the list of this game's players.
     */
    public void createGame(ArrayList<String> playersList, String gameFilePath) {
        this.gameFilePath = gameFilePath;

        if (playersList.size() < minNumberOfPlayers || playersList.size() > maxNumberOfPlayers) {
            System.out.println("Error: number of players not correct.");
            return;
        }

        // creates the list used to iterate on players
        this.playersList.addAll(playersList);
        Collections.shuffle(this.playersList);
        connectionControl.sendGameIsStarting(playersList, null);
        gameModel = new GameModel();

        // sets itself as a listener of the model
        gameModel.setListener(this);

        // creates the board, and the bookshelves
        // assigns personalGoals and commonGoals
        gameModel.CreateGame(this.playersList, gameFilePath);
    }

    /**
     * Called to resume game from file.
     *
     * @param onlinePlayers: players already online to play their game.
     * @param playersList:   all the players of the game.
     * @param json:          the jsonObject with game's details.
     * @param gameFilePath:  the path of the file with game's details.
     */
    public void resumeGame(ArrayList<String> onlinePlayers, List<String> playersList, JsonObject json, String gameFilePath) {
        this.gameFilePath = gameFilePath;
        this.playersList.addAll(playersList);
        gameModel = new GameModel();
        gameModel.setListener(this);

        try {
            json.get("winner").getAsString();
            this.winner = true;
        } catch (Exception e) {
            System.out.println("Restored game doesn't have a winner yet.");
        }

        this.currPlayer = json.get("lastPlayer").getAsString();
        connectionControl.sendGameIsStarting(new ArrayList<>(playersList), null);
        gameModel.resumeGame(onlinePlayers, json, gameFilePath);
    }

    /**
     * Calls each player's turn until somebody completes his/her Bookshelf.
     * If available players are less than two, stops the game and waits for their coming back for 60 seconds: if the timeout exceeds, ends the game.
     *
     * @param startFrom: the position, in playersList array list, that indicates the first player that has to play.
     */
    public void run(int startFrom) {
        gameIsActive = true;
        int i = (startFrom >= playersList.size() ? 0 : startFrom);

        while (!winner) {
            Timer timer = new Timer();

            if (playersList.stream().filter(connectionControl::isOnline).count() < 2) {
                System.out.println("Too many absents for this game.. waiting for players' returning in game.");
                connectionControl.sendGameInterrupted();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        winner = true;
                    }
                }, timeOfReturning);

                while (!winner) {
                    if (playersList.stream().filter(connectionControl::isOnline).count() >= 2) {
                        break;
                    }

                    try {
                        Thread.sleep(timeOfSleep);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep problem.");
                        break;
                    }
                }

                timer.cancel();

                if (winner) {
                    // Took too long! The winner is the remaining player (if it is still online!)
                    System.out.println(endForTimeFinished);
                    connectionControl.sendErrorToEveryone(endForTimeFinished);
                    List<String> remained = playersList.stream().filter(connectionControl::isOnline).toList();

                    if (remained.size() == 1) {
                        System.out.println(oneWinnerEndPhrase + remained.get(0));
                        LinkedHashMap<String, Integer> toBeSent = new LinkedHashMap<>();
                        toBeSent.put(remained.get(0), gameModel.calcFinalScore().get(remained.get(0)));
                        connectionControl.sendFinalScores(toBeSent);
                    }

                    connectionControl.onEndGame(gameFilePath);
                    return;
                } else {
                    connectionControl.sendErrorToEveryone("Game is resuming...");
                }
            }
            if (playersList.get(i).equals(currPlayer)) {
                System.out.println("Going to the next player...");
                i++;
            }
            playerTurn(i);

            if (i < playersList.size() - 1) {
                i++;
            } else {
                i = 0;
            }
        }

        runLastTurn(currPlayer);
    }

    /**
     * If the current player is not the first one, makes all the remaining one play their turn
     * Calls the method to calculate the final score and end the game.
     *
     * @param nickname: the nickname of the player that has just finished his turn.
     */
    public void runLastTurn(String nickname) {
        int i = playersList.indexOf(nickname) + 1;

        while (i < playersList.size()) {
            playerTurn(i);
            i++;
        }

        connectionControl.sendFinalScores(gameModel.calcFinalScore());
        connectionControl.onEndGame(gameFilePath);
    }

    /**
     * Waits for the player's action caught by ConnectionControl and calls the method to check and perform it.
     * Every player has three minutes to complete his turn:
     * if this timer exceeds, the player is set as offline, and the game continues with the next one.
     *
     * @param indexCurrPlayer: the playersList's index of the nickname that has to play.
     */
    private void playerTurn(int indexCurrPlayer) {
        currPlayer = playersList.get(indexCurrPlayer);

        if (connectionControl.isOnline(currPlayer)) {
            Timer timer = new Timer();
            System.out.println(playersList.get(indexCurrPlayer) + "'s turn");
            connectionControl.sendPlayerTurn(currPlayer);
            turnPhase = TurnPhase.SELECTCARDS;
            connectionControl.askSelect(currPlayer);

            timeout = false;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timeout = true;
                }
            }, timeOfTurn);

            while (!timeout) {
                if (!connectionControl.isOnline(currPlayer) || turnPhase == TurnPhase.ENDTURN) {
                    break;
                }

                try {
                    Thread.sleep(timeOfSleep);
                } catch (InterruptedException e) {
                    System.out.println("Sleep problem.");
                    break;
                }
            }

            timer.cancel();

            if (turnPhase == TurnPhase.ENDTURN) {
                gameModel.EndTurn(currPlayer);
            } else {
                if (timeout) {
                    // Took too long: timer expired!
                    connectionControl.SendError("Timeout exceeded: took too long! Disconnecting you from the game...", currPlayer);
                    connectionControl.changePlayerStatus(currPlayer, true);
                }

                if (turnPhase == TurnPhase.INSERTCARDS) {
                    gameModel.resumeBoard();
                }
            }
        }
    }

    /**
     * The method tries to insert the cards selected
     *
     * @param nickname who wants to insert on the bookshelf
     * @param cards    to be inserted
     * @param column   positions on the bookshelf
     */
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {
        if (!(nickname.equals(currPlayer)) || (turnPhase != TurnPhase.INSERTCARDS)) {
            connectionControl.SendError(notYourTurnResponse, nickname);
            return;
        }

        if (cards.size() > maxNumberOfSelectedCards || cards.isEmpty()) {
            connectionControl.SendError(notValidNumberResponse, nickname);
            return;
        }

        try {
            gameModel.InsertCard(nickname, cards, column);
        } catch (NoBookshelfSpaceException e) {
            connectionControl.SendError(noBookshelfSpaceResponse, nickname);
            connectionControl.askInsert(nickname);
        } catch (NotSameSelectedException e) {
            connectionControl.SendError(notSameCardsResponse, nickname);
            connectionControl.askInsert(nickname);
        }
    }

    /**
     * The method tries to select cards from board
     *
     * @param nickname  who wants to select
     * @param positions of the cards to be deleted
     */
    public void selectCard(String nickname, ArrayList<Integer> positions) {
        if (!(nickname.equals(currPlayer)) || (turnPhase != TurnPhase.SELECTCARDS)) {
            connectionControl.SendError(notYourTurnResponse, nickname);
            return;
        }
        try {
            gameModel.selectCard(nickname, positions);
        } catch (NoRightItemCardSelection e) {
            connectionControl.SendError(noRightBoardSelectionResponse, nickname);
            connectionControl.askSelect(nickname);
        } catch (NoBookshelfSpaceException e) {
            connectionControl.SendError(noBookshelfSpaceResponse, nickname);
            connectionControl.askSelect(nickname);
        }
    }

    /**
     * @return If the game is active
     */
    public boolean isGameIsActive() {
        return gameIsActive;
    }

    /**
     * Called by the GameModel when it wants to notify its update/changes.
     * This method is used to implement a listener pattern.
     *
     * @param evt is a PropertyChangeEvent object describing the event source, and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().matches("(.*)ERROR")) {
            connectionControl.SendError(evt.getPropertyName(), (String) evt.getSource());
        } else {
            switch (evt.getPropertyName()) {
                case BOOKSHELF_CHANGED ->
                        connectionControl.SendBookshelfChanged((String) evt.getSource(), (ItemCard[][]) evt.getNewValue(), (String) evt.getOldValue());
                case BOARD_CHANGED ->
                        connectionControl.SendBoardChanged((ItemCard[][]) evt.getNewValue(), (String) evt.getOldValue());
                case BOOKSHELF_RENEWED -> {
                    connectionControl.sendBookshelfRenewed((ItemCard[]) evt.getNewValue(), (Integer) evt.getOldValue(), (String) evt.getSource());
                    turnPhase = TurnPhase.ENDTURN;
                }
                case BOARD_RENEWED -> {
                    connectionControl.sendBoardRenewed((Integer[]) evt.getNewValue());
                    if (turnPhase == TurnPhase.SELECTCARDS) {
                        turnPhase = TurnPhase.INSERTCARDS;
                        connectionControl.askInsert(currPlayer);
                    }
                }
                case COM_GOAL_CREATED ->
                        connectionControl.SendCommonGoalCreated((Integer) evt.getSource(), (Integer) evt.getNewValue(), (String) evt.getOldValue());
                case EMPTY_CARD_BAG -> connectionControl.SendEmptyCardBag();
                case PLAYER_SCORE ->
                        connectionControl.sendPlayerScore((String) evt.getSource(), (Integer) evt.getNewValue());
                case COM_GOAL_DONE ->
                        connectionControl.SendCommonGoalDone((String) evt.getSource(), (int[]) evt.getNewValue());
                case PERS_GOAL_CREATED ->
                        connectionControl.SendPersGoalCreated((String) evt.getSource(), (String) evt.getNewValue());
                case BOOKSHELF_COMPLETED -> {
                    winner = true;
                    connectionControl.SendBookshelfCompleted((String) evt.getSource());
                }
                case FINAL_SCORES ->
                        connectionControl.sendErrorToEveryone(evt.getSource() + " has done" + evt.getNewValue() + " points.");
                default -> {
                }
            }
        }
    }

    /**
     * Called when a player has come back to the game, calls the method on the GameModel to send him all the game's details.
     *
     * @param nickname of the client to send details to.
     */
    public void sendGameDetails(String nickname) {
        gameModel.sendGameDetails(nickname);
        if (playersList.stream().filter(connectionControl::isOnline).count() != 1)
            connectionControl.sendPlayerTurn(currPlayer);
    }
}
