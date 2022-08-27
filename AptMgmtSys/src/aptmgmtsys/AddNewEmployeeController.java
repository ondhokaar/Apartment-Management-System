/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class AddNewEmployeeController implements Initializable {

    @FXML
    private Button btn_cancel;
    @FXML
    private Label label_docs;
    @FXML
    private Button btn_proceed;
    @FXML
    private Button btn_addDocs;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_mail;
    @FXML
    private TextField tf_designation;
    @FXML
    private TextField tf_salary;
    private boolean docAdded;
    private DBConnect dbcon;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        docAdded = false;
        dbcon = new DBConnect();
        try {
            dbcon.connectToDB();
            
        } catch (Exception e) {
        }
    }

    @FXML
    private void onClickBtn_cancel(ActionEvent event) {

        try {
            Parent emp = FXMLLoader.load(getClass().getResource("Employee.fxml"));
            Scene scr = new Scene(emp);
            Stage window = (Stage) btn_cancel.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Employee");
            window.setScene(scr);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(AddNewEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_proceed(ActionEvent event) {

        try {

            if (tf_designation.getText().length() > 0 && tf_name.getText().length() > 0 && tf_phone.getText().length() > 0 && Double.valueOf(tf_salary.getText()) > 0 && docAdded) {

                String insertQry = "insert into Employees values('"
                        + tf_name.getText()
                        + "', '" + tf_phone.getText()
                        + "', '" + tf_mail.getText()
                        + "', getdate(), '"
                        + tf_designation.getText()
                        + "', " + tf_salary.getText()
                        + ", 'present', null, '"
                        + label_docs.getText()
                        + "')";
                boolean st = dbcon.insertDataToDB(insertQry);

                if (st) {
                    //success
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("success");
                    alert.setHeaderText("New Employee appointed");
                    alert.setContentText("");
                    alert.showAndWait();

                    //take back to Employees
                    Parent back;
                    try {
                        back = FXMLLoader.load(getClass().getResource("Employee.fxml"));
                        Scene scr = new Scene(back);
                        Stage window = (Stage) btn_proceed.getScene().getWindow();
                        window.setTitle("Employees");
                        window.setScene(scr);
                        window.show();
                    } catch (IOException ex) {
                        Logger.getLogger(SellFlatController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    //false
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("FAILED");
                    alert.setHeaderText("could not insert");
                    alert.setContentText("---");
                    alert.showAndWait();
                }



            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setHeaderText("INCORRECT INPUT OR EMPTY FIELD");
                alert.setContentText("---");
                alert.showAndWait();

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    private void onClickBtn_addDocs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Resource Files of This Flat");

        File FlatDocs = fileChooser.showOpenDialog((Stage) (btn_addDocs.getScene().getWindow()));

        if (FlatDocs != null) {
            docAdded = true;
            label_docs.setText(FlatDocs.toString());
        } else {
            docAdded = false;
            label_docs.setText("<no file chosen>");
        }
    }

}
