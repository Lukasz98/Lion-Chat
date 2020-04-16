import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {

    private JTextField loginField;
    private JPasswordField passwdField;
    private JButton loginButton;
    private JLabel group;

    public LoginPanel(Connection connection) {
        setLayout(null);
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String msg = "login " + loginField.getText() + " " + new String(passwdField.getPassword());
                System.out.println(msg);
                System.out.println(passwdField.getPassword());
                connection.send(msg);
            }
        });

        loginField = new JTextField();
        passwdField = new JPasswordField();

        setGroup();

        add(group);

        loginButton.setBounds(10, 400, 100, 100);

        add(loginButton);


        setBackground(Color.CYAN);
    }

    private void setGroup() {
        JLabel loginLabel = new JLabel("Login:");
        JLabel passwdLabel = new JLabel("Password");

        group = new JLabel();
        group.setBackground(Color.red);
        group.setBounds(100, 100, 400, 600);
        GroupLayout layout = new GroupLayout(group);
        group.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(loginLabel)
                        .addComponent(passwdLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginField)
                        .addComponent(passwdField))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginLabel)
                        .addComponent(loginField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwdLabel)
                        .addComponent(passwdField))
        );
    }


}
