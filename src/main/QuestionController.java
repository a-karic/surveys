/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.ParticipantDAO;
import dao.QuestionDAO;
import entities.Participant;
import entities.Question;
import entities.Survey;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author almin
 */
public class QuestionController implements Initializable {

    private Main application;
    @FXML
    private Label lblQuestionName;
    @FXML
    private Label lblQuestionDescription;
    @FXML
    private ComboBox<?> cmbAnswer;
    private Question question;
    private final QuestionDAO questionDAO = new QuestionDAO();
    
    public void setApp(Main application) {
        this.application = application;
        this.question = application.getCurrentQuestion();
        lblQuestionName.setText(question.getName());
        System.out.print(question);
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


}
