package it.polimi.ingsw.client.connection;

import it.polimi.ingsw.client.ClientController;

public abstract class ConnectionClient {
    ClientController controller;
    String address;
    int port;

    /**
     * Initialize the connection to the server
     * @param controller ClientController on which it will call all the methods after the server request
     * @param address IP address of the server
     * @param port IP port of the server
     */
    ConnectionClient(ClientController controller,String address,int port){
        this.controller=controller;
        this.address=address;
        this.port=port;
    }

    public void startconnection() throws Exception {

    }

    public ClientController getController() {
        return controller;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
