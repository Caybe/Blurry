package controller;

import connection.Connect;
import dao.MovieDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Client;
import model.Movie;

import java.io.IOException;
import java.util.ArrayList;

public class MovieListController {

    @FXML
    private VBox movieList;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Text noResultTxt;

    private MainApp main;
    private MovieDao movieDao;
    private ArrayList<Movie> movies;
    private MovieThumbnailController movieThumbnailController;
    private int category_id = 0;
    private String research;
    private String filter = "Filter";

    public MovieListController() {
        movieDao = new MovieDao(Connect.getInstance());
        movies= new ArrayList<>();
        initMovieList();

    }

    public void setMain(MainApp main) {
        this.main = main;
        initialize();
    }

    @FXML
    public void initialize(){
        noResultTxt.setVisible(true);
        if(main != null){
            initMovieList();
            /* Setting scrollSpane speed */
            ScrollPaneSkin paneSkin = new ScrollPaneSkin(scrollPane);
            paneSkin.getVerticalScrollBar().setBlockIncrement(0.2);
            paneSkin.getVerticalScrollBar().setUnitIncrement(0.2);
            paneSkin.getSkinnable().addEventFilter(ScrollEvent.SCROLL, event -> {

                if (event.getDeltaX() < 0) {
                    paneSkin.getHorizontalScrollBar().increment();
                } else if (event.getDeltaX() > 0) {
                    paneSkin.getHorizontalScrollBar().decrement();
                }

                if (event.getDeltaY() < 0) {
                    paneSkin.getVerticalScrollBar().increment();
                } else if (event.getDeltaY() > 0) {
                    paneSkin.getVerticalScrollBar().decrement();
                }

                event.consume();
            });

            Movie movie1;
            Movie movie2 = new Movie();
            Movie movie3 = new Movie();
            Movie movie4 = new Movie();

            for(int i = 0; i < movies.size(); i= i+3){
                try {
                    movie1 = movies.get(i);
                    if(i < movies.size()-1){
                        movie2 = movies.get(i+1);
                    }else{
                        movie2.setMovie_id(0);
                    }
                    if(i < movies.size()-2){
                        movie3 = movies.get(i+2);
                    }else{
                        movie3.setMovie_id(0);
                    }
                    if(i < movies.size()-3){
                        movie4 = movies.get(i+3);
                    }else{
                        movie4.setMovie_id(0);
                    }
                    noResultTxt.setVisible(false);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../view/MovieThumbnailPane.fxml"));
                    AnchorPane moviePane = (AnchorPane) loader.load();
                    MovieThumbnailController movieThumbnailController = loader.getController();
                    movieThumbnailController.setMainApp(main);
                    movieThumbnailController.setMovieToDisplay(movie1.getMovie_id(), movie2.getMovie_id(), movie3.getMovie_id(), movie4.getMovie_id()); // setting the movie to display
                    movieThumbnailController.initialize(); // Refreshing the controller with the selected Movie

                    movieList.getChildren().add(moviePane);
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    public void setCategory( int id) {
        this.category_id = id;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void initMovieList(){
        if(!movies.isEmpty()){
            movies.clear();
        }
        if(filter == null || filter.equals("Filter")){
            if(research != null && category_id != 0){
                movies = movieDao.selectRowsByCategoryAndResearch(category_id, research);
            }else if(research != null && category_id == 0){
                movies = movieDao.selectRowsByResearch(research);
            }else if(category_id != 0 && research == null){
                movies = movieDao.selectRowsByCategory(category_id);
            }else{
                movies = movieDao.getAllRows();
            }
        }else{
            switch (filter){
                case "Top Rated":
                    movies = movieDao.getAllRowsOrderedByRate();
                    break;
                case "Most Purchased":
                    movies = movieDao.getAllRowsOrderedByPurchase();
                    break;
            }
        }


    }

}
