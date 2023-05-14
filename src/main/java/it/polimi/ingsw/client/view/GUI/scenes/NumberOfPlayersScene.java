package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class NumberOfPlayersScene implements GUIScene{
    @FXML
    public GridPane firstPlayerPane;
    @FXML
    public Spinner<Integer> playersNum;
    @FXML
    public TextField gameName;
    @FXML
    public Button submitButton;
    @FXML
    TextArea errorArea;
    private ClientController clientController;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        playersNum = new Spinner<Integer>(1, 4, 2);

        bindEvents();
    }

    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    @Override
    public void bindEvents() {
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> submitAction());
    }

    private boolean checkText(TextField text) {
        return !text.getText().isEmpty();
    }

    /**
     * Form to select the number of players
     */
    public void submitAction() {
        int players = Integer.parseInt(playersNum.getValue().toString());

        if (players <= 0 || players >= 5) {
            printError("Error: insert a valid number of players");
            submitButton.setDisable(false);
        }

        if (!checkText(gameName)) {
            printError("Error: insert a valid name for the game");
            submitButton.setDisable(false);
        }

        try {
            clientController.setPlayersNumber(players, gameName.getText());
        } catch (Exception e) {
            printError("Impossible to connect to the server");
        }
    }
}
