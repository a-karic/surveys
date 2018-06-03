/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.Question;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpacontrollers.QuestionJpaController;
import jpacontrollers.exceptions.IllegalOrphanException;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class QuestionDAO {
    private final QuestionJpaController questionController;
    private final EntityManagerFactory emf;

    public QuestionDAO() {
        emf = Persistence.createEntityManagerFactory("surveysPU");
        questionController = new QuestionJpaController(emf);
    }

    public void addQuestion(Question question) throws Exception {
        questionController.create(question);
    }

    public void editQuestion(Question question) throws Exception {
        questionController.edit(question);
    }

    public void removeQuestion(int questionID) throws NonexistentEntityException, IllegalOrphanException {
        questionController.destroy(questionID);
    }

    public List<Question> getAllQuestions() {
        return questionController.findQuestionEntities();
    }

    public Question getQuestionByID(int questionID) {
        return questionController.findQuestion(questionID);
    }
    
}
