/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.Survey;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpacontrollers.SurveyJpaController;
import jpacontrollers.exceptions.IllegalOrphanException;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class SurveyDAO {
    private final SurveyJpaController surveyController;
    private final EntityManagerFactory emf;

    public SurveyDAO() {
        emf = Persistence.createEntityManagerFactory("surveysPU");
        surveyController = new SurveyJpaController(emf);
    }

    public void addSurvey(Survey survey) throws Exception {
        surveyController.create(survey);
    }

    public void editSurvey(Survey survey) throws Exception {
        surveyController.edit(survey);
    }

    public void removeSurvey(int surveyID) throws NonexistentEntityException, IllegalOrphanException {
        surveyController.destroy(surveyID);
    }

    public List<Survey> getAllSurveys() {
        return surveyController.findSurveyEntities();
    }

    public Survey getSurveyByID(int surveyID) {
        return surveyController.findSurvey(surveyID);
    }
    
}
