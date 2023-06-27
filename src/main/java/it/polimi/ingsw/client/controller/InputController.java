package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.utils.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.utils.Utils.*;

/**
 * Defines all the control methods of the Client input CLI or GUI
 */
public class InputController {
    private final ClientController clientController;
    final ArrayList<Integer> coords = new ArrayList<>();
    int coord;

    /**
     * Create the input controller
     *
     * @param clientController The client controller
     */
    public InputController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Checks that the user selects a correct number of cards, that they exist on the board and that they are adjacent
     *
     * @param input The input written from the client
     * @return The Arraylist of positions of the taken ItemTiles if all control are successful, else null
     */
    public ArrayList<Integer> checkTake(String[] input) {
        //System.out.println(Arrays.toString(input));
        if (!clientController.isMyTurn()) {
            clientController.onError("It is not your turn!");
            return null;
        }

        if (input.length < 2 || input.length > maxTilesSize() + 1) {
            clientController.onError("You can only take from 1 to 3 cards!");
            return null;
        }

        coords.clear();

        // starts from 1 because input[0] == "@take"
        for (int i = 1; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                clientController.onError("Parse exception!");
                return null;
            }

            if (checkPosition(coord)) {
                return null;
            }
        }
        if (!checkSelection(coords)) {
            clientController.onError("Position failed");
            return null;
        }

