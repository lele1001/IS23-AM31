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
        timer.scheduleAtFixedRate(task, 0, 5000);
    }

    private void ping() {
        try {
            client.pong();
        } catch (RemoteException e) {
            // se si è disconnesso
            connectionControl.changePlayerStatus(nickname);
            timer.cancel();
            disconnectPlayer();
        }

    }

/*    @Override
    public void addToGame() {
        this.connectionControl.addClient(this, nickname);
    }*/

    @Override
    public void askPlayerNumber() {
        // chiedo il numero di giocatori
        try {
            client.onPlayerNumber();
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " il numero di player");
        }

    }

    @Override
    public void sendPlayerTurn(String playerName) {
        try {
            client.onChangeTurn(playerName);
        } catch (RemoteException e) {
            System.out.println("Impossibile mandare il cambio turno a " + nickname);
        }
    }

    @Override
    public void disconnectPlayer() {
        try {
            timer.cancel();
            client.disconnectMe();
        } catch (RemoteException e) {
            System.out.println("Impossibile disconnettere il player " + nickname);
        }
    }

    @Override
    public void askSelect() {
        //chiama il metodo sul client
        try {
            client.onSelect();
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " di selezionare");
        }
    }

    @Override
    public void askInsert() {
        try {
            client.onInsert();
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " di inserire le carte");
        }
    }

    @Override
    public void sendError(String error) {
        try {
            client.onError(error);
        } catch (RemoteException e) {
            System.out.print("Impossibile mandare a" + nickname + " l'errore: ");
            System.out.println(error);
        }
    }

    @Override
    public void SendBookshelfChanged(String playerName, ItemCard[][] newBookshelf) {
        try {
            client.onBookshelfChanged(playerName, newBookshelf);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a" + nickname + "la bookshelf modificata.");
        }
    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        try {
            client.onCommonGoalDone(source, details);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " il commonGoal effettuato.");
        }
    }

    @Override
    public void SendPersGoalCreated(String persGoal) {
        try {
            client.onPersGoalCreated(persGoal);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " il Personal Goal creato.");
        }
    }

    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        try {
            client.onCommonGoalCreated(comGoalID, score);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " il CommonGoal");
        }
    }

    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        try {
            client.onBoardChanged(newBoard);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere a " + nickname + " la board modificata");
        }
    }

    @Override
    public void sendWinner(List<String> winners) {
        try {
            client.onWinner(winners);
        } catch (RemoteException e) {
            System.out.println("Impossibile dire a " + nickname + " il vincitore");
        }
    }

    @Override
    public void chatToMe(String sender, String message) {
        try {
            client.chatToMe(sender, message);
        } catch (RemoteException e) {
            System.out.println("Impossibile mandare messaggio chat a " + nickname);
        }
    }


    @Override
    public void sendGameIsStarting(ArrayList<String> playersList) {
        try {
            client.onGameIsStarting(playersList);
        } catch (RemoteException e) {
            System.out.println("Impossibile dire a " + nickname + " che il game sta iniziando");
        }
    }

    @Override
    public void sendErrorGameNotAvailable() {
        try {
            client.onErrorGameNotAvailable();
        } catch (RemoteException e) {
            System.out.println("Impossibile dire a " + nickname + " il vincitore");
        }
    }

    @Override
    public void sendPlayerScore(int score) {
        try {
            client.onPlayerScore(score);
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " il suo punteggio..");
        }
    }

    @Override
    public void sendBookshelfCompleted() {
        try {
            client.onBookshelfCompleted();
        } catch (RemoteException e) {
            System.out.println("Impossibile inviare a " + nickname + " che ha completato la sua libreria.");
        }
    }
}

