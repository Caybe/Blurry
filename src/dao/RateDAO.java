package dao;

import connection.Connect;
import model.Movie;
import model.Rate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RateDAO {

    private Connection conn;

    public RateDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert a client rate in the rate table. Update the movie rate average
     * @param rate
     */
    public void insertRow(Rate rate){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO rate (client_id, movie_id, rate) VALUES(?,?,?)");
            statement.setInt(1, rate.getClient_id());
            statement.setInt(2,rate.getMovie_id());
            statement.setInt(3,rate.getRate());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        float rateAvg = 0;
        /* Getting the average rate of the movie */
        String sql = "SELECT  avg(rate) FROM  rate WHERE movie_id = " + rate.getMovie_id();
        try {
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            if(result.first()){
                rateAvg = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* updating the rate field in the movie table */
        MovieDao movieDao = new MovieDao(Connect.getInstance());
        Movie movie = movieDao.selectRow(rate.getMovie_id());
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE movie SET  rate = ? WHERE movie_id =" + movie.getMovie_id());

            statement.setFloat(1, rateAvg);


            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getClientRate(int clientId, int movieId){
        int clientRate = 0;
        String sql = "SELECT rate FROM rate WHERE client_id = "+ clientId + " AND movie_id = "  + movieId;

        try {
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            if(result.first()){
                clientRate = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientRate;
    }

    public float getMovieRate(int movieId){
        float rate = 0;
        String sql = "SELECT avg(rate) FROM  rate WHERE movie_id = " + movieId;

        try {
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            if(result.first()){
                rate = result.getFloat(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rate;
    }

    public boolean exist(int movieId, int clientId){
        String sql= "SELECT * FROM rate WHERE movie_id = " + movieId +" AND client_id = " + clientId;
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

    public ArrayList<Movie> getTopMostRated(){
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT movie.*, avg(r.rate) FROM movie LEFT JOIN rate r ON movie.movie_id = r.movie_id" +
                "                                GROUP BY movie.movie_id " +
                "                               ORDER BY avg(r.rate) DESC nulls last , count(r.movie_id) DESC LIMIT 10";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Movie movie = new Movie(result.getInt("movie_id"), result.getString("title"), result.getString("description"),result.getString("release_date"), result.getString("image"), result.getFloat("price"));
                movies.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

}
