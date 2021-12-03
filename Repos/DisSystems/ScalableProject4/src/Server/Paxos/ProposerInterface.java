package Server.Paxos;

import Server.Commands.Command;
import Server.HashClientInterface;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProposerInterface {
  void onReceivedAccept(PaxosResponse response, ProposalObject proposal, Integer acceptorID);
  boolean sendProposal(ProposalObject proposal) throws RemoteException;
  boolean issueProposal(ProposalObject proposal) throws RemoteException;
  void issueResponse(ProposalObject proposal, Integer acceptorID);
  boolean onConsensusPromise()  throws RemoteException;
  boolean onConsensusIssue() throws RemoteException;
  void registerClient(PaxosClient client);

}
