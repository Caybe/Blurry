package controller;

import connection.Connect;
import dao.CartDAO;
import dao.PurchaseDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartController {

    @FXML
    private VBox cartBox;
    @FXML
    private Text totalTxt;
    @FXML
    private Button payBtn;


    private MainApp main;
    private CartDAO cartDAO;
    private ArrayList<Movie> movies;
    private DecimalFormat df;
    private double totalCart;


    public CartController() {
        cartDAO = new CartDAO(Connect.getInstance());
        movies = new ArrayList<>();
        df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits (2);
    }

    public void setMain(MainApp mainApp) {
        this.main = mainApp;
    }


    @FXML
    public void initialize(){
        totalCart = 0;
        if(main!=null && main.getCurrentClient() != null){
            movies = cartDAO.getClientCart(main.getCurrentClient().getClient_id());
            cartBox.getChildren().clear();
            for(Movie movie : movies){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MovieElemPane.fxml"));
                try {
                    AnchorPane moviePane = (AnchorPane) loader.load();
                    MovieElemController movieElemController = loader.getController();
                    movieElemController.setMainApp(main);
                    movieElemController.setMovieToDisplay(movie); // setting the movie to display
                    movieElemController.setClient(main.getCurrentClient());
                    movieElemController.initialize(); // Refreshing the controller with the selected Movie
                    cartBox.getChildren().add(moviePane);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /* Sum the prices */
                totalCart += movie.getPrice();
            }
            String total = df.format(totalCart);
            totalTxt.setText("Total: " + total  + "€");
        }
    }

    public void initMovieList(){
        if(!movies.isEmpty()){
            movies.clear();
        }
        movies.addAll(cartDAO.getClientCart(main.getCurrentClient().getClient_id()));

    }

    public void removeMovie(Movie movie){
        if(movies != null){
            Cart cart = new Cart();
            cart.setClient_id(main.getCurrentClient().getClient_id());
            cart.setMovie_id(movie.getMovie_id());
            cartDAO.deleteRow(cart);
            initialize();
        }
    }

    @FXML
    public void pay() {
//        Invoice invoice = new Invoice();
        PurchaseDAO purchaseDAO = new PurchaseDAO(Connect.getInstance());
        int clientId = main.getCurrentClient().getClient_id();
        int purchaseId =  purchaseDAO.getNewPurchaseId();
        for(Movie movie : cartDAO.getClientCart(clientId)){
            Purchase purchase = new Purchase();
            purchase.setClient_id(clientId);
            purchase.setMovie_id(movie.getMovie_id());
            purchase.setPurchaseId(purchaseId);
            purchase.setPrice(movie.getPrice());
            purchaseDAO.insertRow(purchase);
        }
        cartDAO.deleteCart(clientId);
        main.showPurchases(main.getCurrentClient());
    }
}
