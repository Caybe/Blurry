package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Client;
import model.Movie;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class OnePurchaseController {
    @FXML
    private Text dateTxt;
    @FXML
    private VBox purchaseBox;
    @FXML
    private Text purchaseTxt;
    @FXML
    private Text totalPriceTxt;

    private MainApp main;
    private Client client;
    private HashMap<Movie, Float> movies;
    private DecimalFormat df;
    private double totalCart;
    private Date dateOfPurchase;
    private int purchaseId;
    private float price;


    public OnePurchaseController() {
        df = new DecimalFormat ( ) ;
        df.setMaximumFractionDigits (2);
    }

    public void setMain(MainApp mainApp) {
        this.main = mainApp;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMovies(HashMap<Movie, Float> movies) {
        this.movies = movies;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @FXML
    public void initialize(){
        if (client != null) {
            purchaseBox.getChildren().clear();
            for (Map.Entry<Movie, Float> elem : movies.entrySet()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/MovieElemPane.fxml"));
                try {
                    AnchorPane moviePane = (AnchorPane) loader.load();
                    MovieElemController movieElemController = loader.getController();
                    movieElemController.setMainApp(main);
                    movieElemController.setMovieToDisplay(elem.getKey()); // setting the movie to display
                    movieElemController.setClient(client);
                    movieElemController.hideRemoveBtn();
                    movieElemController.setPrice(elem.getValue());
                    movieElemController.initialize(); // Refreshing the controller with the selected Movie
                    purchaseBox.getChildren().add(moviePane);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /* Sum the prices */
                totalCart += elem.getValue();
            }
            String total = df.format(totalCart);
            totalPriceTxt.setText(totalPriceTxt.getText() + total + "€");
            dateTxt.setText(dateTxt.getText() + " " + dateOfPurchase);
            purchaseTxt.setText(purchaseTxt.getText() +" " + purchaseId);

        }
    }




}
