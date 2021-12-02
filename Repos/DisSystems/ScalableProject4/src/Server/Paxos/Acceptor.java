package Server.Paxos;

import java.rmi.RemoteException;

public interface Acceptor {
  void onReceive();
  void reject(ProposalObject proposal);
  void acceptProposal(ProposalObject proposal);
  void acceptIssue(ProposalObject proposal) throws RemoteException;
  void sendPromise(ProposalObject proposal, PaxosResponse response);
  void receiveProposal(ProposalObject proposal);
  void receiveIssue(ProposalObject proposal) throws RemoteException;


}
