package http;

public class HTTPMain {
    public static void main(String[] args) {
        HTTPServer server = new HTTPServer();
        HTTPClient client = new HTTPClient();

        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        // Wait for the server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start the client
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
