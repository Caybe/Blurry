package controller;

import connection.Connect;
import dao.ClientDAO;
import jBCrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.Client;
import model.PasswordHash;

import java.sql.Date;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Optional;

public class ProfileController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker birthdayDate;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswdField;
    @FXML
    private Text errorTxt;
    @FXML
    private Text successTxt;
    @FXML
    private Button addMovieBtn;
    @FXML
    private Button deleteAccBtn;
    @FXML
    private Button statBtn;

    private MainApp main;
    private Client client;
    private Date birthday;
    private String password;
    private boolean correct;
    private boolean changePasswd = false;
    private ClientDAO clientDAO;

    public ProfileController() {
        clientDAO = new ClientDAO(Connect.getInstance());
    }

    public void setMain(MainApp main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        if(client != null){
            surnameField.setText(client.getSurname());
            nameField.setText(client.getName());
            emailField.setText(client.getEmail());
            birthdayDate.setValue(client.getBirthday().toLocalDate());

            if(client.getClient_id() == 1){ /* If the client is the administrator */
                surnameField.setDisable(true);
                nameField.setDisable(true);
                birthdayDate.setDisable(true);
                addMovieBtn.setVisible(true);
                deleteAccBtn.setDisable(true);
                statBtn.setVisible(true);
            }else{
                surnameField.setDisable(false);
                nameField.setDisable(false);
                birthdayDate.setDisable(false);
                addMovieBtn.setVisible(false);
                deleteAccBtn.setDisable(false);
                statBtn.setVisible(false);
            }
        }


    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    public void modifyProfile() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Profile Modification");
        dialog.setHeaderText("Please confirm your identity");
        dialog.setContentText("Password:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(passwd -> {
            if (BCrypt.checkpw(passwd, client.getPassword())) {
                initVariable(nameField);
                initVariable(surnameField);
                initVariable(emailField);
                initBirthday();
                initPassword();

                if (initVariable(nameField) &&
                        initVariable(surnameField) &&
                        initVariable(emailField) &&
                        initBirthday() &&
                        initPassword()) {
                    correct = true;
                } else {
                    correct = false;
                }
                System.out.println("Correct : " + correct);
                if (correct) {

                    client.setSurname(surnameField.getText());
                    client.setName(nameField.getText());
                    client.setEmail(emailField.getText());
                    client.setBirthday(birthday);
                    client.setRegistration_date(Date.valueOf(LocalDate.now()));
                    if(changePasswd){
                        client.setPassword(PasswordHash.hashPassword(password));
                    }


                    errorTxt.setVisible(false);
                    successTxt.setText("Profile successfully updated!");
                    successTxt.setVisible(true);
                    clientDAO.updateRow(client);
                    main.initSession(client);

                }
            }else{
                successTxt.setVisible(false);
                errorTxt.setText("Incorrect Password!");
                errorTxt.setVisible(true);
            }
        });


    }

    private boolean initVariable(TextField txt) {
        if (txt.getText().isEmpty()) {
            successTxt.setVisible(false);
            errorTxt.setText(txt.getPromptText() + " required");
            errorTxt.setVisible(true);
            return false;
        }
        if (txt.getPromptText().equals("Email")) {
            if (!txt.getText().matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}" +
                    "[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                successTxt.setVisible(false);
                errorTxt.setText("Email unvalid!");
                errorTxt.setVisible(true);
                return false;
            } else {
                return true;
            }

        }
        return true;
    }

    private boolean initBirthday() {
        ChronoLocalDate localDate = LocalDate.now();
        if (birthdayDate.getValue() == null) {
            errorTxt.setText("Birthday required!");
            errorTxt.setVisible(true);
            return false;
        } else if (birthdayDate.getValue().isAfter(localDate)) {
            successTxt.setVisible(false);
            errorTxt.setText("Birthday incorrect!");
            errorTxt.setVisible(true);
            return false;
        } else {
            LocalDate date = birthdayDate.getValue();
            birthday = Date.valueOf(date);
            return true;
        }

    }

    private boolean initPassword() {

        if (passwordField.getText().isEmpty()) {
            changePasswd = false;
        } else if (!passwordField.getText().equals(rePasswdField.getText())) {
            successTxt.setVisible(false);
            errorTxt.setText("Passwords doesn't match!");
            errorTxt.setVisible(true);
            return false;
        } else {
            password = passwordField.getText();
            changePasswd = true;
            return true;
        }
        return true;
    }

    @FXML
    public void addMovieBtn(){
        main.showMovieManagement(0);
    }

    /**
     * Handle a click on the "Delete Account Button"
     * Delete the user account forever!
     */
    @FXML
    public void deleteAccount(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("You are about to delete your account");
        alert.setContentText("This action cannot be undone. All your movies will be lost!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            clientDAO.deleteRow(client);
            main.initSession(null);
            main.showMovieList();
        }
    }

    /**
     * Handle a click on the "Your purchases" button
     */
    @FXML
    public void purchaseBtnHandler(){
        main.showPurchases(client);
    }

    @FXML
    public void statBtnHandler(){
        main.showStatisticPane();
    }
}
