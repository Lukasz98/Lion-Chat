import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UsersPanel extends JPanel {

    private String userName = "";
    private JLabel nameLabel;
    private JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel usersPanel = new JPanel();
    private JLabel title = new JLabel("Users:");

    public void setClickOnUser(ActionListener clickOnUser) {
        this.clickOnUser = clickOnUser;
    }

    private ActionListener clickOnUser;

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
            b.setName(info[i]); // name = id
            b.setPreferredSize(new Dimension(1000 / 4, 20));
            b.addActionListener(clickOnUser);
            b.setBackground(Color.LIGHT_GRAY);
            usersPanel.add(b);
        }
        usersPanel.repaint();
    }

    public void lightUserButton(String id) {
        Component [] c = usersPanel.getComponents();
        for (int i = 0 ; i < c.length; i++) {
            if (c[i].getName() != null && c[i].getName().equals(id)) {
                ((JButton)c[i]).setBackground(Color.red);
                break;
            }
        }
    }

    public void unLightUserButton(int id) {
        Component [] c = usersPanel.getComponents();
        for (int i = 0 ; i < c.length; i++) {
            System.out.println("attatatata");
            if (c[i].getName() != null && c[i].getName().equals(String.valueOf(id))) {
                ((JButton)c[i]).setBackground(Color.LIGHT_GRAY);
                break;
            }
        }
    }

}
