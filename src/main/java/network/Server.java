package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("⚙️ Server listening on port " + port);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("🔌 Client connected: " + client.getRemoteSocketAddress());
                new Thread(new HandleRequest(client)).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try { serverSocket.close(); }
                catch (IOException ignored) {}
            }
        }
    }
}
