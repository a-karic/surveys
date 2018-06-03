/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.ParticipantAnswerDAO;
import dao.QuestionDAO;
import entities.Answer;
import entities.Participant;
import entities.ParticipantAnswer;
import entities.Question;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author almin
 */
public class SingleQuestionTypeController implements Initializable {

    @FXML
    private TextArea lblQuestionName;
    @FXML
    private TextArea lblQuestionDescription;
    @FXML
    private ListView<Answer> lstAnswers;
    private final ObservableList<Answer> answersList = FXCollections.observableArrayList();
    @FXML
    private Button btnNext;
    private Question question;
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final ParticipantAnswerDAO participantAnswerDAO = new ParticipantAnswerDAO();
    private Main application;

    public void setApp(Main application) {
        this.application = application;
        this.question = application.getCurrentQuestion();
        lblQuestionName.setText(question.getName());
        lblQuestionDescription.setText(question.getDescription());
//        System.out.print(question);

        lstAnswers.setItems(answersList);
        answersList.addAll(question.getAnswerList());

        lstAnswers.setCellFactory(new Callback<ListView<Answer>, ListCell<Answer>>() {
            @Override
            public ListCell<Answer> call(ListView<Answer> param) {
                ListCell<Answer> listCell = new ListCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.

                        if (item != null) {
                            Answer answer = (Answer) item;
                            setText(answer.getName());
                        } else {
                            setText("");
                        }
                    }

                };
                return listCell;
            }
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void processNextQuestion(ActionEvent event) throws Exception {
        Answer selectedAnswer = lstAnswers.getSelectionModel().getSelectedItem();
        if (selectedAnswer != null){
            ParticipantAnswer newParticipantAnswer = new ParticipantAnswer();
            newParticipantAnswer.setAnswerId(selectedAnswer);
            newParticipantAnswer.setParticipantId(application.getParticipant());
            newParticipantAnswer.setQuestionId(question);
            participantAnswerDAO.addParticipantAnswer(newParticipantAnswer);
            application.gotoQuestion();
        }
    }

}
