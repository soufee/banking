package dbutils;

import javax.swing.filechooser.FileSystemView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public static String DB_URL = "jdbc:h2:/c:/temp/Revolut/banking/db/data";
    public static String DB_Driver = "org.h2.Driver";

    static {
        DB_URL = "jdbc:h2:"+System.getProperty("user.dir")+"/db/data.dev";
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); //TODO логирование
        }

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    public static void main(String[] args) {
        try {
            Class.forName(DB_Driver);
            Connection connection = DriverManager.getConnection(DB_URL);
            System.out.println("Соединились...");
            connection.close();
            System.out.println("Отключились...");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
