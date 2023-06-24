package it.polimi.ingsw.client.view.GUI;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class GUIResources {
    //fxml files
    public final static String loginFXML = "FXML/login.fxml";
    public final static String askSavedGamesFXML = "FXML/savedGamesScene.fxml";
    public final static String numberOfPlayerFXML = "FXML/numberOfPlayers.fxml";
    public final static String lobbyFXML = "FXML/lobby.fxml";
    public final static String notMyTurnFXML = "FXML/NotYourTurnScene.fxml";
    public final static String endGameFXML = "FXML/endGame.fxml";
    public final static String putCardsFXML = "FXML/PutCards.fxml";
    public final static String takeCardsFXML = "FXML/TakingCards.fxml";
    public final static String errorFXML = "FXML/error.fxml";

    //images
    public final Image background = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/misc/basePagina2.png")).toString());
    public static final Image bookshelfImage = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/boards/bookshelf.png")).toString());
    // Utility images
    public final static Image icon = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Icon.png")).toString());
    public final static Image publisher = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Publisher.png")).toString());
    public final static Image backgroundLogin = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/publisherMaterial/Display_5.jpg")).toString());

    // Item Tiles
    public final static Image frameFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/frameFirst.png")).toString());
    public final static Image frameSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/frameSecond.png")).toString());
    public final static Image franeThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/frameThird.png")).toString());
    public final static Image catFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/catFirst.png")).toString());
    public final static Image catSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/catSecond.png")).toString());
    public final static Image catThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/catThird.png")).toString());
    public final static Image gamesFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/gamesFirst.png")).toString());
    public final static Image gamesSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/gamesSecond.png")).toString());
    public final static Image gamesThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/gamesThird.png")).toString());
    public final static Image booksFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/booksFirst.png")).toString());
    public final static Image booksSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/booksSecond.png")).toString());
    public final static Image booksThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/booksThird.png")).toString());
    public final static Image plantsFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/plantsFirst.png")).toString());
    public final static Image plantsSecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/plantsSecond.png")).toString());
    public final static Image plantsThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/plantsThird.png")).toString());
    public final static Image trophyFirst = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/trophyFirst.png")).toString());
    public final static Image trophySecond = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/trophySecond.png")).toString());
    public final static Image trophyThird = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/itemTiles/trophyThird.png")).toString());

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
    public final static Image sc00 = new Image(Objects.requireNonNull(GUIResources.class.getResource("/images/scoringTokens/scoring.jpg")).toString());
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
        put("sc00", sc00);
        put("sc01", sc01);
        put("sc02", sc02);
        put("sc04", sc04);
        put("sc06", sc06);
        put("sc08", sc08);
    }};


    private GUIResources() {
    }

    /**
     * @param myItem String that symbolize the item
     * @return The image of the Item
     */
    public static Image getItem(String myItem) {
        return itemTiles.get(myItem);
    }

    /**
     * @param comGoal String that symbolize the Common Goal
     * @return The image of the Common Goal
     */
    public static Image getComGoal(String comGoal) {
        return comGoals.get(comGoal);
    }

    /**
     * @param persGoal String that symbolize the Personal Goal
     * @return The image of the Personal Goal
     */
    public static Image getPersGoal(String persGoal) {
        return persGoals.get(persGoal);
    }

    /**
     * @param s String that symbolize the Score Tile
     * @return The image of the Score Tile
     */
    public static Image getScore(String s) {
        return scoringTokens.get(s);
    }
}