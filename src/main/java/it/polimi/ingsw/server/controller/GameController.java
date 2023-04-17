package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.gameExceptions.NotSameSelectedException;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.ModelInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.*;

public class GameController implements PropertyChangeListener {

    private final ConnectionControl connectionControl;
    private ArrayList<String> playersList = new ArrayList<>();
    private String currPlayer;
    private boolean winner;
    private ModelInterface gameModel;
    private TurnPhase turnPhase;

    private boolean gameIsActive;

    public GameController(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
        gameIsActive = false;
    }

    /**
     * Creates the game with all the necessary (board, bookshelves, ...) and starts it
     */
    public void createGame(ArrayList<String> playersList) {
        if (playersList.size() < 2 || playersList.size() > 4) {
            System.out.println("Error: number of players not correct.");
            return;
        }
        connectionControl.sendGameIsStarting();
        // creates the list used to iterate on players
        this.playersList = playersList;
        gameModel = new GameModel();
        // sets itself as a listener of the model
        gameModel.setListener(this);
        // creates the board, and the bookshelves
        // assigns personalGoals and commonGoals
        gameModel.CreateGame(playersList);

    }

    /**
     * Calls each player's turn until somebody completes his/her Bookshelf
     */
    public void run() {
        gameIsActive = true;
        int i = 0;
        while (!winner) {
            playerTurn(i);
            if (i < playersList.size() - 1)
                i++;
            else
                i = 0;
        /*    while(playersList.stream().map(x -> gameModel.isPlayerOnline(x)).filter(x -> x).count() < 2) {
                if(playersList.stream().map(x -> gameModel.isPlayerOnline(x)).filter(x -> x).count() == 0 ){

                }
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }*/
        }
        runLastTurn(currPlayer);


    }

    /**
     * If the current player is not the first one, makes all the remaining one play their turn
     * Calls the method to calculate the final score and
     */
    public void runLastTurn(String nickname) {
        int i = playersList.indexOf(nickname) + 1;
        while (i < playersList.size()) {
            playerTurn(i);
            i++;
        }
        String gameWinner = gameModel.calcFinalScore();
        System.out.println("The winner of the game is " + gameWinner);
        connectionControl.sendWinner(gameWinner);
    }

    /**
     * Waits for the player's action caught by ConnectionControl and calls the method to check and perform it
     */
    private void playerTurn(int indexCurrPlayer) {
        currPlayer = playersList.get(indexCurrPlayer);
        if (connectionControl.isOnline(currPlayer)) {
            System.out.println(playersList.get(indexCurrPlayer) + "'s turn");
            connectionControl.sendPlayerTurn(currPlayer);
            turnPhase = TurnPhase.SELECTCARDS;
            connectionControl.askSelect(currPlayer);

            ExecutorService service = Executors.newSingleThreadExecutor();
            try {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        while (connectionControl.isOnline(currPlayer) && turnPhase != TurnPhase.ENDTURN) ;
                    }
                };
                Future<?> f = service.submit(r);
                f.get(1, TimeUnit.MINUTES);     // attende il task per un minuto
            } catch (final InterruptedException e) {
                // The thread was interrupted during sleep, wait or join
            } catch (final TimeoutException e) {
                connectionControl.changePlayerStatus(currPlayer);
                // Took too long!
            } catch (final ExecutionException e) {
                // An exception from within the Runnable task
            } finally {
                service.shutdown();
            }

            if (turnPhase == TurnPhase.ENDTURN) {
                gameModel.EndTurn(currPlayer);
            } else {
                connectionControl.sendErrorToEveryone("Player " + currPlayer + " disconnected from the game. Resuming board...");
                gameModel.resumeBoard();
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
            connectionControl.SendError("NOT YOUR TURN", nickname);
            return;
        }
        if (cards.size() > 3 || cards.isEmpty()) {
            connectionControl.SendError("NUMBER NOT VALID", nickname);
            return;
        }
        try {
            gameModel.InsertCard(nickname, cards, column);
        } catch (NoBookshelfSpaceException e) {
            connectionControl.SendError("NO BOOKSHELF SPACE", nickname);
        } catch (NotSameSelectedException e) {
            connectionControl.SendError("CARDS YOU WANT TO INSERT ARE NOT THE SAME YOU SELECTED", nickname);
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
            connectionControl.SendError("NOT YOUR TURN", nickname);
            return;
        }
        try {
            gameModel.selectCard(positions);
        } catch (NoRightItemCardSelection e) {
            connectionControl.SendError("NO RIGHT BOARD SELECTION", nickname);
        }

    }

    public boolean isGameIsActive() {
        return gameIsActive;
    }


    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().matches("(.*)ERROR")) {
            connectionControl.SendError(evt.getPropertyName(), (String) evt.getSource());
        } else
            switch (evt.getPropertyName()) {
                case "BOOKSHELF_CHANGED" -> {
                    connectionControl.SendBookshelfChanged((String) evt.getSource(), (ItemCard[][]) evt.getNewValue());
                    turnPhase = TurnPhase.ENDTURN;
                }
                case "BOARD_CHANGED" -> {
                    connectionControl.SendBoardChanged((ItemCard[][]) evt.getNewValue());
                    if (turnPhase == TurnPhase.SELECTCARDS) {
                        turnPhase = TurnPhase.INSERTCARDS;
                        connectionControl.askInsert(currPlayer);
                    }
                }
                case "COM_GOAL_CREATED" ->
                        connectionControl.SendCommonGoalCreated((Integer) evt.getSource(), (Integer) evt.getNewValue());
                case "EMPTY_CARD_BAG" -> connectionControl.SendEmptyCardBag();
                //  case "PLAYER_POINT_UPDATE" ->
                //          connectionControl.SendPlayerPointUpdate((String) evt.getSource(), (int) evt.getNewValue());
                case "COM_GOAL_DONE" ->
                        connectionControl.SendCommonGoalDone((String) evt.getSource(), (int[]) evt.getNewValue());
                case "PERS_GOAL_CREATED" ->
                        connectionControl.SendPersGoalCreated((String) evt.getSource(), (String) evt.getNewValue());
                case "BOOKSHELF_COMPLETED" -> {
                    winner = true;
                    connectionControl.SendBookshelfCompleted((String) evt.getSource());
                }
                case "FINAL_SCORE" ->
                        connectionControl.sendErrorToEveryone(evt.getSource() + " has done" + evt.getNewValue() + " points.");
                default -> {
                }
            }
    }

    // todo for Mila: call the methods to check for ComGoal and to see if there is a winner
    /*public void endTurn(String nickname) {
        // used to avoid errors... to change...
        winner = true;
        gameModel.EndTurn(nickname);
    }*/


    public void changePlayerStatus(String nickname) {
        gameModel.changePlayerStatus(nickname);
    }

}
