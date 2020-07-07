import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UsersPanel extends JPanel {

    private String userName = "";
    private JLabel nameLabel;
    //private JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
      //      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel usersPanel = new JPanel();
    private JLabel addUserLabel = new JLabel("Add user:");
    private JPanel addingUserListPanel = new JPanel();
    private Connection connection;

    private JScrollPane scrollPaneUserPropos = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JButton addUser = new JButton("+");
    private ActionListener clickOnUser;

    public UsersPanel(Connection connection) {
        this.connection = connection;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000 / 4, 500));
        setBackground(Color.red);

        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setForeground(Color.lightGray);
        //topPanel.setPreferredSize(new Dimension(1000 / 4, 100));
        JPanel namePanel = new JPanel();
        namePanel.setBackground(Color.LIGHT_GRAY);
        nameLabel = new JLabel("?");
        nameLabel.setForeground(Color.DARK_GRAY);
        JButton updateName = new JButton("Edit nick");

        updateName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                mainPanel.setPreferredSize(new Dimension(1000 / 4 -20, 50));

                JPanel writerPanel = new JPanel();
                writerPanel.setPreferredSize(new Dimension(1000 /4 -20, 50));
                writerPanel.setBackground(Color.black);
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(1000 /4 - 40, 30));
                textField.setBackground(Color.GRAY);
                textField.setForeground(Color.WHITE);
                textField.grabFocus();
                writerPanel.add(textField);
                mainPanel.add(writerPanel, BorderLayout.NORTH);

                int val = JOptionPane.showOptionDialog(null, mainPanel, "testst ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"change", "cancel"}, 0);
                if (val == 0 && textField.getText().length() > 3) {
                    System.out.println("done marked");
                    connection.send("change_nick " + textField.getText());
                    nameLabel.setText(textField.getText());
                }

            }
        });

        namePanel.add(nameLabel);
        namePanel.add(updateName);
        topPanel.add(namePanel, BorderLayout.NORTH);
        //title.setPreferredSize(new Dimension(50, 50));

        addUserLabel.setBackground(Color.DARK_GRAY);
        addUserLabel.setForeground(Color.LIGHT_GRAY);
        JPanel addUserPanel = new JPanel();
        addUserPanel.setBackground(Color.DARK_GRAY);
        addUserPanel.setLayout(new BorderLayout());
        addUserPanel.add(addUserLabel, BorderLayout.LINE_START);

        addUser.setBackground(Color.decode("#100e42"));//Color.DARK_GRAY);
        addUser.setForeground(Color.LIGHT_GRAY);
        addUser.setPreferredSize(new Dimension(50, 50));
        addUserPanel.add(addUser, BorderLayout.LINE_END);
        topPanel.add(addUserPanel, BorderLayout.SOUTH);

        setNewUserButtonListener();

        add(topPanel, BorderLayout.PAGE_START);

        usersPanel.setBackground(Color.DARK_GRAY);
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        //usersPanel.setLayout(new GridLayout(0, 1, 0, 2));
        scrollPane.setPreferredSize(new Dimension(1000 / 4, 100));
        scrollPane.setViewportView(usersPanel);
        add(scrollPane);

        //title.setPreferredSize(new Dimension(1000 / 4, 50));
        //usersPanel.add(title);

    }

    public void setUserName(String s) {
        userName = s;
        nameLabel.setText("Nick: " + s);
        nameLabel.repaint();
    }

    public void updateUserNick(String id, String nick) {
        Component rr[] = usersPanel.getComponents();
        for (int i = 0; i < rr.length; i++) {
            if (rr[i].getName().equals(id)) {
                ((JButton)((JPanel)rr[i]).getComponents()[0]).setText(nick + " Id=" + id);
                break;
            }
        }
    }

    public void setUsersInfo(String [] info) {
        usersPanel.removeAll();

        for (int i = 1; i + 1 < info.length; i += 2) {
            JPanel ppp = new JPanel();
            ppp.setName(info[i]);
            ppp.setBackground(Color.DARK_GRAY);
            ppp.setPreferredSize(new Dimension(1000 / 4 + 20, 50));
            ppp.setMaximumSize(new Dimension(1000 / 4 + 20, 50));
            ppp.setMinimumSize(new Dimension(1000 / 4 + 20, 50));
            JButton b = new JButton(info[i + 1] + " id=" + info[i]);
            b.setName(info[i]); // name = id
            b.setMinimumSize(new Dimension(1000 / 8, 40));
            b.setPreferredSize(new Dimension(1000 / 8, 40));
            b.setMaximumSize(new Dimension(1000 / 8, 40));
            b.addActionListener(clickOnUser);
            b.setBackground(Color.LIGHT_GRAY);

            ppp.add(b);
//            usersPanel.add(b);


            JButton bb = new JButton("X");
            bb.setName(info[i]); // name = id
            bb.setMinimumSize(new Dimension(1000 / 12, 40));
            bb.setPreferredSize(new Dimension(1000 / 12, 40));
            bb.setMaximumSize(new Dimension(1000 / 12, 40));
            bb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    connection.send("erase_contact " + ((JButton)actionEvent.getSource()).getName());
                }
            });
            bb.setBackground(Color.LIGHT_GRAY);
            ppp.add(bb);
            usersPanel.add(ppp);
        }
        usersPanel.repaint();
    }

    public void lightUserButton(String id) {
        Component [] c = usersPanel.getComponents();
        for (int i = 0 ; i < c.length; i++) {
            if (c[i].getName() != null && c[i].getName().equals(id)) {
                //((JButton)c[i]).setBackground(Color.red);
                ((JPanel)c[i]).setBackground(Color.red);
                //break;
            }
        }
    }

    public void unLightUserButton(int id) {
        Component [] c = usersPanel.getComponents();
        for (int i = 0 ; i < c.length; i++) {
            System.out.println("attatatata");
            if (c[i].getName() != null && c[i].getName().equals(String.valueOf(id))) {
                //((JButton)c[i]).setBackground(Color.LIGHT_GRAY);
                ((JPanel)c[i]).setBackground(Color.LIGHT_GRAY);
                //break;
            }
        }
    }

    private void setNewUserButtonListener() {
        addingUserListPanel.setLayout(new BoxLayout(addingUserListPanel, BoxLayout.Y_AXIS));
        addingUserListPanel.setBackground(Color.DARK_GRAY);

        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                mainPanel.setPreferredSize(new Dimension(1000 / 4 -20, 50));

                JPanel writerPanel = new JPanel();
                writerPanel.setPreferredSize(new Dimension(1000 /4 -20, 50));
                writerPanel.setBackground(Color.black);
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(1000 /4 - 40, 30));
                textField.setBackground(Color.GRAY);
                textField.setForeground(Color.WHITE);
                textField.grabFocus();
                writerPanel.add(textField);
                mainPanel.add(writerPanel, BorderLayout.NORTH);

                textField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent keyEvent) {}
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        connection.send("get_users_like " + textField.getText());
                    }
                    @Override
                    public void keyReleased(KeyEvent keyEvent) {
                        connection.send("get_users_like " + textField.getText());
                    }
                });


                addingUserListPanel.removeAll();
                addingUserListPanel.setLayout(new BoxLayout(addingUserListPanel, BoxLayout.Y_AXIS));
                mainPanel.add(addingUserListPanel);

                scrollPaneUserPropos.setPreferredSize(new Dimension(1000 / 4, 500));
                scrollPaneUserPropos.setViewportView(mainPanel);

                int val = JOptionPane.showOptionDialog(null, scrollPaneUserPropos, "testst ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"cancel"}, 0);
                if (val == 0) {
                    System.out.println("done marked");
                }

            }
        });
    }

    public void addUserToPropositionsList(String userName, int id) {
        System.out.println("addingUserPropodolisty");
        JButton userButton = new JButton(userName);
        userButton.setMinimumSize(new Dimension(1000 / 4, 40));
        userButton.setPreferredSize(new Dimension(1000 / 4, 40));
        userButton.setMaximumSize(new Dimension(1000 / 4, 40));
        userButton.setName(Integer.toString(id));
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String id = ((JButton)actionEvent.getSource()).getName();
                connection.send("add_contact " + id);
            }
        });
        addingUserListPanel.add(userButton);
        addingUserListPanel.repaint();
        scrollPaneUserPropos.validate();
    }

    public void clearUserPropositionsList() {
        addingUserListPanel.removeAll();
        scrollPaneUserPropos.validate();
    }

    public void setClickOnUser(ActionListener clickOnUser) {
        this.clickOnUser = clickOnUser;
    }

}
