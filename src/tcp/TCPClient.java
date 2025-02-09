package tcp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9876;

    public void start() {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("📩 TCP Client started. Type a message and press Enter (type 'exit' to quit).");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine().trim();

                out.println(message);

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("👋 Client exiting...");
                    break; // Exit loop and close the client
                }

                String response = in.readLine();
                System.out.println("📨 Server: " + response);
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
