package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryDAO {

    protected Connection conn;


    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertRow(Category category) {

        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO category (name) VALUES(?) ");
            statement.setString(1, category.getName());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(Category category){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM category WHERE category_id =" + category.getCategory_id());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRow(Category category){
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE category SET category_id = ?, name = ? WHERE category_id = " + category.getCategory_id());
            statement.setInt(1, category.getCategory_id());
            statement.setString(2, category.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Category selectRow(int category_id){
        Category category = new Category();
        String sql = "SELECT * FROM category WHERE category_id = " + category_id;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                category = new Category();
                category.setCategory_id(result.getInt("category_id"));
                category.setName(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public Category selectByName(String name){
        Category category = new Category();
        String sql = "SELECT * FROM category WHERE UPPER(name) = UPPER('" + name+"')";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            if(result.first()){
                category = new Category();
                category.setCategory_id(result.getInt("category_id"));
                category.setName(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    public  ArrayList<String> getCategoryNames(){
        ArrayList<String> categories = new ArrayList<>();

        String sql = "SELECT name FROM category ORDER BY name";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            while (result.next()){
                categories.add(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;

    }
    public ArrayList<Category> getAllRows(){
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category";

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);


            while (result.next()){
                Category category = new Category();
                category.setCategory_id(result.getInt("category_id"));
                category.setName(result.getString("name"));

                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ArrayList<Category> getByMovie(int movie_id){
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT cat.category_id, name, movie_id FROM category cat, movie_categories mc WHERE mc.movie_id ="+movie_id+ " and cat.category_id= mc.category_id" ;

        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                Category category = new Category();
                category.setCategory_id(result.getInt("category_id"));
                category.setName(result.getString("name"));

                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void updateMovieCategories(int movie_id ,ArrayList<Category> categories){
        /* Deleting the previous categories list */
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM movie_categories WHERE movie_id =" + movie_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /* Inserting new category list */
        for(Category category : categories){
            try {
                PreparedStatement statement = conn.prepareStatement("INSERT INTO movie_categories (movie_id, category_id) VALUES(?,?) ");
                statement.setInt(1, movie_id);
                statement.setInt(2, category.getCategory_id());
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Return true if the category is already in the database
     * @param category
     * @return
     */
    public boolean exist(String category){
        String sql = "SELECT * FROM category WHERE UPPER(name) = UPPER('" + category + "')";

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

    public ObservableList<PieChart.Data> getCategoryPurchased(){
        ObservableList<PieChart.Data> categoriesPurchased = FXCollections.observableArrayList();
        String sql = "SELECT category.name, count(category.category_id) FROM movie_categories" +
                " INNER JOIN purchase p on movie_categories.movie_id = p.movie_id" +
                " INNER JOIN  public.category ON category.category_id = movie_categories.category_id GROUP BY category.category_id";
        try{
            ResultSet result = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);

            while (result.next()){
                categoriesPurchased.add(new PieChart.Data(result.getString(1), result.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return categoriesPurchased;

    }

}
