/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aptmgmtsys.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 *
 * @author shabbir
 */
public class DocumentCreator {

    public static boolean createInvoice(ObservableList<Node> children, String invoiceName) {

        PrintWriter fw = null;

        try {

            fw = new PrintWriter(invoiceName);
            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(tfUsername.getText());
//            bw.newLine();
//            bw.write(tfPassword.getText());

            String service = "", amount = "";
            int i = 0;
            for (Node child : children) {
                i++;
                if(i%2 == 1) {
                    amount = (((Label) child).getText());
                    continue;
                }
                bw.write((((Label) child).getText()) + "\t\t\t\t" + amount);
                bw.newLine();
            }
            bw.close();
            fw.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            fw.close();
            return false;
        }

    }
}
