package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Optional;

public class ClientHandlerRmi extends ClientHandler {

    // come attributo serve il riferimento all'interfaccia client
    public ClientHandlerRmi(ConnectionControl connectionControl, String nickname) {
        this.connectionControl = connectionControl;
        this.nickname = nickname;
    }

    private void ping() {
        // chiamo un metodo sul client in un blocco try catch
        // se si è disconnesso
        connectionControl.changePlayerStatus(nickname);
        //todo
    }

/*    @Override
    public void addToGame() {
        this.connectionControl.addClient(this, nickname);
    }*/

    @Override
    public Optional<Integer> askPlayerNumber() {
        // chiedo il numero di giocatori
        return Optional.of(2);

    }

    @Override
    public void askSelect() {
        //chiama il metodo sul client
    }

    @Override
    public void askInsert() {

    }

    @Override
    public void sendError(String error) {

    }

    @Override
    public void SendBookshelfChanged(String nickname, ItemCard[][] newBookshelf) {

    }

    @Override
    public void SendCommonGoalDone(String source, int[] details) {

    }

    @Override
    public void SendPersGoalCreated(String nickname, String persGoal) {

    }

    @Override
    public void SendCommonGoalCreated(Integer comGoalID, Integer score) {

    }

    @Override
    public void SendBoardChanged(ItemCard[][] newBoard) {

    }

    @Override
    public void sendWinner (String winner) {

    }


    @Override
    public void sendGameIsStarting() {

    }

    @Override
    public void sendErrorGameNotAvailable() {
    }
}
