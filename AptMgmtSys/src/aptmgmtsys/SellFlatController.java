/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class SellFlatController implements Initializable {

    @FXML
    private MenuButton mbtn_existing;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_email;
    @FXML
    private Label label_nid;
    @FXML
    private Button btn_nid;
    private DBConnect dbcon;
    @FXML
    private Pane pane_addnew;
    @FXML
    private TableView<?> tv_owner;
    @FXML
    private Button btn_proceed;
    @FXML
    private MenuItem mi_existing;
    @FXML
    private MenuItem mi_addNew;

    boolean existing, newowner;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        existing = true;
        newowner = false;
        //select ownerID from Owners
        try {
            dbcon = new DBConnect();
            dbcon.connectToDB();
            pane_addnew.setVisible(!true);
            pane_addnew.setDisable(!false);
            tv_owner.setVisible(true);
            tv_owner.setDisable(false);

            //-----
//            ResultSet rs;
//            
//            rs = dbcon.queryToDB("select ownerID from Owners");
//            
//            while(rs.next()) {
//                
//                mbtn_existing.getItems().add(new MenuItem(rs.getString("ownerID")));
//            }
//            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void onClickBtn_nid(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Resource Files of This Flat");

        File FlatDocs = fileChooser.showOpenDialog((Stage) (btn_nid.getScene().getWindow()));

        if (FlatDocs != null) {
            label_nid.setText(FlatDocs.toString());
        } else {
            label_nid.setText("<no file chosen>");
        }
    }

    @FXML
    private void onClickbtn_proceed(ActionEvent event) {

        if (newowner) {
            try {
                //insert into db

                String insertQry = "insert into Owners values( "
                        + " '" + tf_name.getText()
                        + "', '" + tf_phone.getText()
                        + "', '" + tf_email.getText()
                        + "', GETDATE(), "
                        + "'present', null"
                        + ", '" + label_nid.getText()
                        + "')";

                String ownerName = tf_name.getText(); //got the 3rd column of selected row -> first col = course id
                String ownerPhone = tf_phone.getText();

                System.out.println("owner name, phone: " + ownerName + ", " + ownerPhone);

                String flatDate, fapt_no;
                System.out.println(Bundle.selected.toString().split(", ")[0]);
                flatDate = Bundle.selected.toString().split(", ")[4];
                flatDate = flatDate.substring(0, flatDate.length() - 1);
                fapt_no = Bundle.selected.toString().split(", ")[1];
                
                System.out.println("i will show you the secret now ::::::: " + flatDate + "    :::  " + fapt_no);
                System.out.println(Bundle.selected.toString());
                System.out.println(Bundle.selected.toString().split(", "));
                //now insert into
                String mapqry = "insert into _ownerXflat( completionDate, apt_no, phone, name) values("
                        + "   '" + flatDate
                        + "', '" + fapt_no
                        + "', '" + ownerPhone
                        + "', '" + ownerName + "')";
                boolean st = false;
                try{
                    
                    System.out.println(insertQry);
                    System.out.println(mapqry);
                      st = dbcon.insertDataToDB(insertQry);
                }
                catch(Exception e) {
                    System.out.println(e);
                }
              
//                ResultSet r = dbcon.queryToDB("select ownerID from Owners where sl = (select @@identity from Owners)");
//                r.next();
//                ownerID = String.valueOf(r.getInt("ownerID"));

                boolean map = dbcon.insertDataToDB(mapqry);
                if (st && map) {
                    //success
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("success");
                    alert.setHeaderText("success entry");
                    alert.setContentText("---");
                    alert.showAndWait();

                } else {
                    //false
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("FAILED");
                    alert.setHeaderText("could not insert");
                    alert.setContentText("144");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                System.out.println(e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setHeaderText("could not insert");
                alert.setContentText("154");
                alert.showAndWait();

            }
        } else if (existing) {
            Object s = tv_owner.getSelectionModel().getSelectedItems().get(0);

            System.out.println(s.toString().split(", ")[1].substring(1));

            String ownerName = s.toString().split(", ")[1].substring(3); //got the 3rd column of selected row -> first col = course id
            String ownerPhone = s.toString().split(", ")[1].substring(4);
            System.out.println("owner name, phone: " + ownerName + ", " + ownerPhone);

            String flatDate, fapt_no;
            System.out.println(Bundle.selected.toString().split(", ")[1]);
            flatDate = Bundle.selected.toString().split(", ")[1].substring(4);
            fapt_no = Bundle.selected.toString().split(", ")[1].substring(5);

            //now insert into
            String mapqry = "insert into _ownerXflat(apt_no, name, completionDate, , phone) values("
                    + "   '" + flatDate
                    + "', '" + fapt_no
                    + "', '" + ownerPhone
                    + "', '" + ownerName + "')";
            boolean map = dbcon.insertDataToDB(mapqry);
            if (map) {
                //success
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("success");
                alert.setHeaderText("success entry");
                alert.setContentText("---");
                alert.showAndWait();

            } else {
                //false
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setHeaderText("sth went wrong");
                alert.setContentText("---");
                alert.showAndWait();
            }

        }
    }

    @FXML
    private void onClickMi_existing(ActionEvent event) {
        tv_owner.setVisible(true);
        tv_owner.setDisable(false);
        pane_addnew.setVisible(!true);
        pane_addnew.setDisable(!false);

        existing = true;
        newowner = false;
    }

    @FXML
    private void onClickMi_addNew(ActionEvent event) {
        tv_owner.setVisible(!true);
        tv_owner.setDisable(!false);
        pane_addnew.setVisible(true);
        pane_addnew.setDisable(false);

        existing = false;
        newowner = true;

    }

}
