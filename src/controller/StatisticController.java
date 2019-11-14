package controller;

import connection.Connect;
import dao.CategoryDAO;
import dao.MovieDao;
import dao.PurchaseDAO;
import dao.RateDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticController {
    @FXML
    private PieChart categoyChart;
    @FXML
    private BarChart purchaseChart;
    @FXML
    private BarChart rateChart;
    @FXML
    private Text moneyEarnedTxt;
    @FXML
    private Text nbPurchaseTxt;
    @FXML
    private Text nbMovieTxt;
    @FXML
    private ScrollPane scrollPane;

    private CategoryDAO categoryDAO;
    private PurchaseDAO purchaseDAO;
    private MovieDao movieDao;
    private RateDAO rateDAO;


    public StatisticController() {
        categoryDAO = new CategoryDAO(Connect.getInstance());
        purchaseDAO = new PurchaseDAO(Connect.getInstance());
        movieDao = new MovieDao(Connect.getInstance());
        rateDAO = new RateDAO(Connect.getInstance());
    }

    @FXML
    public void initialize(){
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

        /* Figures */
        moneyEarnedTxt.setText(moneyEarnedTxt.getText() + purchaseDAO.getMoneyEarned() + "€");
        nbMovieTxt.setText(nbMovieTxt.getText() + movieDao.getNumberOfMovie());
        nbPurchaseTxt.setText(nbPurchaseTxt.getText() + purchaseDAO.getNumberOfPurchase());

        /* Category Pie Chart */
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData = categoryDAO.getCategoryPurchased();
        categoyChart.setData(pieChartData);
        categoyChart.setClockwise(true);
        categoyChart.setLabelLineLength(50);
        categoyChart.setLabelsVisible(true);

        /* Top 10 Most Purchased Movie */
        HashMap<String, Integer> top10purchase = purchaseDAO.getMoviePurchaseStat();
        purchaseChart.getData().clear();
        for(Map.Entry<String, Integer> key : top10purchase.entrySet()){
            XYChart.Series<String, Integer> dataSeries = new XYChart.Series<String, Integer>();
            dataSeries.getData().add(new XYChart.Data<String, Integer>("", key.getValue()));
            dataSeries.setName(key.getKey());

            purchaseChart.getData().add(dataSeries);
        }
       purchaseChart.setVisible(true);

        /* Top 10 Top Rated Movie */
        ArrayList<Movie> topRate = rateDAO.getTopMostRated();
        rateChart.getData().clear();
        for(Movie movie : topRate){
            XYChart.Series<String, Float> dataSeries = new XYChart.Series<String, Float>();
            dataSeries.getData().add(new XYChart.Data<String, Float>("", rateDAO.getMovieRate(movie.getMovie_id())));
            dataSeries.setName(movie.getTitle());

            rateChart.getData().add(dataSeries);
        }
        rateChart.setVisible(true);




    }
}
