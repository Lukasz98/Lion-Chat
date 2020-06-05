import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MidPanel extends JPanel {

    private ArrayList<PrivateMessagePanel> insidePanels = new ArrayList<>();
    private Connection connection;

    public MidPanel(Connection connection) {
        this.connection = connection;
        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(1000 / 2, 700));
    }

    public void openMessages(int receiverId) {
        boolean found = false;
        for (int i = 0; i < insidePanels.size(); i++) {
            if (insidePanels.get(i).getOtherUserId() == receiverId) {
                insidePanels.get(i).setVisible(true);
                found = true;
            }
            else {
                insidePanels.get(i).setVisible(false);
            }
        }
        if (!found) {
            PrivateMessagePanel p = new PrivateMessagePanel(connection, receiverId);
            add(p);
            insidePanels.add(p);
        }
    }

    public void addMsg(int sender, int receiver, String text) {
        int panelId = sender;
        if (sender != receiver) {
            if (sender == connection.getUserId())
                panelId = receiver;

        }
        for (int i = 0; i < insidePanels.size(); i++) {
            if (insidePanels.get(i).getOtherUserId() == panelId) {
                insidePanels.get(i).addMessage(text, sender);
                return;
            }
        }
        PrivateMessagePanel p = new PrivateMessagePanel(connection, sender);
        add(p);
        insidePanels.add(p);
    }
}
