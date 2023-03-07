package it.polimi.ingsw.model;


import java.util.*;

import static it.polimi.ingsw.model.HouseItem.*;

public enum PersGoal {
    Card1(new ArrayList<>(List.of(2,14,23,31,52,0)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card2(new ArrayList<>(List.of(54,20,34,22,43,11)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card3(new ArrayList<>(List.of(10,20,50,31,34,22)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card4(new ArrayList<>(List.of(32,52,51,4,30,43)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card5(new ArrayList<>(List.of(31,53,32,50,11,44)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card6(new ArrayList<>(List.of(43,4,23,41,2,50)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card7(new ArrayList<>(List.of(13,0,52,44,30,21)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card8(new ArrayList<>(List.of(4,11,43,53,22,30)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card9(new ArrayList<>(List.of(50,22,34,2,41,44)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card10(new ArrayList<>(List.of(41,33,20,11,4,53)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card11(new ArrayList<>(List.of(32,44,11,20,53,2)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants))),
    Card12(new ArrayList<>(List.of(22,50,2,44,33,11)), new ArrayList<>(List.of(Frame, Cat, Books, Games, Trophy, Plants)));

    // Lista ordinata dei punteggi in base al numero (l'indice della lista) di tessere posizionate correttamente
    private final static List<Integer> scoreList = new ArrayList<>(List.of(0,1,2,4,6,9,12));

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
