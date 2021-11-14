package Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ControllerImpl {
  public static void main(String args[]) throws IOException {

    //default port is 1111
    int port = 1111;

    //if ran through terminal with an optional port value it assigns it the port
    if(args.length == 1){
      port = Integer.parseInt(args[0]);
    }

    try {
      ControllerInterface controller = new Controller();
      //runs the tests for the server
      Registry registry = LocateRegistry.createRegistry(port);
      registry.rebind("Controller",  controller);
    }catch(RemoteException e) {
      e.printStackTrace();
    }
  }
}
