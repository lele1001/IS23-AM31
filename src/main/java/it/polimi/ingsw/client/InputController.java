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

    public ArrayList<Integer> checkTake(String[] input) {
        if (!clientController.isMyTurn()) {
            System.out.println("It is not your turn!");
            return null;
        }

        if (input.length < 2 || input.length > 4) {
            System.out.println("You can only take from 1 to 3 cards!");
            return null;
        }
        coords.clear();
        // starts from 1 because input[0] == "@take"
        for (int i = 1; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Parse exception!");
                return null;
            }

           if (!checkPosition(coord)) {
               return null;
           }
        }

        return coords;
    }

    private boolean checkPosition (int coord) {
        if (Position.getRow(coord) > 8 || Position.getColumn(coord) > 8) {
            System.out.println("The requested cell does not exists!");
            return false;
        }

        coords.add(coord);
        return true;
    }

    public ArrayList<ItemCard> checkPut(String[] input) {
        int column;
        ArrayList<ItemCard> tilesToPut = new ArrayList<>();

        if (!clientController.isMyTurn()) {
            System.out.println("It is not your turn!");
            return null;
        }

        if (input.length != clientController.getSelectedTiles().size() + 2) {
            System.out.println("You are trying to put more cards than expected!");
            return null;
        }

        try {
            column = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Parse exception!");
            return null;
        }

        if (column < 0 || column > 4) {
            System.out.println("Wrong column range!");
            return null;
        }

        coords.clear();

        // starts from 2 because input[0] == "@put" and input[1] = column
        for (int i = 2; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("Parse exception!");
                return null;
            }

            if (!checkPosition(coord)) {
                return null;
            }

        }

        if (!clientController.getSelectedTiles().keySet().containsAll(coords)) {
            return null;
        }

        for (Integer i : coords) {
            if (clientController.getSelectedTiles().containsKey(i)) {
                tilesToPut.add(clientController.getSelectedTiles().get(i));
            }
            else {
                return null;
            }
        }

        return tilesToPut;
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
        } else if (clientController.getPlayersBookshelf().containsKey(input[1])) {
            return 1;
        } else if (input[1].equalsIgnoreCase("all")) {
            return 2;
        }

        System.out.println("Recipient not accepted!");
        return 0;
    }

    public int checkPlayers(String[] input) {
        if (input.length > 2) {
            System.out.println("You should only write the number of players!");
            return 0;
        }

        int playersNum = 0;

        try {
            playersNum = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // checks only input[1] because input[0] == "@players"
        if (playersNum < 2 || playersNum > 4) {
            System.out.println("Players number not accepted!");
            return 0;
        }

        return playersNum;

    }
}
