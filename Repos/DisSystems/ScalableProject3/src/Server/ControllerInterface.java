package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Remote {

  /**
   * Registers a hashServer to the controller
   * @param hashServer the hashServer to be registered
   */
  void registerServer(HashServerInterface hasServer) throws RemoteException;

  /**
   * This function checks all connected servers to the controller and see's if they are ready for
   * a new commit
   * @return if all responses are commit returns true, otherwise false
   */
  boolean getResponses() throws RemoteException;

  /**
   * Pushes the delete function to all the servers if they are capable of doing so
   * @param key the key to delete
   * @return true if the key is properly deleted from all servers, false otherwise
   * @throws RemoteException
   */
  boolean pushDelete(String key) throws RemoteException;

  /**
   * pushes the put command to every server
   * @param key the key value to push
   * @param value the value of the key to push
   * @return true if all servers are committed with a push, false otherwise
   * @throws RemoteException
   */
  boolean pushPut(String key, String value) throws RemoteException;
}
