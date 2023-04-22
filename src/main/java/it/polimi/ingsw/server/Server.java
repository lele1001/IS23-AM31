package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.controller.GameController;

import java.io.IOException;
import java.net.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static int port;
    private int availablePlayers;
    private ConnectionControl connectionControl;
    private GameController gameController;
    private ArrayList<String> queue;
    private static boolean stop;
    private RMIInterface rmiInterface;
    private boolean playersNumberAsked = false;


    public Server() {
    }

    public static void main(String[] args) {
        System.out.println("Hello! Starting server...");
        System.out.println("Type \"stop\" to stop server.");
        new Thread(Server::listen).start();
        if (args.length != 1) {
            System.out.println("Error: missing parameters.");
            return;
        }
        Server.port = Integer.parseInt(args[0]);
        Server server = new Server();
        server.start();
    }

    /**
     * Initializes server's attributes (not only at the beginning but also at the end of a game).
     */
    public void initialize() {
        System.out.println("Initializing server...");
        stop = false;
        availablePlayers = -1;
        this.connectionControl = new ConnectionControl(this);
        this.gameController = new GameController(this.connectionControl);
        this.connectionControl.setGameController(gameController);
        this.queue = new ArrayList<>();
    }

    /**
     * Starts server's features, creating RMI and Socket interfaces.
     */
    public void start() {
        initialize();
        new Thread(this::setGame).start();
        if (!startRMI()) {
            System.out.println("Error creating RMI interface.");
        } else
            System.out.println("Server RMI is ready.");
        startSocket();
        System.out.println("Server stopped.");
        System.exit(0);
    }

    /**
     * Opens RMI interface to accept connections with this technology.
     * @return false if an error occurs.
     */
    public boolean startRMI() {
        rmiInterface = new RMIInterface(this, this.connectionControl);
        RMI stub;
        try {
            stub = (RMI) UnicastRemoteObject.exportObject(
                    rmiInterface, port + 1);
        } catch (RemoteException e) {
            System.out.println("Error during stub");
            e.printStackTrace();
            return false;
        }
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(port + 1);
        } catch (RemoteException e) {
            System.out.println("Error during registry");
            e.printStackTrace();
            return false;
        }
        try {
            registry.bind("MyShelfieServer", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Error during binding");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * A loop that always listens from server's terminal (useful if user wants to stop server).
     */
    public static void listen() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String s = in.nextLine();
            if (s.equals("stop") || s.equals("STOP") || s.equals("Stop")) {
                System.out.println("Stopping server...");
                stop = true;
                break;
            }
        }

    }

    /**
     * Opens socket interface, with the infinitive loop that accepts socket's connection (every 20 ms, while stop == false).
     */
    public void startSocket() {
        ExecutorService executor;
        try {
            executor = Executors.newCachedThreadPool();
        } catch (Exception e) {
            System.out.println("Socket error: creating threads.");
            return;
        }
        ServerSocket serverSocket;
        //InetAddress address = InetAddress.getByAddress(InetAddress.getLocalHost().getAddress());
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.err.println("Socket error: the specified port is not available.");
            return;
        }
        System.out.println("Server socket is ready.");
        try {
            serverSocket.setSoTimeout(20);
        } catch (SocketException e) {
            System.err.println("Socket timeout error.");
            stop = true;
        }
        while (!stop) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("There's a new socket client online!");
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(socket, this, this.connectionControl);
                executor.submit(clientHandlerSocket);
            } catch (IOException e) {
                //break;
            }
        }
        this.connectionControl.disconnectAll();
        System.out.println("Closing socket...");
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
    }


    /**
     * Allows the first player to set number of available players. Notifies setGame() method that was waiting for the first.
     *
     * @param availablePlayers to set.
     */
    public synchronized void setAvailablePlayers(int availablePlayers) {
        this.availablePlayers = availablePlayers;
        this.notifyAll();
        System.out.println("Players' number set to " + availablePlayers);
    }

    /**
     * Adds a new client in the queue and notifies setGame() method that was waiting for clients.
     *
     * @param nickname of the new user.
     */
    public synchronized void addInQueue(String nickname) {
        System.out.println("Server: adding in queue.");
        this.queue.add(nickname);
        this.notifyAll();
    }

    /**
     * Removes a client from the queue. Useful at the beginning of the game: if game is not still started, the queue is used to manage clients that go out before the game.
     * @param nickname of the client that has gone out.
     */
    public synchronized void removeFromQueue(String nickname) {
        if (this.queue != null) {
            if ((this.queue.indexOf(nickname) == 0)) {  // it was the first player: let's notify setGame method.
                this.queue.remove(nickname);
                playersNumberAsked = false;
                availablePlayers = -1;
                this.notifyAll();
            } else {
                this.queue.remove(nickname);
            }
        }
    }

    /**
     * A loop used at the beginning of the game with the aim of waiting for the first (to ask him players' number) and for the other clients.
     * When the game is complete (and all the clients are online), it starts the game, calling methods on GameController.
     */
    public synchronized void setGame() {
        while ((availablePlayers == -1) || (this.queue.size() < availablePlayers)) {
            if ((!this.queue.isEmpty()) && (!playersNumberAsked)) {
                this.connectionControl.askPlayerNumber(this.queue.get(0));
                playersNumberAsked = true;
            }
            try {
                //System.out.println("Waiting for the first.");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
/*        while (this.queue.size() < availablePlayers) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
        // Saying other players that game is not available for them.
        for (String s : queue) {
            if (queue.indexOf(s) >= availablePlayers) {
                connectionControl.SendError("Game not available.", s);
                connectionControl.removeClient(s);
                //queue.remove(s);
            }
        }
        queue.removeIf(x -> queue.indexOf(x)>=availablePlayers);
        this.gameController.createGame(queue);
        new Thread(this.gameController::run).start();

    }

    /**
     * Re-initializes server at the end of a game to let it be ready for new games.
     */
    public void onEndGame() {
        initialize();
        rmiInterface.setConnectionControl(connectionControl);
        new Thread(this::setGame).start();
    }
}
