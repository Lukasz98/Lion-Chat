import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
/*
                ou need java.security.MessageDigest.

                        Call MessageDigest.getInstance("MD5") to get a MD5 instance of MessageDigest you can use.

                The compute the hash by doing one of:

                Feed the entire input as a byte[] and calculate the hash in one operation with md.digest(bytes).
                        Feed the MessageDigest one byte[] chunk at a time by calling md.update(bytes). When you're done adding input bytes, calculate the hash with md.digest().
                The byte[] returned by md.digest() is the MD5 hash.
                */
/*
                try {

                    byte[] bytesOfMessage = new String(passwdField.getPassword()).getBytes("UTF-8");

                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] thedigest = md.digest(bytesOfMessage);

                    for (int i = 0; i < thedigest.length; i++) {
                        System.out.print((int)thedigest[i]);
                    }
                    System.out.println();

                    String msg = "login " + loginField.getText() + " " + new String(thedigest);
                    System.out.println(msg);
                    System.out.println(passwdField.getPassword());
                    connection.send(msg);
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
*/
                String msg = "login " + loginField.getText() + " " + new String(passwdField.getPassword());
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
