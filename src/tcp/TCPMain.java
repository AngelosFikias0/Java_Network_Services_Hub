package tcp;

public class TCPMain {
    public static void main(String[] args) {
        TCPServer server = new TCPServer();
        TCPClient client = new TCPClient();

        // Start server in a separate thread
        Thread serverThread = new Thread(server::start);
        serverThread.start();

        // Wait for the server to start before launching the client
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Start the client in the main thread
        client.start();

        // Ensure server exits when client exits
        try {
            serverThread.join();
            System.out.println("✅ Server has shut down.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
