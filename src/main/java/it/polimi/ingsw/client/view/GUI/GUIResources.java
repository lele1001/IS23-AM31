package it.polimi.ingsw.client.view.GUI;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
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

    public final static Image frameFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Cornici1.png")).toString());

    public final static Image frameSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Cornici2.png")).toString());

    public final static Image franeThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Cornici3.png")).toString());

    public final static Image catFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Gatti1.png")).toString());

    public final static Image catSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Gatti2.png")).toString());

    public final static Image catThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Gatti3.png")).toString());

    public final static Image gamesFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Giochi1.png")).toString());

    public final static Image gamesSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Giochi2.png")).toString());

    public final static Image gamesThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Giochi3.png")).toString());

    public final static Image booksFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Libri1.png")).toString());

    public final static Image booksSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Libri2.png")).toString());

    public final static Image booksThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Libri3.png")).toString());

    public final static Image plantsFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Piante1.png")).toString());

    public final static Image plantsSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Piante2.png")).toString());

    public final static Image plantsThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Piante3.png")).toString());

    public final static Image trophyFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Trofei1.png")).toString());

    public final static Image trophySecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Trofei2.png")).toString());

    public final static Image trophyThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/Trofei3.png")).toString());

    private static final Map<String, Image> itemTiles = new HashMap<String, Image>();

    private GUIResources() {
        //mappare item tiles
        itemTiles.put("frameFirst", frameFirst);
        itemTiles.put("frameSecond", frameSecond);
        itemTiles.put("frameThird", franeThird);
        itemTiles.put("catFirst", catFirst);
        itemTiles.put("catSecond", catSecond);
        itemTiles.put("catThird", catThird);
        itemTiles.put("gamesFirst", gamesFirst);
        itemTiles.put("gamesSecond", gamesSecond);
        itemTiles.put("gamesThird", gamesThird);
        itemTiles.put("booksFirst", booksFirst);
        itemTiles.put("booksSecond", booksSecond);
        itemTiles.put("booksThird", booksThird);
        itemTiles.put("plantsFirst", plantsFirst);
        itemTiles.put("plantsSecond", plantsSecond);
        itemTiles.put("plantsThird", plantsThird);
        itemTiles.put("trophyFirst", trophyFirst);
        itemTiles.put("trophySecond", trophySecond);
        itemTiles.put("trophyThird", trophyThird);

    }

    public static Image getItem(String myItem) {
        return itemTiles.get(myItem);
    }
}
