import org.mariadb.jdbc.internal.util.SqlStates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
            sendOldGroupsInfo();
            sendGroupUnreedMsgNotification();
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
            System.out.println("nowa_prywatna wiadomosc");


            // te dwa ify trzeba poprawic
            if (OnlineClientList.isClientOnline(receiverId)) {
                OnlineClientList.sendNewMessage("priv_msg " + id + " " + receiverId + " " + text, receiverId);
            }
            if (receiverId != id) {
                OnlineClientList.sendNewMessage("priv_msg " + id + " " + receiverId + " " + text, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void newGroupChat(String [] args) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            ids.add(Integer.valueOf(args[i]));
        }
        try {
            int gid = MySQL.addNewGroup(id, ids);
            sendNewGroupInfo(id, ids, gid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendNewGroupInfo(int authorId, ArrayList<Integer> ids, int gid) {
        String msg = "new_group " + gid + " " + authorId;
        for (int i = 0; i < ids.size(); i++) {
            msg += " " + ids.get(i);
        }
        send(msg);
        for (int i = 0; i < ids.size(); i++) {
            OnlineClientList.sendNewMessage(msg, ids.get(i));
        }
    }

    private void sendOldGroupsInfo() {
        try {
            ResultSet rs = MySQL.getGroupsInfo(id);
            while (rs.next()) {
                int gid = rs.getInt("group_id");
                ResultSet rs2 = MySQL.getGroupMembers(gid);
                String msg = "new_group " + gid;
                while (rs2.next()) {
                    int mid = rs2.getInt("member_id");
                    msg += " " + mid;
                }
                send(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendAllGrpMsgs(int gid) {
        try {
            ResultSet rs = MySQL.getGrpMsgs(gid);
            String msg = "all_grp_msgs " + gid;
            while (rs.next()) {
                msg += " " + rs.getInt("author_id") + " " + rs.getString("text") + " " + Character.toString((char)3);
            }
            send(msg);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void newGroupMsg(int groupId, String text) {
        try {
            MySQL.sendGroupMsg(id, groupId, text);
            System.out.println("nowa grupowa wiadomosc");
            ResultSet rs = MySQL.getGroupMembers(groupId);
            while (rs.next()) {
                int receiverId = rs.getInt("member_id");
                if (OnlineClientList.isClientOnline(receiverId)) {
                    OnlineClientList.sendNewMessage("group_msg " + groupId + " " + id + " " + text, receiverId);
                    if (receiverId != id)
                        sendNewGroupMsgNotification(groupId, receiverId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendNewGroupMsgNotification(int groupId, int userId) {
        if (userId == id)
            send("group_msg_notification " + groupId);
        else if (OnlineClientList.isClientOnline(userId))
            OnlineClientList.sendNewMessage("group_msg_notification " + groupId, userId);
    }

    private void sendGroupUnreedMsgNotification() {
        try {
            ResultSet rs = MySQL.getUnreedGroupsId(id);
            while (rs.next()) {
                int groupId = rs.getInt("group_id");
                sendNewGroupMsgNotification(groupId, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewedGroup(int groupId) {
        try {
            MySQL.setViewedGroup(groupId, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendUserPropositionsList(String name) {
        try {
            ResultSet rs = MySQL.getListOfUsersLike(name);
            String msg = "user_propositions_list";
            while (rs.next()) {
                msg += " " + rs.getString("login") + " " + rs.getInt("id");
            }
            if (msg.length() > 23)
                send(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addContact(String contactId) {
        try {
            MySQL.addContact(id, Integer.valueOf(contactId));
            sendUsersInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendUsersInfo() {
        try {
            ResultSet rs = MySQL.getContacts(id);
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
                    else if (words.length >= 2 && words[0].equals("new_grp_chat")) {
                        newGroupChat(words);
                    }
                    else if (words.length >= 2 && words[0].equals("get_all_grp_msgs")) {
                        int gid = Integer.valueOf(words[1]);
                        sendAllGrpMsgs(gid);
                    }
                    else if (words.length >= 2 && words[0].equals("send_group_msg")) {
                        int groupId = Integer.valueOf(words[1]);
                        String text = words[2];
                        for (int i = 3; i < words.length; i++)
                            text += " " + words[i];
                        newGroupMsg(groupId, text);
                    }
                    else if (words.length >= 2 && words[0].equals("saw_grp_msg")) {
                        int groupId = Integer.valueOf(words[1]);
                        viewedGroup(groupId);
                    }
                    else if (words.length >= 2 && words[0].equals("get_users_like")) {
                        sendUserPropositionsList(words[1]);
                    }
                    else if (words.length >= 2 && words[0].equals("add_contact")) {
                        addContact(words[1]);
                    }
                }
                else
                    break;
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

            }
            OnlineClientList.removeClient(id);
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
