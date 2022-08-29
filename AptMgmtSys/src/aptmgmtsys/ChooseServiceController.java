/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package aptmgmtsys;

import aptmgmtsys.utils.Bundle;
import aptmgmtsys.utils.DBConnect;
import aptmgmtsys.utils.TableLoader;
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
import javafx.scene.Node;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author shabbir
 */
public class ChooseServiceController implements Initializable {

    @FXML
    private Pane pane_addnew;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_details;
    @FXML
    private Button btn_add;
    @FXML
    private Button btn_showExisting;
    private Label label_dup;
    private DBConnect dbcon;
    @FXML
    private Pane pane_existing;
    @FXML
    private Button btn_gotoAddNew;

    private String dupQry;

    @FXML
    private Label label_dupphone;
    @FXML
    private Label label_dupmail;
    private boolean unique;
    private boolean uniquephone;
    private boolean uniquemail;
    @FXML
    private Button btn_select;
    @FXML
    private TableView<?> tv_emp;
    @FXML
    private MenuButton mbtn_search;
    @FXML
    private Button btn_back;
    @FXML
    private MenuItem mi_name;
    @FXML
    private MenuItem mi_phone;
    private String qry;
    private String dynQry;
    @FXML
    private TextField tf_search;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        final Pattern mailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        final Pattern phonePattern = Pattern.compile("^\\\\d{10}$");
        final Pattern namePattern = Pattern.compile("^[A-Za-z\\s]+$");

        tf_name.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = namePattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));

        tf_email.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", change -> {
            final Matcher matcher = mailPattern.matcher(change.getControlNewText());
            return (matcher.matches() || matcher.hitEnd()) ? change : null;
        }));
        tf_phone.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 1, change -> {
            final Matcher matcher = phonePattern.matcher(change.getControlNewText());
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

        //==============
        dbcon = new DBConnect();
        Bundle.selected = null;
        try {
            dbcon.connectToDB();

        } catch (Exception e) {
        }
        try {
            TableLoader.loadTable("select * from ServiceProviders", tv_emp);
        } catch (Exception ex) {
            showAlert(false, "sth went wrong during loading existing service");
        }
        dupQry = "select count(*) from ServiceProviders where ";
        qry = "select * from ServiceProviders where ";
        dynQry = qry + " phone like '%";
    }

    @FXML
    private void onClickBtn_add(ActionEvent event) {
        try {
            //take inputs, run query
            String name = tf_name.getText(), email = tf_email.getText(), phone = tf_phone.getText(), details = tf_details.getText();
            if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || details.isEmpty()) throw new Exception();
            String qry = "insert into ServiceProviders values('"
                    + details
                    + "', '" + phone
                    + "', '" + name
                    + "', '" + email
                    + "', getdate())";

            boolean ins = dbcon.insertDataToDB(qry);
            if (ins) {
                showAlert(ins, "success");
            } else {
                showAlert(!ins, "failed");
            }

            //now get data, open payment page
            //get data
            Bundle.existing = false;
            ResultSet rss = dbcon.queryToDB("select * from ServiceProviders where sl = (select max(sl) from ServiceProviders)");
            rss.next();

            Bundle.rs = rss;

            //open payment page
            ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();

        } catch (Exception e) {
            showAlert(false, "Please let me know everything :(");
        }
    }

    private void showAlert(boolean success, String msg) {
        Alert alert;
        if (success) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED");
        }

        alert.setHeaderText(msg);
        alert.setContentText("");
        alert.showAndWait();
    }

    @FXML
    private void onClickBtn_gotoAddNew(ActionEvent event) {
        pane_addnew.setVisible(!false);
        pane_addnew.setDisable(!true);

        pane_existing.setVisible(false);
        pane_existing.setDisable(true);
    }

    @FXML
    private void onClickBtn_showExisting(ActionEvent event) {
        pane_addnew.setVisible(false);
        pane_addnew.setDisable(true);

        pane_existing.setVisible(!false);
        pane_existing.setDisable(!true);

        try {
            TableLoader.loadTable("select name, phone, spID from ServiceProviders", tv_emp);
        } catch (Exception ex) {
            showAlert(false, "sth went wrong during loading existing service");
        }

    }

    @FXML
    private void OKR_phone(KeyEvent event) {
        String phone = tf_phone.getText().toString().toLowerCase();

        if (phone != "") {

            try {
                ResultSet rs = dbcon.queryToDB(dupQry + " phone like '%" + phone.trim() + "%'");
                //System.out.println(rs.absolute(1));
                rs.next();
                if (rs.getInt(1) == 1) {
                    uniquephone = false;
                    label_dupphone.setText("duplicate");
                } else {
                    label_dupphone.setText("");
                    uniquephone = true;

                }

            } catch (Exception ex) {
                showAlert(false, "sth went wrong during validating mail");
            }

        }
    }

    @FXML
    private void OKR_mail(KeyEvent event) {
        String mail = tf_email.getText().toString().toLowerCase();

        if (mail != "") {

            try {
                ResultSet rs = dbcon.queryToDB(dupQry + " email like '%" + mail.toLowerCase().trim() + "%'");
                //System.out.println(rs.absolute(1));
                rs.next();
                if (rs.getInt(1) == 1) {
                    uniquemail = false;
                    label_dupmail.setText("duplicate");
                } else {
                    label_dupmail.setText("");
                    uniquemail = true;

                }

            } catch (Exception ex) {
                showAlert(false, "sth went wrong during validating mail");
            }

        }

    }

    @FXML
    private void onClickBtn_select(ActionEvent event) {
        Bundle.selected = (Object) tv_emp.getSelectionModel().getSelectedItems().get(0);
        Bundle.existing = true;
        ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void OMC(MouseEvent event) {
        btn_select.setDisable(tv_emp.getSelectionModel().getSelectedItems().get(0) == null);
    }

    @FXML
    private void onClickBtn_back(ActionEvent event) {
        try {

            ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();

        } catch (Exception ex) {

        }
    }

    @FXML
    private void OKR_search(KeyEvent event) {
        String search_ = tf_search.getText();
        String dynamic;
        if (search_ != "") {
            try {
                dynamic = dynQry + "" + search_ + "%'";
                TableLoader.loadTable(dynamic, tv_emp);
            } catch (Exception ex) {
                showAlert(false, "error during search");
            }
        }
    }

    @FXML
    private void onActionMi_name(ActionEvent event) {
        dynQry = qry + " name like '%";

    }

    @FXML
    private void onActionMi_phone(ActionEvent event) {
        dynQry = qry + " phone like '%";
    }

}
