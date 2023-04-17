package it.polimi.ingsw.server;

import it.polimi.ingsw.client.connection.RMIClientConnection;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.rmi.RemoteException;

public class ClientHandlerRmi extends ClientHandler {
    RMIClientConnection client;
    TaskTimer timer;

    // come attributo serve il riferimento all'interfaccia client
    public ClientHandlerRmi(ConnectionControl connectionControl, String nickname, RMIClientConnection client) {
        this.connectionControl = connectionControl;
        this.nickname = nickname;
        this.client = client;

    }

    private void ping() {
        try {
            client.ping();
        } catch (RemoteException e) {
            // se si è disconnesso
            connectionControl.changePlayerStatus(nickname);
            //todo client disconnection
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
            System.out.println("Impossibile chiedere al client il numero di player");
        }

    }

    @Override
    public void sendPlayerTurn(String nickname) {

    }

    @Override
    public void disconnectPlayer() {

    }

    @Override
    public void askSelect() {
        //chiama il metodo sul client
        try {
            client.onSelect();
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client di selezionare");
        }
    }

    @Override
    public void askInsert() {
        try {
            client.onInsert();
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client di inserire le carte");
        }
    }

    @Override
    public void sendError(String error) {
        try {
            client.onError(error);
        } catch (RemoteException e) {
            System.out.print("Impossibile mandare al client l'errore: ");
            System.out.println(error);
        }
    }

    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {
        try {
            client.onBookshelfChanged(nickname, newBookshelf);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client la bookshelf modificata");
        }
    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {
        try {
            client.onCommonGoalDone(nickname, details);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client la bookshelf modificata");
        }
    }

    @Override
    public void SendPersGoalCreated(String persGoal) {
        try {
            client.onPersGoalCreated(persGoal);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client la bookshelf modificata");
        }
    }

    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {
        try {
            client.onCommonGoalCreated(comGoalID, score);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client il CommonGoal");
        }
    }

    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {
        try {
            client.onBoardChanged(newBoard);
        } catch (RemoteException e) {
            System.out.println("Impossibile chiedere al client la board modificata");
            e.printStackTrace();
        }
    }

    @Override
    public void sendWinner(String winner) {
        try {
            client.onWinner(winner);
        } catch (RemoteException e) {
            System.out.println("Impossibile dire al client il vincitore");
        }
    }


    @Override
    public void sendGameIsStarting() {
        try {
            client.onGameIsStarting();
        } catch (RemoteException e) {
            System.out.println("Impossibile dire al client che il game sta iniziando");
        }
    }

    @Override
    public void sendErrorGameNotAvailable() {
        try {
            client.onErrorGameNotAvailable();
        } catch (RemoteException e) {
            System.out.println("Impossibile dire al client il vincitore");
        }
    }

    private class TaskTimer {
        void run() {

        }
    }
}
