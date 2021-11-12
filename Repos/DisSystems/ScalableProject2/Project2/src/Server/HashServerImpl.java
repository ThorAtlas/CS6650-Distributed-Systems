package Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class HashServerImpl {
  public static void main(String args[]) throws IOException {
    int port = 1099;
    if(args.length == 1){
      port = Integer.parseInt(args[0]);
    }

    try {
      HashInterface server = new HashServer();
      //runs the tests for the server
      ((HashServer) server).runTests();
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind("HashService", server);
    }catch(RemoteException e) {
      e.printStackTrace();
    }
  }
}
