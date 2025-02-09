package rmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.NetworkScanner;

@SuppressWarnings("serial")
public class RMIServer extends UnicastRemoteObject implements RMIService {
    private static final int PORT = 1099;
    private static final String URL = "jdbc:postgresql://localhost:5432/network_services_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private final List<String> messages = new ArrayList<>();

    public RMIServer() throws RemoteException {
        super();
    }

    @Override
    public List<String> getMessages() throws RemoteException {
        return messages;
    }

    @Override
    public void storeMessage(String message) throws RemoteException {
        messages.add(message);
    }

    private void logNetworkActivity() throws RemoteException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sourceIp = null;
			try {
				sourceIp = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            int sourcePort = PORT;
            String destIp = "127.0.0.1";
            int destPort = PORT;

            NetworkScanner ns = new NetworkScanner();
            ns.insertConnection(connection, "RMI", sourceIp, sourcePort, destIp, destPort);
            System.out.println("📡 RMI connection logged.");
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to log RMI network activity: " + e.getMessage());
        }
    }

    @Override
    public String exitServer() throws RemoteException {
        System.out.println("🔴 RMI Server shutting down...");
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return "🔴 RMI Server will shut down shortly...";
    }

    public void start() {
        try {
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(PORT);
                System.out.println("🆕 Created new RMI registry.");
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(PORT);
                System.out.println("🔄 Found existing RMI registry.");
            }

            registry.rebind("RMIService", this);
            System.out.println("🚀 RMI Server is running on port " + PORT);
            logNetworkActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            RMIServer server = new RMIServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}