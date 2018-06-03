/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpacontrollers;

import entities.Participant;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.User;
import entities.Survey;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class ParticipantJpaController implements Serializable {

    public ParticipantJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Participant participant) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User userId = participant.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                participant.setUserId(userId);
            }
            Survey surveyId = participant.getSurveyId();
            if (surveyId != null) {
                surveyId = em.getReference(surveyId.getClass(), surveyId.getId());
                participant.setSurveyId(surveyId);
            }
            em.persist(participant);
            if (userId != null) {
                userId.getParticipantList().add(participant);
                userId = em.merge(userId);
            }
            if (surveyId != null) {
                surveyId.getParticipantList().add(participant);
                surveyId = em.merge(surveyId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Participant participant) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participant persistentParticipant = em.find(Participant.class, participant.getId());
            User userIdOld = persistentParticipant.getUserId();
            User userIdNew = participant.getUserId();
            Survey surveyIdOld = persistentParticipant.getSurveyId();
            Survey surveyIdNew = participant.getSurveyId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                participant.setUserId(userIdNew);
            }
            if (surveyIdNew != null) {
                surveyIdNew = em.getReference(surveyIdNew.getClass(), surveyIdNew.getId());
                participant.setSurveyId(surveyIdNew);
            }
            participant = em.merge(participant);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getParticipantList().remove(participant);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getParticipantList().add(participant);
                userIdNew = em.merge(userIdNew);
            }
            if (surveyIdOld != null && !surveyIdOld.equals(surveyIdNew)) {
                surveyIdOld.getParticipantList().remove(participant);
                surveyIdOld = em.merge(surveyIdOld);
            }
            if (surveyIdNew != null && !surveyIdNew.equals(surveyIdOld)) {
                surveyIdNew.getParticipantList().add(participant);
                surveyIdNew = em.merge(surveyIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = participant.getId();
                if (findParticipant(id) == null) {
                    throw new NonexistentEntityException("The participant with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Participant participant;
            try {
                participant = em.getReference(Participant.class, id);
                participant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The participant with id " + id + " no longer exists.", enfe);
            }
            User userId = participant.getUserId();
            if (userId != null) {
                userId.getParticipantList().remove(participant);
                userId = em.merge(userId);
            }
            Survey surveyId = participant.getSurveyId();
            if (surveyId != null) {
                surveyId.getParticipantList().remove(participant);
                surveyId = em.merge(surveyId);
            }
            em.remove(participant);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Participant> findParticipantEntities() {
        return findParticipantEntities(true, -1, -1);
    }

    public List<Participant> findParticipantEntities(int maxResults, int firstResult) {
        return findParticipantEntities(false, maxResults, firstResult);
    }

    private List<Participant> findParticipantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Participant.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Participant findParticipant(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Participant.class, id);
        } finally {
            em.close();
        }
    }

    public int getParticipantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Participant> rt = cq.from(Participant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
