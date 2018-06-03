/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dao.UserDAO;
import entities.Participant;
import entities.Question;
import entities.Survey;
import entities.User;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eclipse.persistence.internal.libraries.antlr.runtime.DFA;

/**
 *
 * @author almin
 */
public class Main extends Application {

    private Stage stage;
    private User loggedUser;
    private Survey survey;
    private Participant participant;
    private Question currentQuestion;
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            stage = primaryStage;
            stage.setTitle("Survey demo app");
            gotoRegistration();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[]) null);
    }

    private void gotoRegistration() {
        try {
            RegistrationController registration = (RegistrationController) replaceSceneContent("registration.fxml");
            registration.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoSurveys() {
        try {
            SurveysController survey = (SurveysController) replaceSceneContent("surveys.fxml");
            survey.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoQuestion() {
        try {
            if (nextQuestion()) {
                String questionType = currentQuestion.getType();
                switch (questionType) {
                    case "number":
                        gotoNumberQuestionType();
                        break;
                    case "multiple":
                        gotoMultipleQuestionType();
                        break;
                    default:
                        gotoSingleQuestionType();
                        break;
                }
            } else {
                gotoThankYou();
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public void setQuestion(Question question) {
        if (question == null) {
            currentQuestion = survey.getQuestionList().get(0);
        } else {
            currentQuestion = question;
        }
    }

    public Question getCurrentQuestion() {
        return this.currentQuestion;
    }

    public boolean userRegistration(String firstName, String lastName, String email) throws Exception {
        if (foundUserByEmail(email)) {
            loggedUser = getUserByEmail(email);
        } else {
            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            userDAO.addUser(newUser);
            loggedUser = newUser;
        }
        gotoSurveys();
        return true;
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    private Boolean foundUserByEmail(String email) {
        List<User> allUsers = userDAO.getAllUsers();
        Boolean foundUser = false;
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                foundUser = true;
            }
        }
        return foundUser;
    }

    private User getUserByEmail(String email) {
        List<User> allUsers = userDAO.getAllUsers();
        User foundedUser = null;
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                foundedUser = user;
            }
        }
        return foundedUser;
    }

    private Boolean nextQuestion() {
        if (currentQuestion == null) {
            currentQuestion = survey.getQuestionList().get(0);
            return true;
        }
        int currentQuestionIndex = survey.getQuestionList().indexOf(currentQuestion);

        if ((currentQuestionIndex + 1) == survey.getQuestionList().size()) {
            return false;
        } else {
            currentQuestion = survey.getQuestionList().get(currentQuestionIndex + 1);
            return true;
        }
    }

    private void gotoThankYou() throws Exception {
        ThankyouController thankyou = (ThankyouController) replaceSceneContent("thankyou.fxml");
        thankyou.setApp(this);
    }

    private void gotoNumberQuestionType() throws Exception {
        NumberQuestionTypeController question = (NumberQuestionTypeController) replaceSceneContent("numberQuestionType.fxml");
        question.setApp(this);
    }

    private void gotoMultipleQuestionType() throws Exception {
        MultipleQuestionTypeController question = (MultipleQuestionTypeController) replaceSceneContent("multipleQuestionType.fxml");
        question.setApp(this);
    }

    private void gotoSingleQuestionType() throws Exception {
        SingleQuestionTypeController question = (SingleQuestionTypeController) replaceSceneContent("singleQuestionType.fxml");
        question.setApp(this);
    }

}
