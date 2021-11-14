package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote {

  void registerServer(HashServerInterface hasServer) throws RemoteException;
  public boolean getResponses() throws RemoteException;
  public boolean pushDelete(String key) throws RemoteException;
  public boolean pushPut(String key, String value) throws RemoteException;
}
