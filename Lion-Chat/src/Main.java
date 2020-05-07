import javax.swing.*;
import javax.swing.border.Border;
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
        UsersPanel usersPanel = new UsersPanel();
        MessagePanel messagePanel = new MessagePanel();
        GroupChatsPanel groupChatsPanel = new GroupChatsPanel();

        mainPanel.add(usersPanel, BorderLayout.LINE_START);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(groupChatsPanel, BorderLayout.LINE_END);

        BRunnable afterLogin = new BRunnable() {
            private String name;
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

        connection.setAfterLogin(afterLogin);
        connection.start();


        frame.getContentPane().add(loginPanel);
        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
