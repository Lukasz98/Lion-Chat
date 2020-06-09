import java.sql.*;
import java.util.ArrayList;

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

    public synchronized static ResultSet getUsersInfo() {
        synchronized (mysql) {
            try {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id, login FROM userrs;"
                );
                return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized static ResultSet getPrivMsgs(int auth1, int auth2) {
        synchronized (mysql) {
            try {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT priv_msg.sender_id as author, priv_msg.receiver_id as receiver, text FROM priv_msg WHERE" +
                                "(priv_msg.sender_id=" + auth1 + " AND priv_msg.receiver_id=" + auth2 + ") OR" +
                                "(priv_msg.sender_id=" + auth2 + " AND priv_msg.receiver_id=" + auth1 + ");"
                );
                return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized static void markPrivMsgViewed(int id1, int id2) {
        synchronized (mysql) {
            try {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "UPDATE priv_msg SET viewed=true WHERE" +
                                " (priv_msg.receiver_id=" + id1 + "" + " AND priv_msg.sender_id=" + id2 + ") "
//                                " (priv_msg.receiver_id=" + id1 + "" + " AND priv_msg.sender_id=" + id1 + ");"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static ResultSet getUnseenPrivMsgsIds(int id) {
        synchronized (mysql) {
            try {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT priv_msg.sender_id as id FROM priv_msg WHERE" +
                                " priv_msg.receiver_id=" + id + "" + " AND priv_msg.viewed=FALSE;"
                );
                /*"SELECT priv_msg.sender_id as id FROM priv_msg WHERE" +
                                "(priv_msg.sender_id=" + id + ")" + " UNION " +
                            "SELECT priv_msg.receiver_id as id FROM priv_msg WHERE" +
                                "(priv_msg.receiver_id=" + id + ")"

                * */

                return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized static void sendNewPrivMsg(int authorId, int receiverId, String text) throws SQLException{
        synchronized (mysql) {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "INSERT INTO priv_msg (sender_id, receiver_id, text, viewed) VALUES(" + authorId + ", " + receiverId + ", '" + text + "', false);"
                );
        }
    }

    public synchronized static int addNewGroup(int authorId, ArrayList<Integer> ids) throws SQLException{
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "INSERT INTO groups VALUES();"
            );
            rs = stmt.executeQuery(
                    "SELECT id FROM groups ORDER BY id DESC LIMIT 1;"
            );
            int gid = -1;
            while (rs.next()) {
                gid = rs.getInt("id");
            }
            rs = stmt.executeQuery(
                    "INSERT INTO group_members (group_id, member_id) VALUES(" + gid + ", " + authorId + ");"
            );

            for (int i = 0; i < ids.size(); i++) {
                rs = stmt.executeQuery(
                        "INSERT INTO group_members (group_id, member_id) VALUES(" + gid + ", " + ids.get(i) + ");"
                );
            }
            return gid;
        }
    }

    public synchronized static ResultSet getGroupsInfo(int memberId) throws SQLException{
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT group_id FROM group_members WHERE member_id=" + memberId + ";"
            );
            return rs;
        }
    }

    public synchronized static ResultSet getGroupMembers(int gid) throws SQLException{
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT member_id FROM group_members WHERE group_id=" + gid + ";"
            );
            return rs;
        }
    }

    public synchronized static ResultSet getGrpMsgs(int gid) throws SQLException {
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT author_id, text FROM groups_messages WHERE group_id=" + gid + ";"
            );
            return rs;
        }
    }

    public synchronized static void sendGroupMsg(int authorId, int groupId, String text) throws SQLException{
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "INSERT INTO groups_messages (group_id, author_id, text) VALUES(" + groupId + ", " + authorId + ", '" + text + "');"
            );
            // tutaj bedzie wyzwalacz
            rs = stmt.executeQuery(
                    "SELECT id FROM groups_messages ORDER BY id DESC LIMIT 1;"
            );
            int msgId = -1;
            while (rs.next()) {
                msgId = rs.getInt("id");
            }

            rs = stmt.executeQuery(
                    "SELECT member_id FROM group_members WHERE group_id=" + groupId + ";"
            );

            while (rs.next()) {
                int memberId = rs.getInt("member_id");
                System.out.println("MEMBER: " + memberId);
                if (authorId == memberId) {
                    stmt.executeQuery(
                            "INSERT INTO group_msg_views (msg_id, group_id, viewer_id, viewed) VALUES(" + msgId + ", " + groupId + ", " + memberId + ", true);"
                    );
                }
                else {
                    stmt.executeQuery(
                            "INSERT INTO group_msg_views (msg_id, group_id, viewer_id, viewed) VALUES(" + msgId + ", " + groupId + ", " + memberId + ", false);"
                    );
                }
            }
        }
    }

    public synchronized static ResultSet getUnreedGroupsId(int userId) throws SQLException{
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT group_id FROM group_msg_views WHERE viewer_id=" + userId + " AND viewed=false;"
            );
            return rs;
        }
    }

    public synchronized static ResultSet setViewedGroup(int groupId, int userId) throws SQLException{
        synchronized (mysql) {
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "UPDATE group_msg_views SET viewed=true WHERE viewer_id=" + userId + " AND group_id='" + groupId + "';"
            );
            return rs;
        }
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
