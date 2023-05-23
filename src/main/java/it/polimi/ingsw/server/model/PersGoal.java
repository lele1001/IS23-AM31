package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public enum PersGoal {
    pg01, pg02, pg03, pg04, pg05, pg06, pg07, pg08, pg09, pg10, pg11, pg12;

    /**
     * An ordered list of different scores based on the number of correct ItemCard
     */
    private final static List<Integer> scoreList = new ArrayList<>(List.of(0, 1, 2, 4, 6, 9, 12));

    /**
     * For any position (the integer key) indicates the correct HouseItem
     */
    private final Map<Integer, HouseItem> positions = new HashMap<>();

    /**
     * Builds the card such as parameters indicates.
     */
    PersGoal() {
        String json;
        {
            try {
                json = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("PersGoalConfiguration.json")))).lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (Exception e) {
                System.out.println("ERROR: No such PersGoalConfiguration file.");
                return;
            }
        }
        Gson gson = new Gson();

        Type cardsType = new TypeToken<Map<String, ArrayList<Integer>>>() {
        }.getType();
        Map<String, ArrayList<Integer>> cards = gson.fromJson(json, cardsType);
        List<Integer> index = cards.get(this.toString());

        // Conventional order in which we read items from JsonConfiguration file.
        List<HouseItem> items = new ArrayList<>(List.of(HouseItem.Frame, HouseItem.Cat, HouseItem.Books, HouseItem.Games, HouseItem.Trophy, HouseItem.Plants));
        for (int i = 0; i < items.size(); i++)
            positions.put(index.get(i), items.get(i));
    }

    /**
     * @return the score reached on the library it takes as a parameter.
     */
    public int calcScore(Bookshelf l) {
        int numberOfDone = 0;
        for (int i : positions.keySet()) {
            if ((l.get(Position.getRow(i), Position.getColumn(i)) != null) && (l.get(Position.getRow(i), Position.getColumn(i)).getMyItem() == positions.get(i))) {
                numberOfDone++;
            }
        }
        return scoreList.get(numberOfDone);
    }
}