package controller;

import connection.Connect;
import dao.MovieDao;
import javafx.fxml.FXML;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Movie;


import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MovieThumbnailController {

    @FXML
    private Text movie_id1;
    @FXML
    private Text movie_id2;
    @FXML
    private Text movie_id3;
    @FXML
    private Text movie_id4;
    @FXML
    private Text title1;
    @FXML
    private Text title2;
    @FXML
    private Text title3;
    @FXML
    private Text title4;
    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;
    @FXML
    private VBox box1;
    @FXML
    private VBox box2;
    @FXML
    private VBox box3;
    @FXML
    private VBox box4;

    private MainApp main;
    private MovieDao movieDao;
    private Movie movie1;
    private Movie movie2;
    private Movie movie3;
    private Movie movie4;

    public MovieThumbnailController() {
        movieDao = new MovieDao(Connect.getInstance());
        movie1 = movieDao.selectRow(1);

    }

    public void setMainApp(MainApp main) {
        this.main = main;
    }

    public void setMovieToDisplay(int movie1, int movie2, int movie3, int movie4) {
        this.movie1 = movieDao.selectRow(movie1);
        if (movie2 != 0) {
            this.movie2 = movieDao.selectRow(movie2);
        } else {
            this.movie2 = null;
        }

        if(movie3 != 0){
            this.movie3 = movieDao.selectRow(movie3);
        }else{
            this.movie3 = null;
        }

        if(movie4 != 0){
            this.movie4 = movieDao.selectRow(movie4);
        }else{
            this.movie4 = null;
        }

    }


    @FXML
    public void initialize() {
//        // Applying a blurry effect on the images
//        BoxBlur bb = new BoxBlur();
//        bb.setWidth(20);
//        bb.setHeight(10);
//        bb.setIterations(30);
//        image1.setEffect(bb);
//        image2.setEffect(bb);
//        image3.setEffect(bb);
//        image4.setEffect(bb);

        // Movie 1
        initMovie(movie1, movie_id1, image1, title1);
        FileInputStream input;

        //Other Movies
        // Checking if there are still some movie to display
        if (prepareMovie(movie2, title2, box2)) {initMovie(movie2, movie_id2, image2, title2);}
        if (prepareMovie(movie3, title3, box3)) {initMovie(movie3, movie_id3, image3, title3);}
        if (prepareMovie(movie4, title4, box4)) {initMovie(movie4, movie_id4, image4, title4);}


        //Handle a click on the movie 1
        box1.setOnMouseClicked(e -> {
            Text movie_id = (Text) box1.getChildren().get(1);
            main.showMovie(Integer.valueOf(movie_id.getText().toString()));
        });

        //Handle a click on the movie 2
        box2.setOnMouseClicked(e ->{
            Text movie_id = (Text) box2.getChildren().get(1);
            main.showMovie(Integer.valueOf(movie_id.getText().toString()));
        });

        //Handle a click on the movie 3
        box3.setOnMouseClicked(e ->{
            Text movie_id = (Text) box3.getChildren().get(1);
            main.showMovie(Integer.valueOf(movie_id.getText().toString()));
        });

        //Handle a click on the movie 4
        box4.setOnMouseClicked(e ->{
            Text movie_id = (Text) box4.getChildren().get(1);
            main.showMovie(Integer.valueOf(movie_id.getText().toString()));
        });
    }

    private boolean prepareMovie(Movie movie, Text title, VBox box){
        if (movie == null) {
            title.setText("");
            box.setVisible(false);
            return false;
        } else {  // If there is one
            box.setVisible(true);
            return true;
        }
    }

    private void initMovie(Movie movie, Text movie_id, ImageView imageView, Text title) {
        movie_id.setText(String.valueOf(movie.getMovie_id()));
        title.setText(movie.getTitle());
        FileInputStream input = null;
        try {
            input = new FileInputStream(movie.getImage());
            Image image = new Image(input);
            imageView.setImage(image);

        } catch (FileNotFoundException | NullPointerException e) {
            try {
                input = new FileInputStream("images/movie/default.png");
                Image image = new Image(input);
                imageView.setImage(image);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }


}
