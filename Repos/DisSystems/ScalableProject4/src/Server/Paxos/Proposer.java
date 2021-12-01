package Server.Paxos;

import Server.Commands.Command;
import java.util.ArrayList;
import java.util.List;

public class Proposer implements ProposerInterface {

  //ID of the machine proposing
  int machineID;
  //Sequence value of the proposal
  int sequenceID;


  //List of responses from the responders
  List listOfResponses = new ArrayList();

  //List of all the servers connected to the network (knows where to send things to
  List listOfServers;

  public Proposer(){

  }

  public void sendProposal(Acceptor acceptor, ProposalObject proposal){
    acceptor.receiveProposal(proposal);
  }

  public void issueProposal(Acceptor acceptor, ProposalObject proposal){
    acceptor.receiveIssue(proposal);
  }

  public void issueProposal(ProposalObject proposal) {

  }

  @Override
  public void onReceivedAccept(PaxosResponse response, ProposalObject proposal, Integer a) {

    //This is what will happen if we received an accept
  }

}
