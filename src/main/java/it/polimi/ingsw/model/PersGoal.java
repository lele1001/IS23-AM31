package it.polimi.ingsw.model;


import java.util.*;

import static it.polimi.ingsw.model.HouseItem.*;

public enum PersGoal {
    Card1(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card2(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card3(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card4(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card5(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card6(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card7(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card8(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card9(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card10(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card11(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat))),
    Card12(new ArrayList<>(List.of(10,20,21,23,34,35)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Cat)));

    // Lista ordinata dei punteggi in base al numero (l'indice della lista) di tessere posizionate correttamente
    private final static List<Integer> scoreList = new ArrayList<>(List.of(1,2,4,6,9,12));

    // Ad ogni posizione (l'Integer) associa l'HouseItem che ci deve andare
    private final Map<Integer, HouseItem> positions = new HashMap<>(6);

    // Costruisce la carta come indicato dai parametri
    PersGoal(ArrayList<Integer> index, ArrayList<HouseItem> items) {
        for (int i = 0; i < 6; i++) {
            positions.put(index.get(i), items.get(i));
        }
    }

    // Calcola il punteggio valutando, sulla libreria, gli indici presenti nella mappa
    public int calcScore (Library l) {
        int score = 0; int numberofDone = 0;
        for(int i : positions.keySet()) {
            if(l.get(i/10, i%10).getMyItem() == positions.get(i)) {
                numberofDone++;
                score += scoreList.get(numberofDone);
            }
        }
        return score;
    }

}
