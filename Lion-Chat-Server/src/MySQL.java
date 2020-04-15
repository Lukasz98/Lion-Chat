import java.sql.*;

public class MySQL {

    private Connection conn;
    private static MySQL mysql;

    private MySQL() throws SQLException {
        final String host = "jdbc:mysql://localhost";
        final String login = "server_user";
        final String passwd = "123";

        conn = DriverManager.getConnection(host, login, passwd);
    }

    public static void Init() throws SQLException {
        mysql = new MySQL();
    }

    public static void LoadPrintTest() {
        try {
            Statement stmt = mysql.conn.createStatement();
            stmt.executeQuery("use lion_chat");
            ResultSet rs = stmt.executeQuery("SELECT * FROM userrs");
            while (rs.next()) {
                System.out.println(rs.getString("login"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
