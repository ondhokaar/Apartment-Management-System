/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private TableView<?> tab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            label_doc.setText(FlatDocs.toString());
        } else {
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

            if (tf_aptNo.getText().length() > 0 && Double.valueOf(tf_area.getText()) > 0 && a && tf_level.getText().length() > 0 && tf_specification.getText().length() > 0) {

                insertQry = "insert into Flats "
                        + "values( GETDATE(), '"
                        + (LocalDate) dp_completionDate.getValue()
                        + "', '" + tf_aptNo.getText().toUpperCase()
                        + "', " + tf_level.getText()
                        + ", '" + tf_specification.getText()
                        + "', " + tf_area.getText()
                        + ", '" + label_doc.getText()
                        + "')";
                dbcon.insertDataToDB(insertQry);
                System.out.println("done");
                TableLoader.loadTable("select * from Flats", tab);

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

}
