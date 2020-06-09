import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String args[]) {
        JFrame frame = new JFrame("Lion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 700);
        frame.getContentPane().setBackground(Color.decode("#191919"));

        Connection connection;
        try {
            connection = new Connection();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        LoginPanel loginPanel = new LoginPanel(connection);

        JPanel mainPanel = new JPanel(new BorderLayout(0,0));
        UsersPanel usersPanel = new UsersPanel();
        GroupChatsPanel groupChatsPanel = new GroupChatsPanel(connection);
        MidPanel midPanel = new MidPanel(connection, usersPanel, groupChatsPanel);


        ActionListener clickOnUserName = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JButton button = (JButton)actionEvent.getSource();
                midPanel.openMessages(Integer.valueOf(button.getName()));
                frame.getContentPane().repaint();
                frame.setVisible(true);
            }
        };

        usersPanel.setClickOnUser(clickOnUserName);

        mainPanel.add(usersPanel, BorderLayout.LINE_START);
        mainPanel.add(midPanel, BorderLayout.CENTER);
        mainPanel.add(groupChatsPanel, BorderLayout.LINE_END);

        ActionListener clickOnGroupName = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JButton button = (JButton)actionEvent.getSource();
                midPanel.openGroupMessages(Integer.valueOf(button.getName()));
                frame.getContentPane().repaint();
                frame.setVisible(true);
            }
        };

        groupChatsPanel.setClickOnGroupListener(clickOnGroupName);

        BRunnable afterLogin = new BRunnable() {
            private String name;
            @Override
            public void setArg(String arg) {
                name = arg;
            }
            @Override
            public void run() {
                frame.getContentPane().remove(loginPanel);
                usersPanel.setUserName(name);
                midPanel.setMyId(connection.getUserId());
                frame.getContentPane().add(mainPanel);
                //frame.getContentPane().add(usersPanel);
                frame.getContentPane().repaint();
                frame.setVisible(true);
            }
        };

        BRunnable usersInfoUpdate = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {
                usersPanel.setUsersInfo(args);
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
                String myId = Integer.toString(connection.getUserId());
                for (int i = 0; i < arr.size(); i += 2) {
                    if (arr.get(i).equals(myId)) {
                        arr.remove(i);
                        if (i < arr.size()) arr.remove(i);
                        break;
                    }
                }
                groupChatsPanel.setUsers(arr);
                frame.setVisible(true);
            }
        };

        BRunnable newPrivMsg = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {

                int sender = Integer.valueOf(args[1]);
                int receiver = Integer.valueOf(args[2]);
                String text = args[3];
                for (int i = 4; i < args.length; i++)
                    text += " " + args[i];
                midPanel.addMsg(sender, receiver, text, true);
                frame.setVisible(true);
            }
        };

        BRunnable populateWithPrivMsgs = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {

                int otherUserId = Integer.valueOf(args[1]);
                for (int i = 2; i < args.length; i++) {
                    int author = Integer.valueOf(args[i]), receiver = Integer.valueOf(args[i + 1]);
                    String text = args[i + 2];
                    for (i = i + 3; i < args.length; i++) {
                        if (args[i].equals(Character.toString((char)3)))
                            break;
                        text += " " + args[i];
                    }
                    midPanel.addMsg(author, receiver, text, false);
                }
                frame.setVisible(true);
            }
        };

        BRunnable unseenMsgs = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {
                System.out.println("tu");

                for (int i = 1; i < args.length; i++) {
                    usersPanel.lightUserButton(args[i]);
                    midPanel.makePanelUnseen(Integer.valueOf(args[i]));
                }
                frame.setVisible(true);
            }
        };

        BRunnable newGroup = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {
                String gid = args[1];
                groupChatsPanel.addGroup(gid, Arrays.copyOfRange(args, 1, args.length));
                frame.setVisible(true);
            }
        };

        BRunnable populateWithGrpMsgs = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {
                int gid = Integer.valueOf(args[1]);
                for (int i = 2; i < args.length; i++) {
                    int authorId = Integer.valueOf(args[i]);
                    String text = args[i + 1];
                    for (i = i + 2; i < args.length; i++) {
                        if (args[i].equals(Character.toString((char)3)))
                            break;
                        text += " " + args[i];
                    }
                    midPanel.addGroupMsg(authorId, gid, text);
                }
                frame.setVisible(true);
            }
        };

        BRunnable newGroupMsg = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {
                int gid = Integer.valueOf(args[1]);
                int authorId = Integer.valueOf(args[2]);
                String text = args[3];
                for (int i = 4; i < args.length; i++) {
                    text += " " + args[i];
                }
                midPanel.addGroupMsg(authorId, gid, text);

                frame.setVisible(true);
            }
        };

        BRunnable unreadGroupMsgNotification = new BRunnable() {
            private String[] args;
            @Override
            public void setArgs(String [] args) { this.args = args; }
            @Override
            public void run() {
                String groupId = args[1];
                groupChatsPanel.groupMsgNotification(groupId);
                midPanel.makeGroupUnread(Integer.valueOf(groupId));
                frame.setVisible(true);
            }
        };

        connection.setAfterLogin(afterLogin);
        connection.setUsersInfoUpdate(usersInfoUpdate );
        connection.setNewPrivMessg(newPrivMsg);
        connection.setPopulateWithPrivMsgs(populateWithPrivMsgs);
        connection.setUnseenMsgs(unseenMsgs);
        connection.setNewGroup(newGroup);
        connection.setPopulateGroupMessages(populateWithGrpMsgs);
        connection.setGroupMsg(newGroupMsg);
        connection.setGroupMsgNotification(unreadGroupMsgNotification);
        connection.start();


        frame.getContentPane().add(loginPanel);
        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
