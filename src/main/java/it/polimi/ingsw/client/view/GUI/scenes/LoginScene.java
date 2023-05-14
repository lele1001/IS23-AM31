package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class LoginScene implements GUIScene {
    @FXML
    public AnchorPane loginScenePane;
    @FXML
    private GridPane connectionPane;
    @FXML
    Label welcomeText, usernameText, connectionText, ipText, portText;
    @FXML
    Button loginButton;
    @FXML
    TextField username, ipPort, ipAddress;
    @FXML
    RadioButton connectionRMI, connectionSocket;
    @FXML
    TextArea errorArea;
    private ClientController clientController;

    /**
     * Initializes the login page showing the connection setup page
     */
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        connectionPane = new GridPane();
        errorArea.setVisible(false);

        connectionPane.setDisable(false);
        connectionPane.setVisible(true);
    }

    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    @Override
    public void bindEvents() {
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> loginAction());
    }

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
            printError("ERROR: " + e.getMessage());
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

