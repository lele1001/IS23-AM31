package it.polimi.ingsw.client.view.GUI.scenes;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.InputController;
import it.polimi.ingsw.client.view.GUI.GUIResources;
import it.polimi.ingsw.server.model.ItemCard;
import it.polimi.ingsw.server.model.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Map;


public class TakeCardsScene extends GUIScene {
    private static final int DIM_BOARD = 9;
    private static final int BOOKSHELF_HEIGHT = 6;
    private static final int BOOKSHELF_LENGTH = 5;
    @FXML
    ScrollPane chatPane;
    @FXML
    AnchorPane takeCardsPane;
    @FXML
    GridPane boardPane, comGoals, bookshelfPane, persGoal, youSelectedThis, score_0, score_1;
    @FXML
    Label errorArea;
    @FXML
    Button selectTiles, undoSelection;
    @FXML
    Label yourPoints;
    private ClientController clientController;
    private ArrayList<Integer> selectedTiles;

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
                    tileImage.setFitHeight(50);
                    tileImage.setFitWidth(50);

                    boardPane.add(tileImage, j, i);
                }
            }
        }
    }

    @Override
    public void updateBookshelf(String nickname, ItemCard[][] bookshelf) {
        if (clientController.getMyNickname().equals(nickname)) {
            for (int i = 0; i < BOOKSHELF_HEIGHT; i++) {
                for (int j = 0; j < BOOKSHELF_LENGTH; j++) {
                    if (bookshelf[i][j] != null) {
                        String itemName = bookshelf[i][j].getMyItem().toString().toLowerCase();
                        String itemNumber = bookshelf[i][j].getMyNum().toString();
                        String myItem = itemName + itemNumber;

                        ImageView tileImage = new ImageView(GUIResources.getItem(myItem));
                        tileImage.setPreserveRatio(true);
                        tileImage.setFitHeight(24);
                        tileImage.setFitWidth(24);

                        bookshelfPane.add(tileImage, j, i);
                    }
                }
            }
        }
    }

    @Override
    public void comGoal(Map<Integer, Integer> playerCommonGoal) {
        int n = 0;
        for (Integer i : playerCommonGoal.keySet()) {
            ImageView scoreImage = new ImageView(GUIResources.getScore("sc0" + playerCommonGoal.get(i).toString()));
            scoreImage.setFitHeight(60);
            scoreImage.setFitWidth(60);

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
            comGoalImage.setFitHeight(150);
            comGoalImage.setFitWidth(200);

            comGoals.add(comGoalImage, n, 0);
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
    public void initialize(ClientController clientController) {
        this.clientController = clientController;
        yourPoints.setText("You have 0 points");
        errorArea.setVisible(false);
        selectedTiles = new ArrayList<>();
        bindEvents();
    }

    @Override
    public void printError(String error) {
        errorArea.setVisible(true);
        errorArea.setText(error);
    }

    @Override
    public void bindEvents() {
        boardPane.addEventHandler(MouseEvent.MOUSE_CLICKED, this::highlightTile);
        selectTiles.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectTiles());

    }

    public void highlightTile(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        int clickedColumn = GridPane.getColumnIndex(clickedNode);
        int clickedRow = GridPane.getRowIndex(clickedNode);

        if (boardPane.getChildren().contains(clickedNode)) {
            ImageView imageView = (ImageView) clickedNode;

            if (imageView != null && selectedTiles.size() < 3) {
                removeTile(imageView, clickedColumn, clickedRow);
            }
        }
    }

    public void removeTile(ImageView imageView, int clickedColumn, int clickedRow) {
        int coord = Position.getNumber(clickedColumn, clickedRow);
        printError("You selected position " + coord);
        selectedTiles.add(coord);

        imageView.setPreserveRatio(true);
        imageView.setOpacity(1);
        imageView.setFitWidth(50);
        imageView.setFitWidth(50);

        youSelectedThis.add(imageView, selectedTiles.size() - 1, 0);
    }

    public void updateCurrPlayer(String player) {
        selectedTiles.clear();
        selectTiles.setDisable(false);
    }

    public void selectTiles() {
        InputController inputController = new InputController(clientController);

        if (!inputController.checkSelection(selectedTiles)) {
            printError("ERROR: wrong selection");
        }

        clientController.setSelectedTiles(selectedTiles);

        try {
            clientController.selectCard();
            selectTiles.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            printError("ERROR: server error");
        }
    }


}
