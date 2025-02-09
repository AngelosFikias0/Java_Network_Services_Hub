package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9876;

    public void start() {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("📩 UDP Client started. Type a message and press Enter (type 'exit' to quit).");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine().trim();

                byte[] sendData = message.getBytes();
                InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                socket.send(sendPacket);
                System.out.println("✅ Message sent!");

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("👋 Client exiting...");
                    break; // Exit loop and close the client
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
