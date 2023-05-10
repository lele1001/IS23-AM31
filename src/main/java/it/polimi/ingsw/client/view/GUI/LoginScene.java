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

public class LoginScene {
    @FXML
    private GridPane currentPane;
    @FXML
    public GridPane connectionPane;
    @FXML
    public AnchorPane firstPlayerPane;
    @FXML
    Label welcomeText, usernameText, connectionText, ipText, portText, error;
    @FXML
    Button submitButton, loginButton;
    @FXML
    TextField username, ipPort, ipAddress;
    @FXML
    RadioButton connectionRMI, connectionSocket;
    @FXML
    Spinner<Integer> playersNum = new Spinner<>(1, 4, 2, 1);
    Integer players;

    private ClientController clientController;

    public void initialize(URL location, ResourceBundle resources) {
        initTableView();
        currentPane = connectionPane;
    }

    public void setCurrentPane(GridPane pane) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(100), currentPane);
        fadeOut.setFromValue(100);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), pane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(100);

        currentPane.setDisable(true);
        SequentialTransition trans = new SequentialTransition(fadeOut, fadeIn);
        currentPane = pane;
        trans.play();
        pane.setDisable(false);
    }

    private void initTableView() {
    }

    @FXML
    public void loginAction() {
        int select = checkConnection();
        int port = checkPort();

        if (select == -1) {
            error.setText("ERROR: select a connection type");
            error.setVisible(true);
            return;
        }

        if (!checkUsername()) {
            error.setText("ERROR: insert a valid username");
            error.setVisible(true);
            return;
        }

        if (!checkAddress()) {
            error.setText("ERROR: insert a valid address");
            error.setVisible(true);
            return;
        }

        if (port == -1) {
            error.setText("ERROR: insert a valid port");
            error.setVisible(true);
            return;
        }

        try {
            clientController.startConnection(select, usernameText.getText(), ipAddress.getText(), port);
        } catch (Exception e) {
            error.setText("ERROR: " + e.getMessage());
            error.setVisible(true);
            loginButton.setDisable(false);
            return;
        }

        // qualcosa per andare avanti
    }

    public void selectPlayers() {
        playersNum.setVisible(true);
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
        if (username.getText().isEmpty()) {
            error.setText("ERROR: insert a valid username");
            error.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean checkAddress () {
        if (username.getText().isEmpty()) {
            error.setText("ERROR: insert a valid address");
            error.setVisible(true);
            return false;
        }

        return true;
    }

    private int checkPort() {
        int port;

        if (username.getText().isEmpty()) {
            error.setText("ERROR: insert a valid username");
            error.setVisible(true);
            return -1;
        }

        try {
            port = Integer.parseInt(ipPort.getText());
        } catch (NumberFormatException e) {
            error.setText("ERROR: " + e.getMessage());
            error.setVisible(true);
            return -1;
        }

        return port;
    }


    public void playersAction(ActionEvent actionEvent) {
        players = playersNum.getValue();

        if (players <= 0 || players >= 5) {
            error.setVisible(true);
            GUIApp.out.setText("Error: insert a valid number of players");
            submitButton.setDisable(false);
        }
    }

    public int getPlayersNum() {
        return players;
    }
}

