/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.FilePaths;
import aptmgmtsys.utils.TableLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import sun.security.action.OpenFileInputStreamAction;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class AddNewFlatController implements Initializable {

    @FXML
    private Button btn_cancel;
    @FXML
    private TextField tf_aptNo;
    @FXML
    private TextField tf_specification;
    @FXML
    private TextField tf_area;
    @FXML
    private TextField tf_level;
    @FXML
    private DatePicker dp_completionDate;
    @FXML
    private Button btn_docs;
    @FXML
    private Label label_doc;
    @FXML
    private Button btn_proceed;
    @FXML
    private Label label_aptNoWarning;

    private boolean a, b, c, d, e, f;
    String insertQry, dupAptQry;
    DBConnect dbcon;
    @FXML
    private Label label_warning;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //***********************************
        final Pattern numPattern = Pattern.compile("^[0-9]+[.]?[0-9]+$");
       final Pattern aptPattern = Pattern.compile("^[a-zA-Z0-9]+$");
        final Pattern levelPattern = Pattern.compile("^[0-9]+$");
        final Pattern specPattern = Pattern.compile("^[A-Za-z0-9.,-]{20,}+$");
        
        
        
        tf_specification.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_specification.getText().matches(aptPattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    label_warning.setText("minimum 20 chars containing a-zA-Z0-9,.-");
                    tf_specification.setText("");
                    tf_specification.setStyle("-fx-border-color:red;");
                } else {
                    tf_specification.setStyle("-fx-border-color:green;");
                    label_warning.setText("");
                }
            }

        });
        
        tf_aptNo.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_aptNo.getText().matches(aptPattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_aptNo.setText("");
                    tf_aptNo.setStyle("-fx-border-color:red;");
                } else {
                    tf_aptNo.setStyle("-fx-border-color:green;");
                }
            }

        });
        tf_level.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_level.getText().matches(levelPattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_level.setText("");
                    tf_level.setStyle("-fx-border-color:red;");
                } else {
                    tf_level.setStyle("-fx-border-color:green;");
                }
            }

        });
        tf_area.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_area.getText().matches(numPattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_area.setText("");
                    tf_area.setStyle("-fx-border-color:red;");
                } else {
                    tf_area.setStyle("-fx-border-color:green;");
                }
            }

        });

        //-----------------------------------
        try {
            // TODO
            dbcon = new DBConnect();
            dbcon.connectToDB();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddNewFlatController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddNewFlatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //query skeleton
        //--add new flat
//        insertQry = "insert into Flats "
//                       + "values( GETDATE(), '"
//                       + dp_completionDate.getValue().toString()
//                       + "', '" + tf_aptNo.getText().toUpperCase()
//                       + "', " + tf_level.getText()
//                       + ", '" + tf_specification.getText()
//                        + "', " + tf_area.getText()
//                        + ", '" + label_doc.getText()
//                        + "')";
        dupAptQry = "select count(*) from Flats where apt_no = '";

        // System.out.println(insertQry);
        System.out.println(dupAptQry);

        a = false;
        b = false;
        c = false;
        d = false;
        e = false;
        f = false;

//============
    }

    @FXML
    private void onClickBtn_cancel(ActionEvent event) {
        try {

            Parent apt = FXMLLoader.load(getClass().getResource("Apartments.fxml"));
            Scene scr = new Scene(apt);
            Stage window = (Stage) btn_cancel.getScene().getWindow();
            window.setTitle("Apartment Mangement System : Home");
            window.setScene(scr);
            window.show();

        } catch (Exception ex) {
            //Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onClickBtn_docs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Resource Files of This Flat");

        File FlatDocs = fileChooser.showOpenDialog((Stage) (btn_docs.getScene().getWindow()));

        if (FlatDocs != null) {
            b=true;
            label_doc.setText(FlatDocs.toString());
        } else {
            b = false;
            label_doc.setText("<no file chosen>");
        }

    }

    @FXML
    private void OKR_aptNo(KeyEvent event) {
        //check duplicate aptNo from database : label_aptNoWarning shows warning
        String aptno = tf_aptNo.getText().toString().toLowerCase();

        if (aptno != "") {

//            System.out.println(dupAptQry + aptno + "'");
//
//            label_aptNoWarning.setText(dupAptQry + aptno.toUpperCase() + "'");
            try {
                ResultSet rs = dbcon.queryToDB(dupAptQry + aptno.toUpperCase() + "'");
                //System.out.println(rs.absolute(1));
                rs.next();
                if (rs.getInt(1) == 1) {
                    a = false;
                    label_aptNoWarning.setText("duplicate apt no");
                } else {
                    label_aptNoWarning.setText("");
                    a = true;

                }

                // TableLoader.loadTable(dupAptQry + aptno + "'", tab);
            } catch (Exception ex) {
                //System.out.println("158" + ex);
            }

        }

    }

    @FXML
    private void onClickBtn_proceed(ActionEvent event) {

        try {
            
            

            if (!tf_aptNo.getText().isEmpty() && !tf_area.getText().isEmpty() && a && b && !tf_level.getText().isEmpty() && !tf_specification.getText().isEmpty()) {

                insertQry = "insert into Flats "
                        + "values( GETDATE(), '"
                        + (LocalDate) dp_completionDate.getValue()
                        + "', '" + tf_aptNo.getText().toUpperCase()
                        + "', " + tf_level.getText()
                        + ", '" + tf_specification.getText()
                        + "', " + tf_area.getText()
                        + ", '" + label_doc.getText()
                        + "')";
                boolean st = dbcon.insertDataToDB(insertQry);

                if (st) {
                    //success
                    //File docs = new File(label_doc.getText());
                    // System.out.println(label_doc.getText().split(Pattern.quote(File.separator)));
                    //docs.renameTo(new File(   FilePaths.FlatDocs + label_doc.getText().split("\\")[1]    )     );

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("New Apartment Has been added");
                    alert.setContentText("This apartment currently has no owner");
                    alert.showAndWait();

                    //take back to apartments
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("Apartments.fxml"));
                        Scene scr = new Scene(root);
                        Stage window = (Stage) btn_cancel.getScene().getWindow();
                        window.setTitle("Apartments");
                        window.setScene(scr);
                        window.show();
                    } catch (Exception ex) {
                        Logger.getLogger(SellFlatController.class.getName()).log(Level.SEVERE, null, ex);
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
                alert.showAndWait();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
