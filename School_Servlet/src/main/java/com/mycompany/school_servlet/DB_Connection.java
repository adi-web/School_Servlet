/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.school_servlet;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author admirportatile
 */
public class DB_Connection {
    
    
    
        public Connection getConnection() {
        try {
            String dbDriver = "com.mysql.jdbc.Driver"; 
            String dbURL = "jdbc:mysql:// localhost/3306"; 
            // Database name to access 
            String dbName = "fi_itis_meucci"; 
            String dbUsername = "mucaj"; 
            String dbPassword = "mucaj12345678"; 

            Class.forName(dbDriver); 
            Connection con = DriverManager.getConnection(dbURL + dbName + "?serverTimezone=UTC", 
                                                         dbUsername,  
                                                         dbPassword); 
            return con;
        } catch (Exception e) {
             e.printStackTrace();
            return null;
        }
    }
    
}
