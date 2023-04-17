package it.polimi.ingsw.client;

import java.util.ArrayList;

public class InputController {
    private final ClientController clientController;

    public InputController(ClientController clientController) {
        this.clientController = clientController;
    }

    public boolean checkTake(String[] input) {
        if (!clientController.myTurn) {
            System.out.println("It is not your turn!");
            return false;
        }

        if (input.length < 2 || input.length > 4) {
            System.out.println("You can only take from 1 to 3 cards!");
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
                System.out.println("Parse exception!");
                return false;
            }

            if (coord / 10 > 8 || coord % 10 > 8) {
                System.out.println("The requested cell does not exists!");
                return false;
            } else {
                coords.add(coord);
            }
        }

        return true;
    }

    public boolean checkPut(String[] input) {
        if (!clientController.myTurn) {
            System.out.println("It is not your turn!");
            return false;
        }

        if (input.length != clientController.selectedTiles.size() + 2) {
            System.out.println("You are trying to put more cards than expected!");
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
                System.out.println("Parse exception!");
                return false;
            }

            // item 1 represent the bookshelf column
            if (i == 1 && (coord < 0 || coord > 4)) {
                System.out.println("Wrong column range!");
                return false;
            }
            // items from 2 to 5 represent the items order (1st, 2nd or 3rd)
            else if (i != 1 && (coord < 0 || coord > 2)) {
                System.out.println("Wrong items order!");
                return false;
            } else {
                coords.add(coord);
            }
        }

        return true;
    }

    public int checkChat(String[] input) {
        if (input.length < 3) {
            System.out.println("You should write both a recipient and a message!");
            return 0;
        }

        // starts from 1 because input[0] == "@chat"
        if (input[1].equals(clientController.getMyNickname())) {
            System.out.println("You can not send a message to yourself!");
            return 0;
        } else if (clientController.playersBookshelf.containsKey(input[1])) {
            return 1;
        } else if (input[1].equalsIgnoreCase("all")) {
            return 2;
        }

        System.out.println("Recipient not accepted!");
        return 0;

    }
}
