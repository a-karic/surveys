/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.ParticipantDAO;
import dao.SurveyDAO;
import entities.Participant;
import entities.Survey;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author almin
 */
public class SurveysController implements Initializable {

    @FXML
    private ListView<Survey> lstSurveys;
    private final ObservableList<Survey> surveysList = FXCollections.observableArrayList();
    @FXML
    private Button btnContinue;

    private final SurveyDAO surveyDAO = new SurveyDAO();

    private Main application;

    public void setApp(Main application) {
        this.application = application;
        lstSurveys.setItems(surveysList);
        surveysList.addAll(surveyDAO.getAllSurveys());

        lstSurveys.setCellFactory(new Callback<ListView<Survey>, ListCell<Survey>>() {
            @Override
            public ListCell<Survey> call(ListView<Survey> param) {
                ListCell<Survey> listCell = new ListCell() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.

                        if (item != null) {
                            Survey survey = (Survey) item;
                            setText(survey.getName());
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
    private void processSelectingSurveys(ActionEvent event) throws Exception {
        Survey selectedSurvey = lstSurveys.getSelectionModel().getSelectedItem();
        if (selectedSurvey != null) {
            application.setSurvey(selectedSurvey);
            Participant newParticipant = createParticipant();
            application.setParticipant(newParticipant);
            application.gotoQuestion();
        }
    }

    private Participant createParticipant() throws Exception {
        ParticipantDAO participantDAO = new ParticipantDAO();
        Participant newParticipant = new Participant();
        newParticipant.setSurveyId(application.getSurvey());
        newParticipant.setUserId(application.getLoggedUser());
        participantDAO.addParticipant(newParticipant);
        return newParticipant;
    }

}
