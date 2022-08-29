/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class ChooseEmployeeController implements Initializable {

    @FXML
    private TableView<?> tv_emp;
    @FXML
    private Button btn_select;
    @FXML
    private Button btn_back;
    private DBConnect dbcon;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dbcon = new DBConnect();
        try {
            dbcon.connectToDB();
        } catch (Exception e) {
        }
        init();
    }

    @FXML
    private void onClickBtn_select(ActionEvent event) {

        
        try {
            //select & set bundle
            Object s = tv_emp.getSelectionModel().getSelectedItems().get(0);
            Bundle.selected = s;
            //===selection done
            
            

            Parent root = FXMLLoader.load(getClass().getResource("Payment.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_select.getScene().getWindow();
            window.close();

 
            

        } catch (Exception e) {
            showAlert(false, "selection failed");

        }

    }

    @FXML
    private void onClickBtn_back(ActionEvent event) {
        try {

            Parent billing = FXMLLoader.load(getClass().getResource("Payment.fxml"));
            Scene scr = new Scene(billing);
            Stage window = (Stage) btn_back.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Payment");
            window.setScene(scr);
            window.show();

        } catch (Exception ex) {

        }
    }

    public void init() {
        try {
            // TODO

            TableLoader.loadTable("select name, phone, email, designation, salary, empID from Employees where status_ = 'present'", tv_emp);
        } catch (Exception ex) {
        }
    }
    
    
    private void showAlert(boolean success, String msg) {
        Alert alert;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED");
        }

        alert.setHeaderText(msg);
        alert.setContentText("---");
        alert.showAndWait();
    }

    @FXML
    private void OMC(MouseEvent event) {
        btn_select.setDisable(tv_emp.getSelectionModel().getSelectedItems().get(0) == null);
    }

}
