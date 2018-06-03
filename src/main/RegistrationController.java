/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author almin
 */
public class RegistrationController extends AnchorPane implements Initializable {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnContinue;

    @FXML
    private Label errorMessage;

    private Main application;

    public void setApp(Main application) {
        this.application = application;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
    }

    @FXML
    private void processRegistration(ActionEvent event) throws Exception {
        if (application == null) {
            // We are running in isolated FXML, possibly in Scene Builder.
            // NO-OP.
            errorMessage.setText("Hello " + txtFirstName.getText());
        } else {
            if (!application.userRegistration(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText())) {
                //errorMessage.setText("Username/Password is incorrect");
            }
        }
    }

}
