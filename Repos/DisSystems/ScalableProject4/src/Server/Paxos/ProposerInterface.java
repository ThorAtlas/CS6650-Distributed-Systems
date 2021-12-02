package Server.Paxos;

import Server.Commands.Command;
import java.rmi.RemoteException;

public interface ProposerInterface {
  void onReceivedAccept(PaxosResponse response, ProposalObject proposal, Integer acceptorID);
  void sendProposal(Acceptor acceptor, ProposalObject proposal);
  void issueProposal(Acceptor acceptor, ProposalObject proposal) throws RemoteException;
  void issueResponse(ProposalObject proposal, Integer acceptorID);
//  void issueProposal(ProposalObject proposal);

}
