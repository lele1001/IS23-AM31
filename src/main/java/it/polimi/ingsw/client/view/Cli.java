package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.connection.ConnectionClient;
import it.polimi.ingsw.client.connection.ConnectionRMI;
import it.polimi.ingsw.client.connection.ConnectionSocket;
import it.polimi.ingsw.server.model.HouseItem;
import it.polimi.ingsw.server.model.ItemCard;

import java.util.Map;
import java.util.Scanner;

public class Cli implements View {
    private Scanner in = new Scanner(System.in);
    ClientController clientController;
    ConnectionClient connectionClient;
    String username;
    String address;
    int port;
    int select=-1;

    public Cli(ClientController clientController) {


        this.clientController=clientController;
        askUsername();
        askConnection();
        System.out.println("Write 0 for RMI, 1 for Socket");
        do {
            if (in.hasNextInt()) {
                select = in.nextInt();
            }
        }while (select != 0 && select != 1);
        askIP();
        askPort();

        System.out.println("Select Ip Address");
        if (in.hasNextLine()) {
            address = in.nextLine();
        }
        System.out.println("Select Ip Port");
        if (in.hasNextInt()) {
            port = in.nextInt();
        }


        if (select == 0) {
            connectionClient=new ConnectionRMI(clientController);
        } else {
            connectionClient=new ConnectionSocket(clientController);
        }
    }

    private void askConnection() {

    }

    private void askPort() {
    }

    private void askIP() {

    }

    void askUsername(){


    }

    @Override
    public void printBoard(ItemCard[][] board) {

    }

    @Override
    public void printBookshelf(Map<String, ItemCard[][]> bookshelfs) {

    }

    @Override
    public void printError(String error) {

    }

    @Override
    public void printCommonGoal(Map<Integer, Integer> playerComGoal) {

    }

    @Override
    public void printPoints(int myPoint) {

    }

    @Override
    public void printPersGoal(Map<Integer, HouseItem> myPersGoal) {

    }

    @Override
    public void print(String yourTurn) {

    }

    @Override
    public void printMyBookshelf(ItemCard[][] book) {

    }
}
