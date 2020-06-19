import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MidPanel extends JPanel {

    private ArrayList<PrivateMessagePanel> insidePanels = new ArrayList<>();
    private ArrayList<GroupMessagePanel> groupMessagePanels = new ArrayList<>();
    private Connection connection;
    private UsersPanel usersPanel;
    private GroupChatsPanel groupChatsPanel;
    private int myId;

    public void setMyId(int s) { myId = s; }

    public MidPanel(Connection connection, UsersPanel usersPanel, GroupChatsPanel groupChatsPanel) {
        this.usersPanel = usersPanel;
        this.connection = connection;
        this.groupChatsPanel = groupChatsPanel;
        setBackground(Color.DARK_GRAY);
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
            insidePanels.get(insidePanels.size() - 1).setVisible(true);
        }
        for (int i = 0; i < groupMessagePanels.size(); i++) {
            groupMessagePanels.get(i).setVisible(false);
        }
    }

    public void openGroupMessages(int groupId) {
        boolean found = false;
        for (int i = 0; i < groupMessagePanels.size(); i++) {
            if (groupMessagePanels.get(i).getGroupId() == groupId) {
                if (groupMessagePanels.get(i).isUnseenMsgs()) {
                    groupChatsPanel.groupButtonUnlight(Integer.toString(groupId));
                    //usersPanel.unLightUserButton(groupMessagePanels.get(i).getOtherUserId());
                }
                groupMessagePanels.get(i).setVisible(true);
                found = true;
            }
            else {
                groupMessagePanels.get(i).setVisible(false);
            }
        }
        if (!found) {
            addNewGroupMsgPanel(groupId);
            groupMessagePanels.get(groupMessagePanels.size() - 1).setVisible(true);
        }
        for (int i = 0; i < insidePanels.size(); i++) {
            insidePanels.get(i).setVisible(false);
        }
    }

    public void addMsg(int sender, String senderName, int receiver, String text, boolean unseen) {
        int panelId = sender;
        if (sender != receiver) {
            if (sender == connection.getUserId())
                panelId = receiver;

        }
        for (int i = 0; i < insidePanels.size(); i++) {
            if (insidePanels.get(i).getOtherUserId() == panelId) {
                insidePanels.get(i).addMessage(text, sender, senderName);
                if (unseen) makePanelUnseen(panelId);
                return;
            }
        }
        addNewPrivateMsgPanel(sender);
        if (unseen)
            makePanelUnseen(sender);
    }

    public void addGroupMsg(int sender, String senderName, int groupId, String text) {
        for (int i = 0; i < groupMessagePanels.size(); i++) {
            if (groupMessagePanels.get(i).getGroupId() == groupId) {
                groupMessagePanels.get(i).addMessage(text, sender, senderName);
                return;
            }
        }
        addNewGroupMsgPanel(groupId);
        addGroupMsg(sender, senderName, groupId, text);
    }

    public void makeGroupUnread(int groupId) {
        for (int i = 0; i < groupMessagePanels.size(); i++) {
            if (groupMessagePanels.get(i).getGroupId() == groupId) {
                groupMessagePanels.get(i).setUnseenMsgs(true);
                return;
            }
        }
        addNewGroupMsgPanel(groupId);
        makeGroupUnread(groupId);
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

    private void addNewGroupMsgPanel(int id) {
        GroupMessagePanel p = new GroupMessagePanel(connection, id, myId);
        add(p);
        p.setVisible(false);
        groupMessagePanels.add(p);
    }
}
