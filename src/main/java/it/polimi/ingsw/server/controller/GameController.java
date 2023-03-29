package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.gameExceptions.NoBookshelfSpaceException;
import it.polimi.ingsw.server.gameExceptions.NoRightItemCardSelection;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.ItemCard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameController implements PropertyChangeListener {
    private final Map<String, ConnectionControl> playersMap;
    private final ArrayList<String> playersList = new ArrayList<>();
    private String currPlayer;
    private boolean winner;
    private GameModel gameModel;
    private TurnPhase turnPhase;

    public GameController(Map<String, ConnectionControl> playersMap) {
        this.playersMap = playersMap;
        createGame();
    }

    /**
     * Creates the game with all the necessary (board, bookshelves, ...) and starts it
     */
    private void createGame() {
        // creates the list used to iterate on players
        playersList.addAll(playersMap.keySet());
        Collections.shuffle(playersList);


        // sets itself as a listener of the model
        gameModel.setListener(this);
        // creates the board, the bookshelves
        // assigns personalGoals and commonGoals
        gameModel = new GameModel(playersList);
        // starts iterating on the players, waits for their actions and perform them after checking if they are correct
        run();
    }

    /**
     * Calls each player's turn until somebody completes his/her Bookshelf
     */
    public void run() {
        winner = false;
        currPlayer = playersList.get(0);

        while (!winner) {
            playerTurn();
        }

        System.out.println("Somebody completed his/her Bookshelf!");
        runLastTurn();
    }

    /**
     * If the current player is not the first one, makes all the remaining one play their turn
     * Calls the method to calculate the final score and
     */
    public void runLastTurn() {
        // makes the remaining players play
        while (playersList.indexOf(currPlayer) != 0) {
            playerTurn();
        }

        // invokes all the controls to calculate the final score
        String gameWinner = gameModel.calcFinalScore();
        System.out.println("The winner of the game is " + gameWinner);
    }

    /**
     * Waits for the player's action caught by ConnectionControl and calls the method to check and perform it
     */
    private void playerTurn () {
/*        ArrayList<Position> positions = ConnectionControl.askSelect(currPlayer);
        if (positions != null && !positions.isEmpty()) {
            selectCard(positions);
        }

        Map<Integer, ItemCard> cardsToInsert = ConnectionControl.askInsert(currPlayer);
        if (cardsToInsert != null && !cardsToInsert.isEmpty()) {
            insertCard(currPlayer, cardsToInsert);
        }*/

        endTurn(currPlayer);

        // updates the current player
        int currIndex = playersList.indexOf(currPlayer);
        if (currIndex + 1 < playersList.size()) {
            currPlayer = playersList.get(currIndex + 1);
        }
        else {
            currPlayer = playersList.get(0);
        }
    }

    /**
     * The method tries to insert the cards selected
     * @param nickname who wants to insert on the bookshelf
     * @param cards to be inserted
     * @param column positions on the bookshelf
     */
    public void insertCard(String nickname, ArrayList<ItemCard> cards, int column) {

        if (!(nickname.equals(currPlayer))||(turnPhase!=TurnPhase.INSERTCARDS)) {
            playersMap.get(nickname).SendError("NOT YOUR TURN");
            return;
        }
        if(cards.size()>3||cards.isEmpty()){
            playersMap.get(nickname).SendError("NUMBER NOT VALID");
            return;
        }
        try {
            gameModel.InsertCard(nickname, cards, column);
        } catch (NoBookshelfSpaceException e) {
            playersMap.get(nickname).SendError("NO BOOKSHELF SPACE");
        }
    }

    /**
     * The method tries to select cards from board
     * @param nickname who wants to select
     * @param positions of the cards to be deleted
     */
    public void selectCard(String nickname, ArrayList<Integer> positions) {
        if (!(nickname.equals(currPlayer))||(turnPhase!=TurnPhase.SELECTCARDS)) {
            playersMap.get(nickname).SendError("NOT YOUR TURN");
            return;
        }
        try {
            gameModel.selectCard(positions);
        } catch (NoRightItemCardSelection e) {
            playersMap.get(nickname).SendError("NO RIGHT BOARD SELECTION");
        }

    }
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().matches("(.*)ERROR")) {
            playersMap.get((String) evt.getSource()).SendError(evt.getPropertyName());
        }
        else
            switch(evt.getPropertyName()) {
                case "BOOKSHELF_CHANGED":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendBookshelfChanged((String) evt.getSource(), (ItemCard[][]) evt.getNewValue());
                case "BOARD_CHANGED":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendBoardChanged((ArrayList<ItemCard>) evt.getNewValue());
                case "COM_GOAL_CREATED":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendCommonGoalCreated((HashMap<Integer,Integer>) evt.getNewValue());
                case "EMPTY_CARD_BAG":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendEmptyCardBag();
                case "PLAYER_POINT_UPDATE":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendPlayerPointUpdate((String) evt.getSource(),(int)evt.getNewValue());
                case "COM_GOAL_DONE":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendCommonGoalDone((String) evt.getSource(),(HashMap<Integer,Integer>) evt.getNewValue());
                case "PERS_GOAL_CREATED":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendPersGoalCreated((String) evt.getNewValue());
                case "BOOKSHELF_COMPLETED":
                    for(ConnectionControl cc : playersMap.values())
                        cc.SendBookshelfCompleted((String) evt.getSource());
                default:
            }
    }
    // todo for Mila: call the methods to check for ComGoal and to see if there is a winner
    public void endTurn(String nickname) {
        // used to avoid errors... to change...
        winner = true;
        gameModel.EndTurn(nickname);
    }
}
