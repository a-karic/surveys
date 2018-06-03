/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpacontrollers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Answer;
import entities.Question;
import entities.Participant;
import entities.ParticipantAnswer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class ParticipantAnswerJpaController implements Serializable {

    public ParticipantAnswerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ParticipantAnswer participantAnswer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Answer answerId = participantAnswer.getAnswerId();
            if (answerId != null) {
                answerId = em.getReference(answerId.getClass(), answerId.getId());
                participantAnswer.setAnswerId(answerId);
            }
            Question questionId = participantAnswer.getQuestionId();
            if (questionId != null) {
                questionId = em.getReference(questionId.getClass(), questionId.getId());
                participantAnswer.setQuestionId(questionId);
            }
            Participant participantId = participantAnswer.getParticipantId();
            if (participantId != null) {
                participantId = em.getReference(participantId.getClass(), participantId.getId());
                participantAnswer.setParticipantId(participantId);
            }
            em.persist(participantAnswer);
            if (answerId != null) {
                answerId.getParticipantAnswerList().add(participantAnswer);
                answerId = em.merge(answerId);
            }
            if (questionId != null) {
                questionId.getParticipantAnswerList().add(participantAnswer);
                questionId = em.merge(questionId);
            }
            if (participantId != null) {
                participantId.getParticipantAnswerList().add(participantAnswer);
                participantId = em.merge(participantId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ParticipantAnswer participantAnswer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ParticipantAnswer persistentParticipantAnswer = em.find(ParticipantAnswer.class, participantAnswer.getId());
            Answer answerIdOld = persistentParticipantAnswer.getAnswerId();
            Answer answerIdNew = participantAnswer.getAnswerId();
            Question questionIdOld = persistentParticipantAnswer.getQuestionId();
            Question questionIdNew = participantAnswer.getQuestionId();
            Participant participantIdOld = persistentParticipantAnswer.getParticipantId();
            Participant participantIdNew = participantAnswer.getParticipantId();
            if (answerIdNew != null) {
                answerIdNew = em.getReference(answerIdNew.getClass(), answerIdNew.getId());
                participantAnswer.setAnswerId(answerIdNew);
            }
            if (questionIdNew != null) {
                questionIdNew = em.getReference(questionIdNew.getClass(), questionIdNew.getId());
                participantAnswer.setQuestionId(questionIdNew);
            }
            if (participantIdNew != null) {
                participantIdNew = em.getReference(participantIdNew.getClass(), participantIdNew.getId());
                participantAnswer.setParticipantId(participantIdNew);
            }
            participantAnswer = em.merge(participantAnswer);
            if (answerIdOld != null && !answerIdOld.equals(answerIdNew)) {
                answerIdOld.getParticipantAnswerList().remove(participantAnswer);
                answerIdOld = em.merge(answerIdOld);
            }
            if (answerIdNew != null && !answerIdNew.equals(answerIdOld)) {
                answerIdNew.getParticipantAnswerList().add(participantAnswer);
                answerIdNew = em.merge(answerIdNew);
            }
            if (questionIdOld != null && !questionIdOld.equals(questionIdNew)) {
                questionIdOld.getParticipantAnswerList().remove(participantAnswer);
                questionIdOld = em.merge(questionIdOld);
            }
            if (questionIdNew != null && !questionIdNew.equals(questionIdOld)) {
                questionIdNew.getParticipantAnswerList().add(participantAnswer);
                questionIdNew = em.merge(questionIdNew);
            }
            if (participantIdOld != null && !participantIdOld.equals(participantIdNew)) {
                participantIdOld.getParticipantAnswerList().remove(participantAnswer);
                participantIdOld = em.merge(participantIdOld);
            }
            if (participantIdNew != null && !participantIdNew.equals(participantIdOld)) {
                participantIdNew.getParticipantAnswerList().add(participantAnswer);
                participantIdNew = em.merge(participantIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = participantAnswer.getId();
                if (findParticipantAnswer(id) == null) {
                    throw new NonexistentEntityException("The participantAnswer with id " + id + " no longer exists.");
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
            ParticipantAnswer participantAnswer;
            try {
                participantAnswer = em.getReference(ParticipantAnswer.class, id);
                participantAnswer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The participantAnswer with id " + id + " no longer exists.", enfe);
            }
            Answer answerId = participantAnswer.getAnswerId();
            if (answerId != null) {
                answerId.getParticipantAnswerList().remove(participantAnswer);
                answerId = em.merge(answerId);
            }
            Question questionId = participantAnswer.getQuestionId();
            if (questionId != null) {
                questionId.getParticipantAnswerList().remove(participantAnswer);
                questionId = em.merge(questionId);
            }
            Participant participantId = participantAnswer.getParticipantId();
            if (participantId != null) {
                participantId.getParticipantAnswerList().remove(participantAnswer);
                participantId = em.merge(participantId);
            }
            em.remove(participantAnswer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ParticipantAnswer> findParticipantAnswerEntities() {
        return findParticipantAnswerEntities(true, -1, -1);
    }

    public List<ParticipantAnswer> findParticipantAnswerEntities(int maxResults, int firstResult) {
        return findParticipantAnswerEntities(false, maxResults, firstResult);
    }

    private List<ParticipantAnswer> findParticipantAnswerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParticipantAnswer.class));
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

    public ParticipantAnswer findParticipantAnswer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ParticipantAnswer.class, id);
        } finally {
            em.close();
        }
    }

    public int getParticipantAnswerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ParticipantAnswer> rt = cq.from(ParticipantAnswer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
