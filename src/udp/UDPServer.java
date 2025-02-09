package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import db.NetworkScanner;

public class UDPServer {
    private static final int PORT = 9876;
    private static final String URL = "jdbc:postgresql://localhost:5432/network_services_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private volatile boolean running = true; // Flag to control server loop

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("🚀 UDP Server is running on port " + PORT + "...");

            while (running) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength()).trim();

                // If client sends "exit", shut down the server
                if ("exit".equalsIgnoreCase(received)) {
                    System.out.println("❌ Exit signal received. Shutting down server...");
                    running = false;
                    break; // Exit loop and close the server
                }

                // Handle each request in a new thread
                new Thread(new ClientHandler(packet, this)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles individual client messages in separate threads.
     */
    static class ClientHandler implements Runnable {
        private DatagramPacket packet;
        private UDPServer server; // Reference to stop the server if needed

        public ClientHandler(DatagramPacket packet, UDPServer server) {
            this.packet = packet;
            this.server = server;
        }

        @Override
        public void run() {
            try (DatagramSocket responseSocket = new DatagramSocket();
                 Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

                String received = new String(packet.getData(), 0, packet.getLength()).trim();
                String sourceIp = packet.getAddress().getHostAddress();
                int sourcePort = packet.getPort();
                String destIp = "127.0.0.1"; // Assuming server runs locally
                int destPort = PORT;

                System.out.println("📩 Received: " + received + " from " + sourceIp + ":" + sourcePort);

                // Insert into the database
                NetworkScanner ns = new NetworkScanner();
                ns.insertConnection(connection, "UDP", sourceIp, sourcePort, destIp, destPort);

                sendAcknowledgment(responseSocket, packet);

                // If client sends "exit", notify the server to shut down
                if ("exit".equalsIgnoreCase(received)) {
                    System.out.println("🔴 Exit command received. Server will stop.");
                    server.running = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendAcknowledgment(DatagramSocket socket, DatagramPacket packet) throws Exception {
            String response = "✅ Message received!";
            byte[] responseData = response.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
            socket.send(responsePacket);
        }
    }
}