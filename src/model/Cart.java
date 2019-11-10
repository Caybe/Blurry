package model;

public class Cart {
    private int client_id;
    private int movie_id;

    public Cart() {
    }

    public Cart(int client_id, int movie_id) {
        this.client_id = client_id;
        this.movie_id = movie_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
}
