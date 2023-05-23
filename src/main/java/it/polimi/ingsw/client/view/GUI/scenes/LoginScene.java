package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    }

    /**
     * Prints the error received by the server
     */
    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    /**
     * Connects the FXML file to the scene
     */
    @Override
    public void bindEvents() {
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loginAction());
    }

    /**
     * Checks the parameters inserted by the user and tries to start a connection with the server
     */
    @FXML
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

    @Override
    public void updateBoard(ItemCard[][] board) {
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
    }

    @Override
    public void persGoal(String newValue) {

    }

    @Override
    public void updateCurrPlayer(String player) {
    }

}