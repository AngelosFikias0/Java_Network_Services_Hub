package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NetworkScanner {
	
    public void insertConnection(Connection connection, String protocol, String sourceIp, int sourcePort, String destIp, int destPort) {
        String query = "INSERT INTO connections (protocol, source_ip, source_port, destination_ip, destination_port) " +
                       "VALUES (?, ?::INET, ?, ?::INET, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, protocol);
            stmt.setString(2, sourceIp);  // Store IP as string
            stmt.setInt(3, sourcePort);
            stmt.setString(4, destIp);
            stmt.setInt(5, destPort);

            stmt.executeUpdate();
            System.out.println("✅ Inserted: " + protocol + " | " + sourceIp + ":" + sourcePort + " → " + destIp + ":" + destPort);
        } catch (SQLException e) {
            System.err.println("❌ Insert failed: " + e.getMessage());
        }
    }
}
