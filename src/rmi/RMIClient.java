package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    private static final int PORT = 1099;

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PORT);
            RMIService service = (RMIService) registry.lookup("RMIService");

            System.out.println("📩 RMI Client connected to server.");
            System.out.println("Commands: 'send' (send message), 'message' (get all messages), 'exit' (shut down server).");

            while (true) {
                System.out.print("Enter command: ");
                String command = scanner.nextLine().trim().toLowerCase();

                switch (command) {
                    case "exit":
                        System.out.println(service.exitServer());
                        System.out.println("👋 Client exiting...");
                        return;

                    case "message":
                        System.out.println("📨 Messages: " + service.getMessages());
                        break;

                    case "send":
                        System.out.print("Enter your message (type 'STOP' to finish): ");
                        StringBuilder message = new StringBuilder();
                        while (scanner.hasNext()) {
                            String word = scanner.next();
                            if (word.equalsIgnoreCase("STOP")) break;
                            message.append(word).append(" ");
                        }
                        service.storeMessage(message.toString().trim());
                        System.out.println("📤 Message sent.");
                        break;

                    default:
                        System.out.println("❌ Unknown command.");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
