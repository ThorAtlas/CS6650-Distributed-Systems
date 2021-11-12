package Server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This controller will act as a mediator for the servers and clients
 * Order of interaction:
 * Client will interact with a server, they can connect to any one of the servers
 * On a read request the server will respond
 * One a delete or put request the server will ping the controller and ask for a twophase commit
 * protocol to commence
 * The controller will request for the servers to commit to the operation
 *  ON SUCCESS: All servers ping back commit
 *  ON FAILURE: At least one server does not ping back or gives and abort signal
 * On a success the controller server will call a RPC on ALL servers to put or delete and item
 * in their data
 * On a failure the controller will not call anything and will not perform an action.
 */
public class Controller {
  //figure out whats going in ArrayList is it the port? The IP? an object to grab all of the above?
  private static ArrayList<HashInterface> listOfServers;

  public Controller(){
    //need to establish an IP for the controller
    listOfServers = new ArrayList<>();

  }

  public void registerServer(){

  }

}
