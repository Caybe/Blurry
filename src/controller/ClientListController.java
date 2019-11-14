package controller;

import connection.Connect;
import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import model.Client;

import java.util.ArrayList;

public class ClientListController {
    @FXML
    private VBox clientBox;
    @FXML
    private ScrollPane scrollPane;

    private ClientDAO clientDAO;
    private ArrayList<Client> clients;

    public ClientListController() {
        clientDAO = new ClientDAO(Connect.getInstance());
        clients = new ArrayList<>();
    }

    @FXML
    public void initialize(){
        clients = clientDAO.getAllRows();

        /* Setting scrollSpane speed */
        ScrollPaneSkin paneSkin = new ScrollPaneSkin(scrollPane);
        paneSkin.getVerticalScrollBar().setBlockIncrement(0.2);
        paneSkin.getVerticalScrollBar().setUnitIncrement(0.2);
        paneSkin.getSkinnable().addEventFilter(ScrollEvent.SCROLL, event -> {

            if (event.getDeltaX() < 0) {
                paneSkin.getHorizontalScrollBar().increment();
            } else if (event.getDeltaX() > 0) {
                paneSkin.getHorizontalScrollBar().decrement();
            }

            if (event.getDeltaY() < 0) {
                paneSkin.getVerticalScrollBar().increment();
            } else if (event.getDeltaY() > 0) {
                paneSkin.getVerticalScrollBar().decrement();
            }

            event.consume();
        });

        /* Creating a pane for each client */
        for(Client client : clients){
            if(client.getClient_id() != -1){
                clientBox.setSpacing(20);
                HBox oneClientBox = new HBox();
                oneClientBox.getStyleClass().add("border");
                oneClientBox.setAlignment(Pos.CENTER_LEFT);
                oneClientBox.setPrefHeight(100);
                oneClientBox.setMinHeight(60);
                oneClientBox.setMaxWidth(800);
                oneClientBox.setSpacing(10);
                Font font = new Font("verdana", 15);
                Label name = new Label(client.getName());
                name.setTextFill(Color.WHITE);
                name.setFont(font);
                Label surname = new Label(client.getSurname());
                surname.setTextFill(Color.WHITE);
                surname.setFont(font);
                Label email = new Label("| Email : " + client.getEmail());
                email.setTextFill(Color.WHITE);
                email.setFont(font);
                Label registration = new Label( "| Registration Date: " + client.getRegistration_date());
                registration.setTextFill(Color.WHITE);
                registration.setFont(font);

                oneClientBox.getChildren().add(surname);
                oneClientBox.getChildren().add(name);
                oneClientBox.getChildren().add(email);
                oneClientBox.getChildren().add(registration);

                clientBox.getChildren().add(oneClientBox);
            }

        }
    }
}
