import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    private final int port = 55555;

    public Listener() {

    }

    @Override
    public void run() {
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port);) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket);
                clientThread.start();
                OnlineClientList.addClient(clientThread);
                System.out.println("nowy klient");

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

}
