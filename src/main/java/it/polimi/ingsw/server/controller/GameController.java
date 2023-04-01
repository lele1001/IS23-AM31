package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.ModelInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;

public class GameController implements PropertyChangeListener {

    private ConnectionControl connectionControl;
    private ArrayList<String> playersList = new ArrayList<>();
    private String currPlayer;
    private boolean winner;
    private ModelInterface gameModel;
    private TurnPhase turnPhase;

    public GameController(ConnectionControl connectionControl) {
        this.connectionControl = connectionControl;
    }

    /**
     * Creates the game with all the necessary (board, bookshelves, ...) and starts it
     */
    public void createGame() {
        // creates the list used to iterate on players
        Collections.shuffle(playersList);
        gameModel = new GameModel();
        // sets itself as a listener of the model
        gameModel.setListener(this);
        // creates the board, the bookshelves
        // assigns personalGoals and commonGoals
        gameModel.CreateGame(playersList);
        // starts iterating on the players, waits for their actions and perform them after checking if they are correct
        //run();
    }

    /**
     * Calls each player's turn until somebody completes his/her Bookshelf
     */
    public void run() {
        int i = 0;

        while (!winner) {
            int tot=0;
            currPlayer = playersList.get(i);
            System.out.println(playersList.get(i) + "'s turn");
            turnPhase = TurnPhase.SELECTCARDS;
            connectionControl.askSelect(currPlayer);
            while (gameModel.isPlayerOnline(currPlayer) && turnPhase != TurnPhase.ENDTURN) {
                //da finire
            }
            if (turnPhase == TurnPhase.ENDTURN) {
                gameModel.EndTurn(currPlayer);
            } else {//riesumiamo la vecchia board
            }
            i++;
            for(String player: playersList)
                if(gameModel.isPlayerOnline(player)) tot++;
            if(tot<2){
                //stop the game
            }
            //se disconnesso, chiamare sul model metodo per avere la board vecchia
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
            currPlayer = playersList.get(i);
            connectionControl.askSelect(currPlayer);
            while (gameModel.isPlayerOnline(currPlayer) && turnPhase != TurnPhase.ENDTURN) {
                //da finire
            }
            if (turnPhase == TurnPhase.ENDTURN) {
                gameModel.EndTurn(currPlayer);
            } else {//riesumiamo la vecchia board
            }
            i++;
        }
        // makes the remaining players play
        // invokes all the controls to calculate the final score
        String gameWinner = gameModel.calcFinalScore();
        System.out.println("The winner of the game is " + gameWinner);
    }

    /**
     * Waits for the player's action caught by ConnectionControl and calls the method to check and perform it
     */
    /*private void playerTurn() {
        ArrayList<Position> positions = ConnectionControl.askSelect(currPlayer);
        if (positions != null && !positions.isEmpty()) {
            selectCard(positions);
        }

        Map<Integer, ItemCard> cardsToInsert = ConnectionControl.askInsert(currPlayer);
        if (cardsToInsert != null && !cardsToInsert.isEmpty()) {
            insertCard(currPlayer, cardsToInsert);
        }

        endTurn(currPlayer);

        // updates the current player
        int currIndex = playersList.indexOf(currPlayer);
        if (currIndex + 1 < playersList.size()) {
            currPlayer = playersList.get(currIndex + 1);
        } else {
            currPlayer = playersList.get(0);
        }
    } */

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
                    }
                }
                case "COM_GOAL_CREATED" ->
                        connectionControl.SendCommonGoalCreated((Integer) evt.getSource(), (Integer) evt.getNewValue());
                case "EMPTY_CARD_BAG" -> connectionControl.SendEmptyCardBag();
                case "PLAYER_POINT_UPDATE" ->
                        connectionControl.SendPlayerPointUpdate((String) evt.getSource(), (int) evt.getNewValue());
                case "COM_GOAL_DONE" ->
                        connectionControl.SendCommonGoalDone((String) evt.getSource(), (int[]) evt.getNewValue());
                case "PERS_GOAL_CREATED" -> connectionControl.SendPersGoalCreated((String) evt.getNewValue());
                case "BOOKSHELF_COMPLETED" -> connectionControl.SendBookshelfCompleted((String) evt.getSource());
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

    public void addPlayer(String nickname) {

        if (playersList.size() > 4) {
            System.out.println("Too many players for this game");
            return;
        }
        else
            playersList.add(nickname);
    }
}
