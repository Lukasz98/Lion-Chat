import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;

public class GroupChatsPanel extends JPanel {

    private JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel groupsPanel = new JPanel();
    private JLabel title = new JLabel("Groups:");
    private JButton newGroupButton = new JButton("+");
    private ArrayList<String> users = new ArrayList<>();
    private ActionListener groupButtonListener;
    private ArrayList<JButton> groupButtons = new ArrayList<>();

    public GroupChatsPanel(Connection connection) {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(1000 / 4, 500));

        title.setForeground(Color.lightGray);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        title.setPreferredSize(new Dimension(50, 50));
        newGroupButton.setPreferredSize(new Dimension(50, 50));
        newGroupButton.setBackground(Color.decode("#100e42"));
        newGroupButton.setForeground(Color.LIGHT_GRAY);
        topPanel.add(title, BorderLayout.LINE_START);
        topPanel.add(newGroupButton, BorderLayout.LINE_END);
        add(topPanel, BorderLayout.PAGE_START);

        groupsPanel.setBackground(Color.DARK_GRAY);
        groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
        scrollPane.setBackground(Color.DARK_GRAY);
        scrollPane.setPreferredSize(new Dimension(1000 / 4, 100));
        scrollPane.setViewportView(groupsPanel);

        add(scrollPane);

        setNewGroupButtonListener(connection);

    }

    public void addGroup(String id, String [] users) {
        JButton b = new JButton("Group id=" + id);
        b.setName(id); // name = id
        b.setMinimumSize(new Dimension(1000 / 4, 40));
        b.setPreferredSize(new Dimension(1000 / 4, 40));
        b.setMaximumSize(new Dimension(1000 / 4, 40));
        b.setBackground(Color.LIGHT_GRAY);
        b.addActionListener(groupButtonListener);
        groupsPanel.add(b);
        groupButtons.add(b);
        groupsPanel.repaint();
    }

    private void setNewGroupButtonListener(Connection connection)
    {
        newGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                ArrayList<String> markedUsers = new ArrayList<>();

                for (int i = 0; i + 1 < users.size(); i += 2) {
                //for (int i = 0; i < 35; i++) {
                    JButton b = new JButton(users.get(i + 1) + " id=" + users.get(i));
                    b.setName(Integer.toString(i)); // name = id
                    b.setMinimumSize(new Dimension(1000 / 4, 20));
                    b.setPreferredSize(new Dimension(1000 / 4, 20));
                    b.setMaximumSize(new Dimension(1000 / 4, 20));
                    b.setName(users.get(i));
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            JButton button = (JButton)actionEvent.getSource();
                            String uid = button.getName();
                            for (int i = 0; i < markedUsers.size(); i++) {
                                if (markedUsers.get(i).equals(uid)) {
                                    markedUsers.remove(i);
                                    button.setBackground(Color.LIGHT_GRAY);
                                    return;
                                }
                            }
                            if (uid.length() > 0) {
                                markedUsers.add(uid);
                                button.setBackground(Color.GREEN);
                            }
                        }
                    });
                    b.setBackground(Color.LIGHT_GRAY);

                    panel.add(b);
                }

                scrollPane.setPreferredSize(new Dimension(1000 / 4, 500));
                scrollPane.setViewportView(panel);
                int val = JOptionPane.showOptionDialog(null, scrollPane, "testst ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"done", "cancel"}, 0);
                if (val == 0) {
                    System.out.println("done marked");
                    if (markedUsers.size() > 0) {
                        String msg = "new_grp_chat";
                        for (int i = 0; i < markedUsers.size(); i++) {
                            msg += " " + markedUsers.get(i);
                            System.out.println("marked user = " + markedUsers.get(i));
                        }
                        connection.send(msg);
                    }
                }


                //JOptionPane.showMessageDialog(null, scrollPane, "tekst jakis", JOptionPane.OK_CANCEL_OPTION);
            }
        });
    }

    public void groupMsgNotification(String groupId) {
        for (int i = 0; i < groupButtons.size(); i++) {
            if (groupButtons.get(i).getName() != null && groupButtons.get(i).getName().equals(groupId)) {
                groupButtons.get(i).setBackground(Color.red);
                groupButtons.get(i).repaint();
            }
        }
    }

    public void groupButtonUnlight(String groupId) {
        for (int i = 0; i < groupButtons.size(); i++) {
            if (groupButtons.get(i).getName() != null && groupButtons.get(i).getName().equals(groupId)) {
                groupButtons.get(i).setBackground(Color.LIGHT_GRAY);
                groupButtons.get(i).repaint();
            }
        }
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public void setClickOnGroupListener(ActionListener actionListener) {
        groupButtonListener = actionListener;
    }
}
