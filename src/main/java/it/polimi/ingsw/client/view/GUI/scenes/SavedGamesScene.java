package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.Exceptions.NotAskedException;
import it.polimi.ingsw.client.Exceptions.NotExistingGameException;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Map;

public class SavedGamesScene extends GUIScene{
    @FXML
    Button yesButton, noButton;
    @FXML
    MenuButton listGames;
    @FXML
    Label errorLabel;

    ClientController clientController;


    @Override
    public void updateSavedGames(List<String> savedGames){
        for(String name:savedGames){
            MenuItem currGameSaved = new MenuItem(name);
            currGameSaved.setId(name);
            listGames.getItems().add(currGameSaved);
            currGameSaved.setOnAction(event -> {
                listGames.setDisable(false);
                listGames.setText(currGameSaved.getText());
        });
    }
    }
    @Override
    public void initialize(ClientController clientController) {
       this.clientController = clientController;
        bindEvents();
    }

    @Override
    public void printError(String error) {

    }
    private void sendSavedGames(boolean val,String name){
        try {
            clientController.setSavedGame(val, name);

        } catch (NotAskedException e){
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
    @Override
    public void bindEvents() {
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> checkGameSelection());
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> sendSavedGames(false,null));
    }

    private void checkGameSelection(){
        sendSavedGames(true, listGames.getText());
    }
    @Override
    public void updateCurrPlayer(String player) {

    }

    @Override
    public void updateBoard(ItemCard[][] board) {

    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {

    }

    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {

    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {

    }

    @Override
    public void persGoal(String newValue) {

    }

    @Override
    public void printPoints(int myPoint) {

    }

    @Override
    public void setPlayers() {

    }

    @Override
    public void receiveMessage(String sender, String message) {

    }
}
