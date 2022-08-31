/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

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
public class BillStatusController implements Initializable {

    @FXML
    private TableView<?> tv_bills;
    @FXML
    private Button btn_paid;
    private String billID;
    private DBConnect dbcon;
    @FXML
    private Button btn_back;
    private String billAmt;
    private int availableAmount;

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
        } catch (Exception ex) {
        } 
    }

    @FXML
    private void onClickBtn_paid(ActionEvent event) {
        Object s = tv_bills.getSelectionModel().getSelectedItems().get(0);
        billID = s.toString().split(", ")[1];
        billAmt = s.toString().split(", ")[4];
        double latestAmt = calcLatestFund();

        //trx table, get current amount
        if (latestAmt != -1) {
            latestAmt += Double.valueOf(billAmt);

            boolean fundAdded = dbcon.insertDataToDB("insert into Transactions values(getdate(), 'bill', " + billAmt + ", " + latestAmt + ")");
            if (fundAdded) {
                boolean b = dbcon.insertDataToDB("update Billings set status_ = 'paid' where billID = '" + billID + "'");
                showAlert(true, "Trx made, Bill paid");
                btn_paid.setDisable(true);
                try {

                    TableLoader.loadTable("select * from Billings", tv_bills);
                } catch (Exception ex) {
                    showAlert(false, "Could not load bills");
                }

            }

        } else {
            showAlert(false, "failed to read latest available fund");
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

    @FXML
    private void OMC_tv_bills(MouseEvent event) {
        btn_paid.setDisable(!(!(tv_bills.getSelectionModel().getSelectedItems().get(0) == null)    
                
                &&     !(tv_bills.getSelectionModel().getSelectedItems().get(0).toString().contains("paid"))));

    }

    private double calcLatestFund() {

        try {
            ResultSet rss = dbcon.queryToDB("select count(*) from Transactions");
            rss.next();
            int totalRow = rss.getInt(1);
            if (totalRow == 0) {
                return 0;
            }

        } catch (Exception e) {
            showAlert(false, "sth went wrong during checking latest fund");

        }

        try {
            //trx table, get current amount
            ResultSet rs = dbcon.queryToDB("select latestAvailableAmount from Transactions where sl = (select max(sl) from Transactions)");
            rs.next();
            return rs.getDouble("latestAvailableAmount");

        } catch (Exception ex) {
            showAlert(false, "sth went wrong during checking fund availibility");
        }
        return -1;

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
        alert.showAndWait();
    }
}
