import org.mariadb.jdbc.MySQLDataSource;

import java.sql.*;

public class Main {

    public static void main(String args[]) {
        System.out.println("Server");

        try {
            MySQL.Init();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cannot connect with database.");
            return;
        }

        MySQL.LoadPrintTest();
    }

}
