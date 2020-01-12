package controller;

import connection.Connect;
import dao.PurchaseDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Client;
import model.Movie;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseController {

    @FXML
    private VBox cartBox;
    @FXML
    private Text totalTxt;
    @FXML
    private Button payBtn;

    private MainApp main;
    private PurchaseDAO purchaseDAO;
    private HashMap<Movie, Float> movies;
    private DecimalFormat df;

    public PurchaseController() {
        purchaseDAO = new PurchaseDAO(Connect.getInstance());
        movies = new HashMap<>();
    }

    public void setMain(MainApp mainApp) {
        this.main = mainApp;
    }

    @FXML
    public void initialize() {

        if (main!=null && main.getCurrentClient() != null) {
            Client client = main.getCurrentClient();
            cartBox.getChildren().clear();
            ArrayList<Integer> purchaseIds = purchaseDAO.getPurchaseIds(client.getClient_id());
            for(Integer purchaseId : purchaseIds){
                movies = purchaseDAO.getClientPurchase(client.getClient_id(), purchaseId);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/OnePurchasePane.fxml"));
                try {
                    AnchorPane moviePane = (AnchorPane) loader.load();
                    OnePurchaseController onePurchaseController = loader.getController();
                    onePurchaseController.setMain(main);
                    onePurchaseController.setMovies(movies);
                    onePurchaseController.setClient(client);
                    onePurchaseController.setPurchaseId(purchaseId);
                    Date dateSql = purchaseDAO.getPurchaseDate(purchaseId);
                    onePurchaseController.setDateOfPurchase(dateSql);
                    onePurchaseController.initialize();
                    cartBox.getChildren().add(moviePane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
