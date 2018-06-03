/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.Participant;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpacontrollers.ParticipantJpaController;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class ParticipantDAO {
    private final ParticipantJpaController participantController;
    private final EntityManagerFactory emf;

    public ParticipantDAO() {
        emf = Persistence.createEntityManagerFactory("surveysPU");
        participantController = new ParticipantJpaController(emf);
    }

    public void addParticipant(Participant participant) throws Exception {
        participantController.create(participant);
    }

    public void editParticipant(Participant participant) throws Exception {
        participantController.edit(participant);
    }

    public void removeParticipant(int participantID) throws NonexistentEntityException {
        participantController.destroy(participantID);
    }

    public List<Participant> getAllParticipants() {
        return participantController.findParticipantEntities();
    }

    public Participant getParticipantByID(int participantID) {
        return participantController.findParticipant(participantID);
    }
}
