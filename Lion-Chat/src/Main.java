import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String args[]) {
        JFrame frame = new JFrame("Lion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 1000, 700);
        frame.getContentPane().setBackground(Color.decode("#191919"));
        frame.setVisible(true);
    }
}
