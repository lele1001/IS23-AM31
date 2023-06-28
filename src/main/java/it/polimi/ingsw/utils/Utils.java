package it.polimi.ingsw.utils;

/**
 * Defines static value used in all the project
 */
public abstract class Utils {
    //TODO Connection control
    /*
    DONE:
    gameController
    MODEL (/NO STRING OF GAME MODEL)

     */
    public static final int BOOKSHELF_HEIGHT = 6;
    public static final int BOOKSHELF_LENGTH = 5;
    public static final int DIM_BOARD = 9;
    public static final int minNumberOfPlayers = 2;
    public static final int maxNumberOfPlayers = 4;
    public static final int timeOfReturning = 60000;
    public static final int timeOfTurn = 180000;
    public static final int timeOfSleep = 500;
    public static final String endForTimeFinished = "Took too long for returning... game is ending.";
    public static final String oneWinnerEndPhrase = "The winner of the game is ";
    //public static final String moreWinnerEndPhrase = "Parity: winners are ";
    public static final String notYourTurnResponse = "NOT YOUR TURN";
    public static final String notValidNumberResponse = "NUMBER NOT VALID";
    public static final String noBookshelfSpaceResponse = "NO BOOKSHELF SPACE";
    public static final String noRightBoardSelectionResponse = "NO RIGHT BOARD SELECTION";
    public static final String notSameCardsResponse = "CARDS YOU WANT TO INSERT ARE NOT THE SAME YOU SELECTED";
    public static final int maxNumberOfSelectedCards = 3;
    public static final int pingTimer = 3000;
    public static final int maxNameLength = 18;
    public static final String grey=(char) 27 + "[0;39m";
    public static final String green=(char) 27 + "[1;92m";
    public static final String blue=(char) 27 + "[1;94m";
    public static final String yellow=(char) 27 + "[1;93m";
    public static final String purple=(char) 27 + "[1;95m";
    public static final String cyan=(char) 27 + "[1;96m";
    public static final String white=(char) 27 + "[1;97m";
    public static final String red=(char) 27 + "[0;31m";


    /**
     * Defines all the value for the different goal description
     */
    public enum comGoalDescription {
        comGoal1("Two separate groups each containing four tiles of the same type in a 2x2 square.\n" + "The tiles of one square can be different from those of the other square."),
        comGoal2("Two columns each formed by 6 different types of tiles.\n" + "One column can show the same or a different combination of the other column."),
        comGoal3(
                "Four groups each containing at least 4 tiles of the same type.\n" + "The tiles of one group can be different from those of another group."),
        comGoal4(
                "Six groups each containing at least 2 tiles of the same type.\n" + "The tiles of one group can be different from those of another group."),
        comGoal5(
                "Three columns each formed by 6 tiles of maximum three different types.\n" + "One column can show the same or a different combination of another column."),
        comGoal6(
                "Two lines each formed by 5 different types of tiles.\n" + "One line can show the same or a different combination of the other line."),
        comGoal7(
                "Four lines each formed by 5 tiles of maximum three different types.\n" + "One line can show the same or a different combination of another line."),
        comGoal8(
                "Four tiles of the same type in the four corners of the bookshelf."),
        comGoal9(
                "Eight tiles of the same type.\n" + "There is no restriction about the position of these tiles."),
        comGoal10(
                "Five tiles of the same type forming an X."),
        comGoal11(
                "Five tiles of the same item forming a diagonal."),
        comGoal12("Five columns of increasing or decreasing height. Starting from the first column on the left or on the right, each next column must be made of exactly one more tile. Tiles can be of any type.");

        private final String description;

        /**
         * Set the description of the Common Goal
         *
         * @param description The description of the Common Goal
         */
        comGoalDescription(String description) {
            this.description = description;
        }

        /**
         * Return the String description of the Common Goal
         *
         * @return Return the String description of the Common Goal
         */
        public String getDescription() {
            return description;
        }

    }
}