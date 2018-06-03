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
import entities.User;
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
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getParticipantList() == null) {
            user.setParticipantList(new ArrayList<Participant>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Participant> attachedParticipantList = new ArrayList<Participant>();
            for (Participant participantListParticipantToAttach : user.getParticipantList()) {
                participantListParticipantToAttach = em.getReference(participantListParticipantToAttach.getClass(), participantListParticipantToAttach.getId());
                attachedParticipantList.add(participantListParticipantToAttach);
            }
            user.setParticipantList(attachedParticipantList);
            em.persist(user);
            for (Participant participantListParticipant : user.getParticipantList()) {
                User oldUserIdOfParticipantListParticipant = participantListParticipant.getUserId();
                participantListParticipant.setUserId(user);
                participantListParticipant = em.merge(participantListParticipant);
                if (oldUserIdOfParticipantListParticipant != null) {
                    oldUserIdOfParticipantListParticipant.getParticipantList().remove(participantListParticipant);
                    oldUserIdOfParticipantListParticipant = em.merge(oldUserIdOfParticipantListParticipant);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            List<Participant> participantListOld = persistentUser.getParticipantList();
            List<Participant> participantListNew = user.getParticipantList();
            List<String> illegalOrphanMessages = null;
            for (Participant participantListOldParticipant : participantListOld) {
                if (!participantListNew.contains(participantListOldParticipant)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Participant " + participantListOldParticipant + " since its userId field is not nullable.");
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
            user.setParticipantList(participantListNew);
            user = em.merge(user);
            for (Participant participantListNewParticipant : participantListNew) {
                if (!participantListOld.contains(participantListNewParticipant)) {
                    User oldUserIdOfParticipantListNewParticipant = participantListNewParticipant.getUserId();
                    participantListNewParticipant.setUserId(user);
                    participantListNewParticipant = em.merge(participantListNewParticipant);
                    if (oldUserIdOfParticipantListNewParticipant != null && !oldUserIdOfParticipantListNewParticipant.equals(user)) {
                        oldUserIdOfParticipantListNewParticipant.getParticipantList().remove(participantListNewParticipant);
                        oldUserIdOfParticipantListNewParticipant = em.merge(oldUserIdOfParticipantListNewParticipant);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Participant> participantListOrphanCheck = user.getParticipantList();
            for (Participant participantListOrphanCheckParticipant : participantListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Participant " + participantListOrphanCheckParticipant + " in its participantList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
