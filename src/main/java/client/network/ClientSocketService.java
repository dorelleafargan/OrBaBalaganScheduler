package client.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.LocalDateAdapter;
import model.Request;
import model.Response;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Map;

public class ClientSocketService {
    private final String host = "localhost";
    private final int port = 34567;
    private final Gson gson;

    public ClientSocketService() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public Response sendRequest(String action, String bodyJson) {
        try (
                Socket socket = new Socket(host, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            Request request = new Request(Map.of("action", action), bodyJson);
            writer.println(gson.toJson(request));
            String responseJson = reader.readLine();
            return gson.fromJson(responseJson, Response.class);
        } catch (IOException e) {
            return new Response(500, "Client error: " + e.getMessage(), "");
        }
    }
}
