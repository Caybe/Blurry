package controller;

import connection.Connect;
import dao.WishListDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Client;
import model.Movie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MovieElemController {
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
    private int origin = 0;
    private float price = 0;

    public MovieElemController() {
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

    public void setOrigin(int origin) {
        this.origin = origin;
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
        if(origin == 0){
            main.removeFromCart(movie, client);
        }else if(origin == 1){
            WishListDAO wishListDAO = new WishListDAO(Connect.getInstance());
            wishListDAO.deleteRow(client.getClient_id(), movie.getMovie_id());
            main.showWishList();
        }

    }

    @FXML
    public void showMovie(){
        main.showMovie(movie.getMovie_id());
    }
}
