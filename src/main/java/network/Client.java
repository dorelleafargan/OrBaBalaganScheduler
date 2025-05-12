package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Lead;
import model.LocalDateAdapter;
import model.Request;
import model.Response;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Client {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        Lead[] leads = new Lead[] {
                new Lead(1005, "דניאל", "050-1234567", 8, LocalDate.now()),
                new Lead(1006, "יואב",  "050-2222222", 6, LocalDate.now()),
                new Lead(1007, "אורית","050-3333333", 9, LocalDate.now())
        };

        // 1) שליחת כל לידים
        for (Lead lead : leads) {
            try (Socket socket = new Socket("localhost", 34567);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("action", "lead/add");
                Request req = new Request(headers, gson.toJson(lead));
                writer.println(gson.toJson(req));

                String respJson = reader.readLine();
                if (respJson == null) {
                    System.err.println("❌ No response for lead " + lead.getName());
                    continue;
                }
                System.out.println("📩 Raw response: " + respJson);

                Response resp = gson.fromJson(respJson, Response.class);
                System.out.println("✅ Added: " + lead.getName() +
                        " (Client Number: " + lead.getClientNum() + ")");
                System.out.println("Status:  " + resp.getStatusCode());
                System.out.println("Message: " + resp.getMessage());
                System.out.println("Body:    " + resp.getBody());
                System.out.println("────────────");
            } catch (IOException e) {
                System.err.println("❌ Client error for " + lead.getName() + ": " + e.getMessage());
            }
        }

        // 2) בקשה לקבלת כל הלידים
        try (Socket socket = new Socket("localhost", 34567);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            Map<String, String> headers = new HashMap<>();
            headers.put("action", "lead/getAll");
            Request req = new Request(headers, "");
            writer.println(gson.toJson(req));

            String respJson = reader.readLine();
            if (respJson == null) {
                System.err.println("❌ No response for getAll");
                return;
            }
            System.out.println("📋 All Leads (raw): " + respJson);

            Response resp = gson.fromJson(respJson, Response.class);
            System.out.println("📋 All Leads (pretty):");
            System.out.println(resp.getBody());
        } catch (IOException e) {
            System.err.println("❌ Error fetching all leads: " + e.getMessage());
        }
        // === אנליטיקה מהשרת ===
        try (Socket socket = new Socket("localhost", 34567);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            Map<String, String> headers = new HashMap<>();
            headers.put("action", "lead/analytics");

            Request request = new Request(headers, "");
            writer.println(gson.toJson(request));

            String responseJson = reader.readLine();
            if (responseJson == null) {
                System.err.println("❌ No response for analytics");
            } else {
                Response response = gson.fromJson(responseJson, Response.class);
                System.out.println("📊 Analytics Summary:");
                System.out.println(response.getBody());
            }

        } catch (IOException e) {
            System.err.println("❌ Error fetching analytics: " + e.getMessage());
        }

    }
}
