package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;

import java.util.ArrayList;

public class InputController {
    private final ClientController clientController;
    ArrayList<Integer> coords = new ArrayList<>();
    int coord;

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

        coords = null;

        // starts from 1 because input[0] == "@take"
        for (int i = 1; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Parse exception!");
                return false;
            }

           if (!checkPosition(coord)) {
               return false;
           }
        }

        clientController.setSelectedTiles(coords);
        return true;
    }

    private boolean checkPosition (int coord) {
        if (Position.getRow(coord) > 8 || Position.getColumn(coord) > 8) {
            System.out.println("The requested cell does not exists!");
            return false;
        }

        coords.add(coord);
        return true;
    }

    public boolean checkPut(String[] input) {
        int column;
        ArrayList<ItemCard> tilesToPut = null;

        if (!clientController.myTurn) {
            System.out.println("It is not your turn!");
            return false;
        }

        if (input.length != clientController.selectedTiles.size() + 2) {
            System.out.println("You are trying to put more cards than expected!");
            return false;
        }

        coords = null;

        try {
            column = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Parse exception!");
            return false;
        }

        if (column < 0 || column > 4) {
            System.out.println("Wrong column range!");
            return false;
        }

        // starts from 2 because input[0] == "@put" and input[1] = column
        for (int i = 2; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Parse exception!");
                return false;
            }

            if (!checkPosition(coord)) {
                return false;
            }

        }

        if (!clientController.getSelectedTiles().keySet().containsAll(coords)) {
            return false;
        }

        for (Integer i : coords) {
            if (clientController.getSelectedTiles().containsKey(i)) {
                tilesToPut.add(clientController.getSelectedTiles().get(i));
            }
            else {
                return false;
            }
        }

        try {
            clientController.insertCard(tilesToPut, column);
        } catch (Exception e) {
            e.printStackTrace();
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
