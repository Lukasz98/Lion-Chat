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
        stmt.executeQuery("use Lion_chat");
    }

    public synchronized static int tryToLogin(String login, String passwd) {
        synchronized (mysql) {
            try {
                String query = "SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
                PreparedStatement stmt = mysql.conn.prepareStatement(query);
                stmt.setString(1, login);
                stmt.setString(2, passwd);
                ResultSet rs = stmt.executeQuery();
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
/*
    public synchronized static ResultSet getUsersInfo() {
        synchronized (mysql) {
            try {
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id, login FROM users;"
                );
                return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
*/
    public synchronized static ResultSet getPrivMsgs(int auth1, int auth2) {
        synchronized (mysql) {
            try {
                String query = "SELECT priv_messages.sender_id as author_id, priv_messages.receiver_id as receiver_id, text FROM priv_messages WHERE" +
                        "(priv_messages.sender_id=? AND priv_messages.receiver_id=?) OR" +
                        "(priv_messages.sender_id=? AND priv_messages.receiver_id=?);";
                PreparedStatement stmt = mysql.conn.prepareStatement(query);
                stmt.setInt(1, auth1);
                stmt.setInt(2, auth2);
                stmt.setInt(3, auth2);
                stmt.setInt(4, auth1);
                ResultSet rs = stmt.executeQuery();
/*
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT priv_messages.sender_id as author_id, priv_messages.receiver_id as receiver_id, text FROM priv_messages WHERE" +
                                "(priv_messages.sender_id=" + auth1 + " AND priv_messages.receiver_id=" + auth2 + ") OR" +
                                "(priv_messages.sender_id=" + auth2 + " AND priv_messages.receiver_id=" + auth1 + ");"
                );
  */
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
                String query = "UPDATE priv_messages SET viewed=true WHERE" +
                        " (priv_messages.receiver_id=? AND priv_messages.sender_id=?);";
                //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
                PreparedStatement stmt = mysql.conn.prepareStatement(query);
                stmt.setInt(1, id1);
                stmt.setInt(2, id2);
                ResultSet rs = stmt.executeQuery();
             /*
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "UPDATE priv_messages SET viewed=true WHERE" +
                                " (priv_messages.receiver_id=" + id1 + "" + " AND priv_messages.sender_id=" + id2 + ") "
                );*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static ResultSet getUnseenPrivMsgsIds(int id) {
        synchronized (mysql) {
            try {
                String query =       "SELECT priv_messages.sender_id as id FROM priv_messages WHERE" +
                        " priv_messages.receiver_id=? AND priv_messages.viewed=FALSE;";
                //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
                PreparedStatement stmt = mysql.conn.prepareStatement(query);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                /*
                Statement stmt = mysql.conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT priv_messages.sender_id as id FROM priv_messages WHERE" +
                                " priv_messages.receiver_id=" + id + "" + " AND priv_messages.viewed=FALSE;"
                );

                 */
                return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized static void sendNewPrivMsg(int authorId, int receiverId, String text) throws SQLException{
        synchronized (mysql) {
//                Statement stmt = mysql.conn.createStatement();
  //              ResultSet rs = stmt.executeQuery(
    //                    "INSERT INTO priv_messages (sender_id, receiver_id, text, viewed) VALUES(" + authorId + ", " + receiverId + ", '" + text + "', false);"
      //          );/*  ab', false); delete * from priv_messages; insert into priv_messages values(null, 1, 2, 'ddd */

            String query = "INSERT INTO priv_messages (sender_id, receiver_id, text, viewed) VALUES(" + authorId + ", " + receiverId + ", ?, false);";
//            ;"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setString(1, text);
            ResultSet rs = stmt.executeQuery();
        }
    }

    public synchronized static int addNewGroup(int authorId, ArrayList<Integer> ids) throws SQLException{
        synchronized (mysql) {
            Statement stmtt = mysql.conn.createStatement();
            ResultSet rs = stmtt.executeQuery(
                    "INSERT INTO groups VALUES();"
            );
            rs = stmtt.executeQuery(
                    "SELECT id FROM groups ORDER BY id DESC LIMIT 1;"
            );
            int gid = -1;
            while (rs.next()) {
                gid = rs.getInt("id");
            }

            String query = "INSERT INTO group_members (group_id, member_id) VALUES(?, ?);";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, gid);
            stmt.setInt(2, authorId);
            rs = stmt.executeQuery();

//            rs = stmt.executeQuery(
//                    "INSERT INTO group_members (group_id, member_id) VALUES(" + gid + ", " + authorId + ");"
  //          );

            for (int i = 0; i < ids.size(); i++) {
                query =         "INSERT INTO group_members (group_id, member_id) VALUES(?, ?);";
                //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
                stmt = mysql.conn.prepareStatement(query);
                stmt.setInt(1, gid);
                stmt.setInt(2, ids.get(i));
                rs = stmt.executeQuery();
                //rs = stmt.executeQuery(
                //        "INSERT INTO group_members (group_id, member_id) VALUES(" + gid + ", " + ids.get(i) + ");"
                //);
            }
            return gid;
        }
    }

    public synchronized static ResultSet getGroupsInfo(int memberId) throws SQLException{
        synchronized (mysql) {
            String query = "SELECT group_id FROM group_members WHERE member_id=?;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            /*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT group_id FROM group_members WHERE member_id=" + memberId + ";"
            );*/
            return rs;
        }
    }

    public synchronized static ResultSet getGroupMembers(int gid) throws SQLException{
        synchronized (mysql) {
            String query = "SELECT member_id FROM group_members WHERE group_id=?;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, gid);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT member_id FROM group_members WHERE group_id=" + gid + ";"
            );
*/
            return rs;
        }
    }

    public synchronized static ResultSet getGrpMsgs(int gid) throws SQLException {
        synchronized (mysql) {
            String query = "SELECT author_id, text FROM group_messages WHERE group_id=?;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, gid);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT author_id, text FROM group_messages WHERE group_id=" + gid + ";"
            );
  */
            return rs;
        }
    }

    public synchronized static void sendGroupMsg(int authorId, int groupId, String text) throws SQLException{
        synchronized (mysql) {
            String query = "INSERT INTO group_messages (group_id, author_id, text) VALUES(?, ?, ?);";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, groupId);
            stmt.setInt(2, authorId);
            stmt.setString(3, text);
            ResultSet rs = stmt.executeQuery();
            //return rs;
            /*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "INSERT INTO group_messages (group_id, author_id, text) VALUES(" + groupId + ", " + authorId + ", '" + text + "');"
            );
            return;
  */
        }
    }

    public synchronized static ResultSet getUnreedGroupsId(int userId) throws SQLException{
        synchronized (mysql) {
            String query = "SELECT group_id FROM group_msg_views WHERE viewer_id=? AND viewed=false;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT group_id FROM group_msg_views WHERE viewer_id=" + userId + " AND viewed=false;"
            );
*/
            return rs;
        }
    }

    public synchronized static ResultSet setViewedGroup(int groupId, int userId) throws SQLException{
        synchronized (mysql) {
            String query = "UPDATE group_msg_views SET viewed=true WHERE viewer_id=? AND group_id=?;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, groupId);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "UPDATE group_msg_views SET viewed=true WHERE viewer_id=" + userId + " AND group_id='" + groupId + "';"
            );
  */
            return rs;
        }
    }

    public synchronized static ResultSet getListOfUsersLike(String name) throws SQLException{
        synchronized (mysql) {
            String query = "SELECT login, id FROM users WHERE login LIKE ?;";
            //"INSERT INTO group_messages (group_id, author_id, text) VALUES(" + groupId + ", " + authorId + ", ?);";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setString(1, name + "%");
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT login, id FROM users WHERE login LIKE '" + name + "%';"
            );
  */
            return rs;
        }
    }

    public synchronized static void addContact(int id1, int id2) throws SQLException{
        synchronized (mysql) {
            String query = "INSERT INTO contacts VALUES(?, ?);";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, id1);
            stmt.setInt(2, id2);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "INSERT INTO contacts VALUES(" + id1 + ", " + id2 + ");"
            );
  */
        }
    }

    public synchronized static ResultSet getContacts(int id) throws SQLException{
        synchronized (mysql) {
            String query = "SELECT user_id2 as 'id', login FROM contacts LEFT JOIN users ON users.id=user_id2 WHERE user_id1=?" +
                    " UNION " +
                    "SELECT user_id1 as 'id', login FROM contacts LEFT JOIN users ON users.id=user_id1 WHERE user_id2=?;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT user_id2 as 'id', login FROM contacts LEFT JOIN users ON users.id=user_id2 WHERE user_id1=" + id +
                            " UNION " +
                            "SELECT user_id1 as 'id', login FROM contacts LEFT JOIN users ON users.id=user_id1 WHERE user_id2=" + id + ";"
//                            " SELECT id1 as 'id' FROM contacts WHERE id2=" + id + ";"
            );
  */
            return rs;
        }
    }

    public synchronized static ResultSet getUserName(int id) throws SQLException{
        synchronized (mysql) {
            String query = "SELECT login FROM users WHERE id=?;";
            //"SELECT id, login, passwd FROM users WHERE login=? AND passwd=unhex(md5(?));";
            PreparedStatement stmt = mysql.conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
/*
            Statement stmt = mysql.conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT login FROM users WHERE id=" + id + ";"
            );
  */
            return rs;
        }
    }

}
