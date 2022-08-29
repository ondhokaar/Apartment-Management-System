/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

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
    @FXML
    private Button btn_cancel;
    private boolean allgood;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //+++++++++++++++++++++++
        allgood = false;
        final Pattern mailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        final Pattern phonePattern = Pattern.compile("^[0-9]{11}$");
        final Pattern namePattern = Pattern.compile("^[A-Za-z\\s]+$");

        tf_name.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = namePattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));

        tf_email.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = mailPattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));

        //*********************
        tf_email.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!tf_email.getText().matches(mailPattern.toString())) {
                    //when it not matches the pattern (1.0 - 6.0)
                    //set the textField empty
                    tf_email.setText("");
                    tf_email.setStyle("-fx-border-color:red;");
                } else {
                    tf_email.setStyle("-fx-border-color:green;");
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

        //+++++++++++++
        // TODO
        existing = true;
        newowner = false;
        mbtn_existing.setText("Existing ");
        try {
            tv_owner.setCursor(Cursor.HAND);
            TableLoader.loadTable("select name, phone, status_, email from Owners", tv_owner);
        } catch (Exception ex) {

            System.out.println("nai nai nai");
        }
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

        File FlatDocss = fileChooser.showOpenDialog((Stage) (btn_nid.getScene().getWindow()));
        
        if (FlatDocss != null) {
            allgood = true;
            label_nid.setText(FlatDocss.toString());
        } else {
            allgood = false;
            label_nid.setText("<no file chosen>");
        }
    }

    @FXML
    private void onClickbtn_proceed(ActionEvent event) {

        if (newowner) {
            try {
                //insert into db
                if(tf_name.getText().isEmpty()||tf_phone.getText().isEmpty()||tf_email.getText().isEmpty()||!allgood) throw new Exception();
                
                
                String insertQry = "insert into Owners values( "
                        + " '" + tf_name.getText()
                        + "', '" + tf_phone.getText()
                        + "', '" + tf_email.getText()
                        + "', GETDATE(), "
                        + "'present', null"
                        + ", '" + label_nid.getText()
                        + "')";

                String ownerName = tf_name.getText(); 
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
                try {

                    System.out.println(insertQry);
                    System.out.println(mapqry);
                    st = dbcon.insertDataToDB(insertQry);
                } catch (Exception e) {
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

                    //take back to apartments
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("Apartments.fxml"));
                        Scene scr = new Scene(root);
                        Stage window = (Stage) btn_cancel.getScene().getWindow();
                        window.setTitle("Apartments");
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

            } catch (Exception e) {
                System.out.println(e);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("FAILED");
                alert.setHeaderText("Fill with proper data please");
                alert.showAndWait();

            }
        } else if (existing) {
            //-----------------------------------------------------------------------

            Object s = tv_owner.getSelectionModel().getSelectedItems().get(0);

            System.out.println(s.toString());

            String ownerName = s.toString().split(", ")[0];
            ownerName = ownerName.substring(1, ownerName.length());
            String ownerPhone = s.toString().split(", ")[1];

            System.out.println("owner name, phone: " + ownerName + ", " + ownerPhone);

            String flatDate, fapt_no;
            System.out.println(Bundle.selected.toString());
            flatDate = Bundle.selected.toString().split(", ")[4];
            flatDate = flatDate.substring(0, flatDate.length() - 1);
            fapt_no = Bundle.selected.toString().split(", ")[1];
            //formerOwner
            int formerOwnerFlat = 0;
            String formerOwnerPhone = "";

            ResultSet ors = dbcon.queryToDB("select count(*), phone from _ownerXflat where phone = (select phone from _ownerXflat where apt_no = '" + fapt_no + "') group by phone");

            try {
                ors.next();
                formerOwnerPhone = ors.getString("phone");
                formerOwnerFlat = ors.getInt(1);
            } catch (Exception e) {
                System.out.println("pai nai owner er phone using apt no in mapping, aptcontroller,  " + e);
            }
            //now insert into
            String mapqry = "insert into _ownerXflat(completionDate, apt_no , phone, name) values("
                    + "   '" + flatDate
                    + "', '" + fapt_no
                    + "', '" + ownerPhone
                    + "', '" + ownerName + "')";

            System.out.println("===========================" + mapqry);

            //map update : if map exists -> flatDate & fapt_noo in x table
            //else insert
            try {
                ResultSet rs = dbcon.queryToDB("select * from _ownerXflat where apt_no = '" + fapt_no + "'");
                //System.out.println(rs.absolute(1));
                try {
                    //rs is not null
//                    System.out.println(rs.toString());
//                    rs.next();
//                    //System.out.println(rs);
//                    System.out.println(rs.getString("apt_no")); //correct
//                    System.out.println(rs.toString());
                    //will throw exception if not found mapping

                    //else found  -- update map
//                    String oldaptno = rs.getString("apt_no");
//                    rs.updateString("name", ownerName);
//                    rs.updateString("phone", ownerPhone);
//                    System.out.println(rs.toString());
//                    rs.updateRow();
                    String qry = "delete from _ownerXflat where apt_no = '" + fapt_no + "'";
                    dbcon.insertDataToDB(qry);
                    boolean succ = dbcon.insertDataToDB(mapqry);
                    if (succ) {
                        //owner change success
                        formerOwnerFlat--;
                        if (formerOwnerFlat == 0) {
                            dbcon.insertDataToDB("update Owners set status_ = 'former' where phone = '" + formerOwnerPhone + "'");

                        }
                        //update owner status

                        //update
                        dbcon.insertDataToDB("update Owners set status_ = 'present' where phone = '" + ownerPhone + "'");

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("success");
                        alert.setHeaderText("success owner update");
                        alert.setContentText("---");
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
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("FAILED");
                        alert.setHeaderText("could not sell");

                        alert.showAndWait();
                    }

                } catch (Exception e) {
                    //mapping not found
                    //just insert into map

                    boolean map = dbcon.insertDataToDB(mapqry);
                    System.out.println(map);
                    if (map) {
                        //success
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("success");
                        alert.setHeaderText("success entry");
                        alert.setContentText("---");
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
                        }

                    } else {
                        //false
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("FAILED");
                        alert.setHeaderText("sth went wrong");
                        alert.showAndWait();
                    }
                }

                // TableLoader.loadTable(dupAptQry + aptno + "'", tab);
            } catch (Exception ex) {
                System.out.println("158" + ex);
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

        mbtn_existing.setText("Existing");

        try {
            tv_owner.setCursor(Cursor.HAND);
            TableLoader.loadTable("select name, phone, status_, email from Owners", tv_owner);
        } catch (Exception ex) {

            System.out.println("nai nai nai");
        }

    }

    @FXML
    private void onClickMi_addNew(ActionEvent event) {
        tv_owner.setVisible(!true);
        tv_owner.setDisable(!false);
        pane_addnew.setVisible(true);
        pane_addnew.setDisable(false);

        existing = false;
        newowner = true;
        mbtn_existing.setText("Add New");
    }

    @FXML
    private void onClickBtn_cancel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Apartments.fxml"));
            Scene scr = new Scene(root);
            Stage window = (Stage) btn_cancel.getScene().getWindow();
            window.setTitle("Employee");
            window.setScene(scr);
            window.show();
        } catch (Exception ex) {
        }
    }

}
