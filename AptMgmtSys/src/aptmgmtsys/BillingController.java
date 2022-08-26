/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DocumentCreator;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javax.naming.Binding;

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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

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
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void onClickBtn_billStatus(ActionEvent event) {

    }

    @FXML
    private void onClickBtn_createpdf(ActionEvent event) {

        ObservableList<Node> childrens = gp.getChildren();
//        for(Label child : childrens) {
//            System.out.println();
//        }
        String invname = "billID", deadline = (LocalDate) dp_deadline.getValue() + "";
        
        boolean invoiceCreated = false;
        invoiceCreated = DocumentCreator.createInvoice(deadline, sumtotal, childrens, invname + ".txt");

        if (invoiceCreated) {
            //success
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("success");
            alert.setHeaderText("Invoice created!");
            alert.setContentText("---------------");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("failed");
            alert.setHeaderText("Invoice could not be created!");
            alert.setContentText("");
            alert.showAndWait();
        }

    }

    @FXML
    private void onClickBtn_addToInvoice(ActionEvent event){
        gp.add(new Label(tf_amount.getText()), 1, row);
        gp.add(new Label(tf_service.getText()), 0, row++);
        
        
        
        //sum
        
        sumtotal += Double.valueOf(tf_amount.getText());
        label_totalbill.setText(String.valueOf(sumtotal));
        
        tf_amount.clear();
        tf_service.clear();
        
    }

    @FXML
    private void onClickBtn_owner(ActionEvent event) {

        //choose owner fxml
        row = 0;
        gp.getChildren().clear();

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("ChooseOwner.fxml"));

            Scene scr = new Scene(root);
            Stage window = new Stage();
            window.setTitle("Choose Owner");
            window.setScene(scr);
            window.showAndWait();
            System.out.println("good");

        } catch (IOException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("i am good so far");
        //get owner/
        String ownername = Bundle.selected.toString().split(", ")[0];
        ownername = ownername.substring(1, ownername.length());
        String ownerphone = Bundle.selected.toString().split(", ")[1];
        String owneremail = Bundle.selected.toString().split(", ")[2];
        String ownerid = Bundle.selected.toString().split(", ")[3];
        ownerid = ownerid.substring(0, ownerid.length() - 1);
        btn_owner.setText("Billing for Mr " + ownername);
        //set owner on invoice
        row = 3;
        gp.add(new Label("name: " + ownername), 0, row);
        gp.add(new Label("phone: " + ownerphone), 0, ++row);
        
  
        //gp.add(new Label("==================================="), 1, row);
        row++;
    }
    
    
    
    

}
