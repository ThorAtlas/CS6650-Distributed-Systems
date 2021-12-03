package Server.Paxos;

import Server.Commands.Command;
import Server.Commands.DeleteCommand;
import Server.Commands.PutCommand;
import Server.HashClientInterface;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

/**
 * Every server on the paxos will be a Acceptor, Proposer, and a Learner
 */
public class PaxosServer extends java.rmi.server.UnicastRemoteObject implements PaxosInterface{
  //ID of the machine whcih in this case will be the machine's port number
  int machineID;

  //The highestSequenceID recognized for use when it's an acceptor
  private int highestSequenceID;

  //Initializes the accepted statement as false
  private boolean accepted = false;

  //creates the highestProposal object for the class used by the Acceptor
  private ProposalObject highestProposalAcceptor;

  //creates the highestProposal Object for the class used by the Proposer
  private ProposalObject highestProposalProposer;

  //List of responses from the responders
  Set<Integer> listOfResponses = new HashSet();
  //List of responses from the acceptors about if they accepted an issue
  Set<Integer> listOfIssueResponses = new HashSet<>();

  //List of all the servers connected to the network (knows where to send things to
  //imported from main arguments
  Map<Integer, PaxosInterface> listOfServers;

  //Client list
  private static ArrayList<PaxosClient> clientArrayList;

  //This is the concurrent hashmap to be used when executing a command
  private static ConcurrentHashMap<String, String> keyPairMap =
      new ConcurrentHashMap<String, String>();


  public PaxosServer(Map<Integer, PaxosInterface> serverList, Integer ID) throws RemoteException {
    super();

    listOfServers = serverList;
    //todo access the machine's ID and set it as such have constructor arguments
    machineID = ID;
  }

  //Function that registers a client to the client list
  @Override
  public synchronized void registerClient(PaxosClient client){
    clientArrayList.add(client);
  }


  ///////////////////////////////////////////////////////////////////////////////
  //PHASE 1 STUFF
  ///////////////////////////////////////////////////////////////////////////////
  /**
   * Function that sends the proposal from the Proposer to the Acceptor
   * @param proposal the actual proposal containing the data and where it originally came from,
   *                 the proposal object is created by the request from the client.
   */
  @Override
  public synchronized boolean sendProposal(ProposalObject proposal) throws RemoteException {
    //here we set the proposal value for the proposer
    highestProposalProposer = proposal;
    //Go through each server and make them receive a proposal once they receive a proposal there response will be recorded
    for(Integer ID: listOfServers.keySet()){
      //remote call on the server
      listOfServers.get(ID).receiveProposal(highestProposalProposer);
    }
    //After each server is ran through with the proposal we call onconsensus to make sure we have a consensus to continue
    if(onConsensusPromise()){
      try{
        if(issueProposal(highestProposalProposer));
      }catch (Exception e){
        return false;
      }
    }else{
      return false;
    }
    return false;
  }

  /**
   * Occurs once a proposal is received by the acceptor
   * A remote call called by sendProposal on the acceptor
   * @param proposal
   */
  @Override
  public synchronized void receiveProposal(ProposalObject proposal) {
    /**todo: implement a timeout here using a random number generator that if it reached a certain value
     * it would timeout. This would occur on each of the acceptors functions
     * This would mean every PaxosServer would be threaded or be an executor
     * This would be extended to every other functions to ensure that they would shut down
     * randomly
     **/

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<String> future = executor.submit(new Callable() {

      public String call() throws Exception {
        ProposalObject theProposal = proposal;
        if(theProposal.sequenceID > highestSequenceID){
          highestSequenceID = theProposal.sequenceID;
          acceptProposal(theProposal);
        }
        else{
          reject(theProposal);
        }
        return "OK";
      }
    });
    try {

      /**
       * This will shut down the executor if the timeout reaches 5 seconds, and will randomly shutdown/not work
       * If the random generated int is found to be less than 3.
       */
      Random random = new Random();
      boolean shutdown = 3 > random.nextInt(10 - 1 + 1) + 1;
      if (shutdown){
        System.out.println(future.get(0, TimeUnit.SECONDS));
      }else{
        System.out.println(future.get(5, TimeUnit.SECONDS)); //timeout is in 5 seconds
      }

    } catch (TimeoutException | InterruptedException | ExecutionException e) {
      System.err.println("Timeout");
    }
    executor.shutdownNow();

    //If the proposal's sequenceID is higher than the current highestSequenceID then accept it

  }

