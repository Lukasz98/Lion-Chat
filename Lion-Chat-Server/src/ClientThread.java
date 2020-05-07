import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean set = false;

    private int id = -1;
    private boolean logged = false;

    public  ClientThread(Socket socket) {
        this.socket = socket;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
            return;
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            out.close();
            closeSocket();
            return;
        }
        set = true;
    }

    public synchronized void send(String msg) {
        out.println(msg);
    }

    private void tryToLogin(String [] words) {
        if (words.length < 3)
            return;
        int log = MySQL.tryToLogin(words[1], words[2]);
        if (log != -1){
            logged = true;
            id = log;
            System.out.println("Logged");
            String msg = "logged " + Integer.toString(id) + " " + words[1];
            send(msg);
            sendUsersInfo();
        }
    }

    private void sendUsersInfo() {
        ResultSet rs = MySQL.getUsersInfo();
        try {
            String msg = "usersInfo ";
            while (rs.next()) {
                int uid = rs.getInt("id");
                String uname = rs.getString("login");
                msg += Integer.toString(uid) + " " + uname + " ";
            }
            if (msg.length() > 10) {
                send(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        if (!set)
            return;

        String line = "";
        try {
            while (true) {
                if (((line = in.readLine()) != null)) {
                    System.out.println(line);
                    String words[] = line.split(" ");
                    if (words.length > 1 && words[0].equals("login")) {
                        tryToLogin(words);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeSocket();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
