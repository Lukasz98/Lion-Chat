import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Connection extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private BRunnable afterLogin;

    public Connection() throws IOException {
        String host = "localhost";
        int port = 55555;

        socket = new Socket(host, port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public synchronized void send(String msg)  {
        out.println(msg);
    }

    @Override
    public void run() {
        String fromServer;
        try {
            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
                String [] words = fromServer.split(" ");
                if (words.length > 2 && words[0].equals("logged")) {
                    System.out.println("Moje id = " + words[1]);
                    afterLogin.setArg(words[2]);
                    SwingUtilities.invokeAndWait(afterLogin);
                    //afterLogin.run();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setAfterLogin(BRunnable r) {
        afterLogin = r;
    }
}
