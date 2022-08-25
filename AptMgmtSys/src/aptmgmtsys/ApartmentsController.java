/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sun.security.pkcs11.Secmod;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class ApartmentsController implements Initializable {

    @FXML
    private Button btn_back;
    @FXML
    private Button btn_addNewFlat;
    @FXML
    private TableView<?> tv_apartments;
    private TableLoader tableLoader;
    @FXML
    private TextField tf_search;
    @FXML
    private MenuButton mbtn_searchBy;
    @FXML
    private MenuItem mi_aptno;
    @FXML
    private MenuItem mi_flatid;
    /**
     * Initializes the controller class.
     */

    private String searchQ, dynamicSearch;
    private DBConnect dbcon;
    @FXML
    private Button btn_showall;
    @FXML
    private Button btn_remove;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            dbcon = new DBConnect();
            dbcon.connectToDB();
            tableLoader.loadTable("select flatID, apt_no, area, level_ from Flats", tv_apartments);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        searchQ = "select * from Flats where ";
        dynamicSearch = searchQ + " apt_no = '";

        mbtn_searchBy.setText("search by apt_no");

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
    private void onClickBtn_addNewFlat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddNewFlat.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_addNewFlat.getScene().getWindow();
            window.setTitle("Add New Flat");
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
                dynQry = dynamicSearch + search_ + "'";
                TableLoader.loadTable(dynQry, tv_apartments);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void onClickMi_aptno(ActionEvent event) {
        mbtn_searchBy.setText("search by apt_no");

        dynamicSearch = searchQ + " apt_no like '";
    }

    @FXML
    private void onClickMi_flatid(ActionEvent event) {
        mbtn_searchBy.setText("search by flatID");

        dynamicSearch = searchQ + " flatID like '";
    }

    @FXML
    private void onClickBtn_showall(ActionEvent event) {

        try {
            tableLoader.loadTable("select flatID, apt_no, area, level_ from Flats", tv_apartments);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickbtn_remove(ActionEvent event) {
        try {
            //delete row where id = this

            Object s = tv_apartments.getSelectionModel().getSelectedItems().get(0);

            System.out.println(s.toString().split(", ")[1].substring(1));

            String flatID = s.toString().split(", ")[0].substring(1); //got the 2nd column of selected row -> first col = course id
            System.out.println("flat id : " + flatID);

            dbcon.insertDataToDB("delete from Flats where FlatID = '" + flatID + "'");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Delete success");
            alert.setContentText("---");
            alert.showAndWait();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
