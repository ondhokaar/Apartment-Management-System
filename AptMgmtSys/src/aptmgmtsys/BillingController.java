/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.DocumentCreator;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.naming.Binding;
import javax.print.Doc;

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
    @FXML
    private TextField tf_service;
    @FXML
    private GridPane gp;
    private int row;
    @FXML
    private TextField tf_amount;
    @FXML
    private Button btn_createpdf;
    @FXML
    private Button btn_addToInvoice;
    @FXML
    private Button btn_owner;
    @FXML
    private ScrollPane sp;
    private double sumtotal;
    @FXML
    private Label label_totalbill;
    @FXML
    private DatePicker dp_deadline;
    private DBConnect dbcon;
    private String invname;
    private String deadline;
    private String ownername;
    private String ownerphone;
    private String owneremail;
    private String ownerid;
    @FXML
    private DatePicker dp_billFrom;
    @FXML
    private DatePicker dp_billTo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbcon = new DBConnect();

        btn_addToInvoice.setDisable(true);
        btn_createpdf.setDisable(true);
        restrictDatePicker(LocalDate.of(1989, 4, 16), LocalDate.now(), dp_billFrom);

        restrictDatePicker(LocalDate.now(), LocalDate.MAX, dp_deadline);

        try {
            // TODO

            dbcon.connectToDB();
        } catch (Exception ex) {
        }
        //AnchorPane ap = new AnchorPane();
        // ap.resize(ap.getWidth(), ap.get);
        row = 0;
        sumtotal = 0;

//        RowConstraints fixedRow = new RowConstraints(25);
//        fixedRow.setVgrow(Priority.NEVER);
//
//        RowConstraints growingRow = new RowConstraints();
//        growingRow.setVgrow(Priority.ALWAYS);
//
//        ColumnConstraints column = new ColumnConstraints();
//        column.setPercentWidth(100);
//        
//        gp.getRowConstraints().addAll(fixedRow);
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

    @FXML
    private void onClickBtn_createpdf(ActionEvent event) {
        try {
            ObservableList<Node> childrens = gp.getChildren();
//        for(Label child : childrens) {
//            System.out.println();
//        }

//================now insert into db billings
            deadline = (LocalDate) dp_deadline.getValue() + "";
            //insert into Billings values(GETDATE(), '2022-9-9', 65, 'status_', '01756060071', 'shabbir')
            String qry = "insert into Billings values(getdate(), '" + deadline + "', " + sumtotal + ", 'pending', '" + ownerphone + "', '" + ownername + "')";
            boolean dbin = dbcon.insertDataToDB(qry);

            //=====================create text file
            ResultSet rss = dbcon.queryToDB("select billID from Billings where sl = (select max(sl) from Billings )");
            rss.next();

            invname = rss.getString("billID");

            boolean invoiceCreated = false;
            invoiceCreated = DocumentCreator.createInvoice(deadline, sumtotal, childrens, invname + ".txt");

//==========================================
            if (invoiceCreated) {
                //success
                showAlert(true, "invoice created");

            } else {

                showAlert(false, "Invoice could not be created! >>");
            }

        } catch (Exception e) {
            showAlert(false, "Invoice could not be created! ex");
        }

    }

    @FXML
    private void onClickBtn_addToInvoice(ActionEvent event) {
        gp.add(new Label(tf_amount.getText()), 1, row);
        gp.add(new Label(tf_service.getText()), 0, row++);

        //sum
        sumtotal += Double.valueOf(tf_amount.getText());
        label_totalbill.setText(String.valueOf(sumtotal));

        tf_amount.clear();
        tf_service.clear();
        btn_addToInvoice.setDisable(true);

        dp_deadline.setDisable(false);

    }

    @FXML
    private void onClickBtn_owner(ActionEvent event) {

        //choose owner fxml
        row = 0;
        gp.getChildren().clear();
        sumtotal = 0;
        label_totalbill.setText("" + sumtotal);
        btn_createpdf.setDisable(true);

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ChooseOwner.fxml"));

            Scene scr = new Scene(root);
            Stage window = new Stage();
            window.setTitle("Choose Owner");
            window.setScene(scr);
            window.showAndWait();

            System.out.println("good");

            System.out.println("i am good so far");
            //get owner/
            ownername = Bundle.selected.toString().split(", ")[0];
            ownername = ownername.substring(1, ownername.length());
            ownerphone = Bundle.selected.toString().split(", ")[1];
            owneremail = Bundle.selected.toString().split(", ")[2];
            ownerid = Bundle.selected.toString().split(", ")[3];
            ownerid = ownerid.substring(0, ownerid.length() - 1);
            btn_owner.setText("Billing for Mr " + ownername);
            //set owner on invoice
            row = 3;
            gp.add(new Label("name: " + ownername), 0, row);
            gp.add(new Label("phone: " + ownerphone), 0, ++row);

            //gp.add(new Label("==================================="), 1, row);
            row++;
            tf_amount.setDisable(false);
            tf_service.setDisable(false);

        } catch (Exception ex) {
            showAlert(false, "error during selecting owner");
            tf_amount.setDisable(!false);
            tf_service.setDisable(!false);

        }
    }

    //================================================
    @FXML
    private void onClickDp_billFrom(ActionEvent event) {
        btn_createpdf.setDisable(!(!(sumtotal == 0) && !(dp_billFrom.getValue() == null)));
        dp_billTo.setDisable(dp_billFrom.getValue() == null);
        dp_deadline.setDisable(dp_billFrom.getValue() == null);
        dp_deadline.setDisable(dp_billTo.getValue() == null);

        if (!dp_billTo.isDisable()) {
            restrictDatePicker(dp_billFrom.getValue(), LocalDate.now(), dp_billTo);
        }
    }

    @FXML
    private void onClickDp_billTo(ActionEvent event) {

        btn_createpdf.setDisable(!(!(sumtotal == 0) && !(dp_billTo.getValue() == null)));
        btn_owner.setDisable(dp_billTo.getValue() == null);
        dp_deadline.setDisable(dp_billTo.getValue() == null);

    }

    @FXML
    private void onClickDp_deadline(ActionEvent event) {

        btn_createpdf.setDisable(dp_deadline.getValue() == null);
    }

    @FXML
    private void OKR_tfAdditionals(KeyEvent event) {

        try {
            btn_addToInvoice.setDisable(!(!(tf_service.getText().trim().length() == 0) && !(Double.valueOf(tf_amount.getText()) == 0)));
        } catch (Exception exception) {
            btn_addToInvoice.setDisable(true);
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

    public void restrictDatePicker(LocalDate minDate, LocalDate maxDate, DatePicker dp) {
//        minDate = LocalDate.of(1989, 4, 16); //get joining since date 
//        maxDate = LocalDate.now();

        dp.setDayCellFactory(d
                -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
            }
        });

    }

}
