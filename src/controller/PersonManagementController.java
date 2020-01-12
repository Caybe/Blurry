package controller;

import connection.Connect;
import dao.PersonDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Movie;
import model.Person;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;


public class PersonManagementController {

    private final String IMAGEPATH = "images\\person\\";

    @FXML
    private TextField surnameField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker birthdayDate;
    @FXML
    private TextArea bioArea;
    @FXML
    private TextField birthPlaceField;
    @FXML
    private DatePicker deathDate;
    @FXML
    private Button saveBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private ImageView imagePerson;

    private MainApp main;
    private PersonDAO personDAO;
    private Person person;
    private Boolean addingPerson;

    public PersonManagementController() {
        personDAO = new PersonDAO(Connect.getInstance());
    }

    public void setMain(MainApp main) {
        this.main = main;
    }
    public void setPersonToDisplay(int personId) {
        if(personId != 0 ){
            person = personDAO.selectById(personId);
            saveBtn.setText("Save");
            addingPerson = false;
            deleteBtn.setVisible(true);
        }else{ /* If personId == 0, the user wants to add a new person */
            person = new Person();
            person.setPersonId(0);
            addingPerson = true;
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
        person.setImage(IMAGEPATH + pathParts[pathParts.length-1]);
        try {
            Files.copy(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream input = new FileInputStream(target.toString());
            Image image = new Image(input);
            imagePerson.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        if(main != null && person != null && person.getPersonId() > 0){
            surnameField.setText(person.getSurname());
            nameField.setText(person.getName());
            birthdayDate.setValue(person.getBirthday().toLocalDate());
            bioArea.setText(person.getBiography());
            birthPlaceField.setText(person.getBirthPlace());
            deathDate.setValue(person.getDeathDate().toLocalDate());

            /* Init image */
            try {
                FileInputStream input = new FileInputStream(person.getImage());
                Image image = new Image(input);
                imagePerson.setImage(image);

            } catch (FileNotFoundException | NullPointerException  e) {
                Image image;
                try (FileInputStream input = new FileInputStream(IMAGEPATH + "default.jpg")) {
                    image = new Image(input);
                    imagePerson.setImage(image);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

    }


    @FXML
    private void browseImage(){
        main.showPersonImageBrowser();
    }

    @FXML
    private void backBtn(){
        main.showPerson(person.getPersonId());
    }
    @FXML
    private void savePerson(){
        person.setSurname(surnameField.getText());
        person.setName(nameField.getText());
        person.setBiography(bioArea.getText());
        LocalDate date = birthdayDate.getValue();
        Date birthday = Date.valueOf(date);
        person.setBirthday(birthday);
        person.setBirthPlace(birthPlaceField.getText());
        if(deathDate.getValue() != null){
            date = deathDate.getValue();
            Date deathDate = Date.valueOf(date);
            person.setDeathDate(deathDate);
        }

        if(addingPerson){
            personDAO.insertRow(person);
        }else{
            personDAO.updateRow(person);
        }
    }
}
