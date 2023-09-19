package com.mycompany.hotel_management_system;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Daniel
 */

public class connector {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = (Connection)
                DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hotel_management_system", "root", "ralphonra");
        
        //schema name == hotel_management_system
        //table name == hotel_management_system_main
        
        return connection;
    }
}