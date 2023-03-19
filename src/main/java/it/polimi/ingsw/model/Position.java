package it.polimi.ingsw.model;

public abstract class Position {
    public static int getrow(Integer num) {
        return num / 10;
    }

    public static int getcolumn(Integer num) {
        return num % 10;
    }

    public static int getnumber(int column, int row) {
        return (Integer) row * 10 + column;
    }
}
