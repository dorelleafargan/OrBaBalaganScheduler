package network;

public class ServerDriver {
    public static void main(String[] args) {
        int port = 34567;
        new Thread(new Server(port)).start();
    }
}
