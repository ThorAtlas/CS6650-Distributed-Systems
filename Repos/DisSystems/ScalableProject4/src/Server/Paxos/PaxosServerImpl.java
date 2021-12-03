package Server.Paxos;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaxosServerImpl {

  public static void main(String[] args) throws RemoteException {

    /**
     * Input/Execution/implementation would be as follows:
     * 1) Arguments would be a list of ports for PaxosServers to be implemneted together
     * 2) It would parse through the arguments and get the value of each one
     */
    Map<Integer, PaxosInterface> listOfServer = new HashMap<>();

    //Adds the port vlaues to the hashmap
    for(String port : args){
      Integer portValue = Integer.valueOf(port);
      listOfServer.put(portValue, null);
    }
    //Now we have to create each server and set it to that port value
    for(Integer port: listOfServer.keySet()){
      PaxosServer server = new PaxosServer(listOfServer, port);
      listOfServer.put(port, server);
    }

    //Nowe we have to change each list of server in each of the servers because right now they are incorrect
    for(Integer port: listOfServer.keySet()){
      //This changes each server list properly
      PaxosServer server = (PaxosServer) listOfServer.get(port);
      server.setMapOfServers(listOfServer);

      //This then registers that server with a registry to its port value
      Registry paxosRegistry = LocateRegistry.createRegistry(port);
      paxosRegistry.rebind("PaxosService", server);
    }


  }

}
