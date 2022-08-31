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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

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
    private boolean allgood;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //************************************
        final Pattern mailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        final Pattern phonePattern = Pattern.compile("^[0-9]{11}$");
        final Pattern namePattern = Pattern.compile("^[A-Za-z\\s]+$");
        final Pattern numPattern = Pattern.compile("^[0-9]+[.]?[0-9]+$");
        tf_salary.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_salary.getText().matches(numPattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_salary.setText("");
                    tf_salary.setStyle("-fx-border-color:red;");
                } else {
                    tf_salary.setStyle("-fx-border-color:green;");
                }
            }

        });
        tf_name.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = namePattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));
        tf_designation.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = namePattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));

        tf_mail.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = mailPattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));

        //*********************
        tf_mail.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_mail.getText().matches(mailPattern.toString())) {

                    tf_mail.setText("");
                    tf_mail.setStyle("-fx-border-color:red;");
                } else {
                    tf_mail.setStyle("-fx-border-color:green;");
                }
            }

        });
        tf_name.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_name.getText().matches(namePattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_name.setText("");
                    tf_name.setStyle("-fx-border-color:red;");
                } else {
                    tf_name.setStyle("-fx-border-color:green;");
                }
            }

        });
                tf_designation.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_designation.getText().matches(namePattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_designation.setText("");
                    tf_designation.setStyle("-fx-border-color:red;");
                } else {
                    tf_designation.setStyle("-fx-border-color:green;");
                }
            }

        });

        tf_phone.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_phone.getText().matches(phonePattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_phone.setText("");
                    tf_phone.setStyle("-fx-border-color:red;");
                } else {
                    tf_phone.setStyle("-fx-border-color:green;");

                }
            }

        });

        //***********************************
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
            if (tf_name.getText().isEmpty() || tf_phone.getText().isEmpty() || tf_mail.getText().isEmpty() || tf_designation.getText().isEmpty() || tf_salary.getText().isEmpty() || !docAdded) {
                throw new Exception();
            }
            if (Double.valueOf(tf_salary.getText()) > 0) {

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
                    } catch (Exception ex) {
                    }

                } else {
                    //false
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("FAILED");
                    alert.setHeaderText("could not insert");
                    alert.showAndWait();
                }

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setHeaderText("INCORRECT INPUT OR EMPTY FIELD");
                alert.setContentText("Did you input a valid salary?");
                alert.showAndWait();

            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED");
            alert.setHeaderText("INCORRECT INPUT OR EMPTY FIELD");
            alert.showAndWait();
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
