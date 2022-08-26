/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DocumentCreator;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        row = 0;
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
        String invname = "";

        boolean invoiceCreated = false;
        invoiceCreated = DocumentCreator.createInvoice(childrens, invname + ".txt");

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
            alert.setContentText("---------------");
            alert.showAndWait();
        }

    }

    @FXML
    private void onClickBtn_addToInvoice(ActionEvent event) {
        gp.add(new Label(tf_amount.getText()), 1, row);
        gp.add(new Label(tf_service.getText()), 0, row++);
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
            Stage window = (Stage) btn_owner.getScene().getWindow();
            window.setTitle("Choose Owner");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //get owner
        String ownername = Bundle.selected.toString().split(", ")[0];
        String ownerphone = Bundle.selected.toString().split(", ")[1];
        String owneremail = Bundle.selected.toString().split(", ")[2];
        String ownerid = Bundle.selected.toString().split(", ")[3];

        //set owner on invoice
        gp.add(new Label("name: " + ownername), 0, row);
        gp.add(new Label("phone: " + ownerphone), 0, ++row);
        
        
        gp.add(new Label("============================================\n\n"), 0, ++row);
        row++;
    }

}
