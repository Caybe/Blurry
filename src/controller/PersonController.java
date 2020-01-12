package controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PersonController {

    @FXML
    private Text surnameTxt;
    @FXML
    private Text nameTxt;
    @FXML
    private Text bioTxt;
    @FXML
    private Text birthdayTxt;
    @FXML
    private Text birthPlaceTxt;
    @FXML
    private Text deathTxt;

    private MainApp main;


    public PersonController() {
    }

    public void setMain(MainApp main) {
        this.main = main;
    }
    

    @FXML
    public void initialize(){

    }

    @FXML
    private void backBtn() {
        main.showMovieList();
    }
}
