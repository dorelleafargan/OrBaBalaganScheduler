package network;

import com.google.gson.Gson;
import controller.BaseController;
import controller.ControllerFactory;
import model.Request;
import model.Response;

import java.io.*;
import java.net.Socket;

public class HandleRequest implements Runnable {
    private final Socket clientSocket;
    private final Gson gson = new Gson();

    public HandleRequest(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        PrintWriter writer = null;

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String json = reader.readLine();
            System.out.println("📩 Received: " + json);

            Request request = gson.fromJson(json, Request.class);
            String action = request.getHeaders().get("action");

            BaseController controller = ControllerFactory.getController(action);
            Response response = controller.handle(request);

            String responseJson = gson.toJson(response);
            System.out.println("📤 Sending: " + responseJson);
            writer.println(responseJson);

        } catch (Exception e) {
            System.err.println("❌ Error in HandleRequest: " + e.getMessage());

            if (writer != null) {
                Response errorResponse = new Response(500, "Server error: " + e.getMessage(), "");
                String errorJson = gson.toJson(errorResponse);
                writer.println(errorJson);
            }

        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("❌ Failed to close socket: " + e.getMessage());
            }
        }
    }
}
