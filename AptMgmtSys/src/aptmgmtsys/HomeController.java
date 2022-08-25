/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class HomeController implements Initializable {

    @FXML
    private Button btn_billing;
    @FXML
    private Button btn_apt;
    @FXML
    private Button btn_employee;
    @FXML
    private Button btn_fund;
    @FXML
    private Button btn_payment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        Image img = new Image("bili.gif");
//        i.setImage(img);
        
 
      
    }

    @FXML
    private void onClickBtn_billing(ActionEvent event) {
        try {
//            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Billing.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("My New Stage Title");
//            stage.setScene(new Scene(root));
//            stage.show();
//            // Hide this current window (if this is what you want)
//            ((Node) (event.getSource())).getScene().getWindow().hide();

            //====
            Parent root = FXMLLoader.load(getClass().getResource("Billing.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_billing.getScene().getWindow();
            window.setTitle("Billing");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_apt(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Apartments.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_billing.getScene().getWindow();
            window.setTitle("Billing");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onClickBtn_employee(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Employee.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_billing.getScene().getWindow();
            window.setTitle("Employee");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void onClickBtn_fund(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Fund.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_billing.getScene().getWindow();
            window.setTitle("Billing");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_payment(ActionEvent event) {
         try {
            Parent root = FXMLLoader.load(getClass().getResource("Payment.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_billing.getScene().getWindow();
            window.setTitle("Billing");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
