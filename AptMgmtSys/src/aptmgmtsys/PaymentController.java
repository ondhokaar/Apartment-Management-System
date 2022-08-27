/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    private MenuItem mitem_newService;
    @FXML
    private MenuItem mitem_newService1;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
        //1. show employee table
        
    }

    @FXML
    private void onClickMitem_oldService(ActionEvent event) {
        //==============================OLD SERVICE
    }

    @FXML
    private void onClickMitem_newService(ActionEvent event) {
        //==============================NEW SERVICE

    }

    private boolean pay() {
        return false;
    }

    private boolean fundAvailable() {
        return false;
    }

    private void showAlert(boolean success, String msg) {
        Alert alert;
        if(success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED");
        }
        
        alert.setHeaderText(msg);
        alert.setContentText("---");
        alert.showAndWait();
    }

    @FXML
    private void onClickBtn_addToInvoice(ActionEvent event) {
    }
}
