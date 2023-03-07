package it.polimi.ingsw.model;

import java.util.*;
public class Library {
    private final ItemCard[][] library = new ItemCard[6][5];


    // Inserimento carte
    //@ requires checkSpace(column, cards.size()) == TRUE && cards.size() <= 3
    public void insertCard (List<ItemCard> cards, int column) {
        int i;
        // Controllo il primo posto libero della colonna
        for (i=5; i>=0; i--) {
            if(library[i][column]==null)
                break;
        }

        // Inserisco, in ordine, le carte fornite dal basso verso l'alto
        for(ItemCard ic : cards) {
            library[i][column]  = ic;
            i--;
        }
    }

    public boolean checkSpace (int column, int num) {
        if((num>3)||(num<=0)||(column<0)||(column>4))
            return false;
        int i;
        for (i=5; i>=0; i--) {
            if(library[i][column]==null)
                break;
        }
        return (i >= 0) && (i - num >= -1);
    }

    public ItemCard get(int x, int y) {
        if((x<0)||(y<0)||(x>5)||(y>4)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return library[x][y];
    }

    public int calcScore() {
        boolean[][] matrix = new boolean[6][5];
        int score = 0;
        for(int i = 0; i < 6; i++)
            for(int j=0; j<5; j++)
                matrix[i][j] = false;

        for(int i = 0; i<6; i++) {
            for(int j=0; j<5; j++) {
                if(!matrix[i][j]) {
                    switch(calc(matrix, i, j)) {
                        case 1, 2:
                            break;
                        case 3: score += 2;
                        break;
                        case 4: score += 3;
                        break;
                        case 5: score+=5;
                        break;
                        default: score+=8;
                    }
//                    System.out.println("Riga "+i+" Colonna "+j+": "+score);
                }
            }
        }
        return score;
    }

    private int calc(boolean[][] matrix, int i, int j) {
        int num = 1;
        matrix[i][j] = true;
        if((j!=0) && (library[i][j-1].getMyItem() == library[i][j].getMyItem()) && (!matrix[i][j - 1]))
            num += calc(matrix, i, j-1);
        if((j!=4) && (library[i][j+1].getMyItem() == library[i][j].getMyItem()) && (!matrix[i][j + 1]))
            num += calc(matrix, i, j+1);
        if((i!=5) && (library[i+1][j].getMyItem() == library[i][j].getMyItem()) && (!matrix[i + 1][j]))
            num += calc(matrix, i+1, j);
        if((i!=0) && (library[i-1][j].getMyItem() == library[i][j].getMyItem()) && (!matrix[i-1][j]))
            num += calc(matrix, i-1, j);

        return num;
    }




}
