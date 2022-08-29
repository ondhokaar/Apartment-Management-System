/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.DocumentCreator;
import aptmgmtsys.utils.TableLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
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
    @FXML
    private Button btn_AutoPay;
    private String name;
    private String phone;
    private String desig;
    private String deadline;
    private double availableAmount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dbcon = new DBConnect();

        try {
            dbcon.connectToDB();

            TableLoader.loadTable("select name, phone, empID, designation from Employees where status_ = 'present'", tv_employee);

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
        try {
            Object s = tv_employee.getSelectionModel().getSelectedItems().get(0);
            String empID = s.toString().split(", ")[2];

            boolean b = dbcon.insertDataToDB("update Employees set status_ = 'former' where empID = '" + empID + "'");
            showAlert(b, "done");
            TableLoader.loadTable("select name, phone, empID, designation from Employees where status_ = 'present'", tv_employee);

        } catch (Exception ex) {
            showAlert(false, "could not dismiss");
        }

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

            TableLoader.loadTable("select name, phone, empID, designation from Employees where status_ = 'present'", tv_employee);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_refreshF(ActionEvent event) {

        try {

            TableLoader.loadTable("select name, phone, empID, designation  from Employees", tv_employee);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
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

    private double calcTotalServiceCost() throws SQLException {
        double totalScost = 0;
        ResultSet remp = dbcon.queryToDB("select sum(salary) as tt from Employees where status_ = 'present'");
        remp.next();
        totalScost = remp.getDouble("tt");
        return totalScost;
    }

    @FXML
    private void onClickBtn_AutoPay(ActionEvent event) throws SQLException {

        //get last pay month
        double demand = calcTotalServiceCost();
        if (fundAvailable(demand)) {

            YearMonth lastBillYM;
            try {
                ResultSet lbm = dbcon.queryToDB("select lastEmpPayment as dt from DateTrack where sl = (select max(sl) from DateTrack)");
                lbm.next();
                String lastBill = lbm.getString("dt");
                lastBillYM = YearMonth.from(LocalDate.parse(lastBill)).minusMonths(1);
            } catch (Exception e) {
                lastBillYM = YearMonth.from(LocalDate.now()).minusMonths(2);
            }

            //=====
            YearMonth currentYM = YearMonth.from(LocalDate.now()).minusMonths(1);

            while (lastBillYM.isBefore(currentYM)) {
                //next month of last bill
                dbcon.insertDataToDB("insert into DateTrack values('" + LocalDate.now() + "' )");
                lastBillYM = lastBillYM.plusMonths(1);

                //now bill
                //get flat count***********************************
//            ResultSet flatct = dbcon.queryToDB("select count(*) as ct from Flats");
//            flatct.next();
                //calc charge
//            double totalService = calcTotalServiceCost();
//            totalService /= flatct.getInt("ct");
//            double totalOther = calcOther(lastBillYM.getYear() + "", lastBillYM.getMonth().getDisplayName(TextStyle.SHORT, Locale.US));
//            totalOther /= flatct.getInt("ct");
                //******************************************************************************
                ResultSet billrs = dbcon.queryToDB("select * from Employees where status_ = 'present'");

                while (billrs.next()) {
                    //for each owner

                    //SET QRY 
                    name = billrs.getString("name");
                    phone = billrs.getString("phone");
                    desig = billrs.getString("designation");

                    deadline = "" + LocalDate.now().plusDays(7);
                    double salary = billrs.getDouble("salary");

                    //NOW PAY
                    //insert into Billings values(GETDATE(), '2022-9-9', 65, 'status_', '01756060071', 'shabbir')
                    //String qry = "insert into Transactions values(getdate(), 'pay', " + salary + ", 'pending', '" + ownerphone + "', '" + ownername + "')";
                    //***************************************************
                    //NOW INSERT AND CHECK SUCCESS ******************************************
                    if (pay(salary)) {
                        showAlert(true, "Success payment to " + name + ", salary : " + salary);
                        //then create doc

                        //=====================create text file
                        ResultSet rss = dbcon.queryToDB("select trxID from Transactions where sl = (select max(sl) from Transactions)");
                        rss.next();

                        String invname = rss.getString("trxID");
                        String inf = "Paying Month [ " + lastBillYM + " ]\n\n";
                        inf += String.format(" %20s  : %15s", "Employee Name", name);
                        inf += "\n";
                        inf += String.format(" %20s  : %15s", "Employee Phone", phone);
                        inf += "\n";
                        inf += String.format(" %20s  : %15s", "Salary", "" + salary);
                        boolean invoiceCreated = DocumentCreator.createPaySlip(inf, invname + ".txt");

                    } else {
                        showAlert(false, "payment to " + name + " failed");
                    }

                }
                //**************************************************************************

            }
            showAlert(true, "all payments are up to date");

            //*************************************************
        }
        else {
            showAlert(false, "fund Not available");
        }
    }

    private boolean pay(double amountToPay) {
        boolean succ = false;
        availableAmount = availableAmount - amountToPay;
        try {
            succ = dbcon.insertDataToDB("insert into Transactions values(getdate(), 'pay', " + amountToPay + ", " + availableAmount + ")");
        } catch (Exception e) {
            showAlert(false, "sth went wrong during paying");
        }
        return succ;
    }

    private boolean fundAvailable(double demand) {

        try {
            ResultSet rss = dbcon.queryToDB("select count(*) from Transactions");
            rss.next();
            int totalRow = rss.getInt(1);
            if (totalRow == 0) {
                availableAmount = 0;
                return availableAmount >= demand;
            }

        } catch (Exception e) {
            showAlert(false, "sth went wrong during checking fund availibility");

        }

        try {
            //trx table, get current amount
            ResultSet rs = dbcon.queryToDB("select latestAvailableAmount from Transactions where sl = (select max(sl) from Transactions)");
            rs.next();
            availableAmount = rs.getDouble("latestAvailableAmount");
            return availableAmount >= demand;
        } catch (SQLException ex) {
            showAlert(false, "sth went wrong during checking fund availibility");
        }
        return false;
    }

}
