package it.polimi.ingsw.client.view.GUI;

import javafx.scene.image.Image;

import java.util.Objects;

public final class GUIResources {
    //fxml files
    public final static String loginFXML = "FXML/login.fxml";
    public static String lobbyFXML = "FXML/lobby.fxml";
    public static String numberOfPlayerFXML = "FXML/numberOfPlayers.fxml";
    public static String notMyTurnFXML = "FXML/NotYourTurnScene.fxml";
    public static String endGameWinFXML = "FXML/endGameWin.fxml";
    public static String endGameLoseFXML = "FXML/endGameLose.fxml";
    public static String putCardsFXML = "FXML/PutCards.fxml";
    public static String takeCardsFXML = "FXML/TakingCards.fxml";
    public static String errorFXML = "FXML/error.fxml";

    //images
    public final static Image icon = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Icon.png")).toString());

    public final static Image frameFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Cornici1.1.png")).toString());

    public final static Image frameSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Cornici1.2.png")).toString());

    public final static Image franeThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Cornici1.3.png")).toString());

    public final static Image catFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Gatti1.1.png")).toString());

    public final static Image catSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Gatti1.2.png")).toString());

    public final static Image catThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Gatti1.3.png")).toString());

    public final static Image gamesFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Giochi1.1.png")).toString());

    public final static Image gameSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Giochi1.2.png")).toString());

    public final static Image gameThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Giochi1.3.png")).toString());

    public final static Image booksFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Libri1.1.png")).toString());

    public final static Image booksSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Libri1.2.png")).toString());

    public final static Image booksThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Libri1.3.png")).toString());

    public final static Image plantsFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Piante1.1.png")).toString());

    public final static Image plantsSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Piante1.2.png")).toString());

    public final static Image plantsThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Piante1.3.png")).toString());

    public final static Image trophyFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Trofei1.1.png")).toString());

    public final static Image trophySecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Trofei1.2.png")).toString());

    public final static Image trophyThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Trofei1.3.png")).toString());
    private GUIResources() {
        //mappare item tiles
        //


    }
}
