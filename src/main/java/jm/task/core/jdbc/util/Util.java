package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    public static Connection getConnection(){
        try{
           Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
//            if(!connection.isClosed()){
//                System.out.println("Соединение успешно");
//            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
