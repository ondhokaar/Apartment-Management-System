/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.DocumentCreator;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class BillingController implements Initializable {

    @FXML
    private Button btn_back;
    @FXML
    private Button btn_billStatus;


    private DBConnect dbcon;

    private String invname;
    private String deadline;
    private String ownername;
    private String ownerphone;
    private String owneremail;
    private String ownerid;

    @FXML
    private Button btn_generateBill;
    private int flatqty;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbcon = new DBConnect();

        try {
            // TODO

            dbcon.connectToDB();
        } catch (Exception ex) {
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
    private void onClickBtn_billStatus(ActionEvent event) {
        try {

            Parent home = FXMLLoader.load(getClass().getResource("BillStatus.fxml"));
            Scene scr = new Scene(home);
            Stage window = (Stage) btn_back.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Bill Status");
            window.setScene(scr);
            window.show();

        } catch (Exception ex) {
        }

    }
//    

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
    private void onClickBtn_generateBill(ActionEvent event) throws SQLException {
        //get last billing month
        YearMonth lastBillYM;
        try {
            ResultSet lbm = dbcon.queryToDB("select entryDate from Billings where sl = (select max(sl) from Billings)");
            lbm.next();
            String lastBill = lbm.getString("entryDate");
            lastBillYM = YearMonth.from(LocalDate.parse(lastBill));
        } catch (Exception e) {
            lastBillYM = YearMonth.from(LocalDate.now()).minusMonths(2);
        }

        //=====
        YearMonth currentYM = YearMonth.from(LocalDate.now()).minusMonths(1);

        while (lastBillYM.isBefore(currentYM)) {
            //next month of last bill
            lastBillYM = lastBillYM.plusMonths(1);

            //now bill
            //get flat count***********************************
            ResultSet flatct = dbcon.queryToDB("select count(*) as ct from Flats");
            flatct.next();

            //calc charge
            double totalService = calcTotalServiceCost();
            totalService /= flatct.getInt("ct");
            double totalOther = calcOther(lastBillYM.getYear() + "", lastBillYM.getMonth().getDisplayName(TextStyle.SHORT, Locale.US));
            totalOther /= flatct.getInt("ct");
            //******************************************************************************
            ResultSet billrs = dbcon.queryToDB("select  count(*) qty, name, phone from _ownerXflat  group by phone, name");
            while (billrs.next()) {
                //for each owner

                //SET QRY 
                ownername = billrs.getString("name");
                ownerphone = billrs.getString("phone");
                flatqty = billrs.getInt("qty");

                deadline = "" + LocalDate.now().plusDays(7);
                double total = totalService * flatqty;
                total += totalOther * flatqty;

                //insert into Billings values(GETDATE(), '2022-9-9', 65, 'status_', '01756060071', 'shabbir')
                String qry = "insert into Billings values(getdate(), '" + deadline + "', " + total + ", 'pending', '" + ownerphone + "', '" + ownername + "')";
                //***************************************************

                //NOW INSERT AND CHECK SUCCESS ******************************************
                if (dbcon.insertDataToDB(qry)) {
                    showAlert(true, "bill to " + ownername + " success");
                    //then create doc

                    //=====================create text file
                    ResultSet rss = dbcon.queryToDB("select billID from Billings where sl = (select max(sl) from Billings )");
                    rss.next();

                    invname = rss.getString("billID");
                    String inf = "Billing for [ " + lastBillYM + " ]\n\n";
                    inf += String.format(" %20s  : %15s", "Customer Name", ownername);
                    inf += "\n";
                    inf += String.format(" %20s  : %15s", "Customer Phone", ownerphone);
                    inf += "\n";
                    inf += String.format(" %20s  : %15s", "Total Flat Qty", "" + flatqty);
                    boolean invoiceCreated = DocumentCreator.createInvoice(inf, deadline, flatqty, totalService, totalOther, invname + ".txt");
                } else {
                    showAlert(false, "bill to " + ownername + " failed");
                }

            }
            //**************************************************************************

        }
        showAlert(true, "all billings are up to date");

        //*************************************************

    }

    private double calcOther(String yyyy, String mmm) throws SQLException {
        //select sum(trxAmount) from Transactions where trxtype = 'pay' and Datepart( yyyy, entryTimeStamp) = '2022' and datename(month, entryTimeStamp) like '%Aug%'
        ResultSet s = dbcon.queryToDB("select sum(trxAmount) from Transactions where trxtype = 'pay' "
                + "and Datepart( yyyy, entryTimeStamp) = " + yyyy + " and datename(month, entryTimeStamp) like '%" + mmm + "%'");
        s.next();

        return s.getDouble(1);

    }

    private double calcTotalServiceCost() throws SQLException {
        double totalScost = 0;
        ResultSet remp = dbcon.queryToDB("select sum(salary) as tt from Employees where status_ = 'present'");
        remp.next();
        totalScost = remp.getDouble("tt");
        return totalScost;
    }

    //    private void onClickBtn_createpdf(ActionEvent event) {
//        try {
//            ObservableList<Node> childrens = gp.getChildren();
////        for(Label child : childrens) {
////            System.out.println();
////        }
//
////================now insert into db billings
//            deadline = (LocalDate) dp_deadline.getValue() + "";
//            //insert into Billings values(GETDATE(), '2022-9-9', 65, 'status_', '01756060071', 'shabbir')
//            String qry = "insert into Billings values(getdate(), '" + deadline + "', " + sumtotal + ", 'pending', '" + ownerphone + "', '" + ownername + "')";
//            boolean dbin = dbcon.insertDataToDB(qry);
//
//            //=====================create text file
//            ResultSet rss = dbcon.queryToDB("select billID from Billings where sl = (select max(sl) from Billings )");
//            rss.next();
//            
//            invname = rss.getString("billID");
//            
//            boolean invoiceCreated = false;
//            //invoiceCreated = DocumentCreator.createInvoice(deadline, sumtotal, childrens, invname + ".txt");
//
////==========================================
//            if (invoiceCreated) {
//                //success
//                showAlert(true, "invoice created");
//                
//            } else {
//                
//                showAlert(false, "Invoice could not be created! >>");
//            }
//            
//        } catch (Exception e) {
//            showAlert(false, "Invoice could not be created! ex");
//        }
//        
//    }
//    private void onClickBtn_addToInvoice(ActionEvent event) {
//        gp.add(new Label(tf_amount.getText()), 1, row);
//        gp.add(new Label(tf_service.getText()), 0, row++);
//
//        //sum
//        sumtotal += Double.valueOf(tf_amount.getText());
//        label_totalbill.setText(String.valueOf(sumtotal));
//        
//        tf_amount.clear();
//        tf_service.clear();
////        btn_addToInvoice.setDisable(true);
////
////        dp_deadline.setDisable(false);
//
//    }
//    private void onClickBtn_owner(ActionEvent event) {
//
//        //choose owner fxml
//        row = 0;
//        gp.getChildren().clear();
//        sumtotal = 0;
//        label_totalbill.setText("" + sumtotal);
////        btn_createpdf.setDisable(true);
//
//        Parent root;
//        try {
//            root = FXMLLoader.load(getClass().getResource("ChooseOwner.fxml"));
//            
//            Scene scr = new Scene(root);
//            Stage window = new Stage();
//            window.setTitle("Choose Owner");
//            window.setScene(scr);
//            window.showAndWait();
//            
//            System.out.println("good");
//            
//            System.out.println("i am good so far");
//            //get owner/
//            ownername = Bundle.selected.toString().split(", ")[0];
//            ownername = ownername.substring(1, ownername.length());
//            ownerphone = Bundle.selected.toString().split(", ")[1];
//            owneremail = Bundle.selected.toString().split(", ")[2];
//            ownerid = Bundle.selected.toString().split(", ")[3];
//            ownerid = ownerid.substring(0, ownerid.length() - 1);
//            btn_owner.setText("Billing for Mr " + ownername);
//            //set owner on invoice
//            row = 3;
//            gp.add(new Label("name: " + ownername), 0, row);
//            gp.add(new Label("phone: " + ownerphone), 0, ++row);
//
//            //gp.add(new Label("==================================="), 1, row);
//            row++;
////            tf_amount.setDisable(false);
////            tf_service.setDisable(false);
//
//            ResultSet rset = dbcon.queryToDB("select memberSince from Owners where phone = '" + ownerphone + "' and name = '" + ownername + "'");
//            rset.next();
//            
//            String since = rset.getString("memberSince");
//            
//            try {
//                rset = dbcon.queryToDB("select max(entryDate) as latest from Billings where phone='" + ownerphone + "' and name = '" + ownername + "'");
//                rset.next();
//                
//                String latest = rset.getString("latest");
//                restrictDatePicker(LocalDate.parse(latest.split(" ")[0]), LocalDate.now(), dp_billFrom);
//                
//            } catch (Exception e) {
//                
//                restrictDatePicker(LocalDate.parse(since.split(" ")[0]), LocalDate.now(), dp_billFrom);
//                
//            }
//            
//            dp_billFrom.setDisable(false);
//            dp_billTo.setValue(LocalDate.now());
//            
//            calcCostPerHead();
//            
//        } catch (Exception ex) {
//            showAlert(false, "" + ex);
////            tf_amount.setDisable(!false);
////            tf_service.setDisable(!false);
//
//        }
//    }
    //================================================
//    private void onClickDp_billFrom(ActionEvent event) {
////        btn_createpdf.setDisable(!(!(sumtotal == 0) && !(dp_billFrom.getValue() == null)));
////        dp_billTo.setDisable(dp_billFrom.getValue() == null);
////        dp_deadline.setDisable(dp_billFrom.getValue() == null);
////        dp_deadline.setDisable(dp_billTo.getValue() == null);
//
//        if (!dp_billTo.isDisable()) {
//            restrictDatePicker(dp_billFrom.getValue(), LocalDate.now(), dp_billTo);
//        }
//    }
    
    
    
    
//    public void restrictDatePicker(LocalDate minDate, LocalDate maxDate, DatePicker dp) {
////        minDate = LocalDate.of(1989, 4, 16); //get joining since date 
////        maxDate = LocalDate.now();
//
//        dp.setDayCellFactory(d
//                -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
//            }
//        });
//        
//    }
//    void calcCostPerHead() {
//        sumtotal = 0;
//        //add label to grid
//    }
}
