package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.Exceptions.NotAskedException;
import it.polimi.ingsw.client.Exceptions.NotAvailableNameException;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class NumberOfPlayersScene extends GUIScene {
    @FXML
    GridPane firstPlayerPane;
    @FXML
    TextField playersNum, gameName;
    @FXML
    Button submitButton,exitButton;
    @FXML
    AnchorPane playersScenePane;
    @FXML
    TextArea errorArea;
    private ClientController clientController;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        bindEvents();
    }

    /**
     * Connects FXML items to the methods in the scene
     */
    @Override
    public void bindEvents() {
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> submitAction());
        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> closeGame(clientController));
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

/*
    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }*/

    /**
     * Form to select the number of players
     */
    public void submitAction() {
        int players = Integer.parseInt(playersNum.getText());

        if (players <= 0 || players >= 5) {
            printError("Error: insert a valid number of players");
            submitButton.setDisable(false);
        } else {

            if (!checkText(gameName)) {
                printError("Error: insert a valid name for the game");
                submitButton.setDisable(false);
            } else
                try {
                    //submitButton.setDisable(true);
                    clientController.setPlayersNumber(players, gameName.getText());
                } catch (NotAskedException e) {
                    printError("Input not recognised... it's not time to set game's name.");
                } catch (NotAvailableNameException e) {
                    printError("Name you want to set is not available, please try again.");
                } catch (Exception e) {
                    printError("Impossible to connect to the server");
                }
        }
    }

    private boolean checkText(TextField text) {
        return !text.getText().isEmpty();
    }
}