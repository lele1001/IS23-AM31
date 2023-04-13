package it.polimi.ingsw.client;

import java.util.ArrayList;

public class InputController {
    private final ClientController clientController;

    public InputController(ClientController clientController) {
        this.clientController = clientController;
    }

    public boolean checkTake(String[] input) {
        if (input.length < 2 || input.length > 4) {
            return false;
        }

        ArrayList<Integer> coords = new ArrayList<>();
        int coord;

        // starts from 1 because input[0] == "@take"
        for (int i = 1; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            if (coord / 10 > 8 || coord % 10 > 8) {
                return false;
            } else {
                coords.add(coord);
            }
        }

        return true;
    }

    public boolean checkPut(String[] input) {
        if (input.length != clientController.selectedTiles.size() + 2) {
            return false;
        }

        ArrayList<Integer> coords = new ArrayList<>();
        int coord;

        // starts from 1 because input[0] == "@put"
        for (int i = 1; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }

            // item 1 represent the bookshelf column
            if (i == 1 && (coord < 0 || coord > 4)) {
                return false;
            }
            // items from 2 to 5 represent the items order (1st, 2nd or 3rd)
            else if (i != 1 && (coord < 0 || coord > 2)) {
                return false;
            } else {
                coords.add(coord);
            }
        }

        return true;
    }

    public int checkChat(String[] input) {
        if (input.length < 3) {
            return 0;
        }

        // starts from 1 because input[0] == "@chat"
        if (input[1].equals(clientController.getMyNickname())) {
            return 0;
        }
        else if (clientController.playersBookshelf.containsKey(input[1])) {
            return 1;
        }
        else if (input[1].equalsIgnoreCase("all")) {
            return 2;
        }

        return 0;

    }
}
