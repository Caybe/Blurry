package model;


import java.util.ArrayList;

public class Movie {
    private int movie_id;
    private String title;
    private String description;
    private String release_date;
    private String image;
    private float rate;
    private ArrayList<Director> directors;
    private ArrayList<Category> categories;
    private ArrayList<Actor> actors;
    private float price;


    //
    // Empty Constructor
    //

    public Movie() {

    }

    //
    // Constructor used to get the data from the database
    //
    public Movie(int movie_id, String title, String description, String release_date, String image, float rate, float price) {
        this.movie_id = movie_id;
        this.title = title;
        this.description = description;
        this.release_date = release_date;
        this.image = image;
        this.rate = rate;
        this.price = price;
    }

    //
    // Constructor used to insert data in the database (the id_movie will be created by the database)
    //
    public Movie(String title, String description, String release_date, String image, float rate,float price) {
        this.title = title;
        this.description = description;
        this.release_date = release_date;
        this.image = image;
        this.rate = rate;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) { this.image = image; }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<Director> directors) {
        this.directors = directors;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object obj) {
        Movie temp = (Movie) obj;
        return this.getMovie_id() == temp.getMovie_id();
    }
}
