package rmi;

import java.rmi.RemoteException;

public class RMIMain {
    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
			try {
				new RMIServer().start();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        serverThread.start();

        // Allow server time to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start the client
        new RMIClient().start();

        // Ensure server exits when client exits
        try {
            serverThread.join();
            System.out.println("✅ RMI Server has shut down.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
