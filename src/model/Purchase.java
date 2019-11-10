package model;

import java.sql.Date;

public class Purchase {
    private int purchaseId;
    private int client_id;
    private int movie_id;
    private float price;
    private Date purshaseDate;

    public Purchase() {
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

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public Date getPurshaseDate() {
        return purshaseDate;
    }

    public void setPurshaseDate(Date purshaseDate) {
        this.purshaseDate = purshaseDate;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }
}
