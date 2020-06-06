import org.mariadb.jdbc.internal.util.SqlStates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean set = false;

    private int id = -1;
    private boolean logged = false;

    public  ClientThread(Socket socket) {
        this.socket = socket;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
            return;
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            out.close();
            closeSocket();
            return;
        }
        set = true;
    }

    public synchronized void send(String msg) {
        out.println(msg);
    }

    private void tryToLogin(String [] words) {
        if (words.length < 3)
            return;
        int log = MySQL.tryToLogin(words[1], words[2]);
        if (log != -1){
            logged = true;
            id = log;
            System.out.println("Logged");
            String msg = "logged " + Integer.toString(id) + " " + words[1];
            send(msg);
            sendUsersInfo();
            sendUnseenPrivMsgsIds();
        }
    }

    private void sendUsersInfo() {
        ResultSet rs = MySQL.getUsersInfo();
        try {
            String msg = "usersInfo ";
            while (rs.next()) {
                int uid = rs.getInt("id");
                String uname = rs.getString("login");
                msg += Integer.toString(uid) + " " + uname + " ";
            }
            if (msg.length() > 10) {
                send(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    private void sendOldPrivMsg(int auth2) {
        ResultSet rs = MySQL.getUsersInfo();
        try {
            while (rs.next()) {
                String msg = "priv_msg " + auth2;
                String text = rs.getString("text");
                msg += text;
                send(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
    private void sendUnseenPrivMsgsIds() {
        ResultSet rs = MySQL.getUnseenPrivMsgsIds(id);
        try {
            String m = "";
            while (rs.next()) {
                m += rs.getString("id") + " ";
            }
            if (m.length() > 0) {
                send("unseenPrivMsgsIds " + m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void newPrivMsg(int receiverId, String text) {
        try {
            MySQL.sendNewPrivMsg(id, receiverId, text);
            if (OnlineClientList.isClientOnline(receiverId))
                OnlineClientList.sendNewMessage("priv_msg " + id + " " + receiverId + " " + text, receiverId);
            if (receiverId != id)
                OnlineClientList.sendNewMessage("priv_msg " + id + " " + receiverId + " " + text, id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
        try {
            while (rs.next()) {
                String msg = "priv_msg " + auth2;
                String text = rs.getString("text");
                msg += text;
                send(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    private void sendAllPrivMsgs(int auth2) {
        ResultSet rs = MySQL.getPrivMsgs(id, auth2);
        if (rs == null)
            return;
        String msg = "allPrivMsgs " + auth2;
        try {
            while (rs.next()) {
                msg += " " + rs.getString("author") + " " + rs.getString("receiver") + " " + rs.getString("text") + " " + Character.toString((char)3);
            }
            send(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        if (!set)
            return;

        String line = "";
        try {
            while (true) {
                if (((line = in.readLine()) != null)) {
                    System.out.println(line);
                    String words[] = line.split(" ");
                    if (words.length > 1 && words[0].equals("login")) {
                        tryToLogin(words);
                    }
                    //else if (words.length >= 2 && words[0].equals("get_old_priv_msg")) {
                    //    int auth2 = Integer.valueOf(words[1]);
                    //    sendOldPrivMsg(auth2);
                    //}
                    else if (words.length >= 3 && words[0].equals("send_priv_msg")) {
                        int auth2 = Integer.valueOf(words[1]);
                        String text = words[2];
                        for (int i = 3; i < words.length; i++)
                            text += " " + words[i];
                        newPrivMsg(auth2, text);
                    }
                    else if (words.length == 2 && words[0].equals("get_all_priv_msgs")) {
                        int auth2 = Integer.valueOf(words[1]);
                        sendAllPrivMsgs(auth2);
                    }
                    else if (words.length == 2 && words[0].equals("saw_priv_msg")) {
                        int auth2 = Integer.valueOf(words[1]);
                        MySQL.markPrivMsgViewed(id, auth2);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                closeSocket();
                OnlineClientList.removeClient(id);
            }
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return id;
    }
}
