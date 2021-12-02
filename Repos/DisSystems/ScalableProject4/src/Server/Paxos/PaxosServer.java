package Server.Paxos;

import Server.Commands.Command;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Every server on the paxos will be a Acceptor, Proposer, and a Learner
 */
public class PaxosServer implements ProposerInterface, Acceptor{
  //ID of the machine proposing/port nuimber
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
  Map<Integer, PaxosServer> listOfServers;

  private static ConcurrentHashMap<String, String> keyPairMap =
      new ConcurrentHashMap<String, String>();

  public PaxosServer(){
    //todo access the machine's ID and set it as such have constructor arguments
    machineID = 0;
  }

  /**
   * Function that sends the proposal from the Proposer to the Acceptor
   * @param acceptor the server to send the proposal to
   * @param proposal the actual proposal containing the data and where it originally came from
   */
  public void sendProposal(Acceptor acceptor, ProposalObject proposal){
    acceptor.receiveProposal(proposal);
  }

  public void sendProposal(ProposalObject proposal) throws RemoteException {
    //Go through each server and make them receive a proposal once they receive a proposal there response will be recorded
    for(Integer ID: listOfServers.keySet()){
      //remote call on the server
      listOfServers.get(ID).receiveProposal(proposal);
    }
    //After each server is ran through with the proposal we call onconsensus to make sure we have a consensus to continue
    onConsenus();
  }

  /**
   * Occurs once a proposal is received by the acceptor
   * A remote call called by sendProposal on the acceptro
   * @param proposal
   */
  @Override
  public void receiveProposal(ProposalObject proposal) {
    if(highestSequenceID > proposal.sequenceID){
      highestSequenceID = proposal.machineID;
      acceptProposal(proposal);
    }
    else{
      reject(proposal);
    }
  }

  /**
   * Accepts a proposal from a proposer and promises to the promise
   * @param proposal
   */
  @Override
  public void acceptProposal(ProposalObject proposal) {
    if(accepted){
      //return a response to the proposer saying we've already accepted
      sendPromise(highestProposalAcceptor, PaxosResponse.ACCEPTCONDITIONALLY);
    }
    else {
      highestProposalAcceptor = proposal;
      sendPromise(highestProposalAcceptor, PaxosResponse.ACCEPT);
      accepted = true;
    }
  }

  /**
   * todo comment this
   * @param proposal
   * @param response
   */
  @Override
  public void sendPromise(ProposalObject proposal, PaxosResponse response) {
    //Todo: here we would use proposal.machineID to get the server that proposed this to return the promise.
    //InetAddress proposerAddress = (InetAddress) proposal.machineID;
    //search through list of servers for the

    //Fidns the proposer that matches the proposal's ID
    ProposerInterface proposer = listOfServers.get(proposal.machineID);

    //Calls the proposer's onReceivedAccept
    if(proposer != null){
      //todo change machineID to whatever I need to send for the unique identifier
      //remote call on proposer
      proposer.onReceivedAccept(response, proposal, this.machineID);
    }

  }


  /**
   * Function called when an Acceptor accepts a proposal called on the Proposer
   * @param response
   */
  @Override
  public void onReceivedAccept(PaxosResponse response, ProposalObject proposal, Integer acceptorID) {
    if(response.equals(PaxosResponse.ACCEPTCONDITIONALLY)){
      if(proposal.sequenceID > highestProposalProposer.sequenceID){
        highestProposalProposer.command = proposal.command;
      }
      listOfResponses.add(acceptorID);
    }
    if(response.equals(PaxosResponse.ACCEPT)){
      listOfResponses.add(acceptorID);
    }
  }

  /**
   * Function called to check onConensus. If consensus is met then it'll issue the proposal to each acceptor
   * todo implement timeout functionality, and implement this functionality
   */
  public void onConsenus() throws RemoteException {
    int numToBeat = (listOfServers.size() / 2);
    if(listOfResponses.size() > numToBeat){
      for(Integer id: listOfResponses){
        //calling receiveIssue on each server remotely
        ((PaxosServer)listOfServers.get(id)).receiveIssue(highestProposalProposer);
      }
    }
  }

  /**
   * Function that sends an issue from the Proposer to the Acceptor
   * @param acceptor
   * @param proposal
   */
  public void issueProposal(Acceptor acceptor, ProposalObject proposal) throws RemoteException {
    acceptor.receiveIssue(proposal);
  }

  public void issueProposal(ProposalObject proposal) throws RemoteException {
    //Go through each server and make them receive a proposal once they receive a proposal there response will be recorded
    for(Integer ID: listOfServers.keySet()){
      //remote call on the server
      listOfServers.get(ID).receiveIssue(proposal);
    }
    //After each server is ran through with the proposal we call onconsensus to make sure we have a consensus to continue
    //todo new onConsenus() for when issueing a proposal

  /**
   * todo comment out
   * @param proposal
   * @throws RemoteException
   */
  public void receiveIssue(ProposalObject proposal) throws RemoteException {
    if(proposal.sequenceID >= highestSequenceID) {
      acceptIssue(proposal);
    }
  }

  @Override
  public void acceptIssue(ProposalObject proposal) throws RemoteException {
    //todo go through each server's keyvalue and read the command
    highestProposalAcceptor = proposal;
    ProposerInterface proposer = listOfServers.get(proposal.machineID);
    proposer.issueResponse(proposal, machineID);
    //send back a confirmation response to the proposer
    //then proposer calls command on each learner if another consensus made otherwise fail call
  }

  public void issueResponse(ProposalObject proposal, Integer acceptorID) {
      listOfIssueResponses.add(acceptorID);
  }


  /**
   * Three new functions
   * 1) Sending backa  response from acceptor to proposer saying i accepted issue
   * 2) The proposer makes a new consensus based on the issue responses
   * 3) calls on every server/learner the actual command
   * 4) Lastly the clear the respnse list and reset other variables
   *
   * Can just make a return function situation insterad constantly calling new functions
   */


  public void
    String key = null;
    String value = null;
    if(proposal.command.getCommand().get(0).equals("DELETE")){
      key = proposal.command.getCommand().get(1);
      for(Integer ID: listOfServers.keySet()){
        //todo DELETE COMMAND GOES HERE
        listOfServers.get(ID).serverDeleteHandler(key);
      }

    }
    if(proposal.command.getCommand().get(0).equals("PUT")){
      key = proposal.command.getCommand().get(1);
      value = key = proposal.command.getCommand().get(2);
      for(Integer ID: listOfServers.keySet()){
        //todo PUT COMMAND GOES HERE
        listOfServers.get(ID).serverPutHandler(key, value);
      }
    }
    else{
      //todo throw an error
    }
  }


  @Override
  public void reject(ProposalObject proposal) {
    //ignore and don't send anything back

  }

  //On receive: Proposal that contains machineID and sequenceID
  @Override
  public void onReceive(){

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

}

//todo Clean up code
//todo Add Logger coding
//todo add key-value hashmap
//todo implement timeouts
//todo connect servers together
//todo PaxosServer implementation
//todo inside Paxos implementation have proposal create a 