  /**
   * Accepts a proposal from a proposer
   */
  @Override
  public void acceptProposal(ProposalObject proposal) {
    if(accepted){
      //return a response to the proposer saying we've already accepted someone else's
      sendPromise(highestProposalAcceptor, PaxosResponse.ACCEPTCONDITIONALLY);
    }
    else {
      highestProposalAcceptor = proposal;
      sendPromise(highestProposalAcceptor, PaxosResponse.ACCEPT);
      accepted = true;
    }
  }

  /**
   * Sends a promise response to a proposer after it has accepted a proposal
   * @param proposal the proposal to be sent
   * @param response the response from the acceptor
   */
  @Override
  public void sendPromise(ProposalObject proposal, PaxosResponse response) {
    //Fidns the proposer that matches the proposal's ID
    ProposerInterface proposer = listOfServers.get(proposal.machineID);

    //Calls the proposer's onReceivedAccept
    if(proposer != null){
      //remote call on proposer because it received an accept message
      proposer.onReceivedAccept(response, proposal, this.machineID);
    }

  }


  /**
   * Function called when an Acceptor accepts a proposal called on the Proposer
   * @param response takes a response
   */
  @Override
  public void onReceivedAccept(PaxosResponse response, ProposalObject proposal, Integer acceptorID) {
    if(response.equals(PaxosResponse.ACCEPTCONDITIONALLY)){
      //we change the command if the returned proposal has a higher sequenceID
      if(proposal.sequenceID > highestProposalProposer.sequenceID){
        highestProposalProposer.command = proposal.command;
      }
      //add the server to the list of responders
      listOfResponses.add(acceptorID);
    }
    if(response.equals(PaxosResponse.ACCEPT)){
      listOfResponses.add(acceptorID);
    }
  }

  /**
   * Function called to check consensus. If consensus is met then it'll issue the proposal to each
   * acceptor
   */
  @Override
  public boolean onConsensusPromise() throws RemoteException {
    int numToBeat = (listOfServers.size() / 2);
    if(listOfResponses.size() > numToBeat){
      return true;
      }
    return false;
    }

  //////////////////////////////////////////////////////////////////////
  //PHASE 2 THE ISSUING/LEARNING
  ////////////////////////////////////////////////////////////////////
  /**
   * Function that sends an issue from the Proposer to the Acceptor
   * @param proposal
   */
  public synchronized boolean issueProposal(ProposalObject proposal) throws RemoteException {
    //Go through each server and make them receive a proposal once they receive a proposal there response will be recorded
    for (Integer ID : listOfServers.keySet()) {
      //remote call on the server
      listOfServers.get(ID).receiveIssue(proposal);
    }
    //After each server is ran through with the proposal we call onconsensus to make sure we have a consensus to continue
    try{
      return onConsensusIssue();
    }catch (Exception e){
      return false;
    }

  }


  /**
   * Proposer issues a proposal to the acceptors that have promised. Receiveissue is called on the
   * acceptor remotely
   * @param proposal
   * @throws RemoteException
   */
  public synchronized void receiveIssue(ProposalObject proposal) throws RemoteException {
    if(proposal.sequenceID >= highestSequenceID) {
      acceptIssue(proposal);
    }
  }

