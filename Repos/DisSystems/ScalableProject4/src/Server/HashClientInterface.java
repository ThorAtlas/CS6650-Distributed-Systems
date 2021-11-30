package Server;

import java.rmi.RemoteException;

public interface HashClientInterface {


  /**
   * Puthandler for storing key:value pairs
   * @param key a string
   * @param value a string
   * @return a list of Strings stating if the value was added successfuly or not
   * @throws RemoteException
   */
  static String[] clientPutHandler(String key, String value) throws RemoteException {
    return new String[0];
  }
  /**
   * Gethandler for getting a value from a key
   * @param key a string
   * @return a list of Strings stating if the value was added successfuly or not,
   * and the value of stored at that key
   * @throws RemoteException
   */
  static String[] clientGetHandler(String key) throws RemoteException {
    return new String[0];
  }
  /**
   * Deletehandler for storing key:value pairs
   * @param key a string
   * @return a list of Strings stating if the value was added successfuly or not and
   * the value of the removed key
   * @throws RemoteException
   */
  static String[] clientDeleteHandler(String key) throws RemoteException {
    return new String[0];
  }
}
