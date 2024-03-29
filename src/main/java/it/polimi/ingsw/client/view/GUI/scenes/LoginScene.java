package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.InputController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * Defines the controller for the LoginScene
 */
public class LoginScene extends GUIScene {
    @FXML
    private GridPane connectionPane;
    @FXML
    private Button loginButton, exitButton;
    @FXML
    private TextField username, ipPort, ipAddress;
    @FXML
    private RadioButton connectionRMI, connectionSocket;
    private InputController inputController;

    /**
     * Initializes the login page showing the connection setup page
     */
    public void initialize(ClientController clientController) {
        super.initialize(clientController);
        inputController = new InputController(clientController);
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
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
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
        clientController.startConnection(select, username.getText(), ipAddress.getText(), port);

    }

    /**
     * Check if the connection parameters are correct
     *
     * @return 0 if RMI, 1 if Socket, -1 if incorrect
     */
    private int checkConnection() {
        if (connectionRMI.isSelected()) {
            return 0;
        } else if (connectionSocket.isSelected()) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Checks if the username contains any special character
     *
     * @param text the username
     * @return true if it does not contain special characters
     */
    private boolean checkText(TextField text) {
        String toCheck = text.getText();

        return !toCheck.isEmpty() && toCheck.matches("[a-zA-Z0-9]+");
    }

    /**
     * Check if the IP is correct
     *
     * @param text the IP
     * @return true if the IP is correct
     */
    private boolean checkIP(TextField text) {
        String toCheck = text.getText();

        return !toCheck.isEmpty() && inputController.isValidInet4Address(toCheck);
    }

    /**
     * Check if the port is a number
     *
     * @return the port number, -1 if not a number
     */
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