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

    public static boolean createInvoice(String deadline, double sumtotal, ObservableList<Node> children, String invoiceName) {

        PrintWriter fw = null;

        try {

            fw = new PrintWriter(invoiceName);
            BufferedWriter bw = new BufferedWriter(fw);
            

            String service = "", amount = "";
            int i = 0;
            for (Node child : children) {
                i++;
                if(i == 3) {
                    bw.write("--------------------------------------------------");
                    bw.newLine();
                }
                if(i%2 == 1) {
                    amount = (((Label) child).getText());
                    continue;
                }
                bw.write((((Label) child).getText()) + "\t\t\t\t" + amount);
                //bw.write((((Label) child).getText()) + " - " + i++);
                bw.newLine();
            }
            bw.write("\n-------------------------------------------------");
            bw.write("\nTotal Amount Payable :     [ " + sumtotal + " BDT ]");
            
            
            bw.write("\nPay on or before : " + deadline);
            
            
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
