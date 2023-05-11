package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.ClientController;

public interface SceneHandler {
    void initialize(ClientController clientController);

    void printError(String error);
}
