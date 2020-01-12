package controller;

import connection.Connect;
import dao.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

public class MovieManagementController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField dateField;
    @FXML
    private Text rateTxt;
    @FXML
    private TextField priceField;
    @FXML
    private ImageView imageMovie;
    @FXML
    private VBox actorBox;
    @FXML
    private TextField addActorField;
    @FXML
    private VBox categoryBox;
    @FXML
    private TextField addCategoryField;
    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private VBox directorBox;
    @FXML
    private TextField addDirectorField;
    @FXML
    private Text errorTxt;
    @FXML
    private Text successTxt;
    @FXML
    private Button saveBtn;
    @FXML
    private Button deleteBtn;


    private final String IMAGEPATH = "images\\movie\\";
    private final String MOVIEPATH = "movies\\";
    private MainApp main;
    private Movie movie;
    private MovieDao movieDao;
    private PersonDAO personDAO;
    private CategoryDAO categoryDAO;
    private RateDAO rateDAO;
    private ArrayList<Person> actors;
    private ArrayList<Category> categories;
    private ArrayList<Person> directors;
    private ArrayList<Person> people;
    private boolean addingMovie = false;


    public MovieManagementController() {
        movieDao = new MovieDao(Connect.getInstance());
        categoryDAO = new CategoryDAO(Connect.getInstance());
        rateDAO = new RateDAO(Connect.getInstance());
        personDAO = new PersonDAO(Connect.getInstance());
    }

    public void setMain(MainApp mainApp) {
        this.main = mainApp;
    }

    /**
     * Set the movie that will be modify
     * @param movie_id
     */
    public void setMovieToDisplay(int movie_id) {
        if(movie_id != 0 ){
            movie = movieDao.selectRow(movie_id);
            actors = personDAO.getActorsByMovie(movie.getMovie_id());
            categories = categoryDAO.getByMovie(movie.getMovie_id());
            directors = personDAO.getDirectorsByMovir(movie.getMovie_id());
            saveBtn.setText("Save");
            addingMovie = false;
            deleteBtn.setVisible(true);
        }else{ /* If movie_id == 0, the user wants to add a new movie */
            movie = new Movie();
            actors = new ArrayList<>();
            directors = new ArrayList<>();
            categories = new ArrayList<>();
            addingMovie = true;
            saveBtn.setText("Add Movie");
            deleteBtn.setVisible(false);
        }

        initialize();
    }

    /**
     * Copy and paste the movie image in the appropriate folder
     * Update the database with the new path
     * @param path
     */
    public void setImage(String path){

        Path source = Paths.get(path);

        String []  pathParts = path.split("\\\\");

        Path target = Paths.get(IMAGEPATH + pathParts[pathParts.length-1]);
        movie.setImage(IMAGEPATH + pathParts[pathParts.length-1]);
        try {
            Files.copy(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream input = new FileInputStream(target.toString());
            Image image = new Image(input);
            imageMovie.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy and paste the movie in the appropriate folder
     * Update the database with the new path
     * @param path
     */
    public void setMovieFile(String path){

        Path source = Paths.get(path);

        String []  pathParts = path.split("\\\\");
        Path target = Paths.get(MOVIEPATH + pathParts[pathParts.length-1]);
        movie.setMoviePath(MOVIEPATH + pathParts[pathParts.length-1]);
        try {
            Files.copy(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        successTxt.setVisible(false);
        errorTxt.setVisible(false);
        if(movie != null){
            categoryCombo.getItems().clear();
            categoryCombo.getItems().addAll(categoryDAO.getCategoryNames());
            categoryCombo.setValue("Adventure");
            titleField.setText(movie.getTitle());
            descriptionArea.setText(movie.getDescription());
            dateField.setText(movie.getRelease_date());

            rateTxt.setText(String.valueOf(rateDAO.getMovieRate(movie.getMovie_id())));
            priceField.setText(String.valueOf(movie.getPrice()));
            /* Init image */
            try {
                FileInputStream input = new FileInputStream(movie.getImage());
                Image image = new Image(input);
                imageMovie.setImage(image);

            } catch (FileNotFoundException | NullPointerException  e) {
                Image image;
                try (FileInputStream input = new FileInputStream(IMAGEPATH + "default.png")) {
                    image = new Image(input);
                    imageMovie.setImage(image);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            /* Init actor list */
            createActorList();
            /* Init category list */
            createCategoryList();

            /* Init director list */
            createDirectorList();

        }
    }

    private void createActorList() {
        actorBox.getChildren().clear();
        for(Person actor: actors){
            HBox hbox = new HBox();
            hbox.getStyleClass().add("hbox");
            hbox.setSpacing(5);
            Button deleteButton = new Button("x");

            deleteButton.getStyleClass().add("bouton");
            Text actorName = new Text();
            actorName.setFill(Color.WHITE);
            actorName.setText(actor.getSurname() + " " + actor.getName());
            deleteButton.addEventHandler(ActionEvent.ACTION, event -> deleteActorBtnHandler(actor));

            hbox.getChildren().addAll(actorName, deleteButton);
            actorBox.getChildren().add(hbox);
        }
    }

    public void deleteActorBtnHandler(Person actor){
        actors.remove(actor);
        createActorList();
    }
    private void createDirectorList() {
        directorBox.getChildren().clear();
        for(Person director: directors){
            HBox hbox = new HBox();
            hbox.getStyleClass().add("hbox");
            hbox.setSpacing(5);
            Button deleteButton = new Button("x");

            deleteButton.getStyleClass().add("bouton");
            Text directorName = new Text();
            directorName.setFill(Color.WHITE);
            directorName.setText(director.getName());
            deleteButton.addEventHandler(ActionEvent.ACTION, event -> deleteDirectorBtnHandler(director));

            hbox.getChildren().addAll(directorName, deleteButton);
            directorBox.getChildren().add(hbox);
        }
    }

    private void deleteDirectorBtnHandler(Person director) {
        directors.remove(director);
        createDirectorList();
    }

    private void createCategoryList() {
        categoryBox.getChildren().clear();
        for(Category category: categories){
            HBox hbox = new HBox();
            hbox.getStyleClass().add("hbox");
            hbox.setSpacing(5);
            Button deleteButton = new Button("x");
            deleteButton.getStyleClass().add("bouton");
            Text categoryName = new Text();
            categoryName.setFill(Color.WHITE);
            categoryName.setText(category.getName());
            deleteButton.addEventHandler(ActionEvent.ACTION, event -> deleteCategoryBtnHandler(category));

            hbox.getChildren().addAll(categoryName, deleteButton);
            categoryBox.getChildren().add(hbox);
        }
    }

    private void deleteCategoryBtnHandler(Category category) {
        categories.remove(category);
        createCategoryList();
    }


    @FXML
    public void browseImageBtn(){
        main.showMovieImageBrowser();
    }

    @FXML
    public void browseMovieBtn(){
        main.showMovieBrowser();
    }
    @FXML
    public void backBtn() {
        main.showMovie(movie.getMovie_id());
    }

    @FXML
    public void deleteBtnHandler(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("You are about to delete this movie : " + movie.getTitle());
        alert.setContentText("This action cannot be undone");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            movieDao.deleteRow(movie);
            main.showMovieList();
        } else {
            alert.close();
        }
    }

    @FXML
    public void suggestActor(){
        ArrayList<String> fullnames = new ArrayList<>();
        people = personDAO.researchPerson(addActorField.getText());
        for(Person person : people){
            fullnames.add(person.getSurname() + " " + person.getName());
        }
        TextFields.bindAutoCompletion(addActorField, fullnames);
    }

    @FXML
    public void suggestDirector(){
        ArrayList<String> fullnames = new ArrayList<>();
        people = personDAO.researchPerson(addDirectorField.getText());
        for(Person person : people){
            fullnames.add(person.getSurname() + " " + person.getName());
        }
        TextFields.bindAutoCompletion(addDirectorField, fullnames);
    }

    @FXML
    public void addActor(){
        if(!addActorField.getText().isEmpty()){
            Person actor = new Person();
            actor.setPersonId(0);
            String parts[] = addActorField.getText().split("\\s", 2);
            String surname = parts[0].trim();
            String name = parts[1].trim();
            actor.setSurname(surname);
            actor.setName(name);
            actor.setPersonId(personDAO.selectByName(actor.getSurname(), actor.getName()).getPersonId());
            actors.add(actor);
            createActorList();
            addActorField.setText(null);
        }
    }
    @FXML
    public void addDirector(){
        if(!addDirectorField.getText().isEmpty()){
            Person director = new Person();
            director.setPersonId(0);
            String parts[] = addDirectorField.getText().split("\\s", 2);
            String surname = parts[0].trim();
            String name = parts[1].trim();
            director.setSurname(surname);
            director.setName(name);
            director.setPersonId(personDAO.selectByName(director.getSurname(), director.getName()).getPersonId());
            directors.add(director);
            createDirectorList();
            addDirectorField.setText(null);
        }
    }

    @FXML
    public void addCategory(){
            Category category = new Category();
            category.setCategory_id(0);
            category.setName(categoryCombo.getSelectionModel().getSelectedItem().trim());
            if(!categoryDAO.exist(category.getName())){
                categoryDAO.insertRow(category);
            }
            category.setCategory_id(categoryDAO.selectByName(category.getName()).getCategory_id());
            categories.add(category);
            createCategoryList();

    }

    /**
     * Save the modifications in the database
     */
    @FXML
    public void saveBtnHandler(){
        if(addingMovie){
            addMovie();
        }else{
            updateMovie();
        }

    }
    private void addMovie(){
        if(updateElem(titleField.getText(), "title") &&
                updateElem(descriptionArea.getText(), "description") &&
                updateElem(priceField.getText(), "price")&&
                updateDate()){
            movieDao.insertRow(movie);
            movie = movieDao.selectRowByTitleAndDesc(movie);
            updateActors();
            updateCategories();
            updateDirectors();
            successTxt.setText("Movie Successfully added");
            successTxt.setVisible(true);
            main.showMovie(movie.getMovie_id());
        }
    }
    private void updateMovie(){
        if(updateElem(titleField.getText(), "title") &&
                updateElem(descriptionArea.getText(), "description") &&
                updateElem(priceField.getText(), "price")&&
                updateDate()){
            updateActors();
            updateCategories();
            updateDirectors();
            movieDao.updateRow(movie);
            successTxt.setText("Movie Successfully updated");
            successTxt.setVisible(true);

        }
    }

    private void updateActors() {
        movie.setActors(actors);
        personDAO.updateMovieActors(movie.getMovie_id(), actors);
    }

    private void updateCategories(){
        movie.setCategories(categories);
        categoryDAO.updateMovieCategories(movie.getMovie_id(), categories);
    }

    private  void updateDirectors(){
        movie.setDirectors(directors);
        personDAO.updateMovieDirectors(movie.getMovie_id(), directors);
    }

    /**
     * Update the movie considering the new element, its name (title, description or price)
     * @param text
     * @param elem
     */
    private boolean updateElem(String text, String elem) {
        if(text!= null){
            if (text.isEmpty()) {
                errorTxt.setVisible(true);
                successTxt.setVisible(false);
                errorTxt.setText("ERROR: "+elem+" missing!");
                return  false;
            } else {
                errorTxt.setVisible(false);
                switch (elem){
                    case "title":
                        movie.setTitle(text);
                        break;
                    case "description":
                        movie.setDescription(text);
                        break;
                    case "price":
                        try{
                            if(Float.valueOf(text) != 0){
                                movie.setPrice(Float.valueOf(text));
                            }else{
                                successTxt.setVisible(false);
                                errorTxt.setText("ERROR: please fille a price!");
                                errorTxt.setVisible(true);
                            }

                        }catch (NumberFormatException e){
                            successTxt.setVisible(false);
                            errorTxt.setText("ERROR: unvalid price!");
                            errorTxt.setVisible(true);
                            return false;
                        }
                        break;

                }

                return true;
            }
        }else{
            errorTxt.setVisible(true);
            successTxt.setVisible(false);
            errorTxt.setText("ERROR: "+elem+" missing!");
            return false;
        }
    }

    private boolean updateDate() {
        try{
                if(dateField.getText().isEmpty()){
                    errorTxt.setVisible(true);
                    successTxt.setVisible(false);
                    errorTxt.setText("ERROR: date of release missing!");
                    return false;
                }else if(Integer.valueOf(dateField.getText()) < 1888 || Integer.valueOf(dateField.getText()) > Calendar.getInstance().get(Calendar.YEAR)){
                    errorTxt.setVisible(true);
                    successTxt.setVisible(false);
                    errorTxt.setText("ERROR: unvalid date of release!");
                    return false;
                }else{
                    errorTxt.setVisible(false);
                    movie.setRelease_date(dateField.getText());
                    return true;
                }


        }catch(NumberFormatException | NullPointerException e){
            errorTxt.setVisible(true);
            successTxt.setVisible(false);
            errorTxt.setText("ERROR: unvalid date of release!");
            return false;
        }
    }


}
