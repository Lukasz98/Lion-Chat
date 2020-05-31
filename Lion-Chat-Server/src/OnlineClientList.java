import java.util.ArrayList;

public class OnlineClientList {

    private static ArrayList<ClientThread> clientThreads = new ArrayList<>();

    public synchronized static boolean isClientOnline(int id) {
        synchronized (clientThreads) {
            for (int i = 0; i < clientThreads.size(); i++) {
                if (clientThreads.get(i).getClientId() == id) {
                    return true;
                }
            }
            return false;
        }
    }

    public synchronized static void sendNewMessage(String msg, int id) {
        synchronized (clientThreads) {
            for (int i = 0; i < clientThreads.size(); i++) {
                if (clientThreads.get(i).getClientId() == id) {
                    clientThreads.get(i).send(msg);
                }
            }
        }
    }

    public synchronized static void addClient(ClientThread ct) {
        synchronized (clientThreads) {
            boolean found = false;
            for (int i = 0; i < clientThreads.size(); i++) {
                if (clientThreads.get(i).getClientId() == ct.getClientId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Online user: " + ct.getClientId());
                clientThreads.add(ct);
            }
        }
    }

    public synchronized static void removeClient(int id) {
        synchronized (clientThreads) {
            for (int i = 0; i < clientThreads.size(); i++) {
                if (clientThreads.get(i).getClientId() == id) {
                    System.out.println("Disconected user: " + id);
                    clientThreads.remove(i);
                    break;
                }
            }
        }
    }

}
