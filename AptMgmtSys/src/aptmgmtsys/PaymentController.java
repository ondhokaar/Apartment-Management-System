/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import java.awt.image.RescaleOp;
import java.io.IOException;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.security.pkcs11.Secmod;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class PaymentController implements Initializable {

    @FXML
    private Button btn_back;
    @FXML
    private Button btn_pay;
    @FXML
    private MenuItem mitem_employee;
    @FXML
    private MenuButton mbtn_type;
    @FXML
    private MenuItem mitem_oldService;
    @FXML
    private Label label_sumtotal;
    @FXML
    private Label label_payingto;
    @FXML
    private TextField tf_serviceName;
    @FXML
    private TextField tf_serviceCost;
    @FXML
    private ScrollPane sp;
    @FXML
    private GridPane gp;
    @FXML
    private Button btn_addToInvoice;
    private int row;
    private String empname;
    private String empphone;
    private String empid;
    private Double empsalary;
    private String empdesignation;
    private double sumtotal;
    private String sname;
    private String sphone;
    private String spid;
    private DBConnect dbcon;
    private double availableAmount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btn_addToInvoice.setDisable(true);
        btn_pay.setDisable(true);
        row = 0;
        dbcon = new DBConnect();

        try {
            dbcon.connectToDB();
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
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_pay(ActionEvent event) {
        if (fundAvailable()) {
            pay();
            showAlert(true, "Success payment");
            gp.getChildren().clear();
            label_payingto.setText("<select payee>");
            label_sumtotal.setText("000");

            btn_pay.setDisable(true);

        } else {
            showAlert(false, "Not Enough Fund");
        }
    }

    @FXML
    private void onClickMitem_employee(ActionEvent event) {
//        try {
//
////
////            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Home.fxml"));
////            p.getChildren().add(newLoadedPane);
//
//        } catch (Exception ex) {
//            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
//        }

        //====================EMPLOYEE
        //1. choose employee first
        gp.getChildren().clear();

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ChooseEmployee.fxml"));

            Scene scr = new Scene(root);
            Stage window = new Stage();
            window.setTitle("Choose Employee");
            window.setScene(scr);
            window.showAndWait();

            //get employee
            empname = Bundle.selected.toString().split(", ")[0];
            empname = empname.substring(1, empname.length());
            empphone = Bundle.selected.toString().split(", ")[1];
            empsalary = Double.valueOf(Bundle.selected.toString().split(", ")[4]);

            empdesignation = Bundle.selected.toString().split(", ")[3];

            empid = Bundle.selected.toString().split(", ")[5];
            empid = empid.substring(0, empid.length() - 1);

            //set employee on invoice
            row = 3;
            gp.add(new Label("name: " + empname), 0, row);
            gp.add(new Label("phone: " + empphone), 0, ++row);
            gp.add(new Label("Designation: " + empdesignation), 0, ++row);
//            gp.add(new Label("Salary"), 0, ++row);
//            gp.add(new Label("" + empsalary), 1, row);
            sumtotal = 0;
            label_sumtotal.setText(String.valueOf(sumtotal));

            row++;

            label_payingto.setText("mr " + empname);

            tf_serviceCost.setDisable(false);
            tf_serviceName.setDisable(false);
            //
        } catch (IOException ex) {
            btn_addToInvoice.setDisable(true);
            tf_serviceCost.setDisable(!false);
            tf_serviceName.setDisable(!false);
            showAlert(false, "error during opening choose employee page");
            label_payingto.setText("<none chosen>");
            sumtotal = 0;
            gp.getChildren().clear();
        }
    }

    @FXML
    private void onClickMitem_oldService(ActionEvent event) {
        //==============================OLD SERVICE
        gp.getChildren().clear();

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ChooseService.fxml"));

            Scene scr = new Scene(root);
            Stage window = new Stage();
            window.setTitle("Choose Service");
            window.setScene(scr);
            window.showAndWait();

            //get service
            if (Bundle.existing) {
                sname = Bundle.selected.toString().split(", ")[0];
                sname = sname.substring(1, sname.length());
                sphone = Bundle.selected.toString().split(", ")[1];

                spid = Bundle.selected.toString().split(", ")[2];
                spid = spid.substring(0, spid.length() - 1);

                //set service on invoice
                row = 3;
                gp.add(new Label("name: " + sname), 0, row);
                gp.add(new Label("phone: " + sphone), 0, ++row);
                gp.add(new Label("SPID: " + spid), 0, ++row);
                row++;

                label_payingto.setText(sname);
                //

                sumtotal = 0;

            } else {
                ResultSet rs = (ResultSet) Bundle.selected;
                try {
                    sname = rs.getString("name");

                    sphone = rs.getString("phone");
                    spid = rs.getString("spID");

                    //set service on invoice
                    row = 3;
                    gp.add(new Label("name: " + sname), 0, row);
                    gp.add(new Label("phone: " + sphone), 0, ++row);
                    gp.add(new Label("SPID: " + spid), 0, ++row);
                    row++;

                    label_payingto.setText(sname);
                    //

                    sumtotal = 0;

                } catch (SQLException ex) {
                    showAlert(false, "error during fetching selected service");
                    label_payingto.setText("<nothing selected>");

                    sumtotal = 0;
                    gp.getChildren().clear();
                }
            }

            tf_serviceCost.setDisable(false);
            tf_serviceName.setDisable(false);

        } catch (IOException ex) {
            tf_serviceCost.setDisable(!false);
            tf_serviceName.setDisable(!false);
            btn_addToInvoice.setDisable(true);
            showAlert(false, "error loading choose service page :(");
        }
    }

    @FXML
    private void onClickBtn_addToInvoice(ActionEvent event) {
        try {
                    gp.add(new Label(tf_serviceCost.getText()), 1, row);
        gp.add(new Label(tf_serviceName.getText()), 0, row++);

        //sum
        sumtotal += Double.valueOf(tf_serviceCost.getText());
        label_sumtotal.setText(String.valueOf(sumtotal));

        tf_serviceCost.clear();
        tf_serviceName.clear();

        btn_pay.setDisable(false);
        } catch (Exception e) {
            showAlert(false, "something went wrong");
        }


    }

    private boolean pay() {
        boolean succ = false;
        double latestAmt = availableAmount - sumtotal;
        try {
            succ = dbcon.insertDataToDB("insert into Transactions values(getdate(), 'pay', " + sumtotal + ", " + latestAmt + ")");
        } catch (Exception e) {
            showAlert(false, "sth went wrong during paying");
        }
        return succ;
    }

    private boolean fundAvailable() {

        try {
            ResultSet rss = dbcon.queryToDB("select count(*) from Transactions");
            rss.next();
            int totalRow = rss.getInt(1);
            if (totalRow == 0) {
                availableAmount = 0;
                return availableAmount >= sumtotal;
            }

        } catch (Exception e) {
            showAlert(false, "sth went wrong during checking fund availibility");

        }

        try {
            //trx table, get current amount
            ResultSet rs = dbcon.queryToDB("select latestAvailableAmount from Transactions where sl = (select max(sl) from Transactions)");
            rs.next();
            availableAmount = rs.getDouble("latestAvailableAmount");
            return availableAmount >= sumtotal;
        } catch (SQLException ex) {
            showAlert(false, "sth went wrong during checking fund availibility");
        }
        return false;
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
    private void OKR_add(KeyEvent event) {
        btn_addToInvoice.setDisable(!(tf_serviceName.getText() != null  && tf_serviceCost.getText() != null));


    }
}
