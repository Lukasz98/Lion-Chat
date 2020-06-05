import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PrivateMessagePanel extends JPanel {

    private Connection connection;
    private MessagesPanel messagesPanel = new MessagesPanel();
    private JPanel writerPanel = new JPanel();
    private JTextField textField = new JTextField();
    private int otherUserId;
    private boolean unseenMsgs = false;

    public PrivateMessagePanel(Connection connection, int receiverId) {
        otherUserId = receiverId;
        this.connection = connection;
        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(1000 / 2 - 20, 700 - 10));

//        messagesPanel.setPreferredSize(new Dimension(1000 / 2 -20, 600));
  //      messagesPanel.setBackground(Color.pink);

        add(messagesPanel);

        writerPanel.setPreferredSize(new Dimension(1000 / 2 -20, 50));
        writerPanel.setBackground(Color.black);
        textField.setPreferredSize(new Dimension(1000 /2 - 40, 30));
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
                    String msg = "send_priv_msg " + receiverId +  " " + textField.getText();
                    connection.send(msg);
                }
            }
        });
        writerPanel.add(textField);

        //add(messagesPanel);
//        add(scrollPane);
        add(writerPanel);

        connection.send("get_all_priv_msgs " + receiverId);
    }

    public void addMessage(String msg, int authorId) {
        messagesPanel.addMessage(msg, authorId, authorId != otherUserId);
    }

    public int getOtherUserId() { return otherUserId; }
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
            connection.send("saw_priv_msg " + otherUserId);
            unseenMsgs = false;
        }
    }


}
