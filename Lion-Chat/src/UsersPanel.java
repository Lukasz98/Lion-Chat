import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {

    private String userName = "";
    private JLabel nameLabel;

    public UsersPanel() {
        setBackground(Color.red);
        nameLabel = new JLabel("?");
        add(nameLabel);
        setPreferredSize(new Dimension(1000 / 4, 700));
    }

    public void setUserName(String s) {
        userName = s;
        nameLabel.setText(s);
        nameLabel.repaint();
    }
}