        return coords;
    }

    /**
     * Controls all the bookshelf and return the max number of Tiles that can be inserted
     *
     * @return The maximum number of a tiles that can be inserted in the client's bookshelf
     */
    public int maxTilesSize() {
        int maxTiles = 0;

        for (int column = 0; column < BOOKSHELF_LENGTH; column++) {
            int i;

            for (i = BOOKSHELF_HEIGHT - 1; i >= 0; i--) {
                if (clientController.getPlayersBookshelves().get(clientController.getMyNickname())[i][column] == null) {
                    break;
                }
            }

            if (i > maxTiles) {
                maxTiles = i;
            }
        }

        maxTiles++;
        return maxTiles;
    }

    /**
     * Checks that the given coordinate is inside the board range
     *
     * @param coord is the number from which we can extract row and column
     * @return false if the position is correct
     */
    private boolean checkPosition(int coord) {
        if (Position.getRow(coord) < DIM_BOARD || Position.getColumn(coord) < DIM_BOARD) {
            coords.add(coord);
            return false;
        }

        clientController.onError("The requested cell does not exists!");
        return true;
    }

    /**
     * Check if the Itemcard can be deleted
     * First check controls that the position numbers are contained in the board
     * Second check controls that in the selected Cells there are the ItemCards
     * Called two separate methods to do other controls
     *
     * @param position number from which we can extract row and column
     * @return true if the Itemcard can be deleted
     */
    public boolean checkSelection(ArrayList<Integer> position) {
        Collections.sort(position);

        for (Integer pos : position) {
            if (clientController.getBoard()[Position.getRow(pos)][Position.getColumn(pos)] == null) {
                return false;
            }
        }

        if (position.size() > 1) {
            return checkStraightSelection(position) && checkClearSideSelection(position);
        } else {
            return checkClearSideSelection(position);
        }
    }

    /**
     * Private method called by checkSelection that controls that all the tiles
     * in the ArrayList position have at least 1 clear side from other ItemCards
     *
     * @param position number from which we can extract row and column
     * @return true if a side of an Itemcard is clear from others
     */
    private boolean checkClearSideSelection(ArrayList<Integer> position) {
        Collections.sort(position);
        int i;
        int j;
        boolean sideClear = true;

        for (int k = 0; k < position.size() && sideClear; k++) {
            i = Position.getRow(position.get(k));
            j = Position.getColumn(position.get(k));

            if (i != 0 && i != 8 && j != 0 && j != 8) {
                sideClear = (clientController.getBoard()[i + 1][j] == null || clientController.getBoard()[i - 1][j] == null || clientController.getBoard()[i][j - 1] == null || clientController.getBoard()[i][j + 1] == null);
            }
        }

        return sideClear;
    }

    /**
     * Private method called by checkSelection that controls that the selected tiles
     * form a straight line (change of row_position or column_position)
     *
     * @param position number from which we can extract row and column
     * @return true if the ItemCards are in a straight line
     */
    private boolean checkStraightSelection(ArrayList<Integer> position) {
        int pos0 = position.get(0);
        int pos1 = position.get(1);

        if (position.size() == 3) {
            int pos2 = position.get(2);

            if ((Position.getRow(pos0) == Position.getRow(pos1) - 1) && (Position.getRow(pos1) == Position.getRow(pos2) - 1) && (Position.getColumn(pos0) == Position.getColumn(pos1)) && (Position.getColumn(pos1) == Position.getColumn(pos2))) {
                return true;
            }

            if ((Position.getColumn(pos0) == Position.getColumn(pos1) - 1) && (Position.getColumn(pos1) == Position.getColumn(pos2) - 1) && (Position.getRow(pos0) == Position.getRow(pos1)) && (Position.getRow(pos1) == Position.getRow(pos2))) {
                return true;
            }
        }

        if (position.size() == 2) {
            if ((Position.getRow(pos0) == Position.getRow(pos1) - 1) && (Position.getColumn(pos0) == Position.getColumn(pos1))) {
                return true;
            }
            return (Position.getColumn(pos0) == Position.getColumn(pos1) - 1) && (Position.getRow(pos0) == Position.getRow(pos1));
        }

        return false;
    }

    /**
     * Checks that the user selects a correct number of cards, that the column exists in the bookshelf
     * and that the cards that he wants to put in it are the same he selected from the board
     *
     * @param input The input inserted by the Client
     * @return The ItemCard, which has to be inserted if all controls are successful or null
     */
    public ArrayList<ItemCard> checkPut(String[] input) {
        int column;
        ArrayList<ItemCard> tilesToPut = new ArrayList<>();

        if (!clientController.isMyTurn()) {
            clientController.onError("It is not your turn!");
            return null;
        }

        if (input.length != clientController.getSelectedTiles().size() + 2) {
            clientController.onError("You are trying to put more cards than expected!");
            return null;
        }

        try {
            column = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            clientController.onError("Parse exception!");
            return null;
        }

        if (column < 0 || column > 4) {
            clientController.onError("Wrong column range!");
            return null;
        }
        int j;

        for (j = BOOKSHELF_HEIGHT - 1; j >= 0; j--) {
            if (clientController.getPlayersBookshelves().get(clientController.getMyNickname())[j][column] == null) {
                break;
            }
        }
        j++;
        if (clientController.getSelectedTiles().size() > j) {
            clientController.onError("Not enough space in that column");
            return null;
        }
        coords.clear();

        // starts from 2 because input[0] == "@put" and input[1] = column
        for (int i = 2; i < input.length; i++) {
            try {
                coord = Integer.parseInt(input[i]);
            } catch (NumberFormatException e) {
                clientController.onError("Parse exception!");
                return null;
            }

            if (checkPosition(coord)) {
                clientController.onError("Wrong tiles selected");
                return null;
            }

        }

        if (!clientController.getSelectedTiles().keySet().containsAll(coords)) {
            clientController.onError("Wrong tiles selected");
            return null;
        }

        for (Integer i : coords) {
            if (clientController.getSelectedTiles().containsKey(i)) {
                tilesToPut.add(clientController.getSelectedTiles().get(i));
            } else {
                clientController.onError("Wrong tiles selected");
                return null;
            }
        }

        return tilesToPut;
    }

    /**
     * Checks that the chat is directed to an existing player or to all players in the game
     *
     * @param input The input inserted by the Client
     * @return 0 if there is an error, 1 if the recipient is a player, 2 if the recipient are all players
     */
    public int checkChat(String[] input) {
        if (input.length < 3) {
            clientController.onError("You should write both a recipient and a message!");
            return 0;
        }

        // starts from 1 because input[0] == "@chat"
        if (input[1].equals(clientController.getMyNickname())) {
            clientController.onError("You can not send a message to yourself!");
            return 0;
        } else if (clientController.getPlayersBookshelves().containsKey(input[1])) {
            return 1;
        } else if (input[1].equalsIgnoreCase("all") || input[1].equalsIgnoreCase("everybody")) {
            return 2;
        }

        clientController.onError("Recipient not accepted!");
        return 0;
    }

    /**
     * Checks that the selected number of player is in the correct range
     *
     * @param input The input inserted by the Client
     * @return the number of players selected
     */
    public int checkPlayers(String[] input) {
        if (input.length != 3) {
            clientController.onError("It looks like you're missing something... please, try again.");
            return -1;
        }

        int playersNum;

        try {
            playersNum = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            clientController.onError("Parse exception!");
            return -1;
        }

        // checks only input[1] because input[0] == "@players"
        if (playersNum < 2 || playersNum > 4) {
            clientController.onError("Players number not accepted!");
            return 0;
        }

        return playersNum;
    }

    /**
     * Controls if the inserted String is a possible IP
     *
     * @param ip IP inserted by the user
     * @return true if it is a possible ip else false
     */
    public boolean isValidInet4Address(String ip) {
        String[] groups = ip.split("\\.");
        if (groups.length != 4) {
            return false;
        }
        try {
            return Arrays.stream(groups)
                    .filter(s -> !(s.length() > 1 && s.startsWith("0")))
                    .map(Integer::parseInt)
                    .filter(i -> (i >= 0 && i <= 255))
                    .count() == 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}