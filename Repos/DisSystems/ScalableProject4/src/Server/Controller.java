package Server;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.util.logging.Logger;

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

  //The arraylist storing each server connected to the controller
  private static ArrayList<HashServerInterface> listOfServers;
  private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());

  public Controller() throws IOException {
    super();

    listOfServers = new ArrayList<>();

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


  @Override
  public boolean getResponses() {
    List<ServerResponse> serverResponses = new ArrayList<>();
    for (int i = 0; i< listOfServers.size(); i++){
      serverResponses.add(ServerResponse.ABORT);
    }
    for(int i = 0; i< listOfServers.size(); i++){
        //This atempts to connect to each server
        try {
          LOGGER.log(Level.INFO, "Getting Responses from servers at " + getCurrentTimeStamp());
          //have to create a temporary final int for a lamda function
          int finalI = i;
          //setting server response to a proper response
          ServerResponse response = CompletableFuture.supplyAsync(() -> {
            try {
              //if it goes through we set it as commit
              return listOfServers.get(finalI).giveResponse();
            } catch (RemoteException e) {
              LOGGER.log(Level.WARNING, "Could not get a response from a server");
              //e.printStackTrace();
            }
            //otherwise we return the response as abort
            return ServerResponse.ABORT;

            //This .get sets a timeout response. If it takes too long it will throw a timeout error
          }).get(1, TimeUnit.SECONDS);
          //Here we set the response to the i value in the server responses
          serverResponses.set(i,response);
          LOGGER.log(Level.INFO, "Set the response");

        } catch (TimeoutException e) {
          System.out.println("Time out has occurred");
        } catch (InterruptedException | ExecutionException e) {
        }
    }

    //if each response is a COMMIT then we return true, otherwise it's an abort. It's faster to
    //check for one abort than for all commits on average.
    for (int i =0; i < serverResponses.size(); i++){
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

  /**
   * pushes the put command to every server
   * @param key the key value to push
   * @param value the value of the key to push
   * @return true if all servers are committed with a push, false otherwise
   * @throws RemoteException
   */
  @Override
  public synchronized boolean pushPut(String key, String value) throws RemoteException {
    if (getResponses()) {
      try {
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
