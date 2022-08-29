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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class FundController implements Initializable {

    @FXML
    private Button btn_back;
    @FXML
    private Label label_availableFund;
    private TextField tb_search;
    @FXML
    private TableView<?> tv_fund;
    @FXML
    private TextField tf_search;
    private DBConnect dbcon;
    private String dynamicSearch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dynamicSearch = "select * from Transactions where trxID like '";
        dbcon = new DBConnect();
        try {
            dbcon.connectToDB();

            TableLoader.loadTable("select * from Transactions", tv_fund);

            label_availableFund.setText("" + calcLatestFund());

        } catch (Exception e) {
        }
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

        }
    }

    @FXML
    private void OKR_tf_search(KeyEvent event) {
        String search_ = tf_search.getText();
        String dynQry;
        if (search_ != "") {
            try {
                dynQry = dynamicSearch + "%" + search_ + "%'";
                TableLoader.loadTable(dynQry, tv_fund);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ApartmentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
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

        } catch (SQLException ex) {
            showAlert(false, "sth went wrong during checking fund availibility");
        }
        return -1;

    }
}
