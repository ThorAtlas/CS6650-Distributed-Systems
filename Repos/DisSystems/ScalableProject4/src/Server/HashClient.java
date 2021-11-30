package Server;

import java.io.IOException;
import java.io.Serializable;
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

public class HashClient implements HashClientInterface, Serializable {

  private final static Logger LOGGER = Logger.getLogger(HashClient.class.getName());
  private static String request;
  private static String[] inputList;
  private static HashServerInterface hashServer;
  private static InetAddress CLIENTID;
  // hello we are testing

  /**
   * A constructor method
   * @param hashServer the server which the client will remotely access
   * @throws UnknownHostException
   * @throws RemoteException
   */
  public HashClient(HashServerInterface hashServer) throws UnknownHostException, RemoteException {
    this.hashServer = hashServer;
    CLIENTID = InetAddress.getLocalHost();
    hashServer.registerClient((HashClientInterface) this);

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
  public static boolean runTest(String str) throws IOException {
    String[] inputList = str.split(" ");
    switch (inputList[0]){
      case "put":
        if(hashServer.putHandler(inputList[1], inputList[2])[0].equalsIgnoreCase("true")){
          return true;
        }else{ return false;}

      case "get":
        if(hashServer.getHandler(inputList[1])[0].equalsIgnoreCase("true")){
          return true;
        }else{ return false;}
      case "delete":
        if(hashServer.deleteHandler(inputList[1])[0].equalsIgnoreCase("true")){
        return true;
      }else{return false;}
      case "count":
        return true;
      default:
        return false;
    }
  }
  public static void runTests() throws IOException {
    LOGGER.log(Level.INFO, "TESTS STARTED at " + getCurrentTimeStamp());
    System.out.println("Testing...");
    completedTest(runTest("put test1 1"));
    completedTest(runTest("put test2 2"));
    completedTest(runTest("put test3 3"));
    completedTest(runTest("put test4 4"));
    completedTest(runTest("put test5 5"));
    completedTest(runTest( "get test1"));
    completedTest(runTest( "get test2"));
    completedTest(runTest( "get test3"));
    completedTest(runTest( "get test4"));
    completedTest(runTest( "get test5"));
    completedTest(runTest( "delete test1"));
    completedTest(runTest( "delete test2"));
    completedTest(runTest( "delete test3"));
    completedTest(runTest( "delete test4"));
    completedTest(runTest( "delete test5"));
    System.out.println("Testing done");
    LOGGER.log(Level.INFO, "TEST FINISHED at " + getCurrentTimeStamp());

  }
  
  public static String[] clientPutHandler(String key, String value) throws RemoteException {
    //String[] answer = hashServer.putHandler(key, value);
    return  hashServer.putHandler(key, value);
  }

  public static String[] clientGetHandler(String key) throws RemoteException {
    return hashServer.getHandler(key);
  }

  public static String[] clientDeleteHandler(String key) throws RemoteException {
    return hashServer.deleteHandler(key);
  }



  public static void main(String[] args) throws IOException {
    HashClient client = null;

    //todo need to change hostname once implementing docker stuff
    String hostName = "localhost";//"my-hashserver";


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
    FileHandler fileHandler = new FileHandler("HashClient.log", true);
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
      client = new HashClient((HashServerInterface) registry.lookup("HashService"));
      LOGGER.log(Level.INFO, "Connected to hashservice via RMI at" + getCurrentTimeStamp());
      runTests();

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
