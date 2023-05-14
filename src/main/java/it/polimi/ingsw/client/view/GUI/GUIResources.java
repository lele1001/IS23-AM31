package it.polimi.ingsw.client.view.GUI;

import javafx.scene.image.Image;

import java.util.Objects;

public final class GUIResources {
    //fxml files
    public final static String loginFXML = "FXML/login.fxml";
    public static String lobbyFXML = "FXML/lobby.fxml";
    public static String numberOfPlayerFXML = "FXML/numberOfPlayers.fxml";
    public static String notMyTurnFXML = "FXML/notMyTurn.fxml";
    public static String endGameWinFXML = "FXML/endGameWin.fxml";
    public static String endGameLoseFXML = "FXML/endGameLose.fxml";
    public static String putCardsFXML = "FXML/putCards.fxml";
    public static String takeCardsFXML = "FXML/takeCards.fxml";
    public static String errorFXML = "FXML/error.fxml";

    //images
    public final static Image icon = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Icon.png")).toString());



    private GUIResources() {}
}
