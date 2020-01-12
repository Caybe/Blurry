package controller;

import connection.Connect;
import dao.MovieDao;
import dao.WishListDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Client;
import model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class WishListController {

    @FXML
    private VBox wishBox;

    private MainApp mainApp;
    private WishListDAO wishListDAO;
    private MovieDao movieDao;

    public WishListController() {
        wishListDAO = new WishListDAO(Connect.getInstance());
        movieDao = new MovieDao(Connect.getInstance());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize(){
        if (mainApp!= null && mainApp.getCurrentClient() != null) {
            Client client = mainApp.getCurrentClient();
            ArrayList<Movie> movies = new ArrayList<>();
            movies = movieDao.getMovieWished(client.getClient_id());
            wishBox.getChildren().clear();
            for (Movie movie : movies) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MovieElemPane.fxml"));
                try {
                    AnchorPane moviePane = (AnchorPane) loader.load();
                    MovieElemController movieElemController = loader.getController();
                    movieElemController.setMainApp(mainApp);
                    movieElemController.setMovieToDisplay(movie); // setting the movie to display
                    movieElemController.setClient(client);
                    movieElemController.setOrigin(1);
                    movieElemController.setPrice(movie.getPrice());
                    movieElemController.initialize(); // Refreshing the controller with the selected Movie
                    wishBox.getChildren().add(moviePane);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }
    }

}
