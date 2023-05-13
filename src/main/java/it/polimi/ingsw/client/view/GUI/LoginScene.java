package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class LoginScene implements SceneHandler {
    @FXML
    public AnchorPane loginScenePane;
    @FXML
    private GridPane currentPane, connectionPane, firstPlayerPane, errorPane;
    @FXML
    Label welcomeText, usernameText, connectionText, ipText, portText;
    @FXML
    Button submitButton, loginButton;
    @FXML
    TextField username, ipPort, ipAddress, gameName;
    @FXML
    RadioButton connectionRMI, connectionSocket;
    @FXML
    Spinner<Integer> playersNum;
    @FXML
    public TextArea errorArea;
    Integer players;
    boolean isFirstPlayer;
    private ClientController clientController;

    /**
     * Initializes the login page showing the connection setup page
     */
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        connectionPane = new GridPane();
        firstPlayerPane = new GridPane();
        errorPane = new GridPane();

        firstPlayerPane.setVisible(false);
        firstPlayerPane.setDisable(true);
        isFirstPlayer = false;

        errorPane.setVisible(false);
        errorPane.setDisable(true);

        currentPane = connectionPane;
        setCurrentPane(connectionPane);
    }

    @Override
    public void printError(String error) {
        errorPane.setVisible(true);
        errorArea.setText(error);
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
        if (currentPane != null) {
            currentPane.setDisable(true);
            currentPane.setVisible(false);
        }

        currentPane = pane;
        currentPane.setDisable(false);
        currentPane.setVisible(true);
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

        if (!checkText(ipAddress)) {
            printError("ERROR: insert a valid IP Address");
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
        return !text.getText().isEmpty();
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

    public void submitAction(ActionEvent actionEvent) {
        players = playersNum.getValue();

        if (players <= 0 || players >= 5) {
            printError("Error: insert a valid number of players");
            submitButton.setDisable(false);
        }

        if (!checkText(gameName)) {
            printError("Error: insert a valid name for the game");
            submitButton.setDisable(false);
        }

        try {
            clientController.setPlayersNumber(players, "prova");
            //clientController.setGameName(gameName.getText());
        } catch (Exception e) {
            printError("Impossible to connect to the server");
        }
    }
}

