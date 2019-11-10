package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Client;
import model.Movie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CartComponentController {
    @FXML
    private ImageView imageView;
    @FXML
    private Text titleTxt;
    @FXML
    private Text priceTxt;
    @FXML
    private Button removeBtn;


    private MainApp main;
    private Movie movie;
    private Client client;
    private float price = 0;

    public CartComponentController() {
    }

    public void setMainApp(MainApp main) {
        this.main = main;
    }


    public void setMovieToDisplay(Movie movie) {
        this.movie = movie;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void hideRemoveBtn(){
        removeBtn.setVisible(false);
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @FXML
    public void initialize() {
        if(movie != null){
            FileInputStream input = null;
            try {
                input = new FileInputStream(movie.getImage());
                Image image = new Image(input);
                imageView.setImage(image);

            } catch (FileNotFoundException | NullPointerException e) {
                try {
                    input = new FileInputStream("src/images/movie/default.png");
                    Image image = new Image(input);
                    imageView.setImage(image);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

            titleTxt.setText(movie.getTitle());
            if(price == 0){
                priceTxt.setText(movie.getPrice() + "€");
            }else{
                priceTxt.setText(price+ "€");
            }
        }
    }

    @FXML
    public void removeFromCart(){
        main.removeFromCart(movie, client);
    }

    @FXML
    public void showMovie(){
        main.showMovie(movie.getMovie_id());
    }
}
