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
import entities.Participant;
import entities.Survey;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpacontrollers.exceptions.IllegalOrphanException;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class SurveyJpaController implements Serializable {

    public SurveyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Survey survey) {
        if (survey.getParticipantList() == null) {
            survey.setParticipantList(new ArrayList<Participant>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Participant> attachedParticipantList = new ArrayList<Participant>();
            for (Participant participantListParticipantToAttach : survey.getParticipantList()) {
                participantListParticipantToAttach = em.getReference(participantListParticipantToAttach.getClass(), participantListParticipantToAttach.getId());
                attachedParticipantList.add(participantListParticipantToAttach);
            }
            survey.setParticipantList(attachedParticipantList);
            em.persist(survey);
            for (Participant participantListParticipant : survey.getParticipantList()) {
                Survey oldSurveyIdOfParticipantListParticipant = participantListParticipant.getSurveyId();
                participantListParticipant.setSurveyId(survey);
                participantListParticipant = em.merge(participantListParticipant);
                if (oldSurveyIdOfParticipantListParticipant != null) {
                    oldSurveyIdOfParticipantListParticipant.getParticipantList().remove(participantListParticipant);
                    oldSurveyIdOfParticipantListParticipant = em.merge(oldSurveyIdOfParticipantListParticipant);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Survey survey) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Survey persistentSurvey = em.find(Survey.class, survey.getId());
            List<Participant> participantListOld = persistentSurvey.getParticipantList();
            List<Participant> participantListNew = survey.getParticipantList();
            List<String> illegalOrphanMessages = null;
            for (Participant participantListOldParticipant : participantListOld) {
                if (!participantListNew.contains(participantListOldParticipant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Participant " + participantListOldParticipant + " since its surveyId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Participant> attachedParticipantListNew = new ArrayList<Participant>();
            for (Participant participantListNewParticipantToAttach : participantListNew) {
                participantListNewParticipantToAttach = em.getReference(participantListNewParticipantToAttach.getClass(), participantListNewParticipantToAttach.getId());
                attachedParticipantListNew.add(participantListNewParticipantToAttach);
            }
            participantListNew = attachedParticipantListNew;
            survey.setParticipantList(participantListNew);
            survey = em.merge(survey);
            for (Participant participantListNewParticipant : participantListNew) {
                if (!participantListOld.contains(participantListNewParticipant)) {
                    Survey oldSurveyIdOfParticipantListNewParticipant = participantListNewParticipant.getSurveyId();
                    participantListNewParticipant.setSurveyId(survey);
                    participantListNewParticipant = em.merge(participantListNewParticipant);
                    if (oldSurveyIdOfParticipantListNewParticipant != null && !oldSurveyIdOfParticipantListNewParticipant.equals(survey)) {
                        oldSurveyIdOfParticipantListNewParticipant.getParticipantList().remove(participantListNewParticipant);
                        oldSurveyIdOfParticipantListNewParticipant = em.merge(oldSurveyIdOfParticipantListNewParticipant);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = survey.getId();
                if (findSurvey(id) == null) {
                    throw new NonexistentEntityException("The survey with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Survey survey;
            try {
                survey = em.getReference(Survey.class, id);
                survey.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The survey with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Participant> participantListOrphanCheck = survey.getParticipantList();
            for (Participant participantListOrphanCheckParticipant : participantListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Survey (" + survey + ") cannot be destroyed since the Participant " + participantListOrphanCheckParticipant + " in its participantList field has a non-nullable surveyId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(survey);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Survey> findSurveyEntities() {
        return findSurveyEntities(true, -1, -1);
    }

    public List<Survey> findSurveyEntities(int maxResults, int firstResult) {
        return findSurveyEntities(false, maxResults, firstResult);
    }

    private List<Survey> findSurveyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Survey.class));
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

    public Survey findSurvey(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Survey.class, id);
        } finally {
            em.close();
        }
    }

    public int getSurveyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Survey> rt = cq.from(Survey.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
