/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class EmployeeController implements Initializable {

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_addNew;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void onClickBtn_back(ActionEvent event) {
        try {

            Parent home = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Scene scr = new Scene(home);
            Stage window = (Stage) btn_back.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Home");
            window.setScene(scr);
            window.show();

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_addNew(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddNewEmployee.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_addNew.getScene().getWindow();
            window.setTitle("Add New Employee");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
