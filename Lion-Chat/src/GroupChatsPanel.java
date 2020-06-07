import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupChatsPanel extends JPanel {

    private JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel groupsPanel = new JPanel();
    private JLabel title = new JLabel("Groups:");
    private JButton newGroupButton = new JButton("+");

    public GroupChatsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.cyan);
        setPreferredSize(new Dimension(1000 / 4, 500));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        //topPanel.setPreferredSize(new Dimension(1000 / 5, 50));
        //title.setPreferredSize(new Dimension(1000 / 5, 50));
        title.setPreferredSize(new Dimension(50, 50));
        newGroupButton.setPreferredSize(new Dimension(50, 50));
        newGroupButton.setBackground(Color.DARK_GRAY);
        newGroupButton.setForeground(Color.LIGHT_GRAY);
        topPanel.add(title, BorderLayout.LINE_START);
        topPanel.add(newGroupButton, BorderLayout.LINE_END);
        add(topPanel, BorderLayout.PAGE_START);

        groupsPanel.setBackground(Color.pink);
        //groupsPanel.setSize(new Dimension(1000 / 4, 100));
        groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
        //groupsPanel.setLayout(new GridLayout(0, 1, 0, 2));
 //       groupsPanel.setSize(1000 /4, -1);
//        groupsPanel.setLayout(Layout());
        //groupsPanel.setPreferredSize(new Dimension(1000 / 4, 500));
//        groupsPanel.setMinimumSize(new Dimension(1000 / 4, 500));
        //groupsPanel.setMaximumSize(new Dimension(1000 / 4, 5500));
        scrollPane.setBackground(Color.GREEN);
        scrollPane.setPreferredSize(new Dimension(1000 / 4, 100));
        scrollPane.setViewportView(groupsPanel);

        add(scrollPane);

        setNewGroupButtonListener();

        String testGroups = "- 1 grupa 2 grupka";
        //for (int i = 0; i < 30; i++)
        //    testGroups += " " + i + " grupka";
        addGroups(testGroups.split(" "));
    }

    public void addGroups(String [] info) {
        //groupsPanel.removeAll();
        //groupsPanel.add(title);
        for (int i = 1; i + 1 < info.length && i < info.length; i += 2) {
            JButton b = new JButton(info[i + 1] + " id=" + info[i]);
            b.setName(info[i]); // name = id
            b.setMinimumSize(new Dimension(1000 / 4, 40));
            b.setPreferredSize(new Dimension(1000 / 4, 40));
            b.setMaximumSize(new Dimension(1000 / 4, 40));
            //b.addActionListener(clickOnUser);
            b.setBackground(Color.LIGHT_GRAY);
            groupsPanel.add(b);
            //groupsPanel.add(Box.createHorizontalGlue());
        }
        groupsPanel.repaint();
    }

    private void setNewGroupButtonListener()
    {
        newGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                for (int i = 0; i < 35; i++) {
                    JButton b = new JButton("id=" + i);
                    b.setName(Integer.toString(i)); // name = id
                    b.setMinimumSize(new Dimension(1000 / 4, 20));
                    b.setPreferredSize(new Dimension(1000 / 4, 20));
                    b.setMaximumSize(new Dimension(1000 / 4, 20));
                    //b.addActionListener(clickOnUser);
                    b.setBackground(Color.LIGHT_GRAY);
                    panel.add(b);
                }

                scrollPane.setPreferredSize(new Dimension(1000 / 4, 500));
                scrollPane.setViewportView(panel);
                int val = JOptionPane.showOptionDialog(null, scrollPane, "testst ",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"done", "cancel"}, 0);
                if (val == 0) {
                    System.out.println("done marked");
                }
                //JOptionPane.showMessageDialog(null, scrollPane, "tekst jakis", JOptionPane.OK_CANCEL_OPTION);
            }
        });
    }

}
