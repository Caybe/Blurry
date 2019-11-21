package controller;

import connection.Connect;
import dao.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

public class MovieController {
    @FXML
    private Text titleTxt;
    @FXML
    private Text descriptionTxt;
    @FXML
    private Text directorTxt;
    @FXML
    private Text releaseTxt;
    @FXML
    private Text rateTxt;
    @FXML
    private ImageView imageMovie;
    @FXML
    private Text actorsTxt;
    @FXML
    private Text categoriesTxt;
    @FXML
    private Text priceTxt;
    @FXML
    private Button actionBtn;
    @FXML
    private Text successTxt;
    @FXML
    private RadioButton rd1;
    @FXML
    private RadioButton rd2;
    @FXML
    private RadioButton rd3;
    @FXML
    private RadioButton rd4;
    @FXML
    private RadioButton rd5;
    @FXML
    private Text yourRateTxt;
    @FXML
    private Button wishBtn;

    private MainApp main;
    private Movie movie;
    private MovieDao movieDao;
    private ActorDAO actorDAO;
    private DirectorDAO directorDAO;
    private CategoryDAO categorieDAO;
    private PurchaseDAO purchaseDAO;
    private RateDAO rateDAO;
    WishListDAO wishListDAO;
    private ToggleGroup toggleGroup;
    private DecimalFormat df;

    public MovieController() {

        movieDao = new MovieDao(Connect.getInstance());
        movie = movieDao.selectRow(1);
        actorDAO = new ActorDAO(Connect.getInstance());
        directorDAO = new DirectorDAO(Connect.getInstance());
        categorieDAO = new CategoryDAO(Connect.getInstance());
        purchaseDAO = new PurchaseDAO(Connect.getInstance());
        rateDAO = new RateDAO(Connect.getInstance());
        wishListDAO = new WishListDAO(Connect.getInstance());
        toggleGroup = new ToggleGroup();

        df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits(2);
    }

    public void setMain(MainApp main) {
        this.main = main;
    }

    public void setMovieToDisplay(int movie_id){
        movie = movieDao.selectRow(movie_id);
    }

