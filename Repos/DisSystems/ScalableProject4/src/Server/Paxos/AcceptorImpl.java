package Server.Paxos;

import java.net.InetAddress;

class AcceptorImpl implements Acceptor{
  private int highestSequenceID;
  private boolean accepted = false;
  private ProposalObject highestProposal;


  //On receive: Proposal that contains machineID and sequenceID
  @Override
  public void onReceive(){

  }



  /**
   * Accepts a proposal
   * @param proposal
   */
  @Override
  public void acceptProposal(ProposalObject proposal) {
    if(accepted){
      //return a response to the proposer saying we've already accepted
      sendPromise(highestProposal, PaxosResponse.ACCEPTCONDITIONALLY);
    }
    else {
      highestProposal = proposal;
      sendPromise(highestProposal, PaxosResponse.ACCEPT);
      accepted = true;
    }
  }

  /**
   * Occurs once a proposal is received by the acceptor
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

  @Override
  public void acceptIssue(ProposalObject proposal){

  }

  @Override
  public void sendPromise(ProposalObject proposal, PaxosResponse response) {
    //Todo: here we would use proposal.machineID to get the server that proposed this to return the promise.
    //InetAddress proposerAddress = (InetAddress) proposal.machineID;

    //search through list of servers for the


  }

  @Override
  public void receiveIssue(ProposalObject proposal) {

  }

  @Override
  public void reject(ProposalObject proposal) {
    //ignore and don't send anything back

  }

}
