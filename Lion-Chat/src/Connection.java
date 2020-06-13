import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Connection extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private int id = -1;
    private BRunnable afterLogin;
    private BRunnable usersInfoUpdate;
    private BRunnable newPrivMessg;
    private BRunnable unseenMsgs;
    private BRunnable newGroup;
    private BRunnable populateGroupMessages;
    private BRunnable groupMsg;
    private BRunnable populateWithPrivMsgs;
    private BRunnable groupMsgNotification;


    private BRunnable userPropositionsList;

    public Connection() throws IOException {
        String host = "localhost";
        int port = 55555;

        socket = new Socket(host, port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public synchronized void send(String msg)  {
            out.println(msg);
    }

    @Override
    public void run() {
        String fromServer;
        try {
            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
                String [] words = fromServer.split(" ");
                if (words.length > 2 && words[0].equals("logged")) {
                    id = Integer.valueOf(words[1]);
                    System.out.println("Moje id = " + words[1]);
                    afterLogin.setArg(words[2]);
                    SwingUtilities.invokeAndWait(afterLogin);
                }
                else if (words.length > 2 && words[0].equals("usersInfo")) {
                    usersInfoUpdate.setArgs(words);
                    SwingUtilities.invokeAndWait(usersInfoUpdate);
                }
                else if (words.length > 3 && words[0].equals("priv_msg")) {
                    newPrivMessg.setArgs(words);
                    SwingUtilities.invokeAndWait(newPrivMessg);
                }
                else if (words.length > 3 && words[0].equals("allPrivMsgs")) {
                    populateWithPrivMsgs.setArgs(words);
                    SwingUtilities.invokeAndWait(populateWithPrivMsgs);
                }
                else if (words.length > 1 && words[0].equals("unseenPrivMsgsIds")) {
                    unseenMsgs.setArgs(words);
                    SwingUtilities.invokeAndWait(unseenMsgs);
                }
                else if (words.length > 1 && words[0].equals("new_group")) {
                    newGroup.setArgs(words);
                    SwingUtilities.invokeAndWait(newGroup);
                }
                else if (words.length > 1 && words[0].equals("all_grp_msgs")) {
                    populateGroupMessages.setArgs(words);
                    SwingUtilities.invokeAndWait(populateGroupMessages);
                }
                else if (words.length > 2 && words[0].equals("group_msg")) {
                    groupMsg.setArgs(words);
                    SwingUtilities.invokeAndWait(groupMsg);
                }
                else if (words.length >= 2 && words[0].equals("group_msg_notification")) {
                    System.out.println("msg notification");
                    groupMsgNotification.setArgs(words);
                    SwingUtilities.invokeAndWait(groupMsgNotification);
                }
                else if (words.length >= 2 && words[0].equals("user_propositions_list")) {
                    System.out.println("userPropoList");
                    userPropositionsList.setArgs(words);
                    SwingUtilities.invokeAndWait(userPropositionsList);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public int getUserId() { return id; }

    public void setAfterLogin(BRunnable r) {
        afterLogin = r;
    }
    public void setUsersInfoUpdate(BRunnable r) { usersInfoUpdate = r; }
    public void setNewPrivMessg(BRunnable r) { newPrivMessg = r; }
    public void setPopulateWithPrivMsgs(BRunnable populateWithPrivMsgs) { this.populateWithPrivMsgs = populateWithPrivMsgs; }
    public void setUnseenMsgs(BRunnable unseenMsgs) { this.unseenMsgs = unseenMsgs; }
    public void setNewGroup(BRunnable r) { newGroup = r; }
    public void setPopulateGroupMessages(BRunnable populateGroupMessages) { this.populateGroupMessages = populateGroupMessages; }
    public void setGroupMsg(BRunnable groupMsg) { this.groupMsg = groupMsg; }
    public void setGroupMsgNotification(BRunnable groupMsgNotification) { this.groupMsgNotification = groupMsgNotification; }
    public void setUserPropositionsList(BRunnable userPropositionsList) { this.userPropositionsList = userPropositionsList; }

}
