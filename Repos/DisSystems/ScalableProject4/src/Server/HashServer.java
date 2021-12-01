package Server;


import Server.Paxos.ServerResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class HashServer extends java.rmi.server.UnicastRemoteObject implements HashServerInterface {

  private final static Logger LOGGER = Logger.getLogger(HashServer.class.getName());
  private static ConcurrentHashMap<String, String> keyPairMap =
      new ConcurrentHashMap<String, String>();
  private static ArrayList<HashClientInterface> clientArrayList;

  public static String getCurrentTimeStamp() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
  }

  private static InetAddress SERVERID;
  private static ControllerInterface controller;


  /**
   * Constructor for the hashserver
   * @throws IOException
   */
  protected HashServer(ControllerInterface controller) throws IOException {
    super();
    clientArrayList = new ArrayList<>();
    this.controller = controller;
    SERVERID = InetAddress.getLocalHost();
    controller.registerServer((HashServerInterface) this);


    //creates the file handler that'll output the log and then format it properly
    FileHandler fileHandler = new FileHandler("HashServer.log", true);
    LOGGER.addHandler(fileHandler);
    SimpleFormatter formatter = new SimpleFormatter();
    fileHandler.setFormatter(formatter);

    LOGGER.log(Level.INFO, "Log started at " + getCurrentTimeStamp());
  }

  //Function that registers a client to the client list
  @Override
  public synchronized void registerClient(HashClientInterface client){
    clientArrayList.add(client);
  }


  @Override
  public synchronized String[] putHandler(String key, String value) throws RemoteException {
    String[] output = new String[2];
    LOGGER.log(Level.INFO, "Asking controller for put at " + getCurrentTimeStamp());
    if(controller.pushPut(key,value)){ output[0] = "true"; }
    else{output[0] = "false";}
    return output;
  }

  @Override
  public synchronized String[] deleteHandler(String key) throws RemoteException {
    LOGGER.log(Level.INFO, "Delete request for " + key + " at " + getCurrentTimeStamp());
    String[] output = new String[2];
    if(controller.pushDelete(key)){ output[0] = "true"; }
    else{output[0] = "false";}
    return output;

  }

  /**
   * Put handler that the controller will call
   * @param key
   * @param value
   * @throws RemoteException
   */
  public void serverPutHandler(String key, String value) throws RemoteException{
    LOGGER.log(Level.INFO, "Put completed for " + key + " "
            + value + " at " + getCurrentTimeStamp());
    keyPairMap.put(key, value);
  }

  /**
   * The delete handler that the controller will call
   * @param key
   * @throws RemoteException
   */
  public void serverDeleteHandler(String key) throws RemoteException{
    LOGGER.log(Level.INFO, "Delete completed for " + key + " at " + getCurrentTimeStamp());
    try{
      keyPairMap.remove(key);
    }catch (Exception e){
    }
  }

  @Override
  public synchronized String[] getHandler(String key) throws RemoteException {
    LOGGER.log(Level.INFO, "Get request for " + key + " at " + getCurrentTimeStamp());
    String[] output = new String[2];
    output[0] = "false";
    try{
      output[0] = "true";
      output[1] = keyPairMap.get(key);
    }catch (Exception e){
      output[0] = "false";
    }

    return output;

  }


  /**
   * This message is given to the controller stating that the server is ready for a commit
   * @return a commit message if the server is ready for a commit or not
   * @throws RemoteException
   */
  @Override
  public ServerResponse giveResponse() throws RemoteException {
    LOGGER.log(Level.INFO, "Return commit to server at " + getCurrentTimeStamp());
    return ServerResponse.COMMIT;
  }


  /**
   * Testing functions to make sure server runs properly
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
  public boolean runTest(String str) throws IOException {
    String[] inputList = str.split(" ");
    switch (inputList[0]){
      case "put":
        if(putHandler(inputList[1], inputList[2])[0].equalsIgnoreCase("true")){
          return true;
        }else{ return false;}
      case "get":
        if(getHandler(inputList[1])[0].equalsIgnoreCase("true")){
          return true;
        }else{ return false;}
      case "delete":
        if(deleteHandler(inputList[1])[0].equalsIgnoreCase("true")){
          return true;
        }else{return false;}
      case "count":
        return true;
      default:
        return false;
    }
  }
  public void runTests() throws IOException {
    LOGGER.log(Level.INFO, "TESTS STARTED at "+ getCurrentTimeStamp());
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


}
