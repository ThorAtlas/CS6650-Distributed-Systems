package Server.Paxos;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PaxosClient {

  private final static Logger LOGGER = Logger.getLogger(PaxosClient.class.getName());
  private static String request;
  private static String[] inputList;
  private static PaxosInterface paxosServer;
  private static InetAddress CLIENTID;
  // hello we are testing

  /**
   * A constructor method
   * @throws UnknownHostException
   * @throws RemoteException
   */
  public PaxosClient(PaxosInterface paxos) throws UnknownHostException, RemoteException {
    this.paxosServer = paxos;
    CLIENTID = InetAddress.getLocalHost();
    paxosServer.registerClient((PaxosClient) this);

  }

  public static String getCurrentTimeStamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }


  /**
   * Testing functions
   * @param complete
   * @throws IOException
   */
  public static void completedTest(Boolean complete) throws IOException {
    if (complete) {
      System.out.println("passed");
    }
    else{
      System.out.println("failed");
    }
  }

  public static String[] clientPutHandler(String key, String value) throws RemoteException {
    return  paxosServer.putHandler(key, value);
  }

  public static String[] clientGetHandler(String key) throws RemoteException {
    return paxosServer.getHandler(key);
  }

  public static String[] clientDeleteHandler(String key) throws RemoteException {
    return paxosServer.deleteHandler(key);
  }



  public static void main(String[] args) throws IOException {
    PaxosClient client = null;

    //todo need to change hostname once implementing docker stuff
    String hostName = "localhost";


    //defualt port for a server is 1099
    int port = 1099;
    if(args.length == 1){
      hostName = args[0];
    }
    else if(args.length == 2){
      hostName = args[0];
      port = Integer.valueOf(args[1]);
    }

    /**
     * Starting the log with proper formatting
     */
    FileHandler fileHandler = new FileHandler("PaxosClient.log", true);
    LOGGER.addHandler(fileHandler);
    SimpleFormatter formatter = new SimpleFormatter();
    fileHandler.setFormatter(formatter);
    LOGGER.log(Level.INFO, "Log started at " + getCurrentTimeStamp());
    Scanner scanner = new Scanner((System.in));

    //todo change this after done with testing
    System.out.println("What server port do you want to connect to?");
    port = Integer.parseInt(scanner.nextLine());




    /**
     * Getting the server to request from
     */
    try {
      Registry registry = LocateRegistry.getRegistry(hostName, port);
      client = new PaxosClient((PaxosInterface) registry.lookup("PaxosService"));
      LOGGER.log(Level.INFO, "Connected to paxosService via RMI at" + getCurrentTimeStamp());

    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Unsuccessful connection");
      System.out.println("Not making it" + e);
    }


    //While loop to have the client continuously run
    while (true) {

      /**
       * While loop to get a proper request
       */
      while (true) {
        System.out.println("What's your request?");
        String input = scanner.nextLine();
        inputList = input.split(" ");
        request = inputList[0].toLowerCase();
        //If the request is proper we break the cycle
        if (request.equals("put") || request.equals("get") || request.equals("delete")) {
          break;
        } else {
          System.out.println("Invalid request try again");
        }
      }

      /**
       * Switch cases to determine what to do based on the input
       */
      switch (request) {
        case "put":
          if (inputList.length < 3){
            LOGGER.log(Level.INFO, "Unsuccessful put request at " + getCurrentTimeStamp());
            System.out.println("Not enough arguments for put request");
            break;
          }

          try{
            if(client.clientPutHandler(inputList[1], inputList[2])[0].equalsIgnoreCase("true")) {
              System.out.println("ADDED PAIR " + inputList[1] + " : " + inputList[2]);
              LOGGER.log(Level.INFO, "ADDED PAIR " + inputList[1] + " : "
                  + inputList[2] + " at " + getCurrentTimeStamp());
            }
            else{
              LOGGER.log(Level.WARNING, "Unsuccessful put request at " + getCurrentTimeStamp());
            }
          }catch (Exception e){
            LOGGER.log(Level.WARNING, "Unsuccessful put request at " + getCurrentTimeStamp());
          }
          break;
        case "get":
          if (inputList.length < 2){
            LOGGER.log(Level.INFO, "Unsuccessful get request at " + getCurrentTimeStamp());
            System.out.println("Not enough arguments for get request");
            break;
          }
          try{
            System.out.println(client.clientGetHandler(inputList[1])[1]);
            LOGGER.log(Level.INFO, "Successful get request for " + inputList[1]
                + " at " + getCurrentTimeStamp());
          }catch (Exception e){
            LOGGER.log(Level.INFO, "Unsuccessful get request for " + inputList[1]
                + " at " + getCurrentTimeStamp());
          }

          break;
        case "delete":
          if (inputList.length < 2){
            LOGGER.log(Level.INFO, "Unsuccessful delete request at " + getCurrentTimeStamp());
            System.out.println("Not enough arguments for delete request");
            break;
          }
          try{
            if(client.clientDeleteHandler(inputList[1])[0].equalsIgnoreCase("true")) {
              LOGGER.log(Level.INFO, "Successful delete request of " + inputList[1]
                  + "at " + getCurrentTimeStamp());
            }
            else{
              LOGGER.log(Level.INFO, "Unsuccessful delete request of " + inputList[1]
                  + "at " + getCurrentTimeStamp());
            }
          }catch (Exception e){
            LOGGER.log(Level.INFO, "Unsuccessful delete request of " + inputList[1]
                + "at " + getCurrentTimeStamp());
          }
          break;

        default:
          System.out.println("Request was not valid try again");
          break;
      }


    }
  }

}
