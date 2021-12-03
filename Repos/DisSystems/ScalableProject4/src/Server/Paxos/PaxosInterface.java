package Server.Paxos;

import java.rmi.RemoteException;
import java.util.Map;

public interface PaxosInterface extends ProposerInterface, Acceptor{

  void setMapOfServers(Map<Integer, PaxosInterface> map);
  String[] putHandler(String key, String value) throws RemoteException;
  String[] deleteHandler(String key) throws RemoteException;
  String[] getHandler(String key) throws RemoteException;
  void registerClient(PaxosClient client);

}
