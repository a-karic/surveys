/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.Answer;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpacontrollers.AnswerJpaController;
import jpacontrollers.exceptions.IllegalOrphanException;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class AnswerDAO {
    private final AnswerJpaController answerController;
    private final EntityManagerFactory emf;

    public AnswerDAO() {
        emf = Persistence.createEntityManagerFactory("surveysPU");
        answerController = new AnswerJpaController(emf);
    }

    public void addAnswer(Answer answer) throws Exception {
        answerController.create(answer);
    }

    public void editAnswer(Answer answer) throws Exception {
        answerController.edit(answer);
    }

    public void removeAnswer(int answerID) throws NonexistentEntityException, IllegalOrphanException {
        answerController.destroy(answerID);
    }

    public List<Answer> getAllAnswers() {
        return answerController.findAnswerEntities();
    }

    public Answer getAnswerByID(int answerID) {
        return answerController.findAnswer(answerID);
    }
}
