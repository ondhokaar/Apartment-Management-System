/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aptmgmtsys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author shabbir
 */
public class DBConnect {
    private  Statement stmt = null;
    private Connection connection;
    
    public void connectToDB() throws ClassNotFoundException, SQLException{
        System.out.println("DB connecting..............");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;user=sa;password=p@ssword13;" + "databaseName=apt2;";
        connection = DriverManager.getConnection(connectionUrl);
        System.out.println("Connected database successfully.........");
        java.sql.Statement stmt = connection.createStatement();
    }
    
    
    public void disconnectFromDB(){

        try{
           // if (rs != null){rs.close();}
            if (stmt != null){
                stmt.close();
            }
            if (connection != null){
                connection.close();
            }
        }
        catch (Exception ex)
        {
            //JOptionPane.showMessageDialog(null, "Unable to close connection");
        }
    }
    public boolean insertDataToDB(String query) {
        try
        {
            java.sql.Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);
            return true;
        }
        catch (SQLException ex)
        {
            //JOptionPane.showMessageDialog(null, "Unable to Insert Data");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }

    public ResultSet queryToDB(String query) {
        try {
            java.sql.Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
//            rs.next();
//            Reader reader = rs.getCharacterStream(1);
//            return reader;

        } catch (SQLException ex) {
            //JOptionPane.showMessageDialog(null, "Unable to Insert Data");
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
  
}
