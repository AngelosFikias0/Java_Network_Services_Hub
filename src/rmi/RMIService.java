package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMIService extends Remote {
    List<String> getMessages() throws RemoteException;
    void storeMessage(String message) throws RemoteException;
    String exitServer() throws RemoteException;
}
