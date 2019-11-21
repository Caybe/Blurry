package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import model.Movie;
import model.Purchase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseDAO {
    private Connection conn;

    public PurchaseDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(Purchase purchase){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO purchase (purchase_id, client_id, movie_id, date_of_purchase, price) VALUES(?,?,?, NOW(), ?)");
            statement.setInt(1, purchase.getPurchaseId());
            statement.setInt(2,purchase.getClient_id());
            statement.setInt(3,purchase.getMovie_id());
            statement.setFloat(4, purchase.getPrice());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Purchase purchase){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM purchase WHERE movie_id = " + purchase.getMovie_id() +" AND client_id = " + purchase.getClient_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getPurchaseIds(int client_id){
        ArrayList<Integer> purchaseId = new ArrayList<>();
        String sql = "SELECT DISTINCT purchase_id, purchase.date_of_purchase FROM purchase WHERE client_id = "+ client_id +" ORDER BY date_of_purchase DESC";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                purchaseId.add(result.getInt("purchase_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchaseId;
    }

    public Date getPurchaseDate(int purchaseId){
        String sql = "SELECT date_of_purchase FROM purchase WHERE purchase_id = " + purchaseId;
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            if(result.first()){
                return  result.getDate(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public HashMap<Movie, Float> getClientPurchase(int clientId, int purchaseId){
        HashMap<Movie, Float> movies = new HashMap<Movie, Float>();
        String sql = "SELECT DISTINCT movie.*, purchase.price as pprice FROM movie RIGHT JOIN purchase ON movie.movie_id = purchase.movie_id WHERE movie.movie_id IN" +
                "(SELECT movie_id FROM purchase WHERE purchase_id = "+ purchaseId +") AND purchase.client_id =" +clientId;
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Movie movie = new Movie(result.getInt("movie_id"), result.getString("title"), result.getString("description"), result.getString("release_date"), result.getString("image"), result.getFloat("pprice"));

                movies.put(movie, result.getFloat("pprice"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;

    }

    public int getNewPurchaseId(){
        String sql= "SELECT max(purchase_id)+1 FROM purchase";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            if(result.first()){
                return  result.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean exist(int movieId, int clientId){
        String sql= "SELECT * FROM purchase WHERE movie_id = " + movieId +" AND client_id = " + clientId;
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                return  true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public HashMap<String, Integer> getMoviePurchaseStat(){
        HashMap<String,Integer> categoriesPurchased = new HashMap<>();
        String sql = "SELECT movie.title ,count(purchase.movie_id) as nbMovie FROM purchase " +
                "INNER JOIN movie ON movie.movie_id = purchase.movie_id  GROUP BY movie.title ORDER BY nbMovie desc LIMIT 10";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                categoriesPurchased.put(result.getString(1), result.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return categoriesPurchased;
    }

    public int getNumberOfPurchase(){
        int numberOfPurchase = 0;
        String sql = "SELECT count(*) FROM  purchase";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            if(result.first()){
                numberOfPurchase = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfPurchase;
    }

    public float getMoneyEarned(){
        float money = 0;
        String sql = "SELECT sum(price) FROM  purchase";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            if(result.first()){
                money= result.getFloat(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return money;
    }


}
