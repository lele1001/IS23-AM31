package it.polimi.ingsw.model;


import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PersGoal {
    Card1, Card2, Card3, Card4, Card5, Card6, Card7, Card8, Card9, Card10, Card11, Card12;

    // An ordered list of different scores based on the number of correct ItemCard
    private final static List<Integer> scoreList = new ArrayList<>(List.of(0,1,2,4,6,9,12));

    // For any position (the integer key) indicates the correct HouseItem
    private final Map<Integer, HouseItem> positions = new HashMap<>();


    /**
     * Builds the card such as parameters indicates.
     */
    PersGoal() {
        Reader json;
        {
            try {
                json = new FileReader("src/main/java/it/polimi/ingsw/model/PersGoalConfiguration.json");
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: No such PersGoalConfiguration file.");
                return;
            }
        }
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);

        List<Integer> index = ((ArrayList<Double>) map.get(this.toString())).stream().map(Double::intValue).toList();
        List<HouseItem> items = ((List<String>)map.get("OrderedHouseItem")).stream().map(HouseItem::valueOf).toList();
        for (int i = 0; i < items.size(); i++) {
            positions.put(index.get(i), items.get(i));
        }
    }

    /**
     * @return the score reached on the library it takes as a parameter.
     */
    public int calcScore (Bookshelf l) {
        int score = 0; int numberofDone = 0;
        for(int i : positions.keySet()) {
            if((l.get(i/10, i%10)!=null) && (l.get(i/10, i%10).getMyItem() == positions.get(i))) {
                numberofDone++;
            }
        }
        return scoreList.get(numberofDone);
    }
}