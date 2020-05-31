import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
        MidPanel midPanel = new MidPanel(connection);
        GroupChatsPanel groupChatsPanel = new GroupChatsPanel();

        ActionListener clickOnUserName = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JButton button = (JButton)actionEvent.getSource();
                midPanel.openMessages(Integer.valueOf(button.getName()));
                frame.getContentPane().repaint();
                frame.setVisible(true);
            }
        };

        UsersPanel usersPanel = new UsersPanel(clickOnUserName);

        mainPanel.add(usersPanel, BorderLayout.LINE_START);
        mainPanel.add(midPanel, BorderLayout.CENTER);
        mainPanel.add(groupChatsPanel, BorderLayout.LINE_END);


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
                midPanel.addMsg(sender, receiver, text);
                frame.setVisible(true);
            }
        };

        connection.setAfterLogin(afterLogin);
        connection.setUsersInfoUpdate(usersInfoUpdate );
        connection.setNewPrivMessg(newPrivMsg);
        connection.start();


        frame.getContentPane().add(loginPanel);
        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
