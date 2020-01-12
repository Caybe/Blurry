package dao;

import model.Director;
import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO {
    private Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(Person person){
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO person (surname, name, birthday, biography, image, birth_place, death_date) VALUES(?,?,?,?,?,?,?)");
            prepStatement(person, statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Person person){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM person WHERE person_id =" + person.getPersonId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRow(Person person){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE person SET surname = ?, name = ?, birthday = ?, biography = ?, image = ?, birth_place = ?, death_date = ?  WHERE person = ?" );
            prepStatement(person, statement);
            statement.setInt(8, person.getPersonId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void prepStatement(Person person, PreparedStatement statement) throws SQLException {
        statement.setString(1,person.getSurname());
        statement.setString(2,person.getName());
        statement.setDate(3, person.getBirthday());
        statement.setString(4, person.getBiography());
        statement.setString(5, person.getImage());
        statement.setString(6, person.getBirthPlace());
        statement.setDate(7, person.getDeathDate());
    }

    public Person selectByName(String surname, String name){
        Person person = new Person();
        String sql = "SELECT * FROM person WHERE UPPER(name) = UPPER('" + name+ "') AND UPPER(surname)  = UPPER('"+ surname+"')";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                fillPerson(result, person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public Person selectById(int personId){
        Person person = new Person();
        String sql = "SELECT * FROM person WHERE person_id =" + personId;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                fillPerson(result, person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public ArrayList<Person> getAllRows(){
        ArrayList<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM person";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            while (result.next()){
                Person person = new Person();
                fillPerson(result, person);
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }
    public ArrayList<Person> getActorsByMovie(int movie_id){
        ArrayList<Person> people = new ArrayList<>();
        String sql = "SELECT DISTINCT p.person_id, p.surname, p.name, p.birthday, p.biography, p.image, p.birth_place, p.death_date FROM person p INNER JOIN play  ON p.person_id = play.actor_id WHERE play.movie_id =" + movie_id + "" ;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Person person = new Person();
                fillPerson(result, person);
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public ArrayList<Person> getDirectorsByMovir(int movie_id){
        ArrayList<Person> directors = new ArrayList<>();
        String sql = "SELECT DISTINCT person.person_id, name, surname FROM person INNER JOIN direct  ON person.person_id = direct.director_id WHERE direct.movie_id =" + movie_id ;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Person director = new Person();
                director.setPersonId(result.getInt("person_id"));
                director.setName(result.getString("name"));
                director.setSurname(result.getString("surname"));
                directors.add(director);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return directors;
    }

    public void updateMovieActors(int movie_id ,ArrayList<Person> people){
        /* Deleting the previous people list */
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM play WHERE movie_id =" + movie_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /* Inserting new actor list */
        for(Person person : people){
            try {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO play (movie_id, actor_id) VALUES(?,?) ");
                statement.setInt(1, movie_id);
                statement.setInt(2, person.getPersonId());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateMovieDirectors(int movie_id ,ArrayList<Person> people){
        /* Deleting the previous people list */
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM direct WHERE movie_id =" + movie_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /* Inserting new actor list */
        for(Person person : people){
            try {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO direct (movie_id, director_id) VALUES(?,?) ");
                statement.setInt(1, movie_id);
                statement.setInt(2, person.getPersonId());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Tell if a person already exist in the database
     * @param surname
     * @return true if the surname and name already exist in the database, false if not
     */
    public boolean exist(String surname, String name){
        String sql = "SELECT * FROM person WHERE UPPER(surname) = UPPER('" + surname + "') AND UPPER(name) = UPPER('" + name + "')";

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

    private void fillPerson(ResultSet result, Person person) throws SQLException {
        person.setPersonId(result.getInt("person_id"));
        person.setSurname(result.getString("surname"));
        person.setName(result.getString("name"));
        person.setBirthday(result.getDate("birthday"));
        person.setBiography(result.getString("biography"));
        person.setImage(result.getString("image"));
        person.setBirthPlace(result.getString("birth_place"));
        person.setDeathDate(result.getDate("death_date"));
    }

    public ArrayList<Person> researchPerson(String research){
        ArrayList<Person> people = new ArrayList<>();
        String parts[] = research.split("\\s", 2);
        String sql;

        try{
            PreparedStatement ps;
            if(parts.length <= 1){
                sql = "SELECT person_id, surname, name FROM person WHERE UPPER(surname) LIKE UPPER(?) OR UPPER(name) LIKE UPPER(?) ORDER BY name, surname LIMIT 5";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%"+research+"%");
                ps.setString(2, "%"+research+"%");
            }else{
                sql = "SELECT person_id, surname, name FROM person WHERE UPPER(surname) LIKE UPPER(?) OR UPPER(name) LIKE UPPER(?) OR UPPER(surname) LIKE UPPER(?) OR UPPER(name) LIKE UPPER(?) " +
                        "ORDER BY name, surname LIMIT 5";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%"+parts[0]+"%");
                ps.setString(2, "%"+parts[1]+"%");
                ps.setString(3, "%"+parts[1]+"%");
                ps.setString(4, "%"+parts[0]+"%");
            }

            ResultSet result = ps.executeQuery();


            while (result.next()){
                Person person = new Person();
                person.setPersonId(result.getInt("person_id"));
                person.setSurname(result.getString("surname"));
                person.setName(result.getString("name"));
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }


}
