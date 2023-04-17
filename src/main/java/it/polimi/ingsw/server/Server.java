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

    public void initialize() {
        stop = false;
        availablePlayers = -1;
        this.connectionControl = new ConnectionControl(this);
        this.gameController = new GameController(this.connectionControl);
        this.connectionControl.setGameController(gameController);
        this.queue = new ArrayList<>();
    }

    public void start() {
        initialize();
        new Thread(this::setGame).start();
        if (startRMI() != 0) {
            System.out.println("Error creating RMI interface.");
        } else
            System.out.println("Server RMI is ready.");

        try {
            startSocket();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server stopped.");
        System.exit(0);
    }

    public int startRMI() {
        rmiInterface = new RMIInterface(this, this.connectionControl);
        RMI stub = null;
        try {
            stub = (RMI) UnicastRemoteObject.exportObject(
                    rmiInterface, port + 1);
        } catch (RemoteException e) {
            System.out.println("Error during stub");
            e.printStackTrace();
            return -1;
        }
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port + 1);
        } catch (RemoteException e) {
            System.out.println("Error during registry");
            e.printStackTrace();
            return -1;
        }
        try {
            registry.bind("MyShelfieServer", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Error during binding");
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

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

    public void startSocket() throws UnknownHostException {
        ExecutorService executor;
        try {
            executor = Executors.newCachedThreadPool();
        } catch (Exception e) {
            System.out.println("Socket error: creating threads.");
            return;
        }
        ServerSocket serverSocket = null;
        InetAddress address = InetAddress.getByAddress(InetAddress.getLocalHost().getAddress());
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.err.println("Socket error: the specified port is not available.");
            return;
        }
        System.out.println("Server socket is ready.");
        while (!stop) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("There's a new client online!");
                ClientHandlerSocket clientHandlerSocket = new ClientHandlerSocket(socket, this, this.connectionControl);
                executor.submit(clientHandlerSocket);
            } catch (IOException e) {
                System.err.println("Socket error: waiting for clients.");
                break;
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
    }


    public synchronized void setAvailablePlayers(int availablePlayers) {
        this.availablePlayers = availablePlayers;
        this.notifyAll();
    }

    public synchronized void addInQueue(String nickname) {
        //System.out.println("Server: adding in queue.");
        this.queue.add(nickname);
        if (this.queue.size() == 1)
            this.notifyAll();

    }

    public synchronized void removeFromQueue(String nickname) {
        if (this.queue != null) {
            if ((this.queue.indexOf(nickname) == 0)) {  // era il primo: notifico setGame()
                this.queue.remove(nickname);
                this.notifyAll();
            } else {
                this.queue.remove(nickname);
            }
        }
    }

    public synchronized void setGame() {
        while (availablePlayers == -1) {
            if (!this.queue.isEmpty()) {
                this.connectionControl.askPlayerNumber(this.queue.get(0));
            }
            try {
                //System.out.println("Waiting for the first.");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Setplayers");
        while (this.queue.size() < availablePlayers) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Past setplayers");
        // dico ai giocatori in eccesso che non possono giocare
        for (String s : queue) {
            if (queue.indexOf(s) >= availablePlayers) {
                connectionControl.SendError("Game not available.", s);
                connectionControl.removeClient(s);
                queue.remove(s);
            }
        }

        this.gameController.createGame(queue);
        new Thread(this.gameController::run).start();

    }

    public void onEndGame() {
        initialize();
        rmiInterface.setConnectionControl(connectionControl);
    }
}
