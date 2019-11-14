package dao;


import model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MovieDao {

    protected Connection conn;


    public MovieDao(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(Movie movie) {

        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO movie (title, description," +
                    " release_date, image, price) VALUES(?,?,?,?,?) ");
            statement.setString(1,movie.getTitle());
            statement.setString(2,movie.getDescription());
            statement.setString(3, movie.getRelease_date());
            statement.setString(4, movie.getImage());
            statement.setFloat(5, movie.getPrice());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Movie movie){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM play WHERE movie_id =" + movie.getMovie_id());
            statement.executeUpdate();
            statement = conn.prepareStatement("DELETE FROM direct WHERE movie_id =" + movie.getMovie_id());
            statement.executeUpdate();
            statement = conn.prepareStatement("DELETE FROM movie_categories WHERE movie_id =" + movie.getMovie_id());
            statement.executeUpdate();
            statement = conn.prepareStatement("DELETE FROM movie WHERE movie_id =" + movie.getMovie_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRow(Movie movie){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE movie SET title = ?, description = ?, release_date = ?, image = ?, price = ? WHERE movie_id =" + movie.getMovie_id());

            statement.setString(1,movie.getTitle());
            statement.setString(2,movie.getDescription());
            statement.setString(3, movie.getRelease_date());
            statement.setString(4, movie.getImage());
            statement.setFloat(6, movie.getPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Movie selectRow(int movie_id){
        Movie movie = new Movie();
        String sql = "SELECT * FROM movie WHERE movie_id = " + movie_id;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                movie = new Movie(movie_id, result.getString("title"), result.getString("description"),result.getString("release_date"), result.getString("image"), result.getFloat("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public Movie selectRowByTitleAndDesc(Movie movie){
        String sql = "SELECT * FROM movie WHERE title = '" + movie.getTitle() + "' AND description = '" + movie.getDescription()+"'";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                movie = new Movie(result.getInt("movie_id"), result.getString("title"), result.getString("description"),result.getString("release_date"), result.getString("image"), result.getFloat("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public ArrayList<Movie> selectRowsByCategoryAndResearch(int category_id, String research){
        ArrayList<Movie> movies = new ArrayList<>();
        HashSet<Movie> movieHashSet = new HashSet<>();
        movieHashSet.addAll(selectRowsByCategory(category_id));
        movieHashSet.retainAll(selectRowsByResearch(research));
        movies.addAll(movieHashSet);

        return movies;
    }

    public ArrayList<Movie> selectRowsByResearch(String research){
        ArrayList<Movie> movies = new ArrayList<>();


        String sql = "SELECT * FROM movie WHERE UPPER(title) LIKE UPPER('%"+research+"%') OR movie_id IN " +
                "(SELECT movie_id FROM play WHERE actor_id IN (" +
                "SELECT actor_id FROM actor WHERE UPPER(name) LIKE UPPER('%"+research+"%')))" +
                "OR movie_id IN" +
                "(SELECT movie_id FROM direct WHERE director_id IN " +
                "(SELECT director_id FROM director WHERE UPPER(name) LIKE UPPER('%"+research+"%'))) ";
        return getMovies(movies, sql);
    }

    public ArrayList<Movie> selectRowsByCategory(int category_id){
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT movie.* FROM movie INNER JOIN movie_categories ON movie.movie_id = movie_categories.movie_id WHERE category_id = "+ category_id;

        return getMovies(movies, sql);
    }

    public ArrayList<Movie> getAllRows(){
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movie ORDER BY title";

        return getMovies(movies, sql);
    }

    public ArrayList<Movie> getAllRowsOrderedByPurchase(){
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT m.*, count(p.movie_id) FROM movie m LEFT JOIN purchase p ON m.movie_id = p.movie_id " +
                "GROUP BY m.movie_id ORDER BY count(p.movie_id) DESC";
        return getMovies(movies, sql);
    }

    public ArrayList<Movie> getAllRowsOrderedByRate(){
        ArrayList<Movie> movies = new ArrayList<>();
        String sql = "SELECT movie.*, avg(r.rate) FROM movie LEFT JOIN rate r ON movie.movie_id = r.movie_id " +
                "                GROUP BY movie.movie_id " +
                "                 ORDER BY avg(r.rate) DESC nulls last , count(r.movie_id) DESC";
        return getMovies(movies, sql);
    }


    private ArrayList<Movie> getMovies(ArrayList<Movie> movies, String sql) {

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            while (result.next()){
                Movie movie = new Movie(result.getInt("movie_id"), result.getString("title"), result.getString("description"), result.getString("release_date"), result.getString("image"), result.getFloat("price"));

                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public int getNumberOfMovie(){
        int numberOfMovie = 0;
        String sql = "SELECT count(*) FROM  movie";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            if(result.first()){
                numberOfMovie = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfMovie;
    }

}
