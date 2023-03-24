package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

public class GameController implements PropertyChangeListener {
    private final Map<String, ConnectionControl> playersMap;
    private final ArrayList<String> playersList = new ArrayList<>();
    private String currPlayer;
    private boolean winner;
    private GameModel gameModel;

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

        // creates the board, the bookshelves
        // assigns personalGoals and commonGoals
        gameModel = new GameModel(playersList);
        // sets itself as a listener of the model
        gameModel.setListener(this);

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
        ArrayList<Position> positions = ConnectionControl.askSelect(currPlayer);
        if (positions != null && !positions.isEmpty()) {
            selectCard(positions);
        }

        Map<Integer, ItemCard> cardsToInsert = ConnectionControl.askInsert(currPlayer);
        if(cardsToInsert != null && !cardsToInsert.isEmpty()) {
            insertCard(currPlayer, cardsToInsert);
        }

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

    // todo for Mila: check if the number of ItemCards <= 3, put each ItemCard in the position of the Bookshelf given by the key (from bottom to top)
    public void insertCard(String nickname, Map<Integer, ItemCard> cards) {}

    // todo for Mila: check if the positions are adjacent and call the method to remove the ItemCards from the Board
    public void selectCard(ArrayList<Position> positions) {}

    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().matches("(.*)ERROR")) {
            playersMap.get((String) evt.getSource()).onError(evt.getPropertyName());
        }
        else
            switch(evt.getPropertyName()) {
                case "BOOKSHELF_CHANGED":
                    for(ConnectionControl cc : playersMap.values())
                        cc.onBookshelfChanged((String) evt.getSource(), (ArrayList<ItemCard>) evt.getNewValue());
                default:
            }
    }
    // todo for Mila: call the methods to check for ComGoal and to see if there is a winner
    public void endTurn(String nickname) {
        // used to avoid errors... to change...
        winner = true;
    }
}
