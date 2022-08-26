/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
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
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class ChooseOwnerController implements Initializable {

    @FXML
    private TableView<?> tv_owner;
    @FXML
    private Button btn_select;

    /**
     * Initializes the controller class.
     */
    public DBConnect dbcon;
    @FXML
    private Button btn_back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbcon = new DBConnect();
        init();
    }

    public void init() {
        try {
            // TODO
            dbcon.connectToDB();

            TableLoader.loadTable("select name, phone, email, ownerID from Owners where status_ = 'present'", tv_owner);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChooseOwnerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChooseOwnerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_select(ActionEvent event) {
        String name = "", phone = "";
        Object s = null;
        try {

            s = tv_owner.getSelectionModel().getSelectedItems().get(0);
            Bundle.selected = s;
            name = s.toString().split(", ")[0];
            phone = s.toString().split(", ")[1];

            //how many flats
            ResultSet rs = dbcon.queryToDB("select count(*) from _ownerXflat where name = '" + name + "' and phone = '" + phone + "'");

            rs.next();
            Bundle.intdata = rs.getInt(1);
            Parent root = FXMLLoader.load(getClass().getResource("Billing.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_select.getScene().getWindow();
            window.setTitle("Billing");
            window.setScene(scr);
            window.show();
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("no owner");
            alert.setHeaderText("nothing selected");
            alert.setContentText("---------------");
            alert.showAndWait();

        }

    }

    @FXML
    private void onClickBtn_back(ActionEvent event) {
        try {

            Parent billing = FXMLLoader.load(getClass().getResource("Billing.fxml"));
            Scene scr = new Scene(billing);
            Stage window = (Stage) btn_back.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Billng");
            window.setScene(scr);
            window.show();

        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
