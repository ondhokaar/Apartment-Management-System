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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class OwnersController implements Initializable {

    @FXML
    private TableView<?> tv_owner;
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
        try {
            dbcon.connectToDB();
        } catch (Exception e) {
        }
        init();
        Bundle.selected = null;
    }

    public void init() {
        try {
            // TODO
            String qry = "SELECT _ownerXflat.name as NAME, _ownerXflat.phone as PHONE, STUFF( "
                    + "(SELECT ', ' + s.apt_no "
                    + "FROM _ownerXflat s "
                    + "WHERE s.name = _ownerXflat.name AND s.phone = _ownerXflat.phone "
                    + "FOR XML PATH('')),1,1,'') AS FLATS_OWNED "
                    + "FROM _ownerXflat "
                    + "GROUP BY _ownerXflat.name, _ownerXflat.phone";
            TableLoader.loadTable(qry, tv_owner);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OwnersController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(OwnersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_back.getScene().getWindow();
            window.setTitle("Apartment Management System : Home");
            window.setScene(scr);
            window.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
