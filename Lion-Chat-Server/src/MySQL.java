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
        Statement stmt = mysql.conn.createStatement();
        stmt.executeQuery("use lion_chat");
    }

    public synchronized static int tryToLogin(String login, String passwd) {
        synchronized (mysql) {
            try {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT id, login, passwd FROM userrs WHERE login='" + login + "' AND passwd='" + passwd + "';"
                );
                while (rs.next()) {
                    System.out.println(rs.getString("login"));
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
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