  /**
   * The acceptor then accepts the issue which responds to the proposer, and changes its
   * highestProposal to the proposal it was just issued.
   * @param proposal
   * @throws RemoteException
   */
  @Override
  public void acceptIssue(ProposalObject proposal) throws RemoteException {
    //todo go through each server's keyvalue and read the command
    highestProposalAcceptor = proposal;
    ProposerInterface proposer = listOfServers.get(proposal.machineID);
    proposer.issueResponse(proposal, machineID);
  }

  //Function called on the Proposer by the Acceptor to add its ID as an acceptor that has responded
  public void issueResponse(ProposalObject proposal, Integer acceptorID) {
      listOfIssueResponses.add(acceptorID);
  }

  /**
   * Same idea as previous onCensus but now if it passes we call on every server/learner to
   * execute the command of the highest proposal.
   * @throws RemoteException
   */
  public boolean onConsensusIssue() throws RemoteException {
    boolean passed = false;
    int numToBeat = (listOfServers.size() / 2);
    if(listOfResponses.size() > numToBeat){
      passed = true;
      for(Integer id: listOfServers.keySet()){
        //calling receiveIssue on each server remotely execute the commands
        ((PaxosServer)listOfServers.get(id)).executeCommand(highestProposalProposer);
      }
    }

    //clears the responses of promises and issue responses to make room for the next proposal
    listOfResponses.clear();
    listOfIssueResponses.clear();
    return passed;
  }

  /**
   * Function be called by the proposer on the Learner once it reaches a consensus.
   * This will execute the commands needed to delete or put a function based on a proposal and then
   * state it no longer has accepted a proposal
   * @param proposal
   * @throws RemoteException
   */
  public void executeCommand(ProposalObject proposal) throws RemoteException {
    String key = null;
    String value = null;
    if(proposal.command.getCommand().get(0).equals("DELETE")){
      key = proposal.command.getCommand().get(1);
        this.serverDeleteHandler(key);

    }
    if(proposal.command.getCommand().get(0).equals("PUT")){
      key = proposal.command.getCommand().get(1);
      value = key = proposal.command.getCommand().get(2);
        this.serverPutHandler(key, value);
    }
    else{
    }
    //resets the accept statement to false
    this.accepted = false;

  }


  @Override
  public void reject(ProposalObject proposal) {
    //ignore and don't send anything back

  }


  /**
   * Put handler that the controller will call
   * @param key
   * @param value
   * @throws RemoteException
   */
  public void serverPutHandler(String key, String value) throws RemoteException{
    keyPairMap.put(key, value);
  }

  /**
   * The delete handler that the controller will call
   * @param key
   * @throws RemoteException
   */
  public void serverDeleteHandler(String key) throws RemoteException{
    try{
      keyPairMap.remove(key);
    }catch (Exception e){
    }
  }

  /**
   * Function to set the list of maps for each server
   * @param map
   */
  @Override
  public void setMapOfServers(Map<Integer, PaxosInterface> map) {
    listOfServers = map;
  }

  @Override
  public synchronized String[] putHandler(String key, String value) throws RemoteException {
    String[] output = new String[2];
    Command command = new PutCommand(key, value);
    //we do highestSequenceID++ because it'll add one to the highestSequenceID we currently have stored
    ProposalObject proposal = new ProposalObject(this.machineID, highestSequenceID++, command);
    if(sendProposal(proposal)){ output[0] = "true"; }
    else{output[0] = "false";}
    return output;
  }

  @Override
  public synchronized String[] deleteHandler(String key) throws RemoteException {
    String[] output = new String[2];
    Command command = new DeleteCommand(key);
    //we do highestSequenceID++ because it'll add one to the highestSequenceID we currently have stored
    ProposalObject proposal = new ProposalObject(this.machineID, highestSequenceID++, command);
    if(sendProposal(proposal)){ output[0] = "true"; }
    else{output[0] = "false";}
    return output;

  }

  @Override
  public synchronized String[] getHandler(String key) throws RemoteException {
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


}
//todo implement logger

