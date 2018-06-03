/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpacontrollers;

import entities.Answer;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Question;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class AnswerJpaController implements Serializable {

    public AnswerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Answer answer) {
        if (answer.getQuestionList() == null) {
            answer.setQuestionList(new ArrayList<Question>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Question> attachedQuestionList = new ArrayList<Question>();
            for (Question questionListQuestionToAttach : answer.getQuestionList()) {
                questionListQuestionToAttach = em.getReference(questionListQuestionToAttach.getClass(), questionListQuestionToAttach.getId());
                attachedQuestionList.add(questionListQuestionToAttach);
            }
            answer.setQuestionList(attachedQuestionList);
            em.persist(answer);
            for (Question questionListQuestion : answer.getQuestionList()) {
                questionListQuestion.getAnswerList().add(answer);
                questionListQuestion = em.merge(questionListQuestion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Answer answer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Answer persistentAnswer = em.find(Answer.class, answer.getId());
            List<Question> questionListOld = persistentAnswer.getQuestionList();
            List<Question> questionListNew = answer.getQuestionList();
            List<Question> attachedQuestionListNew = new ArrayList<Question>();
            for (Question questionListNewQuestionToAttach : questionListNew) {
                questionListNewQuestionToAttach = em.getReference(questionListNewQuestionToAttach.getClass(), questionListNewQuestionToAttach.getId());
                attachedQuestionListNew.add(questionListNewQuestionToAttach);
            }
            questionListNew = attachedQuestionListNew;
            answer.setQuestionList(questionListNew);
            answer = em.merge(answer);
            for (Question questionListOldQuestion : questionListOld) {
                if (!questionListNew.contains(questionListOldQuestion)) {
                    questionListOldQuestion.getAnswerList().remove(answer);
                    questionListOldQuestion = em.merge(questionListOldQuestion);
                }
            }
            for (Question questionListNewQuestion : questionListNew) {
                if (!questionListOld.contains(questionListNewQuestion)) {
                    questionListNewQuestion.getAnswerList().add(answer);
                    questionListNewQuestion = em.merge(questionListNewQuestion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = answer.getId();
                if (findAnswer(id) == null) {
                    throw new NonexistentEntityException("The answer with id " + id + " no longer exists.");
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
            Answer answer;
            try {
                answer = em.getReference(Answer.class, id);
                answer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The answer with id " + id + " no longer exists.", enfe);
            }
            List<Question> questionList = answer.getQuestionList();
            for (Question questionListQuestion : questionList) {
                questionListQuestion.getAnswerList().remove(answer);
                questionListQuestion = em.merge(questionListQuestion);
            }
            em.remove(answer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Answer> findAnswerEntities() {
        return findAnswerEntities(true, -1, -1);
    }

    public List<Answer> findAnswerEntities(int maxResults, int firstResult) {
        return findAnswerEntities(false, maxResults, firstResult);
    }

    private List<Answer> findAnswerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Answer.class));
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

    public Answer findAnswer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Answer.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnswerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Answer> rt = cq.from(Answer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
