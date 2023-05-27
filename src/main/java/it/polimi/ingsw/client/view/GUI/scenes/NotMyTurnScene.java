package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Map;

public class NotMyTurnScene extends GUIScene {
    private static final int DIM_BOARD = 9;
    @FXML
    TabPane bookshelvesPane;
    @FXML
    ScrollPane chatPane;
    @FXML
    AnchorPane notYourPane;
    @FXML
    GridPane boardPane, comGoals, persGoal, score_0, score_1;
    @FXML
    Label yourPoints, userTurn;
    private ClientController clientController;
    private ArrayList<String> players;
    private GridPane bookshelf1, bookshelf2, bookshelf3, bookshelf4;

    @Override
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");
    }

    @Override
    public void printError(String error) {
    }

    @Override
    public void bindEvents() {
    }

    public void updateCurrPlayer(String currPlayer) {
        if (!clientController.getMyNickname().equals(currPlayer)) {
            userTurn.setText("It is " + currPlayer + "'s turn");
        } else {
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
        int index = players.indexOf(nickname);
        Tab tabToModify = bookshelvesPane.getTabs().get(index);

        if (tabToModify.getText().equals(nickname)) {
            for (int i = 0; i < Utils.BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < Utils.BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(25);
                        tileImage.setFitWidth(25);

                        switch (index) {
                            case 0: bookshelf1.add(tileImage, j, i);
                            case 1: bookshelf2.add(tileImage, j, i);
                            case 2: bookshelf3.add(tileImage, j, i);
                            case 3: bookshelf4.add(tileImage, j, i);
                        }
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

    @Override
    public void setPlayers(int playersNumber) {
        for (int i = 0; i < playersNumber; i++) {
            players = new ArrayList<>(clientController.getPlayersBookshelves().keySet());
            String tabId = "player" + i + "tab";

            GridPane bookshelfGrid = new GridPane();
            bookshelfGrid.setId("bookshelf" + i);
            bookshelfGrid.setFocusTraversable(true);
            bookshelfGrid.setHgap(8.0);
            bookshelfGrid.setVgap(4.0);
            bookshelfGrid.setLayoutX(54.0);
            bookshelfGrid.setLayoutY(41.0);
            bookshelfGrid.setPrefSize(172.0, 181.0);
            bookshelfGrid.setVgap(4.0);
            setBookshelf(bookshelfGrid, i);

            ImageView bookshelfImage = new ImageView(GUIResources.bookshelfImage);
            bookshelfImage.setFitHeight(232.0);
            bookshelfImage.setFitWidth(228.0);
            bookshelfImage.setLayoutX(25.0);
            bookshelfImage.setLayoutY(25.0);
            bookshelfImage.setPickOnBounds(true);
            bookshelfImage.setPreserveRatio(true);

            AnchorPane bookshelfPane = new AnchorPane();
            bookshelfPane.setId("bookshelf" + i + "Pane");
            bookshelfPane.getChildren().add(bookshelfImage);
            bookshelfPane.getChildren().add(bookshelfGrid);

            Tab playerTab = new Tab(players.get(i), bookshelfPane);
            playerTab.setId(tabId);
            bookshelvesPane.getTabs().add(playerTab);
        }
    }

    private void setBookshelf(GridPane bookshelf, int index) {
        if (index == 0) {
            bookshelf1 = bookshelf;
        } else if (index == 1) {
            bookshelf2 = bookshelf;
        } else if (index == 3) {
            bookshelf3 = bookshelf;
        } else if (index == 4) {
            bookshelf4 = bookshelf;
        }
    }
}