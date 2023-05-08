package it.polimi.ingsw.client.view.GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Bookshelf extends Gui {
    public ImageView showBookshelf() throws FileNotFoundException {
        InputStream bookshelf = new FileInputStream("C:\\Users\\eleon\\Documents\\GitHub\\newVersion\\src\\main\\resources\\boards\\bookshelf.png");
        Image bookshelfImg = new Image(bookshelf);

        //creates the image view
        ImageView imageView = new ImageView();
        imageView.setImage(bookshelfImg);
        imageView.setX(10);
        imageView.setY(10);
        imageView.setFitWidth(575);
        imageView.setPreserveRatio(true);

        return imageView;
    }
}
