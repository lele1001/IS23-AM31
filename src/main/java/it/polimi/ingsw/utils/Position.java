package it.polimi.ingsw.utils;

/**
 * Class used to get the position of the Item Tiles
 */
public abstract class Position {
    /**
     * Return the row value from an integer
     *
     * @param num Integer that defines the position of the Item Tiles
     * @return Return the row value from an integer
     */
    public static int getRow(Integer num) {
        return num / 10;
    }

    /**
     * Return the column value from an integer
     *
     * @param num Integer that defines the position of the Item Tiles
     * @return Return the column value from an integer
     */
    public static int getColumn(Integer num) {
        return num % 10;
    }

    /**
     * Return an integer from column and row values
     *
     * @param column The column value
     * @param row    the row value
     * @return Return an integer from column and row values
     */
    public static int getNumber(int column, int row) {
        return row * 10 + column;
    }
}
