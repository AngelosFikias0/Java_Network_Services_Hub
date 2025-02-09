package http;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import db.NetworkScanner;

public class HTTPServer {
    private static final int PORT = 8080;
    private static final String URL = "jdbc:postgresql://localhost:5432/network_services_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("🚀 HTTP Server is running on port " + PORT + "...");

        server.createContext("/", new RootHandler());
        server.createContext("/exit", new ExitHandler(this));
        server.createContext("/status", new StatusHandler());
        server.createContext("/unknown", exchange -> sendResponse(exchange, 404, "❌ Not Found: Invalid URL"));

        server.setExecutor(null);
        server.start();
    }

    public void stop() {
        System.out.println("🔴 Shutting down server...");
        server.stop(0);
        System.exit(0);
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                sendResponse(exchange, 405, "❌ Method Not Allowed: Use GET");
                return;
            }

            if (!exchange.getRequestURI().getPath().equals("/")) {
                sendResponse(exchange, 404, "❌ Not Found: Invalid URL");
                return;
            }

            sendResponse(exchange, 200, "✅ HTTP Server is running!");
        }
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                sendResponse(exchange, 405, "❌ Method Not Allowed: Use GET");
                return;
            }

            boolean dbConnected = checkDatabaseConnection();
            String response = dbConnected ? "✅ Server and Database are running!" : "⚠️ Server is running, but database is down!";

            sendResponse(exchange, dbConnected ? 200 : 500, response);
        }

        private boolean checkDatabaseConnection() {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                return connection != null && !connection.isClosed();
            } catch (SQLException e) {
                System.err.println("⚠️ Database connection failed: " + e.getMessage());
                return false;
            }
        }
    }

    static class ExitHandler implements HttpHandler {
        private final HTTPServer server;

        public ExitHandler(HTTPServer server) {
            this.server = server;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                sendResponse(exchange, 405, "❌ Method Not Allowed: Use GET");
                return;
            }

            String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
            int clientPort = exchange.getRemoteAddress().getPort();
            String serverIp = "127.0.0.1";
            int serverPort = PORT;

            if (logConnection(clientIp, clientPort, serverIp, serverPort)) {
                sendResponse(exchange, 200, "🔴 Server is shutting down...");
                new Thread(server::stop).start();
            } else {
                sendResponse(exchange, 500, "⚠️ Failed to log connection, but shutting down.");
                new Thread(server::stop).start();
            }
        }

        private boolean logConnection(String clientIp, int clientPort, String serverIp, int serverPort) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                NetworkScanner ns = new NetworkScanner();
                ns.insertConnection(connection, "HTTP", clientIp, clientPort, serverIp, serverPort);
                return true;
            } catch (Exception e) {
                System.err.println("⚠️ Error logging connection: " + e.getMessage());
                return false;
            }
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
