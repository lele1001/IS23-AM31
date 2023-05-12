package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginScene implements SceneHandler {
    @FXML
    private GridPane currentPane;
    @FXML
    public GridPane connectionPane;
    @FXML
    public GridPane firstPlayerPane;
    @FXML
    Label welcomeText, usernameText, connectionText, ipText, portText;
    @FXML
    Button submitButton, loginButton;
    @FXML
    TextField username, ipPort, ipAddress;
    @FXML
    RadioButton connectionRMI, connectionSocket;
    @FXML
    Spinner<Integer> playersNum;
    Integer players;
    boolean isFirstPlayer;

    private ClientController clientController;

    /**
     * Initializes the login page showing the connection setup page
     */
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        currentPane = connectionPane;
        isFirstPlayer = false;
        setCurrentPane(connectionPane);
    }

    @Override
    public void printError(String error) {
        GUIApp.error.setVisible(true);
        GUIApp.error.setText(error);
    }

    /**
     * Shows the form to select the number of players
     */
    public void isFirst() {
        playersNum = new Spinner<>(1, 4, 2, 1);

        isFirstPlayer = true;
        setCurrentPane(firstPlayerPane);
    }

    public void setCurrentPane(GridPane pane) {
        currentPane.setDisable(true);
        currentPane.setVisible(false);

        currentPane = pane;

        currentPane.setDisable(false);
        currentPane.setVisible(true);
    }

    @FXML
    public void loginAction() {
        int select = checkConnection();
        int port = checkPort();

        if (select == -1) {
           printError("ERROR: select a connection type");
            return;
        }

        if (!checkUsername()) {
            printError("ERROR: insert a valid username");
            return;
        }

        if (!checkAddress()) {
            printError("ERROR: insert a valid address");
            return;
        }

        if (port == -1) {
            printError("ERROR: insert a valid port");
            return;
        }

        try {
            clientController.startConnection(select, usernameText.getText(), ipAddress.getText(), port);
        } catch (Exception e) {
            printError("ERROR: " + e.getMessage());
            loginButton.setDisable(false);
        }
    }

    private int checkConnection () {
        if (connectionRMI.isSelected()) {
            return 0;
        } else if (connectionSocket.isSelected()) {
            return 1;
        } else {
            return -1;
        }
    }

    private boolean checkUsername() {
        return !username.getText().isEmpty();
    }

    private boolean checkAddress () {
        return !username.getText().isEmpty();
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

    //TODO nome gioco
    public void submitAction(ActionEvent actionEvent) {
        players = playersNum.getValue();

        if (players <= 0 || players >= 5) {
            printError("Error: insert a valid number of players");
            submitButton.setDisable(false);
        }

        try {
            clientController.setPlayersNumber(players, "prova");
        } catch (Exception e) {
            printError("Impossible to connect to the server");
        }
    }
}

