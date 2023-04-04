package it.polimi.ingsw.server;

public abstract class ClientHandler {
    public abstract void askSelect();

    public abstract void askInsert();

    public abstract void sendError(String error);

}
