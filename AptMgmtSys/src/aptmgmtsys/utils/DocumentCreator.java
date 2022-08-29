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

    public static boolean createInvoice(String info, String deadline, int qty, double service, double other, String invoiceName) {

        PrintWriter fw = null;

        try {

            fw = new PrintWriter("bills/" + invoiceName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(info);
            bw.newLine();
            
//****************************************************************************************
//            String service = "", amount = "";
//            int i = 0;
//            for (Node child : children) {
//                i++;
//                if(i == 3) {
//                    bw.write("--------------------------------------------------");
//                    bw.newLine();
//                }
//                if(i%2 == 1) {
//                    amount = (((Label) child).getText());
//                    continue;
//                }
//                bw.write((((Label) child).getText()) + "\t\t\t\t" + amount);
//                //bw.write((((Label) child).getText()) + " - " + i++);
//                bw.newLine();
//            }
//*****************************************************************************************
            bw.write("\n************************************************************\n");
            bw.write(String.format(" %30s  : %10s", "Monthly Facility charge", ""+service));
            //bw.write("Monthly Facility charge : \t" + service);
            bw.newLine();
            bw.write(String.format(" %30s  : %10s", "Other added charge", ""+other));
            //bw.write("Other added charge : \t" + other);
            bw.write("\n--------------------------------------------------------------------------\n");
            double sumtotal = service + other;
            bw.write(String.format(" %30s  : %10s", "Total Amount Payable", ""+service));
            //bw.write("\nTotal Amount Payable :     [ " + sumtotal + " BDT ]");
            
            
            bw.write("\nPay on or before : " + deadline);
            bw.write("\ncreated on : " +     new java.util.Date() );
            
            
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
