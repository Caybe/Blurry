package dao;

import model.Cart;
import model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDAO {
    protected Connection conn;

    public CartDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(Cart cart){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO cart (client_id, movie_id) VALUES(?,?)");
            statement.setInt(1,cart.getClient_id());
            statement.setInt(2,cart.getMovie_id());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Cart cart){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM cart WHERE movie_id = " + cart.getMovie_id() +" AND client_id = " + cart.getClient_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCart(int clientId){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM cart WHERE client_id = " + clientId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getClientCart(int client_id){
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie INNER JOIN cart ON movie.movie_id = cart.movie_id WHERE movie.movie_id IN" +
                "(SELECT movie_id FROM cart WHERE client_id = " + client_id + ")";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            while (result.next()){
                Movie movie = new Movie(result.getInt("movie_id"), result.getString("title"), result.getString("description"), result.getString("release_date"), result.getString("image"), result.getFloat("rate"),result.getFloat("price"));

                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;

    }
}
