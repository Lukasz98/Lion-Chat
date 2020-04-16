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

        connection.start();

        LoginPanel loginPanel = new LoginPanel(connection);

        frame.getContentPane().add(loginPanel);
        //frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
