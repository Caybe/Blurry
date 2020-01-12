package dao;

import connection.Connect;
import model.Director;
import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DirectorDAO {
    protected Connection conn;


    public DirectorDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(String director) {

        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO director (name) VALUES(?) ");
            statement.setString(1,director);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Director director){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM director WHERE director_id =" + director.getDirector_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRow(Director director){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE director SET director_id = ?, name = ?  WHERE director_id="+ director.getDirector_id());
            statement.setInt(1,director.getDirector_id());
            statement.setString(2,director.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateMovieDirectors(int movie_id , ArrayList<Director> directors){
        /* Deleting the previous directors list */
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM direct WHERE movie_id =" + movie_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /* Inserting new director list */
        for(Director director : directors){
            try {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO direct (movie_id, director_id) VALUES(?,?) ");
                statement.setInt(1, movie_id);
                statement.setInt(2, director.getDirector_id());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public Director selectRow(int director_id){
        Director director = new Director();
        String sql = "SELECT * FROM director WHERE director_id = " + director_id;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                director = new Director();
                director.setDirector_id(result.getInt("director_id"));
                director.setName(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return director;
    }

    public Director selectByName(String name){
        Director director = new Director();
        String sql = "SELECT * FROM director WHERE UPPER(name) = UPPER('" + name+"')";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                director = new Director();
                director.setDirector_id(result.getInt("director_id"));
                director.setName(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return director;
    }

    public ArrayList<Director> getAllRows(){
        ArrayList<Director> directors = new ArrayList<>();
        String sql = "SELECT * FROM director";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Director director = new Director();
                director.setDirector_id(result.getInt("director_id"));
                director.setName(result.getString("name"));
                Person person = new Person();

                directors.add(director);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return directors;
    }

    public ArrayList<Director> getByMovie(int movie_id){
        ArrayList<Director> directors = new ArrayList<>();
        String sql = "SELECT DISTINCT director.director_id, name FROM director, direct  WHERE direct.movie_id =" + movie_id + " AND director.director_id = direct.director_id" ;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Director director = new Director();
                director.setDirector_id(result.getInt("director_id"));
                director.setName(result.getString("name"));

                directors.add(director);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return directors;
    }

    /**
     * Return true if the director is already in the database
     * @param director
     * @return
     */
    public boolean exist(String director){
        String sql = "SELECT * FROM director WHERE UPPER(name) = UPPER('" + director + "')";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
