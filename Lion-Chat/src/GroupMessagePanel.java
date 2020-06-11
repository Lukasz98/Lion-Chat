import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GroupMessagePanel extends JPanel {

    private Connection connection;
    private MessagesPanel messagesPanel = new MessagesPanel();
    private JPanel writerPanel = new JPanel();
    private JTextField textField = new JTextField();
    private int groupId, myId;
    private boolean unseenMsgs = false;

    public GroupMessagePanel(Connection connection, int groupId, int myId) {
        this.groupId = groupId;
        this.myId = myId;
        this.connection = connection;
        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(1000 / 2 - 20, 700 - 10));

        add(messagesPanel);

        writerPanel.setPreferredSize(new Dimension(1000 / 2 -20, 50));
        writerPanel.setBackground(Color.black);
        textField.setPreferredSize(new Dimension(1000 / 2 - 40, 30));
        textField.setBackground(Color.GRAY);
        textField.setForeground(Color.WHITE);
        textField.grabFocus();
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {}
            @Override
            public void keyPressed(KeyEvent keyEvent) {}
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) { // sending message
                    String msg = "send_group_msg " + groupId +  " " + textField.getText();
                    connection.send(msg);
                }
            }
        });
        writerPanel.add(textField);

        add(writerPanel);

        connection.send("get_all_grp_msgs " + groupId);
    }

    public void addMessage(String msg, int authorId, String authorName) {
        messagesPanel.addMessage(msg, authorId, authorId == myId, authorName);
    }

    public int getGroupId() { return groupId; }
    public boolean isUnseenMsgs() {
        return unseenMsgs;
    }

    public void setUnseenMsgs(boolean unseenMsgs) {
        this.unseenMsgs = unseenMsgs;
    }

    @Override
    public void setVisible(boolean v) {
        super.setVisible(v);
        if (v && unseenMsgs) {
            connection.send("saw_grp_msg " + groupId);
            unseenMsgs = false;
        }
    }

}
