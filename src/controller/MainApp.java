package controller;

import dao.ClientDAO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Client;
import model.Movie;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane mainPane;
    private AnchorPane profilePane;
    private AnchorPane moviePane;
    private AnchorPane movieManagementPane;
    private AnchorPane purchasePane;
    private AnchorPane cartPane;
    private TopPaneController topPaneController;
    private ProfileController profileController;
    private MovieManagementController movieManagementController;
    private MovieListController movieListController;
    private MovieController movieController;
    private CartController cartController;
    private PurchaseController purchaseController;
    private int category_id;
    private String research;
    private String filter;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Blurry");

        initMainView();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                showTopPane();
                showProfile();
                showMovie(1);
                showCart(null);
                showPurchases(null);
                showMovieList();

            }
        });




    }


    /**
    *Init MainView
    */
    private void initMainView(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/MainView.fxml"));
            mainPane = (AnchorPane) loader.load();
            Scene scene = new Scene(mainPane);
            // Getting the BorderPane of the MainView
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);

            try {
                FileInputStream input = new FileInputStream("src/images/logos/logov2.png");
                Image image = new Image(input);
                ImageView imageView = new ImageView(image);
                mainBorderPane.setCenter(imageView);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Adding pane to the center of the borderPane


            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Shows the topPane
    */
    public void showTopPane(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/TopPane.fxml"));
            AnchorPane topPane = (AnchorPane) loader.load();
            // Getting the BorderPane of the MainView
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);

            //Adding pane to the center of the borderPane
            mainBorderPane.setTop(topPane);

            //Allowing Controller to access the view
            topPaneController = loader.getController();
            topPaneController.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Shows The Movie List
    */
    public void showMovieList(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/MovieListPane.fxml"));
            AnchorPane movieListPane = (AnchorPane) loader.load();
            // Getting the BorderPane of the MainView
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);

            //Adding the list to the center of the borderPane
            mainBorderPane.setCenter(movieListPane);

            //Allowing Controller to access the view
            movieListController = loader.getController();
            movieListController.setCategory(getCategory_id());
            movieListController.setResearch(getResearch());
            movieListController.setFilter(getFilter());
            movieListController.setMain(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setResearch(String research) {
        this.research = research;
    }


    public int getCategory_id() {
        return category_id;
    }

    public String getResearch() {
        return research;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    /**
    * Show a particular Movie
    */
    public void showMovie(int id_Movie){
        try{
            if(movieController == null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("../view/MoviePane.fxml"));
                moviePane = (AnchorPane) loader.load();
                //Allowing Controller to access the view
                movieController = loader.getController();
                movieController.setMain(this);
            }

            // Getting the BorderPane of the MainView
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);

            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(moviePane);
            movieController.setMovieToDisplay(id_Movie);
            movieController.initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Show the connection pane
    */
    public void showConnection(){

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/ConnectionPane.fxml"));
            AnchorPane connectionPane = (AnchorPane) loader.load();
            // Getting the BorderPane of the MainView
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);

            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(connectionPane);

            //Allowing Controller to access the view
            ConnectionController connectionController = loader.getController();
            connectionController.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
    * Show the registration pane
    */
    public void showRegistration(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/RegistrationPane.fxml"));
            AnchorPane registrationPane = (AnchorPane) loader.load();
            // Getting the BorderPane of the MainView
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);

            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(registrationPane);

            //Allowing Controller to access the view
            RegistrationController registrationController = loader.getController();
            registrationController.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Show profile pane
    */
    public void showProfile(){
        try{
            if(profileController == null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("../view/ProfilePane.fxml"));
                profilePane = (AnchorPane) loader.load();
                //Allowing Controller to access the view
                profileController = loader.getController();
                profileController.setMain(this);
            }
            // Getting the BorderPane of the MainViews
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);
            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(profilePane);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    * Show movie management pane
    */
    public void showMovieManagement(int movie_id){
        try{
            if(movieManagementController == null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("../view/MovieManagementPane.fxml"));
                movieManagementPane = (AnchorPane) loader.load();
                //Allowing Controller to access the view
                movieManagementController = loader.getController();
            }
            movieManagementController.setMovieToDisplay(movie_id);
            movieManagementController.setMain(this);
            // Getting the BorderPane of the MainViews
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);
            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(movieManagementPane);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    * Show the file browser window
    */
    public void showFileBrowser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.jpg" , "*.jpeg" , "*.png")
                ,new FileChooser.ExtensionFilter("JPG Files", "*.jpg", "*.jpeg")
                ,new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        if(movieManagementController != null){

            movieManagementController.setImage(fileChooser.showOpenDialog(primaryStage).getAbsolutePath()) ;
        }

    }


    /**
     * Show the client's cart
     * @param client
     */
    public void showCart(Client client) {
        try{
            if(cartController == null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("../view/CartPane.fxml"));
                cartPane = (AnchorPane) loader.load();
                //Allowing Controller to access the view
                cartController = loader.getController();
                cartController.setClient(client);
                cartController.setMain(this);
            }
            cartController.initialize();
            // Getting the BorderPane of the MainViews
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);
            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(cartPane);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Show Client's purchases
     *  @param client
     */
    public void showPurchases(Client client) {
        try {
            if (purchaseController == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("../view/PurchasePane.fxml"));
                purchasePane = (AnchorPane) loader.load();
                //Allowing Controller to access the view
                purchaseController = loader.getController();
                purchaseController.setClient(client);
                purchaseController.setMain(this);
            }
            purchaseController.initialize();
            // Getting the BorderPane of the MainViews
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);
            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(purchasePane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the statistic pane
     */
    public void showStatisticPane(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/StatisticPane.fxml"));
            ScrollPane statPane = (ScrollPane) loader.load();
            //Allowing Controller to access the view
            StatisticController statisticController = loader.getController();
            // Getting the BorderPane of the MainViews
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);
            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(statPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showClientList(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../view/ClientListPane.fxml"));
            AnchorPane clientPane = (AnchorPane) loader.load();
            //Allowing Controller to access the view
            ClientListController clientListController = loader.getController();


            // Getting the BorderPane of the MainViews
            BorderPane mainBorderPane = (BorderPane) mainPane.getChildren().get(0);
            //Adding pane to the center of the borderPane
            mainBorderPane.setCenter(clientPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Init the client in the app
     * @param client
     */
    public void initSession(Client client){
        topPaneController.setClient(client);
        topPaneController.initialize();
        profileController.setClient(client);
        profileController.initialize();
        movieListController.setClient(client);
        movieController.setClient(client);
        movieController.initialize();
        cartController.setClient(client);
        cartController.initialize();
        purchaseController.setClient(client);
        purchaseController.initialize();
    }

    public void removeFromCart(Movie movie, Client client){
        if(cartController != null){
            cartController.removeMovie(movie);
            showCart(client);
        }
    }




    public static void main(String[] args) {
        launch(args);
    }


}
