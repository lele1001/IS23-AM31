package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class NotMyTurnScene extends GUIScene {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    @FXML
    ScrollPane chatPane;
    @FXML
    AnchorPane notYourPane;
    @FXML
    Tab player1tab, player2tab, player3tab, player4tab;
    @FXML
    GridPane bookshelf1, bookshelf2, bookshelf3, bookshelf4, boardPane, comGoals, persGoal, score_0, score_1;
    @FXML
    Label yourPoints, userTurn;
    private ClientController clientController;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");
        int i = 0;

        for (String s : clientController.getPlayersBookshelves().keySet()) {
            if (i == 0) {
                player1tab.setText(s);
            } else if (i == 1) {
                player2tab.setText(s);
            } else if (i == 2) {
                player3tab.setText(s);
                //ricordarsi di rendere o meno visibile in base a num giocatori
            } else if (i == 3) {
                player4tab.setText(s);
            }

            i++;
        }
    }

    @Override
    public void printError(String error) {
    }

    @Override
    public void bindEvents() {
    }

    public void updateCurrPlayer(String currPlayer) {
        if(!clientController.getMyNickname().equals(currPlayer)){
            userTurn.setText("It is " + currPlayer + "'s turn");
        }else{
            userTurn.setText("It is your turn");
        }
    }

    @Override
    public void updateBoard(ItemCard[][] board) {
        boardPane.getChildren().clear();

        for (int i = 0; i < DIM_BOARD; i++) {
            for (int j = 0; j < DIM_BOARD; j++) {
                if (board[i][j] != null) {
                    String itemName = board[i][j].getMyItem().toString().toLowerCase();
                    String itemNumber = board[i][j].getMyNum().toString();
                    String myItem = itemName + itemNumber;

                    ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                    tileImage.setPreserveRatio(true);
                    tileImage.setFitHeight(40);
                    tileImage.setFitWidth(40);

                    boardPane.add(tileImage, j, i);
                }
            }
        }
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        if (player1tab.getText().equals(nickname)) {
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(25);
                        tileImage.setFitWidth(25);

                        bookshelf1.add(tileImage, j, i);
                    }
                }
            }

        } else if (player2tab.getText().equals(nickname)) {
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(25);
                        tileImage.setFitWidth(25);

                        bookshelf2.add(tileImage, j, i);
                    }
                }
            }
        } else if (player3tab.getText().equals(nickname)) {
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(25);
                        tileImage.setFitWidth(25);

                        bookshelf3.add(tileImage, j, i);
                    }
                }
            }
        } else if (player4tab.getText().equals(nickname)) {
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(25);
                        tileImage.setFitWidth(25);

                        bookshelf4.add(tileImage, j, i);
                    }
                }
            }
        }
    }

    @Override
    public void updateSelectedTiles(Map<Integer, ItemCard> selectedTiles) {
    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        int n = 0;

        for (Integer i : playerCommonGoal.keySet()) {
            ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + playerCommonGoal.get(i).toString()));
            scoreImage.setFitHeight(45);
            scoreImage.setFitWidth(45);

            if (n == 0) {
                score_0.add(scoreImage, 0, 0);
            } else {
                score_1.add(scoreImage, 0, 0);
            }

            String cgNum = i.toString();
            if (i < 10) {
                cgNum = "0" + cgNum;
            }

            ImageView comGoalImage = new ImageView(GUIResources.getComGoal("cg" + cgNum));
            comGoalImage.setFitHeight(100);
            comGoalImage.setFitWidth(175);

            comGoals.add(comGoalImage, 0, n);
            n++;
        }
    }

    @Override
    public void persGoal(String newValue) {
        ImageView persGoalImage = new ImageView(GUIResources.getPersGoal(newValue));
        persGoalImage.setFitHeight(200);
        persGoalImage.setFitWidth(150);

        persGoal.add(persGoalImage, 0, 0);
    }

    @Override
    public void printPoints(int myPoint) {
        yourPoints.setText("You have " + myPoint + " points");
    }
}