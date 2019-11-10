package controller;

import connection.Connect;
import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Client;
import model.PasswordHash;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class RegistrationController {

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

    private Date birthday;
    private String password;
    private boolean correct = true;

    private MainApp main;

    public RegistrationController() {
    }

    public void setMain(MainApp main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void createAccount() {
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
            Client client = new Client();
            client.setSurname(surnameField.getText());
            client.setName(nameField.getText());
            client.setEmail(emailField.getText());
            client.setBirthday(birthday);
            client.setRegistration_date(Date.valueOf(LocalDate.now()));
            client.setPassword(PasswordHash.hashPassword(password));
            ClientDAO clientDAO = new ClientDAO(Connect.getInstance());
            try {
                clientDAO.insertRow(client);
                main.showConnection();
            } catch (SQLException e) {
                errorTxt.setText("Email Already Registered!");
                errorTxt.setVisible(true);
            }

        }
    }

    private boolean initVariable(TextField txt) {
        if (txt.getText().isEmpty()) {
            errorTxt.setText(txt.getPromptText() + " required");
            errorTxt.setVisible(true);
            return false;
        }
        if (txt.getPromptText().equals("Email")) {
            if (!txt.getText().matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}" +
                    "[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
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
            errorTxt.setText("Password required!");
            errorTxt.setVisible(true);
            return false;
        } else if (rePasswdField.getText().isEmpty()) {
            errorTxt.setText("Please repeat your password");
            errorTxt.setVisible(true);
            return false;
        } else if (!passwordField.getText().equals(rePasswdField.getText())) {
            errorTxt.setText("Passwords doesn't match!");
            errorTxt.setVisible(true);
            return false;
        } else {
            password = passwordField.getText();
            return true;
        }
    }
}

