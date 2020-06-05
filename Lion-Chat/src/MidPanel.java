import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MidPanel extends JPanel {

    private ArrayList<PrivateMessagePanel> insidePanels = new ArrayList<>();
    private Connection connection;
    private UsersPanel usersPanel;

    public MidPanel(Connection connection, UsersPanel usersPanel) {
        this.usersPanel = usersPanel;
        this.connection = connection;
        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(1000 / 2, 700));
    }

    public void openMessages(int receiverId) {
        boolean found = false;
        for (int i = 0; i < insidePanels.size(); i++) {
            if (insidePanels.get(i).getOtherUserId() == receiverId) {
                if (insidePanels.get(i).isUnseenMsgs()) {
                    usersPanel.unLightUserButton(insidePanels.get(i).getOtherUserId());
                }
                insidePanels.get(i).setVisible(true);
                found = true;
            }
            else {
                insidePanels.get(i).setVisible(false);
            }
        }
        if (!found) {
            addNewPrivateMsgPanel(receiverId);
        }
    }

    public void addMsg(int sender, int receiver, String text, boolean unseen) {
        int panelId = sender;
        if (sender != receiver) {
            if (sender == connection.getUserId())
                panelId = receiver;

        }
        for (int i = 0; i < insidePanels.size(); i++) {
            if (insidePanels.get(i).getOtherUserId() == panelId) {
                insidePanels.get(i).addMessage(text, sender);
                makePanelUnseen(panelId);
                return;
            }
        }
        addNewPrivateMsgPanel(sender);
        makePanelUnseen(sender);
    }

    public void makePanelUnseen(int id) {
        boolean found = false;
        for (int i = 0; i < insidePanels.size(); i++) {
            if (insidePanels.get(i).getOtherUserId() == id) {
                insidePanels.get(i).setUnseenMsgs(true);
                found = true;
                break;
            }
        }
        if (!found) {
            addNewPrivateMsgPanel(id);
            insidePanels.get(insidePanels.size() - 1).setUnseenMsgs(true);
        }
        usersPanel.lightUserButton(Integer.toString(id));
    }

    private void addNewPrivateMsgPanel(int id) {
        PrivateMessagePanel p = new PrivateMessagePanel(connection, id);
        add(p);
        p.setVisible(false);
        insidePanels.add(p);
    }
}
