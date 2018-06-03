/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.AnswerDAO;
import dao.ParticipantAnswerDAO;
import entities.Answer;
import entities.ParticipantAnswer;
import entities.Question;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author almin
 */
public class NumberQuestionTypeController implements Initializable {

    @FXML
    private TextArea lblQuestionName;
    @FXML
    private TextArea lblQuestionDescription;
    @FXML
    private TextField txtAnswer;
    @FXML
    private Button btnNext;
    private Question question;
    private final ParticipantAnswerDAO participantAnswerDAO = new ParticipantAnswerDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();
    private Main application;

    public void setApp(Main application) {
        this.application = application;
        this.question = application.getCurrentQuestion();
        lblQuestionName.setText(question.getName());
        lblQuestionDescription.setText(question.getDescription());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void processNextQuestion(ActionEvent event) throws Exception {
        Answer newAnswer = new Answer();
        newAnswer.setName(txtAnswer.getText());
        answerDAO.addAnswer(newAnswer);
        ParticipantAnswer newParticipantAnswer = new ParticipantAnswer();
        newParticipantAnswer.setAnswerId(newAnswer);
        newParticipantAnswer.setParticipantId(application.getParticipant());
        newParticipantAnswer.setQuestionId(question);
        participantAnswerDAO.addParticipantAnswer(newParticipantAnswer);
        application.gotoQuestion();
    }

}
