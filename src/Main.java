import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Network Services Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Select a Service to Launch", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Panel for Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // Create Buttons with Icons
        JButton httpButton = createButton("HTTP Service", "http.HTTPMain", "1)");
        JButton tcpButton = createButton("TCP Service", "tcp.TCPMain", "2)");
        JButton udpButton = createButton("UDP Service", "udp.UDPMain", "3)");
        JButton rmiButton = createButton("RMI Service", "rmi.RMIMain", "4)");

        // Add buttons to panel
        buttonPanel.add(httpButton);
        buttonPanel.add(tcpButton);
        buttonPanel.add(udpButton);
        buttonPanel.add(rmiButton);

        // Add components to frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static JButton createButton(String text, String mainClass, String emoji) {
        JButton button = new JButton(emoji + "  " + text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(50, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 100, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 120, 220));
            }
        });

        button.addActionListener(e -> launchService(mainClass));
        return button;
    }

    private static void launchService(String mainClass) {
        new Thread(() -> {
            try {
                // Debug: Print the main class being launched
                System.out.println("Attempting to launch: " + mainClass);

                // Check if the main class exists
                Class<?> clazz = Class.forName(mainClass);

                // Get the main method of the class
                java.lang.reflect.Method mainMethod = clazz.getMethod("main", String[].class);

                // Invoke the main method
                System.out.println("Launching " + mainClass + "...");
                mainMethod.invoke(null, (Object) new String[]{}); // Pass empty arguments
                System.out.println(mainClass + " has finished executing.");
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + mainClass);
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                System.err.println("No main method found in: " + mainClass);
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Failed to launch " + mainClass);
                e.printStackTrace();
            }
        }).start();
    }
}