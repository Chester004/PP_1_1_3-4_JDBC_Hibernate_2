package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.Properties;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Session getHib() {
        Properties prop = new Properties();
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/mydbtest");
        prop.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");
        prop.setProperty("hibernate.connection.username", "root");
        prop.setProperty("hibernate.connection.password", "root");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        prop.setProperty("show_sql", "true");

        SessionFactory factory = new Configuration()
                .addProperties(prop)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        return factory.openSession();
    }
}
