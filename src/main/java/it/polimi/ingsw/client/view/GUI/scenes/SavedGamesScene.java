package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.Exceptions.NotAskedException;
import it.polimi.ingsw.client.Exceptions.NotExistingGameException;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class SavedGamesScene extends GUIScene {
    @FXML
    Button yesButton, noButton, exitButton;
    @FXML
    MenuButton listGames;
    @FXML
    Label errorLabel;
    @FXML
    AnchorPane savedGamesPane;
    ClientController clientController;

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
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> checkGameSelection());
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendSavedGames(false, null));
    }

    /**
     * Shows the name(s) of the saved game(s)
     *
     * @param savedGames contains all the saved games in which the player was in
     */
    @Override
    public void updateSavedGames(List<String> savedGames) {
        for (String name : savedGames) {
            MenuItem currGameSaved = new MenuItem(name);
            currGameSaved.setId(name);
            listGames.getItems().add(currGameSaved);
            currGameSaved.setOnAction(event -> {
                listGames.setDisable(false);
                listGames.setText(currGameSaved.getText());
            });
        }
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

    private void sendSavedGames(boolean val, String name) {
        try {
            clientController.setSavedGame(val, name);
        } catch (NotAskedException e) {
            errorLabel.setText("Input not recognised... it's not time to set saved games.");
            errorLabel.setVisible(true);
        } catch (NotExistingGameException e) {
            errorLabel.setText("The game you wrote doesn't exist: try again.");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            errorLabel.setText("Impossible to connect to the server.");
            errorLabel.setVisible(true);
        }
    }

    private void checkGameSelection() {
        sendSavedGames(true, listGames.getText());
    }
}
