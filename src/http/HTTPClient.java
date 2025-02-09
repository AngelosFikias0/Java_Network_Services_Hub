package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HTTPClient {
    private static final String SERVER_URL = "http://127.0.0.1:8080";

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("📩 HTTP Client started. Type a URL path (e.g., `/`) or 'exit' to shut down the server.");

            while (true) {
                System.out.print("Enter path: ");
                String path = scanner.nextLine().trim();

                if ("exit".equalsIgnoreCase(path)) {
                    sendRequest("/exit");
                    System.out.println("👋 Client exiting...");
                    break;
                }

                sendRequest(path.startsWith("/") ? path : "/" + path);
            }
        }
    }

    private void sendRequest(String path) {
        try {
            URL url = new URL(SERVER_URL + path);
            System.out.println("🔗 Sending request to: " + url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            System.out.println("📨 Server Response: " + response.toString() + " (Code: " + responseCode + ")");
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
