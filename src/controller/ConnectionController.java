package controller;

import connection.Connect;
import dao.ClientDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Client;
import model.PasswordHash;


public class ConnectionController {

    @FXML
    private TextField loginField;
    @FXML
    private TextField passwdField;
    @FXML
    private Text errorTxt;

    private MainApp main;
    private String login;
    private String passwd;
    private ClientDAO clientDAO;

    public ConnectionController() {
        clientDAO = new ClientDAO(Connect.getInstance());
    }

    public void setMain(MainApp main) {
        this.main = main;
    }


    @FXML
    public void connectRequest(){
        login = loginField.getText();
        passwd = passwdField.getText();
        if(login.isEmpty()|| passwd.isEmpty()){
            errorTxt.setText("Login or password missing!");
            errorTxt.setVisible(true);
        }else{
            Client client = clientDAO.getByEmail(login);
            if(client.getEmail() == null){
                errorTxt.setText("ERROR: wrong password or login");
                errorTxt.setVisible(true);
            }else{
                if(PasswordHash.checkPass(passwd, client.getPassword())){
                    main.showMovieList();
                    main.initSession(client);

                }else{
                    errorTxt.setText("ERROR: wrong password or login");
                    errorTxt.setVisible(true);
                }
            }
        }
    }

    @FXML
    public void registrationRequest(){
        main.showRegistration();
    }
}
