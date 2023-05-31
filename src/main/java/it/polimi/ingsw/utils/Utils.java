package it.polimi.ingsw.utils;

public abstract class Utils {
    //TODO Connection controll
    /*
    DONE:
    gamecontroller
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
    public static final String moreWinnerEndPhrase = "Parity: winners are ";
    public static final String notYourTurnResponse = "NOT YOUR TURN";
    public static final String notValidNumberResponse = "NUMBER NOT VALID";
    public static final String noBookshelfSpaceResponse = "NO BOOKSHELF SPACE";
    public static final String noRightBoardSelectionResponse = "NO RIGHT BOARD SELECTION";
    public static final String notSameCardsResponse = "CARDS YOU WANT TO INSERT ARE NOT THE SAME YOU SELECTED";
    public static final int maxNumberOfSelectedCards = 3;
    public static final int pingTimer = 3000;
}
