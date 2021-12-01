package Server;

import Server.Paxos.ServerResponse;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HashServerInterface extends Remote {

  //Handler functions for the keymap in the hashserver
  String[] putHandler(String key, String value) throws RemoteException;
  String[] getHandler(String key) throws RemoteException;
  String[] deleteHandler(String key) throws RemoteException;
  ServerResponse giveResponse() throws RemoteException;

  void serverPutHandler(String key, String value) throws RemoteException;
  void serverDeleteHandler(String key) throws RemoteException;

  //Method to register the client to the server's list
  void registerClient(HashClientInterface client) throws RemoteException;
}

