package it.polimi.ingsw.server.model;

public abstract class Position {
    public static int getRow(Integer num) {
        return num / 10;
    }

    public static int getColumn(Integer num) {
        return num % 10;
    }

    public static int getNumber(int column, int row) {
        return row * 10 + column;
    }
}
