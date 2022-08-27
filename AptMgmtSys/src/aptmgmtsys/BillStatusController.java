/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
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
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class BillStatusController implements Initializable {

    @FXML
    private TableView<?> tv_bills;
    @FXML
    private Button btn_paid;
    private String billID;
    private DBConnect dbcon;
    @FXML
    private Button btn_back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbcon = new DBConnect();
        try {
            // TODO
            dbcon.connectToDB();
            TableLoader.loadTable("select * from Billings", tv_bills);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BillStatusController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BillStatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_paid(ActionEvent event) {
        Object s = tv_bills.getSelectionModel().getSelectedItems().get(0);
        billID = s.toString().split(", ")[1];

        boolean test = dbcon.insertDataToDB("update Billings set status_ = 'paid' where billID = '" + billID + "'");
        if (test) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("success, added to fund");
            alert.setHeaderText("Bill amount added to fund");
            alert.setContentText("");
            alert.showAndWait();
            try {
                // TODO
   
                TableLoader.loadTable("select * from Billings", tv_bills);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BillStatusController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(BillStatusController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("failed");
            alert.setHeaderText("could not update");
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    @FXML
    private void onClickBtn_back(ActionEvent event) {
        try {

            Parent home = FXMLLoader.load(getClass().getResource("Billing.fxml"));
            Scene scr = new Scene(home);
            Stage window = (Stage) btn_back.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Create Bill");
            window.setScene(scr);
            window.show();

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
