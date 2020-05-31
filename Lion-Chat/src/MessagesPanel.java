import javax.swing.*;
import java.awt.*;

public class MessagesPanel extends JScrollPane {

    private JPanel messagesPanel = new JPanel();

    public MessagesPanel() {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setPreferredSize(new Dimension(1000 / 2 - 20, 600));
        setViewportView(messagesPanel);

        messagesPanel.setBackground(Color.pink);
        messagesPanel.setLayout(null);
    }

    public void addMessage(String msg, int authorId, boolean amIAuthor) {

        Rectangle rectangle;
        Component[] components = messagesPanel.getComponents();
        if (components.length == 0) {
            rectangle = new Rectangle(20, 20, 200, 30);
        }
        else {
            Rectangle lastRect = components[components.length - 1].getBounds();
            rectangle = new Rectangle(lastRect.x, lastRect.y + lastRect.height + 10, lastRect.width, 30);
        }

        for (int i = 50; i < msg.length(); i += 50) {
            String s = msg.substring(0, i) + "<br>&emsp; &emsp; ";
            msg = new String(s + msg.substring(i + 1));
            rectangle.height += 20;
            i += 6; // more characters due to adding <br>...
        }

        msg = "<html>&emsp; " + authorId + ": " + msg + "</html>";
        JLabel label = new JLabel(msg);

        if (amIAuthor) {
            rectangle.x = 170;
            label.setBackground(Color.decode("#404cd1"));
        } else {
            rectangle.x = 20;
            label.setBackground(Color.decode("#1d7038"));
        }

        label.setBounds(rectangle);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);

        messagesPanel.add(label);
        messagesPanel.setPreferredSize(new Dimension(messagesPanel.getWidth(), rectangle.height + rectangle.y + 20));

        super.validate();
        JScrollBar vertical = getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        messagesPanel.repaint();
    }
}
