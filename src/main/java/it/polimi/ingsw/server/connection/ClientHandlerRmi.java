package it.polimi.ingsw.server.connection;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.commons.ItemCard;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.utils.Utils.pingTimer;

/**
 * Extension of Client Handler for RMI connections
 */
public class ClientHandlerRmi extends ClientHandler {
    final RMIClientConnection client;
    final Timer timer = new Timer();
    private boolean isConnected = true;
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    //Requires the client interface reference as an attribute

    /**
     * Create the Client handler RMI every time a client connects to the server
     *
     * @param connectionControl The defined connection control for the game
     * @param nickname          The nickname of the client
     * @param client            The reference to the client RMI connection
     */
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
        new Thread(this::send).start();
    }

    public void send() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (isConnected)
                    queue.take().run();
                else {
                    queue.clear();
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                connectionControl.changePlayerStatus(nickname, true);
                timer.cancel();
            }
        }
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
            isConnected = false;
        }

    }

    /**
     * The RMI server calls the client's method if the player is first in the queue and ha to decide the number of players in the game
     */
    @Override
    public void askPlayerNumber(List<String> notAvailableNames) {
        queue.add(() -> {
            // chiedo il numero di giocatori
            try {
                client.onPlayerNumber(notAvailableNames);
            } catch (RemoteException e) {
                System.out.println("Impossible to ask " + nickname + " the player number");
            }
        });
    }

    /**
     * The RMI server calls the client's method on a new Turn
     *
     * @param playerName the nickname of the players whose turn is
     */
    @Override
    public void sendPlayerTurn(String playerName) {
        queue.add(() -> {
            try {
                client.onChangeTurn(playerName);
            } catch (RemoteException e) {
                System.out.println("impossible to send to " + nickname + "the change of turn");
            }
        });
    }

    /**
     * The RMI server calls the client's method to disconnect the player when an error occurred, or the game is finished
     */
    @Override
    public void disconnectPlayer() {
        queue.add(() -> {
            if (isConnected) {
                try {
                    timer.cancel();
                    client.disconnectMe();
                } catch (RemoteException e) {
                    System.out.println("Impossible to disconnect the player " + nickname);
                }
            }
        });
    }

    /**
     * The RMI server calls the client's method to ask the player to select the Tiles
     */
    @Override
    public void askSelect() {
        queue.add(() -> {
            //chiama il metodo sul client
            try {
                client.onSelect();
            } catch (RemoteException e) {
                System.out.println("Impossible to ask to " + nickname + " to select");
            }
        });
    }

    /**
     * The RMI server calls the client's method to ask the player to insert the Tiles
     */
    @Override
    public void askInsert() {
        queue.add(() -> {
            try {
                client.onInsert();
            } catch (RemoteException e) {
                System.out.println("Impossible to ask to " + nickname + " to insert the cards");
            }
        });
    }

    /**
     * The RMI server calls the client's method in case of error on the server side
     *
     * @param error is the String that describes the type of error
     */
    @Override
    public void sendError(String error) {
        queue.add(() -> {
            try {
                client.onError(error);
            } catch (RemoteException e) {
                System.out.print("Impossible to send to " + nickname + "the error: ");
                System.out.println(error);
            }
        });
    }

    /**
     * The RMI server calls the client's method to update the client's side bookshelf of player nickname
     *
     * @param playerName   is the nickname of the player
     * @param newBookshelf is the updated bookshelf of the player
     */
    @Override
    public void SendBookshelfChanged(String playerName, ItemCard[][] newBookshelf) {
        queue.add(() -> {
            try {
                client.onBookshelfChanged(playerName, newBookshelf);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to" + nickname + "the modified bookshelf");
            }
        });
    }

    /**
     * Sends a bookshelf's update to the client.
     *
     * @param nickname   the player whose bookshelf has changed.
     * @param tilesToAdd the ordered array of tiles to add in the player's bookshelf.
     * @param column     the column of the bookshelf to add tiles into.
     */
    @Override
    public void sendBookshelfRenewed(String nickname, ItemCard[] tilesToAdd, int column) {
        queue.add(() -> {
            try {
                client.onBookshelfRenewed(tilesToAdd, column, nickname);
            } catch (RemoteException e) {
                System.out.println("Impossible to send bookshelf's update to " + this.nickname);
            }
        });
    }

    /**
     * The RMI server calls the client's method when a Common goal is completed by the player nickname
     *
     * @param source  is the nickname of the player
     * @param details is an Array with the Common Goal ID and its new value
     */
    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        queue.add(() -> {
            try {
                client.onCommonGoalDone(source, details);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " the commonGoal done");
            }
        });
    }

    /**
     * The RMI server calls the client's method when a Personal goal is created
     *
     * @param persGoal is the String that determines the PersonalGoal from a Json file
     */
    @Override
    public void SendPersGoalCreated(String persGoal) {
        queue.add(() -> {
            try {
                client.onPersGoalCreated(persGoal);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " the personalGoal created");
            }
        });
    }

    /**
     * The RMI server calls the client's method when a Common goal is created
     *
     * @param comGoalID of the Common goal
     * @param score     is the value assigned if the client reaches the CommonGoal
     */
    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        queue.add(() -> {
            try {
                client.onCommonGoalCreated(comGoalID, score);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " the CommonGoal created");
            }
        });
    }

    /**
     * The RMI server calls the client's method to update the client's side board
     *
     * @param newBoard is the updated board
     */
    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        queue.add(() -> {
            try {
                client.onBoardChanged(newBoard);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " the modified board");
            }
        });
    }

    /**
     * Sends board's update to the client.
     *
     * @param tilesToRemove the array of board's positions to remove tiles from.
     */
    @Override
    public void sendBoardRenewed(Integer[] tilesToRemove) {
        queue.add(() -> {
            try {
                client.onBoardRenewed(tilesToRemove);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " the modified board.");
            }
        });
    }

    /**
     * Called when there are too many absents for the game: it needs to be interrupted waiting for them.
     */
    @Override
    public void sendGameInterrupted() {
        queue.add(() -> {
            try {
                client.onGameInterrupted();
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " that the game has been interrupted.");
            }
        });
    }

    /**
     * The server calls the client's method when a player wins
     *
     * @param finalScores contains all the players' scores.
     */
    @Override
    public void sendFinalScores(LinkedHashMap<String, Integer> finalScores) {
        queue.add(() -> {
            try {
                client.onFinalScores(finalScores);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + this.nickname + " final scores.");
            }
        });
    }

    /**
     * The RMI server calls the client's method when a player wants to send a chat message to the client
     *
     * @param sender  of the message
     * @param message sent to somebody else
     */
    @Override
    public void chatToMe(String sender, String message) {
        queue.add(() -> {
            try {
                client.chatToMe(sender, message);
            } catch (RemoteException e) {
                System.out.println("Impossible to send the chat message to " + nickname);
            }
        });
    }

    /**
     * The RMI server calls the client's method when a game is starting
     */

    @Override
    public void sendGameIsStarting(ArrayList<String> playersList) {
        queue.add(() -> {
            try {
                client.onGameIsStarting(playersList);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " that the game is starting");
            }
        });
    }

    /**
     * The RMI server calls the client's method to update the player score when a player reconnects
     *
     * @param score of the player
     */
    @Override
    public void sendPlayerScore(int score) {
        queue.add(() -> {
            try {
                client.onPlayerScore(score);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " their points");
            }
        });
    }

    /**
     * The RMI server calls the client's method when a player completes his bookshelf
     *
     * @param nickname of the player that has completed the bookshelf.
     */
    @Override
    public void sendBookshelfCompleted(String nickname) {
        queue.add(() -> {
            try {
                client.onBookshelfCompleted(nickname);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " that they have completed their bookshelf");
            }
        });
    }

    /**
     * Asks the client if he wants to resume one of the game he was into.
     *
     * @param savedGames the list of saved games' names the client is into.
     */
    @Override
    public void askSavedGame(List<String> savedGames) {
        queue.add(() -> {
            try {
                client.onSavedGame(savedGames);
            } catch (RemoteException e) {
                System.out.println("Impossible to send to " + nickname + " the saved games");
            }
        });
    }
}

