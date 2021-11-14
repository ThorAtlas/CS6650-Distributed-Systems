package Server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class HashServerImpl {
  public static void main(String args[]) throws IOException {

    //todo will need to edit this so that an argument can be made for the hostname
    //of the controller
    int port = 1099;

    //todo will need to change controller hostname also with docker stuff
    String controllerHost = "localhost";//"Controller";
    int controllerPort = 1111;
    HashServerInterface server = null;

    //if only one argument is given then the default port of the controller
    //remains 1111
    if(args.length == 1){
      port = Integer.parseInt(args[0]);
    }

    //Allows for 2 arguments for custom ports
    if(args.length == 2){
      port = Integer.parseInt(args[0]);
      controllerPort = Integer.parseInt(args[1]);
    }

    Scanner scanner = new Scanner((System.in));



    try {
      //need to fix here for the controller situation
      //todo fix server implementation here
      //server = new HashServer();
      //runs the tests for the server
      //((HashServer) server).runTests();

      //todo need to change this when implementing docker this is for testing purposes
      System.out.println("What port do you want to assign this server?");
      port = Integer.parseInt(scanner.nextLine());

      //gets the controllerRegistry and object and links it to the HashServer
      Registry controllerRegistery =
              LocateRegistry.getRegistry(controllerHost, controllerPort);
      server = new HashServer((ControllerInterface) controllerRegistery.lookup("Controller"));

      //binds the Server's port to the port given when called
      Registry serverRegistry = LocateRegistry.createRegistry(port);
      serverRegistry.rebind("HashService", server);



    }catch(RemoteException e) {
      e.printStackTrace();
    } catch (NotBoundException e) {
      e.printStackTrace();
    }
  }
}
