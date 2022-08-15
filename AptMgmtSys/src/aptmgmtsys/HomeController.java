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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class HomeController implements Initializable {

    @FXML
    private Button btn_billing;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onClickBtn_billing(ActionEvent event) {
        try {
//            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Billing.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("My New Stage Title");
//            stage.setScene(new Scene(root));
//            stage.show();
//            // Hide this current window (if this is what you want)
//            ((Node) (event.getSource())).getScene().getWindow().hide();

            //====
            Parent root = FXMLLoader.load(getClass().getResource("Billing.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_billing.getScene().getWindow();
            window.setTitle("Billing");
            window.setScene(scr);
            window.show();



        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
