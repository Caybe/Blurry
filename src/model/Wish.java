package model;

public class Wish {
    private int movieId;
    private int clientId;

    public Wish(int movieId, int clientId) {
        this.movieId = movieId;
        this.clientId = clientId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
