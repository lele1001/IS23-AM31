package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private int availablePlayers;
    private final ConnectionControl connectionControl;
    private final GameController gameController;

    public Server(int port) {
        this.port = port;
        availablePlayers = 1;
        this.connectionControl = new ConnectionControl();
        this.gameController = new GameController(this.connectionControl);
        this.connectionControl.setGameController(gameController);
    }

    public static void main(String[] args) {
        System.out.println("Starting server...");
        if (args.length != 2) {
            System.out.println("Error: missing parameters.");
            return;
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        Server server = new Server(portNumber);
        server.start();
    }

    public void start() {
        if (startRMI() != 0) {
            System.out.println("Error creating RMI interface.");
            return;
        }
        System.out.println("Server RMI is ready.");

        startSocket();
        System.out.println("Closing server.");
    }

    public int startRMI() {
        RMIInterface rmiInterface = new RMIInterface(this, this.connectionControl);
        RMI stub = null;
        try {
            stub = (RMI) UnicastRemoteObject.exportObject(
                    rmiInterface, port);
        } catch (RemoteException e) {
            return -1;
        }
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            return -1;
        }
        try {
            registry.bind("MyShelfieServer", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            return -1;
        }
        return 0;
    }

    public void startSocket() {
        ExecutorService executor;
        try {
            executor = Executors.newCachedThreadPool();
        } catch (Exception e) {
            System.out.println("Error: creating threads.");
            return;
        }
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.err.println("Error: the specified port is not available.");
            return;
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(socket, this, this.connectionControl);
                executor.submit(clientHandlerSocket);
            } catch (IOException e) {
                break;
            }
        }
        executor.shutdown();


    }

    public int getAvailablePlayers() {
        return availablePlayers;
    }

    public void decrementAvailablePlayers() {
        this.availablePlayers--;
        if (availablePlayers == 0) {
            // inizia il gioco.
        }
    }

    public void setAvailablePlayers(int availablePlayers) {
        this.availablePlayers = availablePlayers;
    }
}
