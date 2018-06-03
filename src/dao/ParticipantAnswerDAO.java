/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.ParticipantAnswer;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpacontrollers.ParticipantAnswerJpaController;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class ParticipantAnswerDAO {
    private final ParticipantAnswerJpaController participantAnswerController;
    private final EntityManagerFactory emf;

    public ParticipantAnswerDAO() {
        emf = Persistence.createEntityManagerFactory("surveysPU");
        participantAnswerController = new ParticipantAnswerJpaController(emf);
    }

    public void addParticipantAnswer(ParticipantAnswer participantAnswer) throws Exception {
        participantAnswerController.create(participantAnswer);
    }

    public void editParticipantAnswer(ParticipantAnswer participantAnswer) throws Exception {
        participantAnswerController.edit(participantAnswer);
    }

    public void removeParticipantAnswer(int participantAnswerID) throws NonexistentEntityException {
        participantAnswerController.destroy(participantAnswerID);
    }

    public List<ParticipantAnswer> getAllParticipantAnswers() {
        return participantAnswerController.findParticipantAnswerEntities();
    }

    public ParticipantAnswer getParticipantAnswerByID(int participantAnswerID) {
        return participantAnswerController.findParticipantAnswer(participantAnswerID);
    }
}
