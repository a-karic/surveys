/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entities.User;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpacontrollers.UserJpaController;
import jpacontrollers.exceptions.IllegalOrphanException;
import jpacontrollers.exceptions.NonexistentEntityException;

/**
 *
 * @author almin
 */
public class UserDAO {

    private final UserJpaController userController;
    private final EntityManagerFactory emf;

    public UserDAO() {
        emf = Persistence.createEntityManagerFactory("surveysPU");
        userController = new UserJpaController(emf);
    }

    public void addUser(User user) throws Exception {
        userController.create(user);
    }

    public void editUser(User user) throws Exception {
        userController.edit(user);
    }

    public void removeUser(int userID) throws NonexistentEntityException, IllegalOrphanException {
        userController.destroy(userID);
    }

    public List<User> getAllUsers() {
        return userController.findUserEntities();
    }

    public User getUserByID(int userID) {
        return userController.findUser(userID);
    }
}
