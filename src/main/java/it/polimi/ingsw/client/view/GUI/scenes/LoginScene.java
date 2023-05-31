package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class LoginScene extends GUIScene {
    @FXML
    AnchorPane loginScenePane;
    @FXML
    GridPane connectionPane;
    @FXML
    Label welcomeText, usernameText, connectionText, ipText, portText, errorArea;
    @FXML
    Button loginButton;
    @FXML
    TextField username, ipPort, ipAddress;
    @FXML
    RadioButton connectionRMI, connectionSocket;
    private ClientController clientController;

    /**
     * Initializes the login page showing the connection setup page
     */
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        errorArea.setVisible(false);
        ipPort.setText("1501");
        ipAddress.setText("127.0.0.1");

        connectionPane.setDisable(false);
        connectionPane.setVisible(true);

        ToggleGroup connectionGroup = new ToggleGroup();
        connectionRMI.setToggleGroup(connectionGroup);
        connectionSocket.setToggleGroup(connectionGroup);
        connectionRMI.setSelected(true);

        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loginAction());
    }

    /**
     * Shows the name(s) of the saved game(s)
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    @Override
    public void updateSavedGames(List<String> savedGames) {
    }

    /**
     * Sets the scene based on the number of players in the game
     */
    @Override
    public void setPlayers() {
    }

    /**
     * Updates the current player
     *
     * @param player is the curren player
     */
    @Override
    public void updateCurrPlayer(String player) {
    }

    /**
     * Shows the Tiles selected from the player in the TakeCard scene
     *
     * @param selectedTiles contains the Tiles selected by the player and their position on the Board
     */
    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
    }

    /**
     * Prints the Board in the scene
     *
     * @param board is the updated Board
     */
    @Override
    public void updateBoard(ItemCard[][] board) {
    }

    /**
     * Updates the Board removing the given Tiles
     *
     * @param tilesToRemove contains the ItemCard to remove and its position on the Board
     */
    @Override
    public void changeBoard(Integer[] tilesToRemove) {
    }

    /**
     * Prints the player's Bookshelf in the scene
     *
     * @param nickname  is the owner of the Bookshelf
     * @param bookshelf is the player's updated Bookshelf
     */
    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
    }

    /**
     * Updates the player's Bookshelf adding the given Tiles
     *
     * @param tilesToAdd contains the ItemCard to add and its position on the Bookshelf
     * @param player     is the owner of the Bookshelf to modify
     */
    @Override
    public void changeBookshelf(Map<Integer, ItemCard> tilesToAdd, String player) {
    }

    /**
     * Prints the CommonGoals and its available score in the scene
     *
     * @param playerCommonGoal contains the CommonGoalID and its available score
     */
    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
    }

    /**
     * Updates the available score of a CommonGoal each time it is reached and in the scene
     *
     * @param comGoalDoneID identifies the CommonGoal reached
     * @param newValue      is the available score
     */
    @Override
    public void updateCommonGoal(int comGoalDoneID, int newValue) {
    }

    /**
     * Prints the PersonalGoal assigned to the player
     *
     * @param newValue is the PersonalGoal name
     */
    @Override
    public void persGoal(String newValue) {
    }

    /**
     * Prints the player's points in the scene
     *
     * @param myPoint are the points of the player
     */
    @Override
    public void printPoints(int myPoint) {
    }

    /**
     * Prints the message in the chat field of the scene
     *
     * @param sender  is the player that sent the message
     * @param message is the message to print
     */
    @Override
    public void receiveMessage(String sender, String message) {
    }

    /**
     * Prints an error message in the scene
     *
     * @param error is the error message to display
     */
    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    /**
     * Checks the parameters inserted by the user and tries to start a connection with the server
     */
    public void loginAction() {
        int select = checkConnection();
        int port = checkPort();

        if (!checkText(username)) {
            printError("ERROR: insert a valid username");
            return;
        }

        if (select == -1) {
            printError("ERROR: select a connection type");
            return;
        }

        if (!checkIP(ipAddress)) {
            printError("ERROR: insert a valid IP Address");
            return;
        }

        if (port == -1) {
            printError("ERROR: insert a valid port");
            return;
        }

        try {
            clientController.startConnection(select, username.getText(), ipAddress.getText(), port);
        } catch (Exception e) {
            printError("ERROR: server error");
            loginButton.setDisable(false);
        }
    }

    private int checkConnection() {
        if (connectionRMI.isSelected()) {
            return 0;
        } else if (connectionSocket.isSelected()) {
            return 1;
        } else {
            return -1;
        }
    }

    private boolean checkText(TextField text) {
        String toCheck = text.getText();

        return !toCheck.isEmpty() && toCheck.matches("[a-zA-Z0-9]+");
    }

    private boolean checkIP(TextField text) {
        String toCheck = text.getText();

        return !toCheck.isEmpty() && toCheck.matches("[.0-9]+");
    }

    private int checkPort() {
        int port;

        if (ipPort.getText().isEmpty()) {
            return -1;
        }

        try {
            port = Integer.parseInt(ipPort.getText());
        } catch (NumberFormatException e) {
            return -1;
        }

        return port;
    }

}