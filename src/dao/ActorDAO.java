package dao;

import model.Actor;
import model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActorDAO {
    protected Connection conn;


    public ActorDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(String actor) {

        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO actor (name) VALUES(?) ");
            statement.setString(1,actor);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Actor actor){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM actor WHERE actor_id =" + actor.getActor_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRow(Actor actor){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE actor SET actor_id = ?, name = ?  WHERE actor_id=" + actor.getActor_id());
            statement.setInt(1,actor.getActor_id());
            statement.setString(2,actor.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Actor selectRow(int actor_id){
        Actor actor = new Actor();
        String sql = "SELECT * FROM actor WHERE actor_id = " + actor_id;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                actor = new Actor();
                actor.setActor_id(result.getInt("actor_id"));
                actor.setName(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actor;
    }

    public Actor selectByName(String name){
        Actor actor = new Actor();
        String sql = "SELECT * FROM actor WHERE UPPER(name) = UPPER('" + name+ "')";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                actor = new Actor();
                actor.setActor_id(result.getInt("actor_id"));
                actor.setName(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actor;
    }

    public ArrayList<Actor> getAllRows(){
        ArrayList<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM actor";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            while (result.next()){
                Actor actor = new Actor();
                actor.setActor_id(result.getInt("actor_id"));
                actor.setName(result.getString("name"));

                actors.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }


    public ArrayList<Actor> getByMovie(int movie_id){
        ArrayList<Actor> actors = new ArrayList<>();
        String sql = "SELECT DISTINCT actor.actor_id, name FROM actor, play  WHERE play.movie_id =" + movie_id + " AND actor.actor_id = play.actor_id" ;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Actor actor = new Actor();
                actor.setActor_id(result.getInt("actor_id"));
                actor.setName(result.getString("name"));

                actors.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }

    public void updateMovieActors(int movie_id ,ArrayList<Actor> actors){
        /* Deleting the previous actors list */
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM play WHERE movie_id =" + movie_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /* Inserting new actor list */
        for(Actor actor : actors){
            try {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO play (movie_id, actor_id) VALUES(?,?) ");
                statement.setInt(1, movie_id);
                statement.setInt(2, actor.getActor_id());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Return true if the actor is already in the database
     * @param actor
     * @return
     */
    public boolean exist(String actor){
        String sql = "SELECT * FROM actor WHERE UPPER(name) = UPPER('" + actor + "')";

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
