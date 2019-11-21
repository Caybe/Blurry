package dao;

import model.Rate;
import model.Wish;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WishListDAO {
    private Connection conn;

    public WishListDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(Wish wish){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO wish_list (client_id, movie_id) VALUES(?,?)");
            statement.setInt(1, wish.getClientId());
            statement.setInt(2, wish.getMovieId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(int clientId, int movieId){
        try{
            PreparedStatement statement = conn.prepareStatement("DELETE FROM wish_list WHERE client_id= ? AND movie_id = ?");
            statement.setInt(1, clientId);
            statement.setInt(2, movieId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Wish> getAllRows(){
        ArrayList<Wish> wishes = new ArrayList<>();
        String sql = "SELECT * FROM wish_list";
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Wish wish = new Wish(resultSet.getInt("movie_id"), resultSet.getInt("client_id"));
                wishes.add(wish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wishes;
    }



    public boolean exist(int movieId, int clientId){
        String sql= "SELECT * FROM wish_list WHERE movie_id = " + movieId +" AND client_id = " + clientId;
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
}
