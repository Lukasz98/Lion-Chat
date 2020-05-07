import javax.swing.*;
import java.awt.*;

public class UsersPanel extends JPanel {

    private String userName = "";
    private JLabel nameLabel;
    private JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel usersPanel = new JPanel();
    private JLabel title = new JLabel("Users:");

    public UsersPanel() {
        setBackground(Color.red);
        nameLabel = new JLabel("?");
        add(nameLabel);


        usersPanel.setBackground(Color.pink);
        usersPanel.setPreferredSize(new Dimension(1000 / 4, 500));
        scrollPane.setViewportView(usersPanel);
        add(scrollPane);

        title.setPreferredSize(new Dimension(1000 / 4, 50));
        usersPanel.add(title);

        setPreferredSize(new Dimension(1000 / 4, 700));
    }

    public void setUserName(String s) {
        userName = s;
        nameLabel.setText(s);
        nameLabel.repaint();
    }

    public void setUsersInfo(String [] info) {
        usersPanel.removeAll();
        usersPanel.add(title);
        for (int i = 1; i + 1 < info.length && i < info.length; i += 2) {
            JButton b = new JButton(info[i + 1] + " id=" + info[i]);
            b.setPreferredSize(new Dimension(1000 / 4, 20));
            usersPanel.add(b);
        }
        usersPanel.repaint();
    }
}
