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
    public final Image background = new Image(Objects.requireNonNull(GUIResources.class.getResource("/image/misc/basepagine2.png")).toString());

    // Utility images
    public final static Image icon = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Icon.png")).toString());
    public final static Image publisher = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Publisher.png")).toString());
    public final static Image backgroundLogin = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Display_5.jpg")).toString());

    // Item Tiles
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

    //common goals
    public final static Image cg01 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/1.jpg")).toString());
    public final static Image cg02 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/2.jpg")).toString());
    public final static Image cg03 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/3.jpg")).toString());
    public final static Image cg04 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/4.jpg")).toString());
    public final static Image cg05 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/5.jpg")).toString());
    public final static Image cg06 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/6.jpg")).toString());
    public final static Image cg07 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/7.jpg")).toString());
    public final static Image cg08 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/8.jpg")).toString());
    public final static Image cg09 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/9.jpg")).toString());
    public final static Image cg10 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/10.jpg")).toString());
    public final static Image cg11 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/11.jpg")).toString());
    public final static Image cg12 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/commonGoalCards/12.jpg")).toString());

    //personal goals
    public final static Image pg01 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals.png")).toString());
    public final static Image pg02 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals2.png")).toString());
    public final static Image pg03 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals3.png")).toString());
    public final static Image pg04 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals4.png")).toString());
    public final static Image pg05 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals5.png")).toString());
    public final static Image pg06 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals6.png")).toString());
    public final static Image pg07 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals7.png")).toString());
    public final static Image pg08 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals8.png")).toString());
    public final static Image pg09 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals9.png")).toString());
    public final static Image pg10 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals10.png")).toString());
    public final static Image pg11 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals11.png")).toString());
    public final static Image pg12 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/personalGoalCards/Personal_Goals12.png")).toString());

    //scoring
    public final static Image sc01 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/scoringTokens/endGame.jpg")).toString());
    public final static Image sc02 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/scoringTokens/scoring_2.jpg")).toString());
    public final static Image sc04 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/scoringTokens/scoring_4.jpg")).toString());
    public final static Image sc06 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/scoringTokens/scoring_6.jpg")).toString());
    public final static Image sc08 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/scoringTokens/scoring_8.jpg")).toString());

    // maps that associate a string to the image
    private static final Map<String, Image> itemTiles = new HashMap<>() {{
        put("frameFirst", frameFirst);
        put("frameSecond", frameSecond);
        put("frameThird", franeThird);
        put("catFirst", catFirst);
        put("catSecond", catSecond);
        put("catThird", catThird);
        put("gamesFirst", gamesFirst);
        put("gamesSecond", gamesSecond);
        put("gamesThird", gamesThird);
        put("booksFirst", booksFirst);
        put("booksSecond", booksSecond);
        put("booksThird", booksThird);
        put("plantsFirst", plantsFirst);
        put("plantsSecond", plantsSecond);
        put("plantsThird", plantsThird);
        put("trophyFirst", trophyFirst);
        put("trophySecond", trophySecond);
        put("trophyThird", trophyThird);
    }};
    private static final Map<String, Image> comGoals = new HashMap<>() {{
        put("cg01", cg01);
        put("cg02", cg02);
        put("cg03", cg03);
        put("cg04", cg04);
        put("cg05", cg05);
        put("cg06", cg06);
        put("cg07", cg07);
        put("cg08", cg08);
        put("cg09", cg09);
        put("cg10", cg10);
        put("cg11", cg11);
        put("cg12", cg12);
    }};

    private static final Map<String, Image> persGoals = new HashMap<>() {{
        put("pg01", pg01);
        put("pg02", pg02);
        put("pg03", pg03);
        put("pg04", pg04);
        put("pg05", pg05);
        put("pg06", pg06);
        put("pg07", pg07);
        put("pg08", pg08);
        put("pg09", pg09);
        put("pg10", pg10);
        put("pg11", pg11);
        put("pg12", pg12);
    }};
    private static final Map<String, Image> scoringTokens = new HashMap<>() {{
        put("sc01", sc01);
        put("sc02", sc02);
        put("sc04", sc04);
        put("sc06", sc06);
        put("sc08", sc08);
    }};


    private GUIResources() {
    }

    public static Image getItem(String myItem) {
        return itemTiles.get(myItem);
    }

    public static Image getComGoal(String comGoal) {
        return comGoals.get(comGoal);
    }

    public static Image getPersGoal(String persGoal){return persGoals.get(persGoal);}

    public static Image getScore(String s) {return scoringTokens.get(s); }
}