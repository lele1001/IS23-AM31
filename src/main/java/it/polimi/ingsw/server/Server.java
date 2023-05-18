package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.controller.ConnectionControl;
import it.polimi.ingsw.server.controller.GameController;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
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
    private boolean savedGameAsked;
    private boolean wantToSave;
    private boolean firstAsked;
    private String gameName;
    private final Map<String, JsonObject> savedGames = new HashMap<>();
    private final static String savedGamesPath = "C:\\MyShelfieSavedGames";


    public Server() {
    }

    public static void main(String[] args) {
        System.out.println("Hello! Starting server...");
        System.out.println("Type \"stop\" to stop server.");
        new Thread(Server::listen).start();
        try {
            Server.port = Integer.parseInt(args[0]);
            int rmiPort = Server.port + 1;
            System.out.println("Ports correctly set: socket " + Server.port + ", RMI " + rmiPort + ".");
        } catch (Exception e) {
            System.out.println("Error: missing parameters.");
            System.out.println("Setting default ports: socket 1500, RMI 1501.");
            Server.port = 1500;
        }
        Server server = new Server();
        server.start();
    }

    /**
     * Initializes the server's attributes (not only at the beginning but also at the end of a game).
     */
    public void initialize() {
        System.out.println("Initializing server...");
        stop = false;
        availablePlayers = -1;
        this.connectionControl = new ConnectionControl(this);
        this.gameController = new GameController(this.connectionControl);
        this.connectionControl.setGameController(gameController);
        this.queue = new ArrayList<>();
        playersNumberAsked = false;
        wantToSave = false;
        savedGameAsked = false;
        firstAsked = false;
        gameName = null;
    }

    /**
     * Starts server's features, creating RMI and Socket interfaces.
     */
    public void start() {
        initialize();
        findSavedGames();
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
     *
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
     * A loop that always listens from the server's terminal (useful if a user wants to stop server).
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

    public void findSavedGames () {
        File directory;
        Gson gson = new Gson();
        try {
            directory = new File(savedGamesPath);
            for (File file : Objects.requireNonNull(directory.listFiles()))
                savedGames.put(file.getName().split("\\.")[0], gson.fromJson(Files.readString(file.toPath()), JsonObject.class));
        } catch (Exception e) {
            System.out.println("Unable to read from savedGames file.");
        }
    }

    /**
     * Allows the first player to set the number of available players.
     * Notifies setGame() method that was waiting for the first.
     *
     * @param availablePlayers to set.
     */
    public synchronized void setAvailablePlayers(int availablePlayers, String gameName) {
        this.availablePlayers = availablePlayers;
        this.gameName = gameName;
        this.notifyAll();
        System.out.println("Players' number set to " + availablePlayers);
        System.out.println("Game name set to " + gameName);
    }

    public synchronized void setSavedGame(boolean wantToSave, String gameName) {
        this.wantToSave = wantToSave;
        this.gameName = gameName;
        firstAsked = false;
        this.notifyAll();
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
     * Removes a client from the queue.
     * Useful at the beginning of the game:
     * if the game is not still started, the queue is used to manage clients that go out before the game.
     *
     * @param nickname of the client that has gone out.
     */
    public synchronized void removeFromQueue(String nickname) {
        if (this.queue != null) {
            if ((this.queue.indexOf(nickname) == 0)) {  // it was the first player: let's notify setGame method.
                this.queue.remove(nickname);
                playersNumberAsked = false;
                savedGameAsked = false;
                firstAsked = false;
                wantToSave = false;
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
        Gson gson = new Gson();

        while ((!wantToSave) && (availablePlayers == -1) || (this.queue.size() < availablePlayers)) {
            if (!this.queue.isEmpty()) {
                if (!firstAsked) {
                    List<String> savedNames = new ArrayList<>();
                    for (String s : savedGames.keySet()) {
                        try {
                            if (Arrays.asList(gson.fromJson(savedGames.get(s).get("nicknames").getAsString(), String[].class)).contains(this.queue.get(0)) && !savedGameAsked)
                                savedNames.add(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(s + " non valid as a gameJson.");
                        }
                        /*if (nicknames.get(s).contains(this.queue.get(0)) && (!savedGameAsked)) {
                            savedNames.add(s);
                        }*/
                    }
                    if (!savedGameAsked && !savedNames.isEmpty()) {
                        System.out.println("Found some saved games with client's nickname.. asking him if he wants to resume it.");
                        System.out.println(savedNames);
                        this.connectionControl.askSavedGame(this.queue.get(0), savedNames);     //passare tutti i nomi possibii
                        savedGameAsked = true;
                        firstAsked = true;
                    } else if (!playersNumberAsked) {
                        this.connectionControl.askPlayerNumber(this.queue.get(0), savedGames.keySet().stream().toList());
                        playersNumberAsked = true;
                        firstAsked = true;
                    }
                }
            }
            try {
                //System.out.println("Waiting for the first.");
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        /* while (this.queue.size() < availablePlayers) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
        if (wantToSave) {
            List<String> players = Arrays.asList(gson.fromJson(savedGames.get(gameName).get("nicknames").getAsString(), String[].class));
            //System.out.println("ok. wants to save.");
            ArrayList<String> onlinePlayers = new ArrayList<>();

            for (String s : queue) {
                if (players.contains(s))
                    onlinePlayers.add(s);
                else {
                    connectionControl.SendError("Game not available.", s);
                    connectionControl.removeClient(s);
                }
            }

            for (String s : players) {
                if (!onlinePlayers.contains(s)) {
                    this.connectionControl.changePlayerStatus(s, false);
                }
            }

            this.gameController.resumeGame(onlinePlayers, players, savedGames.get(gameName), Server.savedGamesPath + "\\" + gameName + ".json");

            new Thread(() -> this.gameController.run(players.indexOf(savedGames.get(gameName).get("lastPlayer").getAsString())+1)).start();
        } else {
            // Saying other players that game is not available for them.
            for (String s : queue) {
                if (queue.indexOf(s) >= availablePlayers) {
                    connectionControl.SendError("Game not available.", s);
                    connectionControl.removeClient(s);
                    //queue.remove(s);
                }
            }

            queue.removeIf(x -> queue.indexOf(x) >= availablePlayers);
            this.gameController.createGame(queue, savedGamesPath + "\\" + gameName + ".json");
            new Thread(() -> this.gameController.run(0)).start();
        }

    }

    /**
     * Re-initializes server at the end of a game to let it be ready for new games.
     */
    public void onEndGame(String gameFilePath) {
        File file = new File(gameFilePath);
        if (file.delete()) {
            System.out.println("Game file successfully deleted.");
        }
        else {
            System.out.println("Game file deletion failed.");
        }
        initialize();
        if (rmiInterface != null)
            rmiInterface.setConnectionControl(connectionControl);
        new Thread(this::setGame).start();
    }
}
