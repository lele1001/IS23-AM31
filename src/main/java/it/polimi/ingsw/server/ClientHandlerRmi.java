package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandlerRmi extends ClientHandler {
    RMIClientConnection client;
    Timer timer = new Timer();

    // come attributo serve il riferimento all'interfaccia client
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
        timer.scheduleAtFixedRate(task, 0, 3000);
    }

    /**
     * The RMI server called a method on the player to see if the connection is still on, if not send a disconnection request to the connectionControl
     */
    private void ping() {
        try {
            client.pong();
        } catch (RemoteException e) {
            // se si è disconnesso
            connectionControl.changePlayerStatus(nickname, true);
            timer.cancel();
            disconnectPlayer();
        }

    }

    /**
     * The RMI server calls the client's method if the player is first in the queue and ha to decide the number of players in the game
     */
    @Override
    public void askPlayerNumber(List<String> notAvailableNames) {
        // chiedo il numero di giocatori
        try {
            client.onPlayerNumber();
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " il numero di player");
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
            System.out.println("Impossibile mandare il cambio turno a " + nickname);
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
            System.out.println("Impossibile disconnettere il player " + nickname);
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
            System.out.println("Impossibile chiedere a " + nickname + " di selezionare");
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
            System.out.println("Impossibile chiedere a " + nickname + " di inserire le carte");
        }
    }

    /**
     * The RMI server calls the client's method in case of error on the server side
     *
     * @param error String that describes the type of error
     */
    @Override
    public void sendError(String error) {
        try {
            client.onError(error);
        } catch (RemoteException e) {
            System.out.print("Impossibile mandare a" + nickname + " l'errore: ");
            System.out.println(error);
        }
    }

    /**
     * The RMI server calls the client's method to update the client's side bookshelf of player nickname
     *
     * @param playerName   the nickname of the player
     * @param newBookshelf the updated bookshelf of the player nickname
     */
    @Override
    public void SendBookshelfChanged(String playerName, ItemCard[][] newBookshelf) {
        try {
            client.onBookshelfChanged(playerName, newBookshelf);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a" + nickname + "la bookshelf modificata.");
        }
    }

    /**
     * The RMI server calls the client's method when a Common goal is completed by the player nickname
     *
     * @param source  nickname of the player
     * @param details Array of Common goal ID and new value of common goal
     */
    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        try {
            client.onCommonGoalDone(source, details);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " il commonGoal effettuato.");
        }
    }

    /**
     * The RMI server calls the client's method when a Personal goal is created
     *
     * @param persGoal String that determines the Personal goal from a Json file
     */
    @Override
    public void SendPersGoalCreated(String persGoal) {
        try {
            client.onPersGoalCreated(persGoal);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " il Personal Goal creato.");
        }
    }

    /**
     * The RMI server calls the client's method when a Common goal is created
     *
     * @param comGoalID ID of the Common goal
     * @param score     Value if the client does the Common goal
     */
    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        try {
            client.onCommonGoalCreated(comGoalID, score);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " il CommonGoal");
        }
    }

    /**
     * The RMI server calls the client's method to update the client's side board
     *
     * @param newBoard the updated board
     */
    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        try {
            client.onBoardChanged(newBoard);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " la board modificata");
        }
    }

    /**
     * The RMI server calls the client's method when a player wins
     *
     * @param winners is the player that won the game
     */
    @Override
    public void sendWinner(List<String> winners) {
        try {
            client.onWinner(winners);
        } catch (RemoteException e) {
            System.out.println("Impossibile dire a " + nickname + " il vincitore");
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
            System.out.println("Impossibile mandare messaggio chat a " + nickname);
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
            System.out.println("Impossibile dire a " + nickname + " che il game sta iniziando");
        }
    }

    /**
     * The RMI server calls the client's method to update the player score when a player reconnects
     *
     * @param score The score of the player
     */
    @Override
    public void sendPlayerScore(int score) {
        try {
            client.onPlayerScore(score);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " il suo punteggio..");
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
            System.out.println("Impossibile inviare a " + nickname + " che ha completato la sua libreria.");
        }
    }

    @Override
    public void askSavedGame(List<String> savedGames) {
        try {
            client.onSavedGame(savedGames);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " i game salvati.");
        }
    }
}

