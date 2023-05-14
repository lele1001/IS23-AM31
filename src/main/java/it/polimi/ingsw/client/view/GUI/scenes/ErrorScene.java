package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorScene implements GUIScene{
    private ClientController clientController;
    @FXML
    private Label errorDescription_label;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
    }

    @Override
    public void printError(String error) {
        this.errorDescription_label.setText(error);
    }

    @Override
    public void bindEvents() {

    }

    public void closeGame() {
        clientController.disconnectMe();
    }
}
