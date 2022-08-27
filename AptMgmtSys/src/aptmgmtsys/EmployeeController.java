/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
    @FXML
    private TextField tf_search;
    @FXML
    private Button btn_dismiss;
    @FXML
    private Button btn_details;
    @FXML
    private Button btn_update;
    @FXML
    private MenuItem mi_empID;
    @FXML
    private MenuItem mi_phone;
    @FXML
    private TableView<?> tv_employee;
    @FXML
    private Button btn_refresh;
    private String dynamicSearch;
    private DBConnect dbcon;
    private String searchQ;
    @FXML
    private MenuButton mbtn_search;
    @FXML
    private Button btn_refreshF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dbcon = new DBConnect();

        try {
            dbcon.connectToDB();

            TableLoader.loadTable("select name, phone, designation from Employees where status_ = 'present'", tv_employee);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        searchQ = "select name, phone, designation from Employees where ";
        dynamicSearch = searchQ + " empID like '%";

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

    @FXML
    private void OKR_search(KeyEvent event) {

        String search_ = tf_search.getText();
        String dynQry;
        if (search_ != "") {
            try {
                dynQry = dynamicSearch + search_ + "%'";
                TableLoader.loadTable(dynQry, tv_employee);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void onClickBtn_dismiss(ActionEvent event) {
    }

    @FXML
    private void onClickBtn_details(ActionEvent event) {

    }

    @FXML
    private void onClickBtn_update(ActionEvent event) {
    }

    @FXML
    private void onClickMi_empID(ActionEvent event) {
        mbtn_search.setText("search by ID");

        dynamicSearch = searchQ + " empID like '%";
    }

    @FXML
    private void onClickMi_phone(ActionEvent event) {
        mbtn_search.setText("search by phone");

        dynamicSearch = searchQ + " phone like '%";
    }

    @FXML
    private void onClickBtn_refresh(ActionEvent event) {

        try {

            TableLoader.loadTable("select name, phone, designation, empID from Employees where status_ = 'present'", tv_employee);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_refreshF(ActionEvent event) {

        try {

            TableLoader.loadTable("select name, phone, designation, empID from Employees", tv_employee);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
