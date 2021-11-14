package Server;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
public class Controller extends java.rmi.server.UnicastRemoteObject  implements ControllerInterface{
  //figure out whats going in ArrayList is it the port? The IP? an object to grab all of the above?
  private static ArrayList<HashServerInterface> listOfServers;
  private static ArrayList<ServerResponse> listOfResponses;
  private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());

  public Controller() throws IOException {
    super();

    //need to establish an IP for the controller
    listOfServers = new ArrayList<>();
    listOfResponses = new ArrayList<>();

    //creates the file handler that'll output the log and then format it properly
    FileHandler fileHandler = new FileHandler("Controller.log", true);
    LOGGER.addHandler(fileHandler);
    SimpleFormatter formatter = new SimpleFormatter();
    fileHandler.setFormatter(formatter);

    LOGGER.log(Level.INFO, "Log started at " + getCurrentTimeStamp());

  }

  public static String getCurrentTimeStamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }

  @Override
  public synchronized void registerServer(HashServerInterface hashServer){
    try{
      listOfServers.add(hashServer);
      LOGGER.log(Level.INFO, "Added a new server at " + getCurrentTimeStamp());
    }catch (Exception e){
      LOGGER.log(Level.INFO, "Unable to add a new server at "+ getCurrentTimeStamp());
    }

  }



  //todo check getResponses work by having it be called in the implementation
  public boolean getResponses() {
    List<ServerResponse> serverResponses = new ArrayList<>();
    for (int i = 0; i< listOfServers.size(); i++){
      serverResponses.add(ServerResponse.ABORT);
    }
    for(int i = 0; i< listOfServers.size(); i++){
      try{
        LOGGER.log(Level.INFO, "Getting Responses from servers at " + getCurrentTimeStamp());

        //getting stuck right the hearing back portion
        ServerResponse response = listOfServers.get(i).getResponse();
        serverResponses.set(i,response);
        LOGGER.log(Level.INFO, "Set the response");
      } catch (RemoteException e) {
        LOGGER.log(Level.WARNING, "Unable to get a response from a server at " + getCurrentTimeStamp());
        e.printStackTrace();
      }
    }

    for (int i =0; i < listOfResponses.size(); i++){
      if(serverResponses.get(i).equals(ServerResponse.ABORT)){
        return false;
      }
    }
    return true;
  }

  @Override
  public synchronized boolean pushDelete(String key) throws RemoteException {
    if (getResponses()){
      try {
        //todo edit logs to be properly documenting
        LOGGER.log(Level.INFO, "Server is calling delete on the servers");
        for (int i = 0; i < listOfServers.size(); i++) {
          listOfServers.get(i).serverDeleteHandler(key);
        }
        return true;
      } catch (Exception e) {
        LOGGER.log(Level.INFO, "Server failed to delete key from servers");
        return false;
      }
    }
    else {
      LOGGER.log(Level.INFO, "Server failed to delete key from servers");
      return false;
    }
  }

  @Override
  public synchronized boolean pushPut(String key, String value) throws RemoteException {
    if (getResponses()) {
      try {
        //todo edit logs to be properly documenting
        LOGGER.log(Level.INFO, "Server is calling push on the servers at " + getCurrentTimeStamp());
        for (int i = 0; i < listOfServers.size(); i++) {
          listOfServers.get(i).serverPutHandler(key, value);
        }
        LOGGER.log(Level.INFO, "Server pushed key-pair to servers at "+ getCurrentTimeStamp());
        return true;
      } catch (Exception e) {
        LOGGER.log(Level.INFO, "Server failed to push key-pair to servers at " + getCurrentTimeStamp());
        return false;
      }

    }
    else {
      LOGGER.log(Level.INFO, "Server failed to push key-pair to servers at " + getCurrentTimeStamp());
      return false;
    }

  }


}
