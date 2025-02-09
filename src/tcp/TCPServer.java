package tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import db.NetworkScanner;

public class TCPServer {
    private static final int PORT = 9876;
    private static final String URL = "jdbc:postgresql://localhost:5432/network_services_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private volatile boolean running = true; // Flag to control server loop

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("🚀 TCP Server is running on port " + PORT + "...");

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("✅ New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle each client in a new thread
                new Thread(new ClientHandler(clientSocket, this)).start();
            }
        } catch (Exception e) {
            if (running) {
                e.printStackTrace();
            } else {
                System.out.println("🔴 Server shut down.");
            }
        }
    }

    /**
     * Handles individual client connections.
     */
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private TCPServer server;

        public ClientHandler(Socket socket, TCPServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

                String clientIp = clientSocket.getInetAddress().getHostAddress();
                int clientPort = clientSocket.getPort();
                String serverIp = "127.0.0.1"; // Assuming server runs locally
                int serverPort = PORT;

                while (true) {
                    String received = in.readLine();
                    if (received == null) break; // Client disconnected

                    System.out.println("📩 Received: " + received + " from " + clientIp + ":" + clientPort);

                    // Insert into the database
                    NetworkScanner ns = new NetworkScanner();
                    ns.insertConnection(connection, "TCP", clientIp, clientPort, serverIp, serverPort);

                    // If the client sends "exit", close everything
                    if ("exit".equalsIgnoreCase(received)) {
                        System.out.println("🔴 Exit command received. Server shutting down...");
                        server.running = false;
                        break;
                    }

                    out.println("✅ Message received!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
