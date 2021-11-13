package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote {

  void registerServer(HashServerInterface hasServer) throws RemoteException;
//  public void getResponses();
//  public void pushDelete();
//  public void pushPut();
}
