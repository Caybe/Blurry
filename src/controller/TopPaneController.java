package controller;

import connection.Connect;
import dao.CategoryDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Category;
import model.Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TopPaneController {

    @FXML
    private Text currentClient;
    @FXML
    private Button profileBtn;
    @FXML
    private Button signInBtn;
    @FXML
    private Button cartBtn;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField researchField;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private ImageView logoImg;

    private MainApp main;
    private Client client = null;
    private CategoryDAO categoryDAO;
    private String selectedCategory;

    public TopPaneController() {
        categoryDAO = new CategoryDAO(Connect.getInstance());
    }

    public void setMain(MainApp main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        FileInputStream input = null;
        try {
            input = new FileInputStream("images/logos/logov2.png");
            Image image = new Image(input);
            logoImg.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        researchField.setDisable(false);
        categoryComboBox.setDisable(false);
        newSession();

        /* Populating the category ComboBox*/
        if (!categoryComboBox.getItems().isEmpty()) {
            categoryComboBox.getItems().clear();
        }
        categoryComboBox.getItems().add("Category");
        categoryComboBox.getItems().addAll(categoryDAO.getCategoryNames());
        categoryComboBox.setValue("Category");

        /* Popuating the filter ComboBox */
        if (filterComboBox.getItems().isEmpty()) {
            filterComboBox.getItems().add("Filter");
            filterComboBox.getItems().add("Top Rated");
            filterComboBox.getItems().add("Most Purchased");
        }
        filterComboBox.setValue("Filter");
    }

    public void newSession(){
        if (main != null && main.getCurrentClient() != null) {
            Client client = main.getCurrentClient();
            currentClient.setText(client.getName() + " " + client.getSurname());
            currentClient.setVisible(true);
            profileBtn.setVisible(true);
            cartBtn.setVisible(true);
            signInBtn.setText("Logout");

        } else {
            currentClient.setVisible(false);
            profileBtn.setVisible(false);
            cartBtn.setVisible(false);
            signInBtn.setText("Sign In");
        }
    }

    /**
     * Handle a click on the "sign in" btn
     */
    @FXML
    public void signInBtn() {
        if (main.getCurrentClient() != null) { // if a client is connected we log him out
            main.initSession(null);
            main.showMovieList();
        } else {
            main.showConnection();
        }

    }

    /**
     * Request the main app to show the movie list if you click ont the title
     */
    @FXML
    public void titleClick() {
        researchField.clear();
        categoryComboBox.setValue("Category");
        filterComboBox.setValue("Filter");
        main.setResearch(null);
        researchField.setDisable(false);
        categoryComboBox.setDisable(false);
        main.setCategory_id(0);
        main.showMovieList();
    }

    /**
     * Request the main app to show the client's profile
     */
    @FXML
    public void accessProfileRequest() {
        main.showProfile();
    }


    /**
     * Request the main app to show the client's cart
     */
    @FXML
    public void accessCart() {
        main.showCart(main.getCurrentClient());
    }


    /**
     * Update the category selected in the comboBox in the mainApp
     */
    @FXML
    public void research() {
        selectedCategory = categoryComboBox.getValue();
        int category_id = categoryDAO.selectByName(selectedCategory).getCategory_id();
        main.setCategory_id(category_id);
        if (!researchField.getText().isEmpty()) {
            main.setResearch(researchField.getText());
        } else {
            main.setResearch(null);
        }
        main.showMovieList();
    }

    /**
     * Update the filter selected in the comboBox in the mainApp
     */
    @FXML
    public void filter() {
        String selectedFilter = "Filter";
        try {
            selectedFilter = filterComboBox.getValue();
        } catch (NullPointerException e) {

        }


        main.setFilter(selectedFilter);
        main.showMovieList();

        if (!selectedFilter.equals("Filter")) {
            researchField.setDisable(true);
            categoryComboBox.setDisable(true);
        } else {
            researchField.setDisable(false);
            categoryComboBox.setDisable(false);
        }
    }

}