    @FXML
    public void initialize(){
        /* Radio buttons are not visible if your not logged in */
        rd1.setVisible(false);
        rd2.setVisible(false);
        rd3.setVisible(false);
        rd4.setVisible(false);
        rd5.setVisible(false);
        rd1.setToggleGroup(toggleGroup);
        rd2.setToggleGroup(toggleGroup);
        rd3.setToggleGroup(toggleGroup);
        rd4.setToggleGroup(toggleGroup);
        rd5.setToggleGroup(toggleGroup);
        yourRateTxt.setVisible(false);

        wishBtn.setVisible(true);
        successTxt.setVisible(false);
        titleTxt.setText(movie.getTitle());
        descriptionTxt.setText(movie.getDescription());
        String rate = df.format(rateDAO.getMovieRate(movie.getMovie_id()));
        rateTxt.setText(rate);
        releaseTxt.setText(movie.getRelease_date());
        priceTxt.setText(movie.getPrice() + "€");
        actorsTxt.setText("");
        for(Actor actor: actorDAO.getByMovie(movie.getMovie_id())){
            actorsTxt.setText(actorsTxt.getText() + ", " + actor.getName());
        }
        //Removing the last coma in actors list
        actorsTxt.setText(actorsTxt.getText().replaceFirst(",", ""));

        directorTxt.setText("");
        for(Director director: directorDAO.getByMovie(movie.getMovie_id())){
            directorTxt.setText(directorTxt.getText() + ", " + director.getName());
        }
        //Removing the last coma in directors list
        directorTxt.setText(directorTxt.getText().replaceFirst(",", ""));

        try {
            FileInputStream input = new FileInputStream(movie.getImage());
            Image image = new Image(input);
            imageMovie.setImage(image);

        } catch (FileNotFoundException | NullPointerException e) {
            try {
                FileInputStream input = new FileInputStream("src/images/movie/default.png");
                Image image = new Image(input);
                imageMovie.setImage(image);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        categoriesTxt.setText("");
        for(Category category : categorieDAO.getByMovie(movie.getMovie_id())){
            categoriesTxt.setText(categoriesTxt.getText() + ", " + category.getName());
        }
        //Removing the last coma in categories list
        categoriesTxt.setText(categoriesTxt.getText().replaceFirst(",", ""));

        if (main!=null && main.getCurrentClient() != null){
            Client client = main.getCurrentClient();
            if(client.getClient_id() == 1){ /* if the user is the administrator */
                actionBtn.setText("Modify");
                actionBtn.setVisible(true);
                wishBtn.setVisible(false);
            }else{      /* if the user is a regular client */

                if(purchaseDAO.exist(movie.getMovie_id(), client.getClient_id())){
                    successTxt.setText("Movie already purchased!");
                    successTxt.setVisible(true);
                    actionBtn.setVisible(false);
                    wishBtn.setVisible(false);
                }else{
                    actionBtn.setDisable(false);
                    actionBtn.setText("Add to cart");
                    actionBtn.setVisible(true);
                }
                rd1.setVisible(true);
                rd2.setVisible(true);
                rd3.setVisible(true);
                rd4.setVisible(true);
                rd5.setVisible(true);
                yourRateTxt.setVisible(true);
                if(rateDAO.exist(movie.getMovie_id(), client.getClient_id())){
                    disableRate();
                    setSelectedRadioButton();
                }else{
                    enableRate();
                }


                if(wishListDAO.exist(movie.getMovie_id(), client.getClient_id())){
                    wishBtn.setDisable(true);
                }else{
                    wishBtn.setDisable(false);
                }
            }
        }else{ /* if the user is a visitor the action button redirect to the connection pane */
            actionBtn.setText("Add to cart");
            actionBtn.setVisible(true);
            wishBtn.setVisible(false);
        }

    }

    /**
     * Handle a click on the movie back button.
     * Request the mainApp to show the movie list
     */
    @FXML
    public void backBtn(){
        main.showMovieList();
    }

    /**
     *  Handle a click on the movie action Button.
     *  The action can be buying the movie or
     *  modifying it if the administrator is connected
     */
    @FXML
    public void actionBtnHandler(){
        if (main!= null && main.getCurrentClient() != null){
            Client client = main.getCurrentClient();
            if(client.getClient_id() == 1){ /* if the user is the administrator */
                main.showMovieManagement(movie.getMovie_id());
            }else{      /* if the user is a regular client */
                CartDAO cartDAO = new CartDAO(Connect.getInstance());
                Cart cart = new Cart(client.getClient_id(),movie.getMovie_id());
                cartDAO.insertRow(cart);
                displaySuccess();
            }
        }else{ /* The user is not connected -> a click redirect him to the connection pane */
            main.showConnection();
        }
    }

    /**
     *  Handle a click on the "Add to wish list" button
     */
    @FXML
    private void wishBtnHandler(){
        if(main!=null && main.getCurrentClient() != null){
            Wish wish = new Wish(movie.getMovie_id(), main.getCurrentClient().getClient_id());
            wishListDAO.insertRow(wish);
            wishBtn.setDisable(true);
        }
    }

    public void displaySuccess(){
        successTxt.setText("Movie successfully added!");
        successTxt.setVisible(true);
    }

    @FXML
    public void rate1(){
        setRate(1);
    }

    @FXML
    public void rate2(){
        setRate(2);
    }

    @FXML
    public void rate3(){
        setRate(3);
    }

    @FXML
    public void rate4(){
        setRate(4);
    }

    @FXML
    public void rate5(){
        setRate(5);
    }

    private void setRate(int clientRate){
        disableRate();
        Rate rate = new Rate();
        rate.setClient_id(main.getCurrentClient().getClient_id());
        rate.setMovie_id(movie.getMovie_id());
        rate.setRate(clientRate);
        rateDAO.insertRow(rate);
    }

    private void disableRate(){
        rd1.setDisable(true);
        rd2.setDisable(true);
        rd3.setDisable(true);
        rd4.setDisable(true);
        rd5.setDisable(true);
    }

    private void enableRate(){
        rd1.setDisable(false);
        rd2.setDisable(false);
        rd3.setDisable(false);
        rd4.setDisable(false);
        rd5.setDisable(false);
        unselectRadioButton();
    }

    /**
     * Select the radio button corresponding to the rate given by the client
     */
    private void setSelectedRadioButton(){
        int clientRate = rateDAO.getClientRate(main.getCurrentClient().getClient_id(), movie.getMovie_id());
        switch (clientRate){
            case 1:
                rd1.setSelected(true);
                break;
            case 2:
                rd2.setSelected(true);
                break;
            case 3:
                rd3.setSelected(true);
                break;
            case 4:
                rd4.setSelected(true);
                break;
            case 5:
                rd5.setSelected(true);
                break;
        }
    }

    private void unselectRadioButton(){
        rd1.setSelected(false);
        rd2.setSelected(false);
        rd3.setSelected(false);
        rd4.setSelected(false);
        rd5.setSelected(false);
    }
}
