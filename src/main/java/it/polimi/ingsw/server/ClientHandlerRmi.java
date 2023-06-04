package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.utils.Utils.pingTimer;

public class ClientHandlerRmi extends ClientHandler {
    RMIClientConnection client;
    Timer timer = new Timer();

    //Requires the client interface reference as an attribute
    public ClientHandlerRmi(ConnectionControl connectionControl, String nickname, RMIClientConnection client) {
        this.connectionControl = connectionControl;
        this.nickname = nickname;
        this.client = client;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        };
        timer.scheduleAtFixedRate(task, 0, pingTimer);
    }

    /**
     * The RMI server called a method on the player to see if the connection is still on, if not send a disconnection request to the connectionControl
     */
    private void ping() {
        try {
            client.pong();
        } catch (RemoteException e) {
            //If it disconnects
            connectionControl.changePlayerStatus(nickname, true);
            timer.cancel();
        }

    }

    /**
     * The RMI server calls the client's method if the player is first in the queue and ha to decide the number of players in the game
     */
    @Override
    public void askPlayerNumber(List<String> notAvailableNames) {
        // chiedo il numero di giocatori
        try {
            client.onPlayerNumber(notAvailableNames);
        } catch (RemoteException e) {
            System.out.println("Impossible to ask " + nickname + " the player number");
        }

    }

    /**
     * The RMI server calls the client's method on a new Turn
     *
     * @param playerName the nickname of the players whose turn is
     */
    @Override
    public void sendPlayerTurn(String playerName) {
        try {
            client.onChangeTurn(playerName);
        } catch (RemoteException e) {
            System.out.println("impossible to send to " + nickname + "the change of turn");
        }
    }

    /**
     * The RMI server calls the client's method to disconnect the player when an error occurred, or the game is finished
     */
    @Override
    public void disconnectPlayer() {
        try {
            timer.cancel();
            client.disconnectMe();
        } catch (RemoteException e) {
            System.out.println("Impossible to disconnect the player " + nickname);
        }
    }

    /**
     * The RMI server calls the client's method to ask the player to select the Tiles
     */
    @Override
    public void askSelect() {
        //chiama il metodo sul client
        try {
            client.onSelect();
        } catch (RemoteException e) {
            System.out.println("Impossible to ask to " + nickname + " to select");
        }
    }

    /**
     * The RMI server calls the client's method to ask the player to insert the Tiles
     */
    @Override
    public void askInsert() {
        try {
            client.onInsert();
        } catch (RemoteException e) {
            System.out.println("Impossible to ask to " + nickname + " to insert the cards");
        }
    }

    /**
     * The RMI server calls the client's method in case of error on the server side
     *
     * @param error is the String that describes the type of error
     */
    @Override
    public void sendError(String error) {
        try {
            client.onError(error);
        } catch (RemoteException e) {
            System.out.print("Impossible to send to " + nickname + "the error: ");
            System.out.println(error);
        }
    }

    /**
     * The RMI server calls the client's method to update the client's side bookshelf of player nickname
     *
     * @param playerName   is the nickname of the player
     * @param newBookshelf is the updated bookshelf of the player
     */
    @Override
    public void SendBookshelfChanged(String playerName, ItemCard[][] newBookshelf) {
        try {
            client.onBookshelfChanged(playerName, newBookshelf);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to" + nickname + "the modified bookshelf");
        }
    }

    /**
     * Sends bookshelf's update to the client.
     * @param nickname: the player whose bookshelf has changed.
     * @param tilesToAdd: the ordered array of tiles to add in nickname's bookshelf.
     * @param column: the column of the bookshelf to add tiles into.
     */
    @Override
    public void sendBookshelfRenewed(String nickname, ItemCard[] tilesToAdd, int column) {
        try {
            client.onBookshelfRenewed(tilesToAdd, column, nickname);
        } catch (RemoteException e) {
            System.out.println("Impossible to send bookshelf's update to " + this.nickname);
        }
    }

    /**
     * The RMI server calls the client's method when a Common goal is completed by the player nickname
     *
     * @param source  is the nickname of the player
     * @param details is an Array with the Common Goal ID and its new value
     */
    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        try {
            client.onCommonGoalDone(source, details);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the commonGoal done");
        }
    }

    /**
     * The RMI server calls the client's method when a Personal goal is created
     *
     * @param persGoal is the String that determines the PersonalGoal from a Json file
     */
    @Override
    public void SendPersGoalCreated(String persGoal) {
        try {
            client.onPersGoalCreated(persGoal);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the personalGoal created");
        }
    }

    /**
     * The RMI server calls the client's method when a Common goal is created
     *
     * @param comGoalID of the Common goal
     * @param score     is the value assigned if the client reaches the CommonGoal
     */
    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        try {
            client.onCommonGoalCreated(comGoalID, score);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the CommonGoal created");
        }
    }

    /**
     * The RMI server calls the client's method to update the client's side board
     *
     * @param newBoard is the updated board
     */
    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        try {
            client.onBoardChanged(newBoard);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the modified board");
        }
    }

    /**
     * Sends board's update to the client.
     * @param tilesToRemove: the array of board's positions to remove tiles from.
     */
    @Override
    public void sendBoardRenewed(Integer[] tilesToRemove) {
        try {
            client.onBoardRenewed(tilesToRemove);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the modified board.");
        }

    }

    /**
     * Called when there are too many absents for the game: it needs to be interrupted waiting for them.
     */
    @Override
    public void sendGameInterrupted() {
        try {
            client.onGameInterrupted();
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " that the game has been interrupted.");
        }
    }

    /*    /**
     * The RMI server calls the client's method when a player wins
     *
     * @param winners contains the winners' nicknames
     *//*
    @Override
    public void sendWinner(List<String> winners) {
        try {
            client.onWinner(winners);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the winner/s");
        }
    }*/

    @Override
    public void sendFinalScores(LinkedHashMap<String, Integer> finalScores) {
        try {
            client.onFinalScores(finalScores);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + this.nickname + " final scores.");
        }
    }

    /**
     * The RMI server calls the client's method when a player wants to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     */
    @Override
    public void chatToMe(String sender, String message) {
        try {
            client.chatToMe(sender, message);
        } catch (RemoteException e) {
            System.out.println("Impossible to send the chat message to " + nickname);
        }
    }

    /**
     * The RMI server calls the client's method when a game is starting
     */

    @Override
    public void sendGameIsStarting(ArrayList<String> playersList) {
        try {
            client.onGameIsStarting(playersList);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " that the game is starting");
        }
    }

    /**
     * The RMI server calls the client's method to update the player score when a player reconnects
     *
     * @param score of the player
     */
    @Override
    public void sendPlayerScore(int score) {
        try {
            client.onPlayerScore(score);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " their points");
        }
    }

    /**
     * The RMI server calls the client's method when a player completes his bookshelf
     */
    @Override
    public void sendBookshelfCompleted() {
        try {
            client.onBookshelfCompleted();
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " that they have completed their bookshelf");
        }
    }

    /**
     * Asks the client if he want to resume one of the game he's into.
     * @param savedGames: the list of saved games' names the client is into.
     */
    @Override
    public void askSavedGame(List<String> savedGames) {
        try {
            client.onSavedGame(savedGames);
        } catch (RemoteException e) {
            System.out.println("Impossible to send to " + nickname + " the saved games");
        }
    }
}

