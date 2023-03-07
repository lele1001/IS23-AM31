package it.polimi.ingsw.model.ComGoals;

import it.polimi.ingsw.model.ItemCard;
import it.polimi.ingsw.model.Library;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.HouseItem.Cat;
import static it.polimi.ingsw.model.HouseItem.Frame;
import static it.polimi.ingsw.model.ItemNumber.First;
import static it.polimi.ingsw.model.ItemNumber.Second;
import static org.junit.jupiter.api.Assertions.*;

class CG1Test {

    @Test
    void goalReached() {
        Library lib = new Library();
        lib.insertCard(new ArrayList<>(List.of(new ItemCard(Cat, First), new ItemCard(Frame, Second))), 2);

        // test with 2 players
        CG1 comG1 = new CG1(2);
    }
}